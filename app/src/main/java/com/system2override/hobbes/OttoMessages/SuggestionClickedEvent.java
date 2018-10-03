package com.system2override.hobbes.OttoMessages;

import com.system2override.hobbes.Models.RoomModels.Suggestion;

public class SuggestionClickedEvent {
    public Suggestion suggestion;
    public SuggestionClickedEvent(Suggestion suggestion) {
        this.suggestion = suggestion;
    }
}
