package com.system2override.yoke.ManageToDo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.SuggestionClickedEvent;
import com.system2override.yoke.OttoMessages.ToDoCreated;
import com.system2override.yoke.R;

public class AddToDoScreen extends ManageToDoScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bar.setTitle(R.string.addToDoHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_todo_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_todo_menu_save) {
            saveNewHabit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onSuggestionClicked(SuggestionClickedEvent e) {
        this.toDoEditText.setText(e.suggestion.getText());
        this.usedSuggestion = e.suggestion;
    }

    public void saveNewHabit() {
        HarnessDatabase dbConn = MyApplication.getDb();
        String toDoText = toDoEditText.getText().toString();
        Habit habit = new Habit();
        habit.setDescription(toDoText);
        int id = (int) dbConn.habitDao().insert(habit);
        Habit newHabit = dbConn.habitDao().getById(id);
        if (dailyHabitCheckBox.isChecked()) {
            newHabit.setIsDailyHabit(true);
        } else {
            newHabit.setIsDailyHabit(false);
        }
        this.bus.post(new ToDoCreated(newHabit));

        if (this.usedSuggestion != null) {
            this.usedSuggestion.setUsed(true);
            dbConn.suggestionDao().update(this.usedSuggestion);
        }

        finish();
    }
}
