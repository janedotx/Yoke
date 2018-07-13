package utils;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.services.tasks.Tasks;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestUtils {
    public static String FOO = "foo";
    public static Object loader;

    public static String getFOO() {
        return FOO;
    }

    public String readJSONFile(String file) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
        try {
            String str = IOUtils.toString(in, "UTF-8");
            System.out.println(str);
            return str;
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
            return "";
        }
    }

    /*
    public static Object setClassLoaderObject(Object o) {
        loader = o;
    }

    public static Object getClassLoaderObject() {
        return loader;
    }

    private static MockLowLevelHttpRequest getListofListsHttpRequest() {
        return new MockLowLevelHttpRequest() {
            @Override
            public MockLowLevelHttpResponse execute() throws IOException {
                MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                response.setContentType(Json.MEDIA_TYPE);
                response.setContent(readJSONFile("listOfTaskLists.json", getClassLoaderObject()));
                return response;
            }
        };
    }


//    /*



    public static com.google.api.services.tasks.Tasks getTaskService(String file, Object o) {
        final String contents = utils.TestUtils.readJSONFile(file, o);
        HttpTransport mockTransport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                System.out.println(url);
                return new MockLowLevelHttpRequest() {

                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
                        result.setContentType(Json.MEDIA_TYPE);
                        result.setContent(contents);
                        return result;
                    }
                };
            }
        };
        return new com.google.api.services.tasks.Tasks.Builder(
                mockTransport, jsonFactory, mockGoogleCredential)
                .setApplicationName("YokeTest")
                .build();
    }
    */
}
