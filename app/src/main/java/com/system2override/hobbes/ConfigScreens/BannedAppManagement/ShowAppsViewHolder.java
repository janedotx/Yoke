package com.system2override.hobbes.ConfigScreens.BannedAppManagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.system2override.hobbes.R;

public class ShowAppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Context context;
    public View singleAppView;
    private BannedAppClickListener listener;
    public ImageView imageView;
    public CheckBox checkBox;
    public TextView appText;
    public ImageView appImage;

    public ShowAppsViewHolder(View view, Context context, BannedAppClickListener listener) {
        super(view);
        this.context = context;
        this.singleAppView = view;
        this.listener = listener;
        this.checkBox = (CheckBox) view.findViewById(R.id.singleAppCheckBox);
        this.appText = (TextView) view.findViewById(R.id.singleAppTextView);
        this.appImage = (ImageView) view.findViewById(R.id.singleAppIcon);
        this.singleAppView.setOnClickListener(this);
        this.checkBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.listener.onClick(v, getAdapterPosition());
    }

    public static interface BannedAppClickListener {
        public void onClick(View view, int position);
    }
}
