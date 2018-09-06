package com.system2override.yoke.ManageToDo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.GeneralDebugging;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.SuggestionClickedEvent;
import com.system2override.yoke.OttoMessages.ToDoDeleted;
import com.system2override.yoke.OttoMessages.ToDoEdited;
import com.system2override.yoke.R;

public class EditToDoScreen extends ManageToDoScreen {
    private static final String TAG = "EditToDoScreen";
    public static String ID_KEY = "id";
    public static String ADAPTER_POSITION_KEY = "pos";

    private int toDoId;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.toDoId = getIntent().getExtras().getInt(EditToDoScreen.ID_KEY);
        this.adapterPosition = getIntent().getExtras().getInt(EditToDoScreen.ADAPTER_POSITION_KEY);

        this.bar.setTitle(R.string.editToDoHeader);
        this.toDoEditText.setHint("");

        ToDoInterface todo = MyApplication.getDb(this).habitDao().getById(this.toDoId);
        this.toDoEditText.setText(todo.getDescription());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_todo, menu);
        return true;
    }

    private void saveEdit() {
        // update in db
        HarnessDatabase dbConn = MyApplication.getDb(this);
        Habit habit = dbConn.habitDao().getById(this.toDoId);
        habit.description = toDoEditText.getText().toString();
        dbConn.habitDao().update(habit);
        GeneralDebugging.printDb(dbConn);

        this.bus.post(new ToDoEdited(this.toDoId, this.adapterPosition));

        if (this.usedSuggestion != null) {
            this.usedSuggestion.setUsed(true);
            dbConn.suggestionDao().update(this.usedSuggestion);
        }

    }

    private void deleteToDo() {
        MyApplication.getDb(this).habitDao().delete(this.toDoId);
        this.bus.post(new ToDoDeleted(this.toDoId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: id  is " + Integer.toString(id));
        if (id == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: home pressed");
            saveEdit();
        }

        if (id == R.id.edit_todo_menu_delete) {
            deleteToDo();
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onSuggestionClicked(SuggestionClickedEvent e) {
        this.toDoEditText.setText(e.suggestion.getText());
        this.usedSuggestion = e.suggestion;
    }
}
