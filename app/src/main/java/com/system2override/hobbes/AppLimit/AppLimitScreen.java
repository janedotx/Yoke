package com.system2override.hobbes.AppLimit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.MainActivity;
import com.system2override.hobbes.Models.RoomModels.Suggestion;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.ToDoInterface;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;
import com.system2override.hobbes.TodoManagement.TodoManagementScreen;
import com.system2override.hobbes.Utilities.RandomUtilities;

import java.util.List;

public class AppLimitScreen extends HobbesScreen {
    private static final String TAG = "AppLimitScreen";

    private AppLimitTasks appLimitTasks;

    private RecyclerView toDoRecyclerView;
    private ToDoReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.appLimitTasks = new AppLimitTasks(MyApplication.getDb());

        Log.d(TAG, "onCreate: ");

        // todo currently i'm ignoring the conditions where the localtasks are incomplete
        switch(this.appLimitTasks.getType()) {
            case AppLimitTasks.NO_STREAK:
                setContentView(R.layout.incomplete_streak_screen);
                break;
            case AppLimitTasks.ALL_COMPLETED:
                setContentView(R.layout.all_complete);
                break;
            case AppLimitTasks.STREAK_COMPLETED:
                setContentView(R.layout.complete_streak);
                break;
            default:
                setContentView(R.layout.incomplete_streak_screen);
                break;
        }

        getSupportActionBar().hide();

        Log.d(TAG, "onCreate: type is " + Integer.toString(this.appLimitTasks.getType()));

        if (findViewById(R.id.appLimitTodos) != null) {
            toDoRecyclerView = (RecyclerView) findViewById(R.id.appLimitTodos);
            toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.adapter = new ToDoReminderAdapter(this, this.appLimitTasks.calculateToDos());
            toDoRecyclerView.setAdapter(this.adapter);
        }


        View v =  findViewById(R.id.appLimitTodos);
        if (v != null) {
            this.toDoRecyclerView = (RecyclerView) v;
            toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            this.adapter = new ToDoReminderAdapter(this, this.appLimitTasks.calculateToDos());
            toDoRecyclerView.setAdapter(this.adapter);
      }

        setDailyStreakNumber();
        setTimeSpentToday();
        setTimeTillReset();

        findViewById(R.id.appLimitGoToApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppLimitScreen.this, TodoManagementScreen.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void setDailyStreakNumber() {
        ((TextView) findViewById(R.id.appLimitDailyStreakNum))
                .setText(Integer.toString(MyApplication.getStreaks().getCurrentStreak()));
    }


    private void setTimeSpentToday() {
        View v = findViewById(R.id.timeSpentTodayTime);
        if (v != null) {
            TextView timeSpentView = (TextView) v;
            long time = MyApplication.getTimeBank().getSpentTime();
            timeSpentView.setText(RandomUtilities.formatMillisecondsToHHMM(time));

        }
    }

    private void setTimeTillReset() {
        View v = findViewById(R.id.timeRemainingTillResetString);
        if (v != null) {
            TextView timeSpentView = (TextView) v;
            long timeTillReset = RandomUtilities.getNextMidnight() - System.currentTimeMillis();
            timeSpentView.setText(RandomUtilities.formatMSToHHMMSS(timeTillReset));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
