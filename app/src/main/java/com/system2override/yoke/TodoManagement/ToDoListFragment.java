package com.system2override.yoke.TodoManagement;


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

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.ToDoInterface;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment {
    private static final String TAG = "ToDoListFragment";
    private RecyclerView toDoListView;
    private ToDoListAdapter adapter;
    public static String TAB_NUMBER = "tab_number";
    public static final int ALL_TODOS = 0;
    public static final int COMPLETED_TODOS = 1;
    public static final int INCOMPLETE_TODOS = 2;

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
        List<Habit> habits = db.habitDao().loadAllHabits();
        switch(tab) {
            // all
            case 0:
                for (Habit h: habits) {
                    items.add((ToDoInterface) h);
                }
                break;
            case 1:
                for (Habit h: habits) {
                    if (h.isCompleted()) {
                        items.add((ToDoInterface) h);
                    }
                }
                break;
            case 2:
                for (Habit h: habits) {
                    if (!h.isCompleted()) {
                        items.add((ToDoInterface) h);
                    }
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
        HarnessDatabase db = MyApplication.getDb(activity);

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
