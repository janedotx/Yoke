package com.system2override.yoke.AppLimit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.R;

public class ToDoReminderViewHolder  extends RecyclerView.ViewHolder {
    public TextView description;
    public CheckBox checkBox;
    public ToDoInterface todo;
    public View toDoViewGroup;
    public Context context;

    public ToDoReminderViewHolder(View view, Context context) {
        super(view);
        this.description = (TextView) view.findViewById(R.id.toDoDescription);
        this.checkBox = (CheckBox) view.findViewById(R.id.toDoCheckBox);
        this.context = context;
        this.toDoViewGroup = view.findViewById(R.id.toDoViewGroup);

    }


}
