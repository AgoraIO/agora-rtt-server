import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {
	public static int port = 80;
	public static void main(String[] args) {
		// start http server
		SimpleHttpServer httpServer = new SimpleHttpServer();
		httpServer.Start(port);
	}

	static class SimpleHttpServer {
		private HttpServer server;
	
		public void Start(int port) {
			try {
				server = HttpServer.create(new InetSocketAddress(port), 0);
				System.out.println("RTT server started on port " + port);
				server.createContext("/", new Handlers.RootHandler());
				server.createContext("/rttStart", new Handlers.StartHandler());
				server.createContext("/rttQuery", new Handlers.QueryHandler());
				server.createContext("/rttStop", new Handlers.StopHandler());
				server.setExecutor(null);
				server.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		public void Stop() {
			server.stop(0);
			System.out.println("server stopped");
		}
	}
}
