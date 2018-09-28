package com.system2override.yoke.TodoManagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.R;

public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView description;
    public CheckBox checkBox;
    public ToDoInterface todo;
    public View toDoViewGroup;
    public Context context;
    public IToDoViewHolderClicks listener;

    public ToDoViewHolder(View view, Context context, IToDoViewHolderClicks listener) {
        super(view);
        this.description = (TextView) view.findViewById(R.id.toDoDescription);
        this.checkBox = (CheckBox) view.findViewById(R.id.toDoCheckBox);
        this.context = context;
        this.listener = listener;
        this.toDoViewGroup = view.findViewById(R.id.toDoViewGroup);

        this.toDoViewGroup.setOnClickListener(this);
        if (this.checkBox != null) {
            this.checkBox.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        // or view.getId(), even better
        if (view instanceof CheckBox) {
            listener.onCheckBoxClick((CheckBox) view, getAdapterPosition());
        } else {
            listener.onTextClick( view, getAdapterPosition());
        }

    }

    public static interface IToDoViewHolderClicks {
        public void onTextClick(View v, int position);
        public void onCheckBoxClick(CheckBox b, int position);
    }
}
