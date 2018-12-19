package com.system2override.hobbes.FirstTimeCompletionDialog;
import com.system2override.hobbes.R;

public class LocalTaskDialog implements Data {
    public LocalTaskDialog () {}
    public String getDescription() {
        return "You finished something!";
    }

    public int getDrawable() {
        return R.drawable.green_star;
    }

    public String getRewardMessage() {
        return "You have earned 15 minutes of time.";
    }
}
