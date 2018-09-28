package com.system2override.yoke.AppLimit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.R;

import java.util.List;

public class ToDoReminderAdapter extends RecyclerView.Adapter<ToDoReminderViewHolder>{
    private static final String TAG = "ToDoReminderAdapter";
    private static final int TRUNCATE_LENGTH = 25;
    
    private List<ToDoInterface> toDoList;
    private Context context;
    
    public ToDoReminderAdapter(Context context, List<ToDoInterface> toDoList) {
        this.toDoList = toDoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_to_do, parent, false);
        int sizeInDP = 10;

        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, this.context.getResources()
                        .getDisplayMetrics());
        int heightInDp =  (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 50, this.context.getResources()
                        .getDisplayMetrics());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        params.setMargins(0, 0, 0, marginInDp);
        v.requestLayout();

        return new ToDoReminderViewHolder(v, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoReminderViewHolder holder, int position) {
        ToDoInterface todo = this.toDoList.get(position);
        String description = todo.getDescription();
        if (description.length() > TRUNCATE_LENGTH) {
            description = description.substring(0, TRUNCATE_LENGTH);
            description += "...";
        }
        holder.description.setText(description);
        if (!todo.getIsDailyHabit()) {
            holder.toDoViewGroup.setBackground(ContextCompat.getDrawable(this.context, R.drawable.one_off_todo_coloring));
            holder.setIsRecyclable(false);
        }
    }


    @Override
    public int getItemCount() {
        return this.toDoList.size();
    }


}
