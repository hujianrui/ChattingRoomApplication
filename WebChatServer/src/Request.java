import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Request {
	
	private Scanner in;
	private boolean isWebSocket;
	private Map<String,String> infoMap;
	private String firstLine;
	
	
	public Request(Socket socket) throws IOException, NoSuchAlgorithmException {
		try {
			in = new Scanner(socket.getInputStream());
		}catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
        }
		firstLine = in.nextLine();
		infoMap = new HashMap<>();
		while (true) {
			String currLine = in.nextLine();
			if(currLine.equals("")) {
				break;
			}
			int colonIndex = currLine.indexOf(":");
			infoMap.put(currLine.substring(0,colonIndex).trim(),currLine.substring(colonIndex + 1).trim());
		}
		System.out.println(" Stream are now steup ");
		isWebSocket = false;
		if(infoMap.containsKey("Sec-WebSocket-Key")) {
			isWebSocket = true;
		}
	}
	
	
	public String getKey() {
		return infoMap.get("Sec-WebSocket-Key");
	}
	
	public boolean isWebSocket() {
		return isWebSocket;
	}
	
	public String getFirstLine() {
		return firstLine;
	}
	
}
