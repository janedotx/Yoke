package com.system2override.yoke.ManageToDo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.Suggestion;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.SuggestionClickedEvent;
import com.system2override.yoke.OttoMessages.ToDoCreated;
import com.system2override.yoke.R;

import java.util.List;

public class ManageToDoScreen extends AppCompatActivity {
    public static String ADD_ACTION = "add";
    public static String ACTION_KEY = "action";
    private static final String TAG = "ManageToDoScreen";
    public Button saveButton;
    public EditText toDoEditText;
    public Bus bus;
    public RecyclerView suggestionsView;
    public SuggestionsAdapter adapter;
    public Suggestion usedSuggestion;
    public ActionBar bar;
    // visibility has been set to "none" in the xml view file
//    CheckBox dailyHabitCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_to_do);
        this.bus = MyApplication.getBus();
        this.bus.register(this);
        this.usedSuggestion = null;

        this.bar = getSupportActionBar();

        suggestionsView = (RecyclerView) findViewById(R.id.addSuggestionRecyclerView);
        suggestionsView.setLayoutManager(new LinearLayoutManager(this));

        List<Suggestion> suggestionList = MyApplication.getDb(this).suggestionDao().loadAllUnusedSuggestions();
        Log.d(TAG, "onCreate: num suggestions found " + Integer.toString(suggestionList.size()));
        this.adapter = new SuggestionsAdapter(this, suggestionList);
        suggestionsView.setAdapter(this.adapter);

        toDoEditText = findViewById(R.id.addToDoEditText);
//        dailyHabitCheckBox = findViewById(R.id.makeDailyHabitCheckBox);
//        /*

    }

    public void saveNewHabit() {
        HarnessDatabase dbConn = MyApplication.getDb(this);
        String toDoText = toDoEditText.getText().toString();
        Habit habit = new Habit();
        habit.setDescription(toDoText);
        int id = (int) dbConn.habitDao().insert(habit);
        Habit newHabit = dbConn.habitDao().getById(id);
        this.bus.post(new ToDoCreated(newHabit));

        if (this.usedSuggestion != null) {
            this.usedSuggestion.setUsed(true);
            dbConn.suggestionDao().update(this.usedSuggestion);
        }

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    ///*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String action = getAction();
        if (action ==  ADD_ACTION) {
            getMenuInflater().inflate(R.menu.add_todo_save, menu);

        } else {
            getMenuInflater().inflate(R.menu.edit_todo, menu);
        }
        return true;
    }
    //*/

    private String getAction() {
        Bundle b = getIntent().getExtras();
        if (b == null) {
            throw new RuntimeException("Action type must be specified. Add or edit?");
        }

        return b.getString(ACTION_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        String action = getAction();
        int id = item.getItemId();
        if (id == R.id.add_todo_menu_save) {
            saveNewHabit();
        }

        if (id == R.id.home) {
            if (action == EDIT_ACTION) {

            }
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onSuggestionClicked(SuggestionClickedEvent e) {
        this.toDoEditText.setText(e.suggestion.getText());
        this.usedSuggestion = e.suggestion;
    }


}
