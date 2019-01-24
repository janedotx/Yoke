package com.system2override.hobbes.FirstTimeCompletionDialog;

import com.system2override.hobbes.R;

public class HabitDialog implements Data {
    public HabitDialog() {}
    public String getDescription() {
        return "You completed a daily habit!";
    }

    public int getDrawable() {
        return R.drawable.shiny_heart;
    }

    public String getRewardMessage() {
        return "You have earned 15 minutes of time.";
    }

}
