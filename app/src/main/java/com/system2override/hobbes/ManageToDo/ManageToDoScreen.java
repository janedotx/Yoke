package com.system2override.hobbes.ManageToDo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.hobbes.Models.RoomModels.Suggestion;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.SuggestionClickedEvent;
import com.system2override.hobbes.R;

import java.util.List;

public class ManageToDoScreen extends AppCompatActivity implements View.OnClickListener {
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

    View makeDailyHabit;
    CheckBox dailyHabitCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_to_do);
        this.bus = MyApplication.getBus();
        this.bus.register(this);
        this.usedSuggestion = null;

        this.bar = getSupportActionBar();

        List<Suggestion> suggestionList = MyApplication.getDb().suggestionDao().loadAllUnusedSuggestions();
        suggestionsView = (RecyclerView) findViewById(R.id.addSuggestionRecyclerView);
        if (suggestionList.size() > 0) {
            this.adapter = new SuggestionsAdapter(this, suggestionList);
            suggestionsView.setAdapter(this.adapter);
            suggestionsView.setLayoutManager(new LinearLayoutManager(this));
            suggestionsView.setVisibility(View.VISIBLE);
        } else {
            suggestionsView.setVisibility(View.INVISIBLE);
        }


        toDoEditText = findViewById(R.id.addToDoEditText);
        makeDailyHabit = findViewById(R.id.makeDailyHabit);
        makeDailyHabit.setOnClickListener(this);
        dailyHabitCheckBox = findViewById(R.id.makeDailyHabitCheckBox);
//        /*

    }

    @Override
    public void onClick(View v) {
        if (dailyHabitCheckBox.isChecked()) {
            dailyHabitCheckBox.setChecked(false);
        } else {
            dailyHabitCheckBox.setChecked(true);
        }
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
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onSuggestionClicked(SuggestionClickedEvent e) {
        this.toDoEditText.setText(e.suggestion.getText());
        this.usedSuggestion = e.suggestion;
    }


}
