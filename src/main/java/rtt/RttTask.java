package rtt;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

public class RttTask {
    static final String appId = "9d2498880e934632b38b0a68fa2f1622"; 
    static final String appCertificate = "19c00334556448c79615cf35d53f8438"; 
    static final String customerId = "9312f615635a47b9a15fd6d1719ef13f"; 
    static final String customerSecret = "b5d6a5f4b9734a338b82d2a0b4ae4495"; 
    /* 
    private static final String appId = "<your app ID from Agora console>";
    private static final String appCertificate = "<your app certificate from Agora  console>";
    private static final String customerId = "<your customer ID from Agora console>";
    private static final String customerSecret = "<your customer secret from Agora console>";
    */
    // Cloud storage parameters
    private String ossSecretKey = "<Your oss secret key>";
    private String ossAccessKey = "<Your oss access key>";
    private String ossBucketName = "<Your oss bucket name>";
    private static final String baseUrl = "https://api.agora.io";

    private static final String authorizationHeader = "Basic " + new String(Base64.getEncoder()
            .encode((customerId + ":" + customerSecret).getBytes()));;
    
    public String channelName, status;
    public String language = "en-US", taskId = "";
    public int userId, maxIdleTime = 120;

    private String tokenAudio, tokenText, builderToken, instanceId;
    private int uidAudio = 111, uidText = 222;
    
    public RttTask(int userId, String channelName) {
        this.userId = userId;
        this.channelName = channelName;
        instanceId = channelName;
    }

    private RttResult getBuilderToken() throws IOException {
        String url = baseUrl + "/v1/projects/" + appId + "/rtsc/speech-to-text/builderTokens";

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create("{\n \"instanceId\" : \""
                + instanceId + "\" \n}", mediaType);
        Request request = new Request.Builder()
                .addHeader("Authorization", authorizationHeader)
                .url(url)
                .method("POST", body)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            if (response.isSuccessful()) {
                builderToken = jsonObject.getString("tokenName");
                return RttResult.SUCCESS;
            } else {
                status = jsonObject.getString("message");
                System.out.println("builderToken not received: " + status);
                return RttResult.FAILED_TO_FETCH_BUILDER_TOKEN;
            }
        }
    }

    public RttResult startTranscription() throws JSONException, IOException {
        // Get builderToken
        getBuilderToken();

        String url = baseUrl + "/v1/projects/" + appId
                + "/rtsc/speech-to-text/tasks?" + "builderToken=" + builderToken;

        // Get and set RTC tokens
        tokenAudio = TokenBuilder.getToken(appId, appCertificate, channelName, uidAudio, 3600);
        tokenText = TokenBuilder.getToken(appId, appCertificate, channelName, uidText, 3600);

        // Build Request body JSON
        JSONObject startConfig = new JSONObject()
                .put("audio", new JSONObject()
                        .put("subscribeSource", "AGORARTC")
                        .put("agoraRtcConfig", new JSONObject()
                                .put("channelName", channelName)
                                .put("uid", String.valueOf(uidAudio))
                                .put("token", tokenAudio)
                                .put("channelType", "LIVE_TYPE")
                                .put("subscribeConfig", new JSONObject()
                                        .put("subscribeMode", "CHANNEL_MODE"))
                                .put("maxIdleTime", maxIdleTime)))
                .put("config", new JSONObject()
                        .put("features", new JSONArray()
                                .put("RECOGNIZE"))
                        .put("recognizeConfig", new JSONObject()
                                .put("language", language)
                                .put("model", "Model")
                                .put("output", new JSONObject()
                                        .put("destinations", new JSONArray()
                                                .put("AgoraRTCDataStream")
                                                .put("Storage"))
                                        .put("agoraRTCDataStream",
                                                new JSONObject()
                                                        .put("channelName",
                                                                channelName)
                                                        .put("uid", String
                                                                .valueOf(uidText))
                                                        .put("token", tokenText))
                                        .put("cloudStorage", new JSONArray()
                                                .put(new JSONObject()
                                                        .put("format", "HLS")
                                                        .put("storageConfig",
                                                                new JSONObject()
                                                                        .put("accessKey", ossAccessKey)
                                                                        .put("secretKey", ossSecretKey)
                                                                        .put("bucket", ossBucketName)
                                                                        .put("vendor", 1) // Your Oss Vendor
                                                                        .put("region", 1) // Your Oss Region
                                                                        .put("fileNamePrefix",
                                                                                new JSONArray()
                                                                                        .put("directory1")
                                                                                        .put("directory2"))))))));

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(startConfig.toString(), mediaType);
        Request request = new Request.Builder()
                .addHeader("Authorization", authorizationHeader)
                .url(url)
                .method("POST", body)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    taskId = jsonObject.getString("taskId");
                    if (status.equals("IN_PROGRESS") || status.equals("STARTED")) {
                        System.out.println(
                                "RTT task started for channel: " + channelName + " ID: "
                                        + taskId);
                        return RttResult.SUCCESS;
                    } else {
                        System.out.println("RTT task status: " + status);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing start call response");
                }
            } else {
                System.out.println("Exception starting speech-to-text task: " + result);
            }
            return RttResult.FAILED_TO_START_TASK;
        }
    }

    public RttResult stopTranscription() {
        String url = baseUrl + "/v1/projects/" + appId
                + "/rtsc/speech-to-text/tasks/" + taskId
                + "?builderToken=" + builderToken;
        // MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorizationHeader)
                .url(url)
                .delete()
                .build();

        OkHttpClient httpClient = new OkHttpClient();
        try {
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                status = "STOPPED";
                return RttResult.SUCCESS;
            } else {
                status = "UNKNOWN";
                return RttResult.FAILED_TO_STOP_TASK;
            }
        } catch (IOException e) {
            status = "Exception stopping RTT task";
            System.out.println(status);
            return RttResult.FAILED_TO_STOP_TASK;
        }

    }

    public RttResult queryTask() {
        String url = baseUrl + "/v1/projects/" + appId
                + "/rtsc/speech-to-text/tasks/" + taskId
                + "?builderToken=" + builderToken;

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorizationHeader)
                .url(url)
                .get()
                .build();

        OkHttpClient httpClient = new OkHttpClient();
        try {
            Response response = httpClient.newCall(request).execute();
            String result = response.body().string();

            JSONObject jsonObject = new JSONObject(result);
            if (response.isSuccessful()) {
                status = jsonObject.getString("status");
                return RttResult.SUCCESS;
            } else {
                status = jsonObject.getString("message");
                return RttResult.FAILED_TO_QUERY_TASK;
            }
        } catch (IOException e) {
            status = "Exception querying RTT task";
            System.out.println(status);
            return RttResult.FAILED_TO_QUERY_TASK;
        }
    }
}
