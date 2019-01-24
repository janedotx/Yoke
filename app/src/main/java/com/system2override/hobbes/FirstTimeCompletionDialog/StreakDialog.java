package com.system2override.hobbes.FirstTimeCompletionDialog;

import com.system2override.hobbes.R;

public class StreakDialog implements Data {
    public StreakDialog() {}
    public String getDescription() {
        return "Your streak increased by 1!";
    }

    public int getDrawable() {
        return R.drawable.heart_one;
    }

    public String getRewardMessage() {
        return "Congratulations on completing all your daily habits today.";
    }

}
