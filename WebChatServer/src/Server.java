import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Server {

	public static HashMap<String, ChatRoom> rooms = new HashMap<String, ChatRoom> ();
	
	
		//set up and run the server
		public static void main(String[] args) {
			try { 
				ServerSocket server = new ServerSocket(8080);
				
				while(true) {
						Socket socket = server.accept();
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Request req = new Request(socket);
									if(req.isWebSocket()) {
										System.out.println("It is a Web Socket Request");
										WSResponse WSRes = new WSResponse(socket,req);
										WSConnection WSCnt = new WSConnection(socket);
										String msg = WSCnt.readMessage();
										
										String[] joinmsg = msg.split(" ");
								    	String roomName;
								    	
										if(joinmsg[0].equals("join")) {
											roomName = joinmsg[1];
											
											if(rooms.containsKey(roomName)) {
												rooms.get(roomName).addClient(WSCnt);
												System.out.println(" Join room: " + roomName);
												for(String message : rooms.get(roomName).getMessageHistory()) {
													WSCnt.sendMessage(WSCnt.getJSON(message));
												}
												
											}else {
												ChatRoom room = new ChatRoom(roomName);
								                room.addClient(WSCnt);
								                rooms.put(roomName, room);
								                System.out.println(" Creat room: " + roomName);
											}	
										}else {
											System.out.println("Room Name Error");
											WSCnt.sendMessage("Room Name Error");
											return;
										} 
								
										while(true) {
											String chatmsg = WSCnt.readMessage();
											if(chatmsg == null) {
												rooms.get(roomName).removeClient(WSCnt);
											}else {
												rooms.get(roomName).update(chatmsg);
											}
										}
										
									}else {
										HTTPResponse HTTPRes = new HTTPResponse(socket,req);
									}
									
								}catch(IOException | NoSuchAlgorithmException ex) {
									ex.printStackTrace();
								}
							}
						}).start();
						System.out.println(" Server ended the connection! ");
				}
			}catch(IOException ex) {
				System.out.println("Can't setup server on this port number. ");
				ex.printStackTrace();
			}
		}

		
		public HashMap<String, ChatRoom> getRooms(){
			return rooms;
		}

		
}
