package com.system2override.yoke.AppLimit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.R;

import java.util.List;

public class ToDoReminderAdapter extends RecyclerView.Adapter<ToDoReminderViewHolder>{
    private static final String TAG = "ToDoReminderAdapter";
    
    private List<ToDoInterface> toDoList;
    private Context context;
    
    public ToDoReminderAdapter(Context context, List<ToDoInterface> toDoList) {
        this.toDoList = toDoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, parent, false);
        View checkbox = v.findViewById(R.id.toDoCheckBox);
        parent.removeView(checkbox);
        return new ToDoReminderViewHolder(v, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoReminderViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return this.toDoList.size();
    }


}
