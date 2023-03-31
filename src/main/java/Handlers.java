import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONObject;

import rtt.TaskList;
import rtt.RttResult;
import rtt.RttTask;

public class Handlers {
	static TaskList taskList = new TaskList();

	private static JSONObject getBodyJSON(HttpExchange he) {
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(he.getRequestBody(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		BufferedReader br = new BufferedReader(isr);
		String body = br.lines()
				.collect(Collectors.joining(System.lineSeparator()));

		// Parse request body to JSON
		JSONObject jsonObj = new JSONObject(body);
		return jsonObj;
	}

	public static class RootHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			String response = "<h1>Agora RTT Server Demo</h1>" + "<h2>Port: "
					+ Main.port + "</h2>";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public static class StartHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			JSONObject jsonObj = getBodyJSON(he);
			// Configure real-time transcription
			RttTask rttTask = new RttTask();
			rttTask.channelName = (String) jsonObj.get("channelName");
			rttTask.instanceId = "rtt" + rttTask.channelName;

			// Identify the user sending the request
			int UserId = Integer.valueOf(jsonObj.get("UserId").toString()); 
			// Add code here to check user privileges and payment options
			// If everything is OK, get a builderToken to start real-time transcription
			RttResult result = rttTask.startTranscription(); 
			if (result == RttResult.SUCCESS) taskList.addTask(rttTask);

			String response = rttTask.status;
			int responseCode = (result == RttResult.SUCCESS) ? 200 : 500;
			he.sendResponseHeaders(responseCode, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public static class StopHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			JSONObject jsonObj = getBodyJSON(he);
			String channelName = (String) jsonObj.get("channelName");

			RttTask rttTask = taskList.getTask(channelName);
			RttResult result = rttTask.stopTranscription();

			String response = rttTask.status;
			int responseCode = (result == RttResult.SUCCESS) ? 200 : 500;
			he.sendResponseHeaders(responseCode, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public static class QueryHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			JSONObject jsonObj = getBodyJSON(he);
			String channelName = (String) jsonObj.get("channelName");

			RttTask rttTask = taskList.getTask(channelName);
			RttResult result = rttTask.queryTask();

			String response = rttTask.status;
			int responseCode = (result == RttResult.SUCCESS) ? 200 : 500;
			he.sendResponseHeaders(responseCode, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}
	