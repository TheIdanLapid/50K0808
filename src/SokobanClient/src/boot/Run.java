package boot;

import client.SokobanClient;

public class Run {

	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		SokobanClient client = new SokobanClient();
		client.start(ip, port);
	}

}
