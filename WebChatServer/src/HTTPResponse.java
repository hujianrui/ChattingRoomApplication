import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPResponse {
	
	private File file;
	
	
	public HTTPResponse(Socket socket, Request req) throws IOException {
	
		OutputStream out = socket.getOutputStream();
	
		String[] elements = req.getFirstLine().split(" ");
 		String fileName = elements[1];
 		if(fileName.equals("/")) {
 			fileName = "/WebChatClient.html";
 		}
 		try {
 			file = new File("resources" + fileName);
		
 		if(!file.exists() || file.isDirectory()) {
 			String error = "HTTP/1.1 404 Not Found\r\n" +"Content-Length: " + "404".getBytes().length + "\r\n\r\n";
 			out.write(error.getBytes());
 			out.write("404".getBytes());
 			out.flush();
 			} 
 		}catch (FileNotFoundException ex) {
 			System.out.println("File not found. ");
 		}
		
		byte[] byteFile = new byte[(int) file.length()];
		BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
		out.write("HTTP/1.1 200 OK\n".getBytes());
		out.write(("Content-Length: " + byteFile.length + "\r\n\r\n").getBytes());
		buffer.read(byteFile, 0, byteFile.length);
		out.write(byteFile, 0, byteFile.length);
		out.flush();
		buffer.close();
		out.close();
	}
	

}
