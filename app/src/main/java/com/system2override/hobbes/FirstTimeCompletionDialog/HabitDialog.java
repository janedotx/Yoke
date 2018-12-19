package com.system2override.hobbes.FirstTimeCompletionDialog;

import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;

public class HabitDialog implements Data {
    public HabitDialog() {}
    public String getDescription() {
        return "Your streak increased by 1!";
    }

    public int getDrawable() {
        return R.drawable.heart;
    }

    public String getRewardMessage() {
        return "You have earned 15 minutes of time.";
    }

}
