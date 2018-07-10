package com.system2override.yoke;

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
import com.google.api.services.tasks.model.TaskList;
import com.system2override.yoke.integrations.GoogleSnapshot;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class GoogleSnapshotTests {
    private static final String TAG = "GoogleSnapshotTests";

    private String readJSONFile(String file) {
        System.out.println("readjsonfile");
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("listOfTaskLists.json");
        try {
            String str = IOUtils.toString(in, "UTF-8");
            System.out.println("STRRR");
            System.out.println(str);
            return str;
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
        /*
        try {
            java.nio.file.Path path = Paths.get(".", "sample_json", file);
            byte[] bytes = java.nio.file.Files.readAllBytes(path);
            String str = new String(bytes);
            return str;

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("ioexception");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
        */
        return "";
    }

    @Test
    public void testAPI() {
        PowerMockito.mockStatic(Log.class);

        MockGoogleCredential mockGoogleCredential = new MockGoogleCredential.Builder().build();
        HttpTransport mockTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
                        result.setContentType(Json.MEDIA_TYPE);
                        String json = readJSONFile("listOfTaskLists.json");
                        System.out.println(json);
                        result.setContent(json);
                        return result;
                    }
                };
            }
        };
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        com.google.api.services.tasks.Tasks mService = new com.google.api.services.tasks.Tasks.Builder(
                mockTransport, jsonFactory, mockGoogleCredential)
                .setApplicationName("Google Tasks API Android Quickstart")
                .build();

        List<TaskList> list = GoogleSnapshot.getTaskLists(mService);
        assertEquals(2, list.size());
        /*
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.tasks.Tasks.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Tasks API Android Quickstart")
                .build();
                */
    }
}
