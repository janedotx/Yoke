package com.system2override.yoke.AppLimit;

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

import com.system2override.yoke.MainActivity;
import com.system2override.yoke.Models.RoomModels.Suggestion;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;
import com.system2override.yoke.TodoManagement.TodoManagementScreen;

import java.util.List;

public class AppLimitScreen extends AppCompatActivity  {
    private static final String TAG = "AppLimitScreen";

    private TableLayout table;

    private AppLimitTasks appLimitTasks;
    private View goToApp;

    private RecyclerView toDoRecyclerView;
    private ToDoReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomplete_streak_screen);
//        setContentView(R.layout.activity_app_limit_screen);

        LinearLayout appLimitLayout = (LinearLayout) findViewById(R.id.app_limit_screen);
        this.appLimitTasks = new AppLimitTasks(MyApplication.getDb(this));

        LayoutInflater inflater = LayoutInflater.from(this);
        /*
        switch(this.appLimitTasks.getType()) {
            case AppLimitTasks.NO_STREAK:
                topBox = inflater.inflate(R.layout.incomplete_streak_top_box, null);
                bottomBox = inflater.inflate(R.layout.incomplete_streak_bottom_box, null);
                break;
            default:
                topBox = inflater.inflate(R.layout.incomplete_streak_top_box, null);
                bottomBox = inflater.inflate(R.layout.incomplete_streak_bottom_box, null);

        }
        appLimitLayout.addView(topBox);
        appLimitLayout.addView(bottomBox);
        */

        toDoRecyclerView = (RecyclerView) findViewById(R.id.appLimitTodos);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.adapter = new ToDoReminderAdapter(this, this.appLimitTasks.calculateToDos());
        toDoRecyclerView.setAdapter(this.adapter);

        findViewById(R.id.appLimitGoToApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppLimitScreen.this, TodoManagementScreen.class);
                startActivity(i);
                finish();
            }
        });
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
