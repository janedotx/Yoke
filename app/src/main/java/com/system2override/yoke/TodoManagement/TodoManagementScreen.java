package com.system2override.yoke.TodoManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.MainActivity;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.MidnightResetEvent;
import com.system2override.yoke.OttoMessages.StreakUpdateEvent;
import com.system2override.yoke.OttoMessages.TimeBankEarnedTime;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.List;

public class TodoManagementScreen extends AppCompatActivity {
    private static final String TAG = "TodoManagementScreen";
    private RecyclerView toDoListView;
    private ToDoListAdapter adapter;

    private List<ToDoInterface> incompletes = new ArrayList<>();
    private List<ToDoInterface> completed;

    private TextView earnedTimeValueView;
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
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: bundle contents " + savedInstanceState.toString());
        }
        MyApplication.getBus().register(this);
        setContentView(R.layout.activity_todo_management);

        initializeValueViews();

        this.tabs = (TabLayout) findViewById(R.id.toDoManagementTabs);
        this.viewPager = (ViewPager) findViewById(R.id.toDoManagementPager);
        this.toDoListPagerAdapter = new ToDoListPagerAdapter(getSupportFragmentManager());

        this.viewPager.setAdapter(this.toDoListPagerAdapter);
        this.tabs.setupWithViewPager(this.viewPager);

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
                                                   Intent i = new Intent(TodoManagementScreen.this, AddToDo.class);
                                                   startActivity(i);
                                               }
                                           }
        );
    }

    private void initializeValueViews() {

        this.earnedTimeValueView = findViewById(R.id.todoManagementAvailableTimeValue);
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(Long.toString(timeBank.getAvailableTime()));

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
        Log.d(TAG, "onResume: " + System.identityHashCode(this));
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
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
    public void makeTimeAvailableChanges(TimeBankEarnedTime event) {
        Log.d(TAG, "makeTimeAvailableChanges: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(Long.toString(timeBank.getTotalEarnedTimeToday()));
    }

    @Subscribe
    public void updateStreak(StreakUpdateEvent event) {
        updateStreakValues();
    }

    @Subscribe
    public void updateInfoViewValues(MidnightResetEvent e) {
        this.earnedTimeValueView.setText("0");
        Streaks streak = MyApplication.getStreaks();
    }

}
