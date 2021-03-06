package com.system2override.hobbes.TodoManagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.hobbes.FirstTimeCompletionDialog.HabitDialog;
import com.system2override.hobbes.FirstTimeCompletionDialog.LocalTaskDialog;
import com.system2override.hobbes.ManageToDo.AddToDoScreen;
import com.system2override.hobbes.ManageToDo.EditToDoScreen;
import com.system2override.hobbes.ManageToDo.ManageToDoScreen;
import com.system2override.hobbes.HarnessDatabase;
import com.system2override.hobbes.Models.RoomModels.Habit;
import com.system2override.hobbes.Models.RoomModels.LocalTask;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.Models.ToDoInterface;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.MidnightResetEvent;
import com.system2override.hobbes.OttoMessages.ToDoCompletedEvent;
import com.system2override.hobbes.OttoMessages.ToDoCreated;
import com.system2override.hobbes.OttoMessages.ToDoDeleted;
import com.system2override.hobbes.OttoMessages.ToDoEdited;
import com.system2override.hobbes.OttoMessages.ToDoUncheckedEvent;
import com.system2override.hobbes.R;
import com.system2override.hobbes.FirstTimeCompletionDialog.Data;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    private static final String TAG = "ToDoListAdapter";
    private static final int TRUNCATE_LENGTH = 48;
    private static final String PLACEHOLDER_TODO_DESC = "31459iamspecialtodo";

    private List<ToDoInterface> toDoList;
    private Context context;
    private int tab;

    public ToDoListAdapter(Context context, List<ToDoInterface> toDoList, int tab) {
        this.toDoList = toDoList;
        Log.d(TAG, "ToDoListAdapter: tab " + Integer.toString(tab));
        if (this.toDoList.size() == 0 && tab != ToDoListFragment.COMPLETED_TODOS) {
            Habit placeHolder = new Habit();
            placeHolder.description = PLACEHOLDER_TODO_DESC;
            this.toDoList.add((ToDoInterface)placeHolder);
        }
        this.context = context;
        this.tab = tab;
        MyApplication.getBus().register(this);
    }

    private boolean isPlaceholderToDo(ToDoInterface todo) {
        return todo.getDescription().equals(PLACEHOLDER_TODO_DESC);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (!isPlaceholderToDo(this.toDoList.get(0))) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, parent, false);
            return new ToDoViewHolder(v, this.context, new ToDoViewHolder.IToDoViewHolderClicks() {

                @Override
                public void onTextClick(View v, int position) {
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
                        Log.d(TAG, "onCheckBoxClick: set completed worked? " + Integer.toString(position));
                        Log.d(TAG, "onCheckBoxClick: this is the tab " + Integer.toString(ToDoListAdapter.this.tab));
                        // this would be a great place to use RxJava, set up this object as an observable
                        // and have the TimeBank subscribe to its changes but I don't know how to make
                        // that work and I don't feel inclined to learn at the moment
                        timeBank.earnTime(toDo);
                        if (ToDoListAdapter.this.tab == ToDoListFragment.INCOMPLETE_TODOS) {
                            toDoList.remove(position);
                        }
                        Log.d(TAG, "onCheckBoxClick: " + toDo.getToDoType());
                        if (toDo.getToDoType().equals(Habit.TAG)) {
                            showFirstTimeCompletionDialog(new HabitDialog());
                        } else {
                            // launch that other dialog
                            showFirstTimeCompletionDialog(new LocalTaskDialog());
                        }
                        MyApplication.getBus().post(new ToDoCompletedEvent(toDo));
                    } else {
                        toDo.setCompleted(false);
                        timeBank.unearnTime(toDo);
                        if (ToDoListAdapter.this.tab == ToDoListFragment.COMPLETED_TODOS) {
                            toDoList.remove(position);
                        }
                        MyApplication.getBus().post(new ToDoUncheckedEvent(toDo));
                    }
                    toDo.save(db);

                    // update streak information as necessary
                    List<Habit> habits = db.habitDao().loadAllHabits();
                    Streaks streak = MyApplication.getStreaks();
                    if (toDo.getIsDailyHabit()) {
                        streak.updateStreakInformation(habits);
                    }

                    ToDoListAdapter.this.notifyDataSetChanged();
                    Log.d(TAG, "notifyDataSetChanged: ");

                }
            });
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.placeholder_todo, parent, false);
            return new ToDoViewHolder(v, this.context, new ToDoViewHolder.IToDoViewHolderClicks() {
                @Override
                public void onTextClick(View v, int position) {
                    ToDoListAdapter.this.toDoList.remove(0);
                    notifyDataSetChanged();
                    Intent i = new Intent(ToDoListAdapter.this.context, AddToDoScreen.class);
                    ToDoListAdapter.this.context.startActivity(i);
                }

                @Override
                public void onCheckBoxClick(CheckBox b, int position) { }
            });
        }
    }

    /*
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    }
    */

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoInterface todo = this.toDoList.get(position);
        if (todo.getDescription().equals(PLACEHOLDER_TODO_DESC)) {
            holder.setIsRecyclable(false);
            return;
        }
        String description = todo.getDescription();
        if (description.length() > TRUNCATE_LENGTH) {
            description = description.substring(0, TRUNCATE_LENGTH);
            description += "...";
        }
        holder.description.setText(description);
        if (holder.checkBox != null) {
            holder.checkBox.setChecked(todo.isCompleted());
        }
        // TODO
        // change this so the left border is green only if it's a one-off task
