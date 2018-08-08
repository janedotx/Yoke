package com.system2override.yoke.AppLimit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.system2override.yoke.MainActivity;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;

import java.util.List;

public class AppLimitScreen extends AppCompatActivity implements AppLimitScreenView {
    private static final String TAG = "AppLimitScreen";
    private AppLimitScreenPresenter presenter;

    private TextView streaksView;
    private TextView todoOneView;
    private TextView todoTwoView;
    private TextView todoThreeView;

    private AppLimitTasks appLimitTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_limit_screen);
        appLimitTasks = new AppLimitTasks(MyApplication.getDb(this));

        setViews();
        populateViews();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppLimitScreen.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void setViews() {
        this.streaksView = findViewById(R.id.streak_number);
        this.todoOneView = findViewById(R.id.todo_one);
        this.todoTwoView = findViewById(R.id.todo_two);
        this.todoThreeView = findViewById(R.id.todo_three);
    }

    public void populateViews() {
        // TODO implement streak stuff later
        // TODO implement habit stuff
        List<ToDoInterface> todos = this.appLimitTasks.getToDos();

        Log.d(TAG, "populateViews: todos " + Integer.toString(todos.size()));
        Log.d(TAG, "populateViews: " + todos.get(0).getDescription());
        Log.d(TAG, "populateViews: " + todos.get(1).getDescription());
        Log.d(TAG, "populateViews: " + todos.get(2).getDescription());
        this.todoOneView.setText(todos.get(0).getDescription());
        this.todoTwoView.setText(todos.get(1).getDescription());
        this.todoThreeView.setText(todos.get(2).getDescription());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
