package webServer;

public class Web implements Runnable {

	@Override
	public void run() {
		WebSocket ws = new WebSocket();
		try {
			WebSocket.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
