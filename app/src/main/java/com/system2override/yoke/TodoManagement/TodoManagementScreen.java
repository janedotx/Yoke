package com.system2override.yoke.TodoManagement;

import android.content.Intent;
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
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
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

    private TextView timeAvailableView;
    private Button goAddToDoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + System.identityHashCode(this));
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: bundle contents " + savedInstanceState.toString());
        }
        MyApplication.getBus().register(this);
        setContentView(R.layout.activity_todo_management);

        timeAvailableView = findViewById(R.id.todoManagementAvailableTime);

        TimeBank timeBank = MyApplication.getTimeBank();
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getAvailableTime()));

        HarnessDatabase db = MyApplication.getDb(this);
        //List<Habit> habits = db.habitDao().getAllHabitsCompletedBefore(today);
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

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TodoManagementScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        goAddToDoButton = findViewById(R.id.toDoManagementGoAddToDoButton);
        goAddToDoButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent i = new Intent(TodoManagementScreen.this, AddToDo.class);
                                                   startActivity(i);
                                               }
                                           }
        );
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
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getAvailableTime()));
    }

}
