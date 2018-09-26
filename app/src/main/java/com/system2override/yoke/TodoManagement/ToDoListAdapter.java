package com.system2override.yoke.TodoManagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.ManageToDo.EditToDoScreen;
import com.system2override.yoke.ManageToDo.ManageToDoScreen;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.MidnightResetEvent;
import com.system2override.yoke.OttoMessages.ToDoCompletedEvent;
import com.system2override.yoke.OttoMessages.ToDoCreated;
import com.system2override.yoke.OttoMessages.ToDoDeleted;
import com.system2override.yoke.OttoMessages.ToDoEdited;
import com.system2override.yoke.OttoMessages.ToDoUncheckedEvent;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    private static final String TAG = "ToDoListAdapter";
    private List<ToDoInterface> toDoList;
    private Context context;
    private int tab;

    public ToDoListAdapter(Context context, List<ToDoInterface> toDoList, int tab) {
        this.toDoList = toDoList;
        this.context = context;
        this.tab = tab;
        MyApplication.getBus().register(this);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, parent, false);
        return new ToDoViewHolder(v, this.context, new ToDoViewHolder.IToDoViewHolderClicks() {
            @Override
            public void onTextClick(TextView v, int position) {
                ToDoInterface t = toDoList.get(position);

                Intent i = new Intent(ToDoListAdapter.this.context, EditToDoScreen.class);
                Bundle b = new Bundle();
                b.putInt(EditToDoScreen.ID_KEY, t.getId());
                b.putInt(EditToDoScreen.ADAPTER_POSITION_KEY, position);
                i.putExtras(b);

                context.startActivity(i);
            }

            @Override
            public void onCheckBoxClick(CheckBox b, int position) {
                ToDoInterface toDo = toDoList.get(position);
                TimeBank timeBank = MyApplication.getTimeBank();
                HarnessDatabase db = MyApplication.getDb();
                if (b.isChecked()) {
                    toDo.setCompleted(true);
                    Log.d(TAG, "onCheckBoxClick: set completed worked? " + Boolean.toString(toDo.isCompleted()));
                    // this would be a great place to use RxJava, set up this object as an observable
                    // and have the TimeBank subscribe to its changes but I don't know how to make
                    // that work and I don't feel inclined to learn at the moment
                    timeBank.earnTime();
                    if (ToDoListAdapter.this.tab == ToDoListFragment.INCOMPLETE_TODOS) {
                        toDoList.remove(position);
                    }
                    MyApplication.getBus().post(new ToDoCompletedEvent(toDo));
                } else {
                    toDo.setCompleted(false);
                    timeBank.unearnTime();
                    if (ToDoListAdapter.this.tab == ToDoListFragment.COMPLETED_TODOS) {
                        toDoList.remove(position);
                    }
                    MyApplication.getBus().post(new ToDoUncheckedEvent(toDo));
                }
                toDo.save(db);

                // update streak information as necessary
                List<Habit> habits = db.habitDao().loadAllHabits();
                Streaks streak = MyApplication.getStreaks();
                streak.updateStreakInformation(habits);
                ToDoListAdapter.this.notifyDataSetChanged();
                Log.d(TAG, "notifyDataSetChanged: ");

            }
        });
    }

    /*
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    }
    */

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + Integer.toString(position));
        ToDoInterface todo = this.toDoList.get(position);
        holder.description.setText(todo.getDescription());
        holder.checkBox.setChecked(todo.isCompleted());
        // TODO
        // change this so the left border is green only if it's a one-off task
//        /*
        if (todo instanceof LocalTask) {
            holder.toDoViewGroup.setBackground(ContextCompat.getDrawable(this.context, R.drawable.one_off_todo_coloring));
        }
//        */
    }

    @Override
    public int getItemCount() {
        return this.toDoList.size();
    }

    // todo
    // this has to change a lot whenever the tasks integration happens
    @Subscribe
    public void updateToDoList(MidnightResetEvent e) {
        List<ToDoInterface> incompletes = new ArrayList<>();
        HarnessDatabase db = MyApplication.getDb();
        List<Habit> habits = db.habitDao().loadAllHabits();
        for (Habit h: habits) {
            incompletes.add((ToDoInterface) h);
        }
        this.toDoList = incompletes;
        notifyDataSetChanged();
    }

    @Subscribe
    public void updateAdaptersOnToDoCompletion(ToDoCompletedEvent e) {
        if (this.tab == ToDoListFragment.COMPLETED_TODOS) {
            this.toDoList.add(e.toDo);
            // oddly, these logging statements never appear, but if i comment out the post
            // events, then the tabs fail to update properly
            Log.d(TAG, "updateAdaptersOnToDoCompletion: " + e.toDo.getDescription());
            notifyDataSetChanged();
        }
    }

    @Subscribe
    public void updateAdaptersOnToDoUnchecking(ToDoUncheckedEvent e) {
        if (this.tab == ToDoListFragment.INCOMPLETE_TODOS) {
            this.toDoList.add(e.toDo);
            Log.d(TAG, "updateAdaptersOnToDoUnchecking: " + e.toDo.getDescription());
        }

        if (this.tab == ToDoListFragment.COMPLETED_TODOS) {
            for (int i = 0; i < this.toDoList.size(); i++) {
                if (this.toDoList.get(i).getId() == e.toDo.getId()) {
                    this.toDoList.remove(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Subscribe
    public void updateAdapterOnToDoCreation(ToDoCreated e) {
        if (this.tab != ToDoListFragment.COMPLETED_TODOS) {
            this.toDoList.add(e.todo);
            notifyDataSetChanged();
        }
    }

    @Subscribe
    public void updateAdapterOnToDoEdit(ToDoEdited e) {
        int id = e.toDoId;

        for (int i = 0; i < this.toDoList.size(); i++) {
            if (toDoList.get(i).getId() == id) {
                ToDoInterface todo = (ToDoInterface) MyApplication.getDb().habitDao().getById(id);
                toDoList.add(i, todo);
                notifyDataSetChanged();
                break;
            }
        }
    }

//    /*
    @Subscribe
    public void updateAdapterOnToDoDeletion(ToDoDeleted e) {
        for (int i = 0; i < this.toDoList.size(); i++) {
            if (toDoList.get(i).getId() == e.toDoId) {
                toDoList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }

    }
//    */

}
