package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;
import com.system2override.yoke.models.LocalTask;
import com.system2override.yoke.models.TodoApp;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LocalTasksInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    TestDbWrapper mTestDbWrapper;
    utils.TestUtils mTestUtils;

    @Before
    public void setUpDb() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
        mTestUtils = new utils.TestUtils();
    }

    @After
    public void closeDb() throws IOException {
        mTestDbWrapper.tearDown();
    }

    @Test
    public void insertLocalTasksWorkProperly() {
        assertEquals(3, mTestDbWrapper.getDb().localTaskDao().loadAllLocalTasks().size());
        List<LocalTask> tasks = mTestDbWrapper.getDb().localTaskDao().loadAllLocalTasks();
        Log.d(TAG, "insertsWorkProperly: " + tasks.get(0).toString());
        assertEquals(1, mTestDbWrapper.getDb()
                .localTaskDao()
                .getLocalTasksForAppSince("test_gtasks", new DateTime(11000).toStringRfc3339()).size());
        assertEquals(3, mTestDbWrapper.getDb()
                .localTaskDao()
                .getIncompleteLocalTasksForApp("test_gtasks").size());
    }

    @Test public void completeTypeConverterWorks() {
        LocalTask t = mTestDbWrapper.getDb().localTaskDao().getLocalTasksByID(1);
        assertEquals(false, t.isCompleted());
        t.completed = true;
        mTestDbWrapper.getDb().localTaskDao().update(t);
        LocalTask t1 = mTestDbWrapper.getDb().localTaskDao().getLocalTasksByID(1);
        assertEquals(true, t1.isCompleted());

    }

    @Test
    public void testLocalTaskConstructor() throws IOException {
        final String json = mTestUtils.readJSONFile("task0.json");
        String taskListId = "MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDow";
        MockGoogleCredential mockGoogleCredential = new MockGoogleCredential.Builder().build();
        JsonFactory jsonFactory=  JacksonFactory.getDefaultInstance();
        HttpTransport mockTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
                        result.setContentType(Json.MEDIA_TYPE);
                        result.setContent(json);
                        return result;
                    }
                };
            }
        };
        com.google.api.services.tasks.Tasks mService = new com.google.api.services.tasks.Tasks.Builder(
                mockTransport, jsonFactory, mockGoogleCredential)
                .setApplicationName("YokeTest")
                .build();
        Task task = mService.tasks().get(taskListId, "MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDo2MDI5OTI1MTU4MjYxMjY3").execute();

        LocalTask localTask = new LocalTask(task, taskListId);
        assertEquals("reset time at midnight", localTask.getDescription());
        assertEquals("MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDow", localTask.getTaskListIdString());
        assertEquals(null, localTask.getParentID());
        assertEquals("gtasks", localTask.getTodoAppName());
        assertEquals(null, localTask.getDueDate());
        assertEquals("2018-07-05T22:01:53.000Z", localTask.getUpdatedAt());
        assertEquals("2018-07-05T22:01:53.000Z", localTask.getDateCompleted());
        assertEquals("MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDo2MDI5OTI1MTU4MjYxMjY3", localTask.getTodoAppIdString());
    }

    @Test
    public void testGetMostRecent() {
        LocalTask l = mTestDbWrapper.getDb().localTaskDao().getMostRecentlyUpdatedLocalTask();
        assertEquals("1969-12-31T21:00:10.000-05:00", l.getUpdatedAt());
    }
}
