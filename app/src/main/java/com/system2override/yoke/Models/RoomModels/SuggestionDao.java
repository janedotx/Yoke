package com.system2override.yoke.Models.RoomModels;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SuggestionDao {
    @Insert
    long insert(Suggestion suggestion);

    @Insert
    void insert(Suggestion... suggestion);

    @Update
    public void updateSuggestions(Suggestion... suggestions);

    @Update
    public void updateSuggestions(List<Suggestion> suggestions);

    @Update
    public void update(Suggestion suggestion);

    @Query("SELECT * FROM Suggestions")
    public List<Suggestion> loadAllSuggestions();

    @Query("SELECT * FROM Suggestions where type = :type")
    public List<Suggestion> loadAllSuggestionsWithType(int type);

    @Query("SELECT * FROM Suggestions where used = 0")
    public List<Suggestion> loadAllUnusedSuggestions();
}
