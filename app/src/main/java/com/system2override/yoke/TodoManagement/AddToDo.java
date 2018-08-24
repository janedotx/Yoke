package com.system2override.yoke.TodoManagement;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.system2override.yoke.GeneralDebugging;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.MainActivity;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;

public class AddToDo extends AppCompatActivity {
    Button saveButton;
    EditText toDoEditText;
    // visibility has been set to "none" in the xml view file
//    CheckBox dailyHabitCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.addToDoHeader);

        saveButton = findViewById(R.id.addToDoSaveButton);
        toDoEditText = findViewById(R.id.addToDoEditText);
//        dailyHabitCheckBox = findViewById(R.id.makeDailyHabitCheckBox);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDoText = toDoEditText.getText().toString();
                Habit habit = new Habit();
                habit.setDescription(toDoText);
                MyApplication.getDb(AddToDo.this).habitDao().insert(habit);
                GeneralDebugging.printDb(MyApplication.getDb(AddToDo.this));

                Intent i = new Intent(AddToDo.this, TodoManagementScreen.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
