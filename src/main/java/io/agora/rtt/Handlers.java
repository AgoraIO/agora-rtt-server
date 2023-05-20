package io.agora.rtt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONObject;

public class Handlers {
	static TaskList taskList = new TaskList();

	private static JSONObject getBodyJSON(HttpExchange he) {
		// Reads the request body and returns it as a JSONObject
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
			// Handles requests sent to http://localhost:<port>
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
			// Read JSON body
			JSONObject jsonObj = getBodyJSON(he);
			String channelName = (String) jsonObj.get("channelName");
			int UserId = Integer.valueOf(jsonObj.get("UserId").toString()); 

			// Add code here to check user privileges and payment options

			// If everything is OK, create and configure an RTT task
			RttTask rttTask = new RttTask(UserId, channelName);
			// Start the task
			RttResult result = rttTask.startTranscription(); 
			// Store the task in a task list for later actions
			if (result == RttResult.SUCCESS) taskList.addTask(rttTask);

			// Returned the task status in response
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
			// Read JSON body
			JSONObject jsonObj = getBodyJSON(he);
			String channelName = (String) jsonObj.get("channelName");
			
			// Retrieve the task for this channelName
			RttTask rttTask = taskList.getTask(channelName);
			// Stop the transcription task
			RttResult result = rttTask.stopTranscription();

			// Returned the task status in response
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
			// Read JSON body
			JSONObject jsonObj = getBodyJSON(he);
			String channelName = (String) jsonObj.get("channelName");

			// Retrieve the task for this channelName
			RttTask rttTask = taskList.getTask(channelName);
			// Query the task
			RttResult result = rttTask.queryTask();

			// Returned the task status in response
			String response = rttTask.status;
			int responseCode = (result == RttResult.SUCCESS) ? 200 : 500;
			he.sendResponseHeaders(responseCode, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}
	