package com.system2override.yoke.TodoManagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.MidnightResetEvent;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    private static final String TAG = "ToDoListAdapter";
    private List<ToDoInterface> toDoList;
    private Context context;

    public ToDoListAdapter(Context context, List<ToDoInterface> toDoList) {
        this.toDoList = toDoList;
        this.context = context;
        MyApplication.getBus().register(this);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, parent, false);
        return new ToDoViewHolder(v, this.context, new ToDoViewHolder.IToDoViewHolderClicks() {
            @Override
            public void onTextClick(TextView v, int position) {
                Log.d(TAG, "onTextClick: " + Integer.toString(position));
                ToDoInterface t = toDoList.get(position);
                Log.d(TAG, "onTextClick: checkbox status should be " + Boolean.toString(t.isCompleted()));

            }

            @Override
            public void onCheckBoxClick(CheckBox b, int position) {
                Log.d(TAG, "onCheckBoxClick: " + Integer.toString(position));
                ToDoInterface toDo = toDoList.get(position);
                TimeBank timeBank = MyApplication.getTimeBank();
                HarnessDatabase db = MyApplication.getDb(ToDoListAdapter.this.context);
                if (b.isChecked()) {
                    toDo.setCompleted(true);
                    Log.d(TAG, "onCheckBoxClick: set completed worked? " + Boolean.toString(toDo.isCompleted()));
                    // this woudl be a great place to use RxJava, set up this object as an observable
                    // and have the TimeBank subscribe to its changes but I don't know how to make
                    // that work and I don't feel inclined to learn at the moment
                    Log.d(TAG, "onCheckBoxClick: availableTime was " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                    timeBank.earnTime();
//                    toDoList.remove(position);

                } else {
                    toDo.setCompleted(false);
                    Log.d(TAG, "onCheckBoxClick: availableTime was " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                    timeBank.unearnTime();
                    Log.d(TAG, "onCheckBoxClick: availableTime is now " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                }
                toDo.save(db);

                List<Habit> habits = db.habitDao().loadAllHabits();
                Log.d(TAG, "onCheckBoxClick: habits " + habits.toString());
                Streaks streak = MyApplication.getStreaks();
                streak.updateStreakInformation(habits);
                Log.d(TAG, "onCheckBoxClick: streaks now is " + Integer.toString(streak.getCurrentStreak()));
                db.close();
                ToDoListAdapter.this.notifyDataSetChanged();
                Log.d(TAG, "notifyDataSetChanged: ");
                Toast.makeText(ToDoListAdapter.this.context,
                        "current streak is now " + Integer.toString(streak.getCurrentStreak()),
                        Toast.LENGTH_LONG)
                        .show();

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
        HarnessDatabase db = MyApplication.getDb(this.context);
        //List<Habit> habits = db.habitDao().getAllHabitsCompletedBefore(today);
        List<Habit> habits = db.habitDao().loadAllHabits();
        for (Habit h: habits) {
            incompletes.add((ToDoInterface) h);
            Log.d(TAG, "onCreate: loading this habit " + h.description + " " + h.isCompleted() + " " + h.getLastDateCompleted());
        }
        this.toDoList = incompletes;
        notifyDataSetChanged();
    }

    /*

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ToDoInterface currentToDo = toDoList.get(position);
        viewHolder.description.setText(currentToDo.getDescription());

        return convertView;
    }
    private class ViewHolder {
        final TextView description;

        ViewHolder(View v) {
            this.description = (TextView) v.findViewById(R.id.toDoDescription);
        }
    }
    */


}
