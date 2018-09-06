package com.system2override.yoke.OttoMessages;

import com.system2override.yoke.Models.RoomModels.Suggestion;

public class SuggestionClickedEvent {
    public Suggestion suggestion;
    public SuggestionClickedEvent(Suggestion suggestion) {
        this.suggestion = suggestion;
    }
}
