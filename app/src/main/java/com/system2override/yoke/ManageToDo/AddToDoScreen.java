package com.system2override.yoke.ManageToDo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.OttoMessages.SuggestionClickedEvent;
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
}
