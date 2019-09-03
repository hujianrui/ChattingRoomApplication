import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;



public class WSResponse {
	
	public WSResponse(Socket socket, Request req) throws IOException, NoSuchAlgorithmException {
		 
		OutputStream out = socket.getOutputStream();
	
		out.write(("HTTP/1.1 101 Switching Protocols\r\n" + 
				"Upgrade: websocket\r\n" + 
				"Connection: Upgrade\r\n"+
				"Sec-WebSocket-Accept: ").getBytes());
		System.out.println("HandShake Key: " + req.getKey());
		String WSAccept = req.getKey()+"258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		System.out.println("HandShake Accept: " + WSAccept);
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		String encode = Base64.getEncoder().encodeToString(md.digest(WSAccept.getBytes()));
		out.write((encode + "\r\n\r\n").getBytes());
		System.out.println("HandShake sended");
	}	
		
}
