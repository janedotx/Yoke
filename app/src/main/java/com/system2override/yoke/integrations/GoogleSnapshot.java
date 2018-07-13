package com.system2override.yoke.integrations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.system2override.yoke.TodoAppConstants;
import com.system2override.yoke.models.LocalTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSnapshot {
    public static final long MAX_LISTS = 10;
    private static final String TAG = "GoogleSnapshot";

    /* to initialize the Tasks service
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.tasks.Tasks.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Tasks API Android Quickstart")
                .build();
     */
    private static final String[] SCOPES = {TasksScopes.TASKS};

    public static List<TaskList> getTaskLists(Tasks mService) {
        try {
            TaskLists result = mService.tasklists().list()
                    .setMaxResults(MAX_LISTS)
                    .execute();
            List<TaskList> taskLists = result.getItems();
            return taskLists;
        } catch (IOException e) {
            return new ArrayList<TaskList>();
        }
    }

    public static List<LocalTask> getTasks(Tasks mService, String updatedMin) throws IOException {
        return new ArrayList<>();
    }

    // make sure device is online before calling ths.
    // make sure device has an account before calling this
    public static List<Task> getTasksForList(Tasks mService, String listID,
                                           String updatedMin) throws IOException {
        Log.d(TAG, "getTasksForList: ");
                 return mService.tasks()
                        .list(listID)
                        .setShowCompleted(true)
                        .setUpdatedMin(updatedMin)
                        .execute()
                        .getItems();
    }

    // get all task objects that are currently in google task, that have been updated since updatedMin
    // i don't know how to test this because i don't know how to mock the MockHTTPTransport blah blah
    // to return different responses depending on the particular URL, which is necessary because this
    // method will spawn two calls to two different API endpoints
    // this jsut seems messy and horrible like wtf do i do about the classloader, so i can read my
    // test JSON files?
    // i guess maybe move it out of the sharedTest directory?
    // also i cant really test the updatedMin stuff working since this is all mocked out, since the
    // code that actually takes updatedMin into account and does stuff differently is on the Google
    // Tasks server, not mine
    public static List<Task> takeSnapshot(Tasks mService, String updatedMin) throws IOException {
        List<TaskList> taskLists = getTaskLists(mService);

        List<Task> allTasks = new ArrayList<>();
        for (TaskList list:taskLists) {
            allTasks.addAll(getTasksForList(mService, list.getId(), updatedMin));
        }
        return allTasks;
    }

    public static int findNumTasksDone(List<Task> oldSnapshot, List<Task> newSnapshot) {

        return -1;
    }

}
