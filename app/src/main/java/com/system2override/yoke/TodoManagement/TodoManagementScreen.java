package com.system2override.yoke.TodoManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.ManageToDo.AddToDoScreen;
import com.system2override.yoke.ManageToDo.ManageToDoScreen;
import com.system2override.yoke.MainActivity;
import com.system2override.yoke.ManagerService;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.MidnightResetEvent;
import com.system2override.yoke.OttoMessages.StreakUpdateEvent;
import com.system2override.yoke.OttoMessages.TimeBankEarnedTime;
import com.system2override.yoke.OttoMessages.TimeBankUnearnedTime;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TodoManagementScreen extends AppCompatActivity {
    private static final String TAG = "TodoManagementScreen";

    private List<ToDoInterface> incompletes = new ArrayList<>();
    private List<ToDoInterface> completed;

    private TextView earnedTimeValueView;
    private TextView remainingTimeValueView;
    private TextView currentStreakValueView;
    private TextView longestStreakValueView;
    private Button goAddToDoButton;
    private FloatingActionButton addNewToDo;

    private TabLayout tabs;
    private ViewPager viewPager;
    private ToDoListPagerAdapter toDoListPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + System.identityHashCode(this));
        MyApplication.getBus().register(this);
        setContentView(R.layout.activity_todo_management);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.toDoManagementActivityTitle);
       // bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6f8e")));

        initializeValueViews();

        this.tabs = (TabLayout) findViewById(R.id.toDoManagementTabs);
        this.viewPager = (ViewPager) findViewById(R.id.toDoManagementPager);
        this.toDoListPagerAdapter = new ToDoListPagerAdapter(getSupportFragmentManager());

        this.viewPager.setAdapter(this.toDoListPagerAdapter);
        this.tabs.setupWithViewPager(this.viewPager);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled},
                new int[] { android.R.attr.state_pressed}
        };
        int[] colors = new int[] {
                ContextCompat.getColor(this, R.color.HobbesOrange),
                ContextCompat.getColor(this, R.color.HobbesOrange)
        };
        ColorStateList fabColorList = new ColorStateList(states, colors);
        findViewById(R.id.addToDoFAB).setBackgroundTintList(fabColorList);

        /*
        HarnessDatabase db = MyApplication.getDb(this);
        List<Habit> habits = db.habitDao().loadAllHabits();
        for (Habit h: habits) {
            incompletes.add((ToDoInterface) h);
            Log.d(TAG, "onCreate: loading this habit " + h.description + " " + h.isCompleted() + " " + h.getLastDateCompleted());
        }
        Log.d(TAG, "onCreate: incompletes size " + incompletes.size());

        this.toDoListView = (RecyclerView) findViewById(R.id.toDoListView);

        this.adapter = new ToDoListAdapter(this, incompletes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        toDoListView.setLayoutManager(mLayoutManager);
        toDoListView.setItemAnimator(new DefaultItemAnimator());
        toDoListView.setAdapter(adapter);
        this.toDoListView.setLayoutManager(mLayoutManager);
        */

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TodoManagementScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        addNewToDo = (FloatingActionButton) findViewById(R.id.addToDoFAB);
        addNewToDo.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                               Intent i = new Intent(TodoManagementScreen.this, AddToDoScreen.class);
                                               Bundle b = new Bundle();
                                               b.putString(ManageToDoScreen.ACTION_KEY, ManageToDoScreen.ADD_ACTION);
                                               i.putExtras(b);
                                               startActivity(i);
                                               }
                                           }
        );
    }

    private void initializeValueViews() {

        this.earnedTimeValueView = findViewById(R.id.todoManagementEarnedTimeValue);
        this.remainingTimeValueView = findViewById(R.id.todoManagementAvailableTimeValue);
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(formatMilliseconds(timeBank.getTotalEarnedTimeToday()));
        long remainingTime = timeBank.getTimeRemaining();
        if (remainingTime < 0L) { remainingTime = 0; }
        this.remainingTimeValueView.setText(formatMilliseconds(remainingTime));

        this.currentStreakValueView = findViewById(R.id.toDoManagementCurrentStreakValue);
        this.longestStreakValueView = findViewById(R.id.toDoManagementLongestStreakValue);

        updateStreakValues();
    }

    @SuppressLint("SetTextI18n")
    private void updateStreakValues() {
        Streaks streak = MyApplication.getStreaks();
        this.currentStreakValueView.setText(Integer.toString(streak.getCurrentStreak()));
        this.longestStreakValueView.setText(Integer.toString(streak.getLongestStreak()));

    }

    @Override
    protected void onResume() {
        initializeValueViews();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startManagerService();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Subscribe
    public void addEarnedTimeView(TimeBankEarnedTime event) {
        Log.d(TAG, "makeTimeAvailableChanges: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(formatMilliseconds(timeBank.getTotalEarnedTimeToday()));
        this.remainingTimeValueView.setText(formatMilliseconds(timeBank.getTimeRemaining()));
    }

    @Subscribe
    public void subtractEarnedTimeView(TimeBankUnearnedTime event) {
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(formatMilliseconds(timeBank.getTotalEarnedTimeToday()));
        long timeRemaining = timeBank.getTimeRemaining();
        if (timeRemaining < 0L) {
            timeRemaining = 0L;
        }
        this.remainingTimeValueView.setText(formatMilliseconds(timeRemaining));
    }

    @Subscribe
    public void updateStreak(StreakUpdateEvent event) {
        updateStreakValues();
    }

    @Subscribe
    public void updateInfoViewValues(MidnightResetEvent e) {
        this.earnedTimeValueView.setText("00:00");
        this.remainingTimeValueView.setText(formatMilliseconds(MyApplication.getTimeBank().getInitialTime()));
        updateStreakValues();
    }

    // according to docs, this is idempotent
    private void startManagerService() {
        Intent intent = new Intent(this, ManagerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.util.Log.d(TAG, "subscribeToSensor: about to start foreground service " + Long.toString(System.currentTimeMillis()));
            startForegroundService(intent);
        } else {
            android.util.Log.d(TAG, "subscribeToSensor: about to start service " + Long.toString(System.currentTimeMillis()));
            startService(intent);
        }
    }

    private String formatMilliseconds(long millis) {
        long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        return String.format("%02d:%02d",
                totalMinutes,
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(totalMinutes));

    }

}
