package com.system2override.hobbes;

import android.util.Log;

import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.system2override.hobbes.integrations.GoogleSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class GoogleSnapshotTests {
    private static final String TAG = "GoogleSnapshotTests";
    private MockGoogleCredential mockGoogleCredential;
    private JsonFactory jsonFactory;
    private utils.TestUtils mTestUtils;

    @Before
    public void before(){
        mockGoogleCredential = new MockGoogleCredential.Builder().build();
        PowerMockito.mockStatic(Log.class);
        jsonFactory =  JacksonFactory.getDefaultInstance();
        mTestUtils = new utils.TestUtils();
    }


    @Test
    public void testTakeSnapshot() {
    }


    private MockLowLevelHttpRequest getMocks(String s) {
        final String file = s;
        return new MockLowLevelHttpRequest() {
            @Override
            public MockLowLevelHttpResponse execute() throws IOException {
                MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                response.setContentType(Json.MEDIA_TYPE);
                if (file != "") {
                    response.setContent(mTestUtils.readJSONFile(file));
                } else {
                    response.setContent("");
                }
                return response;
            }
        };
    }

    public com.google.api.services.tasks.Tasks getComprehensiveTaskService() {
        HttpTransport mockTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                System.out.println("asdfadfas");
                System.out.println(url);
                if (Pattern.matches(".*lists\\?.*", url)) {
                    return getMocks("listOfTaskLists.json");
                } else if (Pattern.matches(".*MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDow.*", url)) {
                    return getMocks("listOfTasks0.json");
                } else if (Pattern.matches("MTEyOTkxOTk5NTY2NDI2OTkwMjI6NTk5NDMyMjUyODAxNzI1NTow", url)) {
                    return getMocks("listOfTasks1.json");
                } else if (Pattern.matches("MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDo2MDI5OTI1MTU4MjYxMjY3", url)) {
                    return getMocks("task0.json");
                } else if (Pattern.matches("MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDo5NTYyMTY2MTQ3NzkwOTg4", url)) {
                    return getMocks("task1.json");
                }
                return getMocks("");
            }
        };
        return new com.google.api.services.tasks.Tasks.Builder(
                mockTransport, jsonFactory, mockGoogleCredential)
                .setApplicationName("YokeTest")
                .build();
    }

    @Test
    public void tryComprehensiveTaskService() throws IOException {
        Tasks taskService = getComprehensiveTaskService();

        // testGoogleSnapshotGetListOfTasksLists
        List<TaskList> list = GoogleSnapshot.getTaskLists(taskService);
        assertEquals(2, list.size());

        // testGoogleSnapshotGetTasksFromList
        List<Task> tasksList = GoogleSnapshot.getTasksForList(taskService, "MTEyOTkxOTk5NTY2NDI2OTkwMjI6MDow", "");
        assertEquals(5, tasksList.size());
    }

}
