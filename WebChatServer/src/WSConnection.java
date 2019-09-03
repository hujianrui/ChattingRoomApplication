import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WSConnection {
	
		private InputStream in;
		private OutputStream out;
		
		
	    public WSConnection(Socket socket) throws IOException {
	        this.in = socket.getInputStream();
	        this.out = socket.getOutputStream();
	    }

	    public String readMessage() throws IOException {
	        DataInputStream stream = new DataInputStream(in);
	        try {
	            byte[] header = new byte[2];
	            stream.read(header, 0, 2);

	            System.out.println(header[0] & 0xf);
	            
	            if((header[0] & 0xf) == 8){
	            	return null;
	            }
	           
	            byte lenByte = header[1];

	            byte readLen = (byte) (lenByte & (byte) 127);
	            System.out.println(readLen);

	            int maskIdx = (readLen == (byte) 126) ? 2 : (readLen == (byte) 127 ? 8 : 0);

	            //get msg length
	            int msgLen = 0;
	            if (maskIdx > 0) {
	                byte[] temp = new byte[maskIdx];
	                msgLen = stream.read(temp, 0, maskIdx);
	            } else {
	                msgLen = readLen;
	            }

	            //get the decoding key
	            byte[] key = new byte[4];
	            for (int i = 0; i < 4; i++) {
	                key[i] = stream.readByte();
	            }

	            //decoded msg
	            byte[] msg = new byte[msgLen];
	            for (int i = 0; i < msgLen; i++) {
	                msg[i] = (byte) (stream.readByte() ^ key[i % 4]);
	            }
	            
	            String s = new String(msg);
	            System.out.println("reading:" + s);
	            
	            return s;
	            
	        }catch(IOException e) {
	            e.printStackTrace();
	        }
	       return "";
	    
	    }

	    //TODO: server cannot support username with spaces!!!
	    public void sendMessage(String JSON) throws IOException {
	    	System.out.println("Sending: "+JSON);
	    	
	        byte[] decoded = JSON.getBytes();

	        int msgLen = decoded.length;

	        byte[] header;

	        if(msgLen < 126) {
	            header = new byte[]{(byte) 129, (byte) msgLen};
	            System.out.println("Short Header");
	        }else if (msgLen < Short.MAX_VALUE) {
	            header = new byte[]{(byte) 129, (byte) 126, (byte) ((msgLen >> 8) & (byte) 255), (byte) (msgLen & (byte) 255)};
	        }else {
	            header = new byte[10];
	            header[0] = (byte) 129;
	            header[1] = (byte) 127;
	            for (int i = 2, j = 56; i < 10; i++, j -= 8) {
	                header[i] = (byte) ((msgLen >> j) & (byte) 255);
	            }
	        }

	        byte[] encoded = new byte[header.length + decoded.length];

	        int idx = 0;

	        for(int i = 0; i < header.length; i++, idx++) {
	            encoded[i] = header[i];
	        }

	        for(int i = 0; i < decoded.length; i++) {
	            encoded[idx++] = decoded[i];
	        }
	        
	
	        System.out.println("Start Write");
	        out.write(encoded);
	        System.out.println("Waiting");
	        
	        out.flush();
	        System.out.println("Sended: "+ encoded);
	    }

	    
	    public String getJSON(String s) {
	    	
	    	int i = s.indexOf(" ");
	    	
	    	if(i>0) {
 				String name = s.substring(0, i);
 				String message = s.substring(i, s.length());
 				String jsonMsg = "{ \"user\" : \"" + name + "\", \"message\" : \"" + message + "\"}";
 				
 				return jsonMsg;
 			}
 			return "";
	    }
	
}
