package com.system2override.yoke.ManageToDo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.system2override.yoke.R;

public class SuggestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public Context context;
    public View view;
    public TextView plus;
    public TextView suggestion;
    private SuggestionOnClickListener listener;

    public SuggestionViewHolder(View view, Context context, SuggestionOnClickListener listener) {
        super(view);
        this.view = view;
        this.context = context;
        this.plus = (TextView) view.findViewById(R.id.addSuggestionPlus);
        this.suggestion = (TextView) view.findViewById(R.id.suggestion);
        this.listener = listener;

        this.view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.listener.onClick(getAdapterPosition());
    }

    public static interface SuggestionOnClickListener {
        public void onClick(int position);
    }

}
