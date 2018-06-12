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
    private static final String TAG = "GoogleSnapshot";

    /* to initialize the Tasks service
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.tasks.Tasks.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Tasks API Android Quickstart")
                .build();
     */
    private static final String[] SCOPES = {TasksScopes.TASKS_READONLY};

    // make sure device is online before calling ths.
    // make sure device has an account before calling this
    public static List<LocalTask> getTasks(Tasks mService,
                                           String updatedMin) throws IOException {
        Log.d(TAG, "getTasks: ");
        TaskLists result = mService.tasklists().list()
                .setMaxResults(Long.valueOf(10))
                .execute();
        Log.d(TAG, "getTasks: result "+ result.toPrettyString());
        List<TaskList> tasklists = result.getItems();
        List<LocalTask> allTasks = new ArrayList<LocalTask>();
        if (tasklists != null) {
            Log.d(TAG, "getTasks: tasks lists are not null");
            for (TaskList taskList : tasklists) {
                List<Task> resultingTasks = mService.tasks()
                        .list(taskList.getId())
                        .setShowCompleted(false)
                        .setUpdatedMin(updatedMin)
                        .execute()
                        .getItems();
                for (int i = 0; i < resultingTasks.size(); i++) {
                    Log.d(TAG, "getTasks: about to convert items");
                    Task resultingTask = resultingTasks.get(i);
                    Log.d(TAG, "getTasks: " + resultingTask.getTitle());
                    Log.d(TAG, "getTasks: " + resultingTask.getStatus());
                    allTasks.add(convertToLocalTask(resultingTask));
                }
            }
        }
        return allTasks;
    }


    private static LocalTask convertToLocalTask(Task task) {
        LocalTask localTask = new LocalTask();

        localTask.setCompleted((task.getCompleted() != null));
        if (task.getCompleted() != null) {
            localTask.setDateCompleted(task.getCompleted().toStringRfc3339());
        }
        localTask.setUpdatedAt(task.getUpdated().toStringRfc3339());
        localTask.setTodoApp(TodoAppConstants.GTASKS);

        return localTask;
    }



}
