import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Connect {
	
	private SocketChannel connection;
	
	public Connect(ServerSocketChannel server) {
		try {
			System.out.println(" Waitting for someone to connect...  ");
			connection = server.accept();
			System.out.println(" Now connectioned to " + connection.socket().getInetAddress().getHostName());
		} catch (IOException ex) {
			System.out.println("Can't accept client connection. ");
		}
	}

	public SocketChannel getSocket() {
		return this.connection;
	}
	
}
