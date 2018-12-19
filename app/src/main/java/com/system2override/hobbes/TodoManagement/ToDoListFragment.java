package com.system2override.hobbes.TodoManagement;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system2override.hobbes.HarnessDatabase;
import com.system2override.hobbes.Models.RoomModels.Habit;
import com.system2override.hobbes.Models.RoomModels.LocalTask;
import com.system2override.hobbes.Models.ToDoInterface;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment {
    private static final String TAG = "ToDoListFragment";
    private RecyclerView toDoListView;
    private ToDoListAdapter adapter;
    public static String TAB_NUMBER = "tab_number";
    public static final int ALL_TODOS = 1;
    public static final int COMPLETED_TODOS = 2;
    public static final int INCOMPLETE_TODOS = 0;

    public ToDoListFragment() {}

    public static ToDoListFragment newInstance(int tab) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, tab);
        fragment.setArguments(args);
        return fragment;

    }

    private List<ToDoInterface> getToDoItems(int tab, HarnessDatabase db) {
        List<ToDoInterface> items = new ArrayList<>();
        List<Habit> incompleteHabits = db.habitDao().loadAllIncompleteHabits();
        List<Habit> completedHabits = db.habitDao().loadAllCompleteHabits();
        List<Habit> incompleteOneOffs = db.habitDao().loadAllIncompleteOneOffs();
        List<Habit> completedOneOffs = db.habitDao().loadAllCompleteOneOffs();
        switch(tab) {
            // all
            case ALL_TODOS:
                for (Habit h: incompleteHabits) {
                    items.add((ToDoInterface) h);
                }
                for (Habit h: completedHabits) {
                    items.add((ToDoInterface) h);
                }
                for (Habit h: incompleteOneOffs) {
                    items.add((ToDoInterface) h);
                }
                for (Habit h: completedOneOffs) {
                    items.add((ToDoInterface) h);
                }
                break;
            case COMPLETED_TODOS:
                for (Habit h: completedHabits) {
                    items.add((ToDoInterface) h);
                }
                for (Habit h: completedOneOffs) {
                    items.add((ToDoInterface) h);
                }

                break;
            case INCOMPLETE_TODOS:
                for (Habit h: incompleteHabits) {
                    items.add((ToDoInterface) h);
                }
                for (Habit h: incompleteOneOffs) {
                    items.add((ToDoInterface) h);
                }

        }

        return items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        View rootView = inflater.inflate(R.layout.todo_list_view, container, false);
        int tab = getArguments().getInt(TAB_NUMBER);
        HarnessDatabase db = MyApplication.getDb();

        List<ToDoInterface> items = getToDoItems(tab, db);
        this.toDoListView = (RecyclerView) rootView.findViewById(R.id.toDoListView);

        this.adapter = new ToDoListAdapter(activity, items, tab);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        toDoListView.setLayoutManager(mLayoutManager);
        toDoListView.setItemAnimator(new DefaultItemAnimator());
        toDoListView.setAdapter(adapter);
        this.toDoListView.setLayoutManager(mLayoutManager);
        return rootView;
    }
}
