package com.system2override.hobbes.TodoManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.FirstTimeCompletionDialog.StreakDialog;
import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.ManageToDo.AddToDoScreen;
import com.system2override.hobbes.ManageToDo.ManageToDoScreen;
import com.system2override.hobbes.Models.OneTimeData;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.Models.ToDoInterface;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.MidnightResetEvent;
import com.system2override.hobbes.OttoMessages.StreakUpdateEvent;
import com.system2override.hobbes.OttoMessages.TimeBankEarnedTime;
import com.system2override.hobbes.OttoMessages.TimeBankUnearnedTime;
import com.system2override.hobbes.R;
import com.system2override.hobbes.ConfigScreens.SetUsageLimitsScreen;
import com.system2override.hobbes.SplashScreen;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;
import com.system2override.hobbes.Utilities.RandomUtilities;
import com.system2override.hobbes.Utilities.UsageStatsHelper;
import com.system2override.hobbes.FirstTimeCompletionDialog.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TodoManagementScreen extends HobbesScreen {
    private static final String TAG = "TodoManagementScreen";

    private List<ToDoInterface> incompletes = new ArrayList<>();
    private List<ToDoInterface> completed;

    private TextView timeSavedView;
    private TextView dateInstalledView;
    private TextView remainingTimeValueView;
    private TextView currentStreakValueView;
    private TextView streakDateView;
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

        // Set usage before Hobbes
        if (MyApplication.getOneTimeData().getAverageDailyUsageBeforeHobbes() == 0L) {
            Map<String, Long> map = UsageStatsHelper.getAppsByTotalTime(this, UsageStatsHelper.WEEK_IN_MS, System.currentTimeMillis());
            long totalTime = UsageStatsHelper.sumTotalTimeOverInterval(map);
            Log.d(TAG, "firstTimeSetup: totaltime " + totalTime);
            MyApplication.getOneTimeData().setAverageDailyUsageBeforeHobbes(totalTime / 7);
            Log.d(TAG, "firstTimeSetup: daily average over last week is " + Long.toString(MyApplication.getOneTimeData().getAverageDailyUsageBeforeHobbes()));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.whitesubject);

        mDrawerLayout = findViewById(R.id.settingsDrawerLayout);

        initializeValueViews();

        this.tabs = (TabLayout) findViewById(R.id.toDoManagementTabs);

        this.viewPager = (ViewPager) findViewById(R.id.toDoManagementPager);
        this.toDoListPagerAdapter = new ToDoListPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.toDoListPagerAdapter);
        this.viewPager.setCurrentItem(1);
        this.tabs.setupWithViewPager(this.viewPager);

        setFABcolor();
        setOnClickListeners();

        OneTimeData oneTimeData = MyApplication.getOneTimeData();
        boolean b = oneTimeData.getHasDoneOnboardingKey();
        if (!b) {
            startActivity(new Intent(this, SplashScreen.class));
        }

        if (!oneTimeData.getHasDoneTutorialKey()) {
            showTutorialFirstBanner();
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

        this.remainingTimeValueView = findViewById(R.id.todoManagementTimeRemainingValue);
        TimeBank timeBank = MyApplication.getTimeBank();
        long remainingTime = timeBank.getTimeRemaining();
        if (remainingTime < 0L) { remainingTime = 0; }
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(remainingTime));

        this.dateInstalledView = findViewById(R.id.todoManagementDateInstalled);
        this.dateInstalledView.setText(String.format("since %s", RandomUtilities.formatMSToDate
                (MyApplication.getOneTimeData().getTimeOfHobbesInstall())));

        this.timeSavedView = findViewById(R.id.todoManagementTimeSavedValue);
        String timeSaved = RandomUtilities.formatMillisecondsToHHMM(timeBank.getTimeSaved());
        this.timeSavedView.setText(String.format("%s", timeSaved));

        this.currentStreakValueView = findViewById(R.id.todoManagementCurrentStreakValue);
        this.streakDateView = findViewById(R.id.todoManagementDateStreakStarted);

        updateStreakValues();
    }

    @SuppressLint("SetTextI18n")
    private void updateStreakValues() {
        Streaks streak = MyApplication.getStreaks();
        this.currentStreakValueView.setText(Integer.toString(streak.getCurrentStreak()));

        long dateInMS = streak.getStreakDateInMS();

        this.streakDateView.setText(String.format("since %s", RandomUtilities.formatMSToDate(dateInMS)));


//        this.longestStreakValueView.setText(Integer.toString(streak.getLongestStreak()));

    }

    @Override
    protected void onResume() {
        initializeValueViews();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        super.onResume();

        /*
        if (!MyApplication.getOneTimeData().getHasDoneOnboardingKey()) {
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
    public void addEarnedTimeViewUpdate(TimeBankEarnedTime event) {
        Log.d(TAG, "makeTimeAvailableChanges: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeBank.getTimeRemaining()));
    }

    @Subscribe
    public void subtractEarnedTimeViewUpdate(TimeBankUnearnedTime event) {
        TimeBank timeBank = MyApplication.getTimeBank();
        long timeRemaining = timeBank.getTimeRemaining();
        if (timeRemaining < 0L) {
            timeRemaining = 0L;
        }
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(timeRemaining));
    }



    private void showTutorialSecondBanner() {
        String header = "ONE-OFF";
        String buttonText = "DONE";
        String explanation = "A one-off is for one-time todos. You can have as many as you want.\n\n" +
                "For every one-off you complete, you earn five more minutes of time on the apps you have limited.";
        LayoutInflater inflater = LayoutInflater.from(this);
        final View tutorialBannerView = inflater.inflate(R.layout.newtorial, null);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialHeading)).setText(header);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialNextButton)).setText(buttonText);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialExplanation)).setText(explanation);

        tutorialBannerView.findViewById(R.id.toDoViewGroup).setBackground(ContextCompat.getDrawable(this, R.drawable.one_off_todo_coloring));
        ((TextView) tutorialBannerView.findViewById(R.id.toDoDescription)).setText("I am a sample one-off.");
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialNumber)).setText("2/2");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MyApplication.getOneTimeData().setHasDoneTutorialKey(true);
            }
        });

        final AlertDialog tutorialBannerDialog = builder.create();
        tutorialBannerDialog.setView(tutorialBannerView);

        tutorialBannerView.findViewById(R.id.tutorialNextButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tutorialBannerDialog.dismiss();
                MyApplication.getOneTimeData().setHasDoneTutorialKey(true);
            }
        });
        tutorialBannerDialog.show();
    }

    private void showTutorialFirstBanner() {
        String header = "DAILY HABIT";
        String buttonText = "NEXT";
        String explanation = "A daily habit repeats daily. You can have at most five.\n\n" +
                "For every daily habit completed, you earn 15 minutes. Completing all of your daily habits makes your streak go up. Failure to do them all resets your streak to 0.";
        LayoutInflater inflater = LayoutInflater.from(this);
        final View tutorialBannerView = inflater.inflate(R.layout.newtorial, null);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialHeading)).setText(header);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialNextButton)).setText(buttonText);
        ((TextView) tutorialBannerView.findViewById(R.id.tutorialExplanation)).setText(explanation);
        ((TextView) tutorialBannerView.findViewById(R.id.toDoDescription)).setText("I am a sample daily habit.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showTutorialSecondBanner();
            }
        });
        final AlertDialog tutorialBannerDialog = builder.create();
        tutorialBannerDialog.setView(tutorialBannerView);

        tutorialBannerView.findViewById(R.id.tutorialNextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialBannerDialog.dismiss();
            }
        });
        tutorialBannerDialog.show();
    }

    @Subscribe
    public void updateStreak(StreakUpdateEvent event) {
        updateStreakValues();
    }

    @Subscribe
    public void showStreakDialog(StreakUpdateEvent event) {
        if (MyApplication.getStreaks().getCurrentStreak() != 0 && !((Activity) this).isFinishing()) {
            Data d = new StreakDialog();
            LayoutInflater inflater = LayoutInflater.from(this);
            final View dialogView = inflater.inflate(R.layout.first_time_completion_dialog, null);
            ((TextView) dialogView.findViewById(R.id.firstTimeCompletionDialogDescription)).setText(d.getDescription());
            ((TextView) dialogView.findViewById(R.id.firstTimeCompletionRewardMessage)).setText(d.getRewardMessage());

            dialogView.findViewById(R.id.firstTimeCompletionImage).setBackground(ContextCompat.getDrawable(this, d.getDrawable()));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final AlertDialog dialog = builder.create();
            dialog.setView(dialogView);
            dialogView.findViewById(R.id.firstTimeDialogDismiss).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    @Subscribe
    public void updateInfoViewValues(MidnightResetEvent e) {
        this.remainingTimeValueView.setText(RandomUtilities.formatMillisecondsToMinutes(MyApplication.getTimeBank().getInitialTime()));
        String timeSaved = RandomUtilities.formatMillisecondsToHHMM(MyApplication.getTimeBank()
                .getTimeSaved());
        this.timeSavedView.setText(String.format("%s", timeSaved));
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
        showTutorialFirstBanner();
    }



}
