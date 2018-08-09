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

import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    private static final String TAG = "ToDoListAdapter";
    private List<ToDoInterface> toDoList;
    private Context context;

    public ToDoListAdapter(Context context, List<ToDoInterface> toDoList) {
        this.toDoList = toDoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, parent, false);
        return new ToDoViewHolder(v, this.context, new ToDoViewHolder.IToDoViewHolderClicks() {
            @Override
            public void onTextClick(TextView v, int position) {
                Log.d(TAG, "onTextClick: " + Integer.toString(position));

            }

            @Override
            public void onCheckBoxClick(CheckBox b, int position) {
                Log.d(TAG, "onCheckBoxClick: " + Integer.toString(position));
                ToDoInterface t = toDoList.get(position);
                TimeBank timeBank = MyApplication.getTimeBank();
                if (b.isChecked()) {
                    t.setCompleted(true);
                    // this woudl be a great place to use RxJava, set up this object as an observable
                    // and have the TimeBank subscribe to its changes but I don't know how to make
                    // that work and I don't feel inclined to learn at the moment
                    Log.d(TAG, "onCheckBoxClick: availableTime was " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                    timeBank.earnTime(ToDoListAdapter.this.context);
                    Log.d(TAG, "onCheckBoxClick: availableTime is now " + Long.toString(timeBank.getAvailableTime()/ 1000L));

                } else {
                    t.setCompleted(false);
                    Log.d(TAG, "onCheckBoxClick: availableTime was " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                    timeBank.unearnTime(ToDoListAdapter.this.context);
                    Log.d(TAG, "onCheckBoxClick: availableTime is now " + Long.toString(timeBank.getAvailableTime()/ 1000L));
                }
                t.save(MyApplication.getDb(ToDoListAdapter.this.context));
                ToDoListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    /*
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    }
    */

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoInterface todo = this.toDoList.get(position);
        holder.description.setText(todo.getDescription());
        holder.checkBox.setChecked(todo.isCompleted());
    }

    @Override
    public int getItemCount() {
        return this.toDoList.size();
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
