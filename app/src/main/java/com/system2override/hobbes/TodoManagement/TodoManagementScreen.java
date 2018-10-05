package com.system2override.hobbes.TodoManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.hobbes.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.ManageToDo.AddToDoScreen;
import com.system2override.hobbes.ManageToDo.ManageToDoScreen;
import com.system2override.hobbes.MainActivity;
import com.system2override.hobbes.ManagerService;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.Models.ToDoInterface;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.MidnightResetEvent;
import com.system2override.hobbes.OttoMessages.StreakUpdateEvent;
import com.system2override.hobbes.OttoMessages.TimeBankEarnedTime;
import com.system2override.hobbes.OttoMessages.TimeBankUnearnedTime;
import com.system2override.hobbes.R;
import com.system2override.hobbes.SetUsageLimitsScreen;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;
import com.system2override.hobbes.Utilities.RandomUtilities;
import com.system2override.hobbes.Utilities.UsageStatsHelper;
import com.system2override.hobbes.WelcomeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getBus().register(this);
        setContentView(R.layout.activity_todo_management);
//        /*
        if (MyApplication.getOneTimeData().getAverageDailyUsageOverall() == 0L) {
            Map<String, Long> map = UsageStatsHelper.getAppsByTotalTime(this, UsageStatsHelper.WEEK_IN_MS, System.currentTimeMillis());
            long totalTime = UsageStatsHelper.sumTotalTimeOverInterval(map);
            Log.d(TAG, "firstTimeSetup: totaltime " + totalTime);
            MyApplication.getOneTimeData().setAverageDailyUsageOverall(totalTime / 7);
            Log.d(TAG, "firstTimeSetup: daily average over last week is " + Long.toString(MyApplication.getOneTimeData().getAverageDailyUsageOverall()));
        }
//        */

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.toDoManagementActivityTitle);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.whitesubject);

        mDrawerLayout = findViewById(R.id.settingsDrawerLayout);

        initializeValueViews();

        this.tabs = (TabLayout) findViewById(R.id.toDoManagementTabs);

        this.viewPager = (ViewPager) findViewById(R.id.toDoManagementPager);
        this.toDoListPagerAdapter = new ToDoListPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.toDoListPagerAdapter);
        this.tabs.setupWithViewPager(this.viewPager);

        setFABcolor();
        setOnClickListeners();

        boolean b = MyApplication.getOneTimeData().getHasDoneTutorialKey();
        Log.d(TAG, "onCreate: has done tutorial? " + Boolean.toString(b));
        if (!b) {
            startActivity(new Intent(this, WelcomeScreen.class));
        }
    }

    private void setOnClickListeners() {

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

    private void setFABcolor() {
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
    }

    private void initializeValueViews() {

        this.earnedTimeValueView = findViewById(R.id.todoManagementEarnedTimeValue);
        this.remainingTimeValueView = findViewById(R.id.todoManagementAvailableTimeValue);
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeBank.getEarnedTime()));
        long remainingTime = timeBank.getTimeRemaining();
        if (remainingTime < 0L) { remainingTime = 0; }
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(remainingTime));

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
        mDrawerLayout.closeDrawer(GravityCompat.START);
        super.onResume();

        /*
        if (!MyApplication.getOneTimeData().getHasDoneTutorialKey()) {
            Intent i = new Intent(this, WelcomeScreen.class);
            startActivity(i);

        }
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void addEarnedTimeView(TimeBankEarnedTime event) {
        Log.d(TAG, "makeTimeAvailableChanges: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeBank.getEarnedTime()));
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeBank.getTimeRemaining()));
    }

    @Subscribe
    public void subtractEarnedTimeView(TimeBankUnearnedTime event) {
        TimeBank timeBank = MyApplication.getTimeBank();
        this.earnedTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeBank.getEarnedTime()));
        long timeRemaining = timeBank.getTimeRemaining();
        if (timeRemaining < 0L) {
            timeRemaining = 0L;
        }
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeRemaining));
    }

    @Subscribe
    public void updateStreak(StreakUpdateEvent event) {
        updateStreakValues();
    }

    @Subscribe
    public void updateInfoViewValues(MidnightResetEvent e) {
        this.earnedTimeValueView.setText("0 m");
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(MyApplication.getTimeBank().getInitialTime()));
        updateStreakValues();
    }

    public void launchUsageLimitScreen(MenuItem item) {
        Intent i = new Intent(this, SetUsageLimitsScreen.class);
        startActivity(i);
    }

    public void launchBannedAppsScreen(MenuItem item) {
        Intent i = new Intent(this, BannedAppScreen.class);
        startActivity(i);
    }

    public void launchUsageHistoryScreen(MenuItem item) {
        Intent i = new Intent(this, UsageHistoryScreen.class);
        startActivity(i);
    }

    public void launchTutorial(MenuItem item) {
        MyApplication.getOneTimeData().setHasDoneTutorialKey(false);
        Intent i = new Intent(this, WelcomeScreen.class);
        startActivity(i);
    }

}