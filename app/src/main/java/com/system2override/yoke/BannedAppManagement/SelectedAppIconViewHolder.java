package com.system2override.yoke.BannedAppManagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.system2override.yoke.R;

public class SelectedAppIconViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    public View wholeView;
    public ImageView iconView;

    public SelectedAppIconViewHolder(View view, Context context) {
        super(view);
        this.context = context;
        this.wholeView = view;
        this.iconView = this.wholeView.findViewById(R.id.bannedAppIcon);
    }
}
