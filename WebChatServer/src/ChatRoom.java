import java.io.IOException;
import java.util.ArrayList;

public class ChatRoom {

	public String name;
	private ArrayList <String> msgHistory;
	private ArrayList <WSConnection> allClient;

	
	public ChatRoom(String name) throws IOException {
		this.name = name;
		msgHistory = new ArrayList<String>();
		allClient = new ArrayList <>();
	}
	
	public synchronized void addClient(WSConnection Client) {
		allClient.add(Client);
		System.out.println("allClient.size" + allClient.size());
	}
	
	public synchronized void removeClient(WSConnection Client) {
		allClient.remove(Client);
		System.out.println("allClient.size" + allClient.size());
	}
	
	public synchronized ArrayList <String> getMessageHistory(){
		return msgHistory;
	}
	
	
	public synchronized void update(String msg) throws IOException {
		System.out.println("allClient.size" + allClient.size());
		msgHistory.add(msg);
		
		for (WSConnection sc : allClient) {
			sc.sendMessage(sc.getJSON(msg));
		}
		System.out.println("sent all");
	}  
	
	
}