//        /*
        if (!todo.getIsDailyHabit() ) {
            holder.toDoViewGroup.setBackground(ContextCompat.getDrawable(this.context, R.drawable.one_off_todo_coloring));
            holder.setIsRecyclable(false);
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
        }

        // this case is for if the todo is checked off while in the ALL tabs
        if (this.tab == ToDoListFragment.INCOMPLETE_TODOS) {
            for (int i = 0; i < this.toDoList.size(); i++) {
                if (this.toDoList.get(i).getId() == e.toDo.getId()) {
                    this.toDoList.remove(i);
                }
            }
            // oddly, these logging statements never appear, but if i comment out the post
            // events, then the tabs fail to update properly
            Log.d(TAG, "updateAdaptersOnToDoCompletion: " + e.toDo.getDescription());
        }

        if (this.tab == ToDoListFragment.ALL_TODOS) {
            Log.d(TAG, "updateAdaptersOnToDoCompletion: all todos adapter got the message");
            int id = e.toDo.getId();
            ToDoInterface todo = (ToDoInterface) MyApplication.getDb().habitDao().getById(id);
            todo.setCompleted(true);
            Log.d(TAG, "updateAdaptersOnToDoCompletion: fresh from db todo is done " + Boolean.toString(todo.isCompleted()));
            for (int i = 0; i < this.toDoList.size(); i++) {
                if (this.toDoList.get(i).getId() == id) {
                    this.toDoList.set(i, todo);
                    break;
                }
            }
            notifyDataSetChanged();

        }
        notifyDataSetChanged();
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

        if (this.tab == ToDoListFragment.ALL_TODOS) {
            Log.d(TAG, "updateAdaptersOnToDoCompletion: all todos adapter got the message");
            int id = e.toDo.getId();
            ToDoInterface todo = (ToDoInterface) MyApplication.getDb().habitDao().getById(id);
            todo.setCompleted(false);
            Log.d(TAG, "updateAdaptersOnToDoCompletion: fresh from db todo is done " + Boolean.toString(todo.isCompleted()));
            for (int i = 0; i < this.toDoList.size(); i++) {
                if (this.toDoList.get(i).getId() == id) {
                    this.toDoList.set(i, todo);

                    break;
                }
            }
            notifyDataSetChanged();

        }
        notifyDataSetChanged();
    }

    @Subscribe
    public void updateAdapterOnToDoCreation(ToDoCreated e) {
        if (this.tab != ToDoListFragment.COMPLETED_TODOS) {
            this.toDoList.add(e.todo);
            for (int i =0; i < this.toDoList.size(); i++) {
                if (isPlaceholderToDo(this.toDoList.get(i))) {
                    this.toDoList.remove(i);
                }
            }
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

    /*
    @Subscribe
    public void launchFirstTimeCompletionDialogs(ToDoCompletedEvent event) {
        /// since all three adapters are subscribed for this event, and i
        // can't think of a better place to put this for now (an exception is thrown for some reason
        // if i stick it into the todomanagementscreen--bizarre, since an instance of it should exist)
        // anyway there seem to be multiple instances of each adapter, judging by how despite the
        // hacky if statement, the dialog still shows up multiple times
        // so using otto is bad

        if (this.tab == ToDoListFragment.ALL_TODOS) {
            ToDoInterface todo = event.toDo;
            if (todo.getToDoType().equals(Habit.TAG)) {
                showFirstTimeCompletionDialog(new HabitDialog());
            } else {
                // launch that other dialog
                showFirstTimeCompletionDialog(new LocalTaskDialog());
            }
        }
    }
    */

    private void showFirstTimeCompletionDialog(Data d) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        final View firstTimeView = inflater.inflate(R.layout.first_time_completion_dialog, null);
        ((TextView) firstTimeView.findViewById(R.id.firstTimeCompletionDialogDescription)).setText(d.getDescription());
        ((TextView) firstTimeView.findViewById(R.id.firstTimeCompletionRewardMessage)).setText(d.getRewardMessage());

        firstTimeView.findViewById(R.id.firstTimeCompletionImage).setBackground(ContextCompat.getDrawable(this.context, d.getDrawable()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        final AlertDialog firstTimeDialog = builder.create();
        firstTimeDialog.setView(firstTimeView);
        firstTimeView.findViewById(R.id.firstTimeDialogDismiss).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                firstTimeDialog.dismiss();
            }
        });

        firstTimeDialog.show();

    }
//    */

}
