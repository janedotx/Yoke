package com.system2override.yoke.ManageToDo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system2override.yoke.Models.RoomModels.Suggestion;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.SuggestionClickedEvent;
import com.system2override.yoke.R;

import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionViewHolder>  {
    private Context context;
    private List<Suggestion> suggestions;
    public SuggestionsAdapter(Context context, List<Suggestion> suggestionList) {
        this.context = context;
        this.suggestions = suggestionList;
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Suggestion suggestion = this.suggestions.get(position);
        holder.suggestion.setText(suggestion.getText());
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion, parent, false);
        return new SuggestionViewHolder(v, context, new SuggestionViewHolder.SuggestionOnClickListener() {
            @Override
            public void onClick(int position) {
                Suggestion suggestion = SuggestionsAdapter.this.suggestions.get(position);
                MyApplication.getBus().post(new SuggestionClickedEvent(suggestion));
            }
        });
    }

}
