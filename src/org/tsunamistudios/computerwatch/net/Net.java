package org.tsunamistudios.computerwatch.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.net.Socket;

import org.tsunamistudios.computerwatch.processes.Program;

public class Net {
	
	private static Socket echoSocket;
	private static PrintWriter out;
    private static BufferedReader in;
	private static ObjectInputStream inFromServer;
	private String hostName = null;

	public void setSocket() {
		try {
            int portNumber = 6984;
        	echoSocket = new Socket(hostName, portNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
//	        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
	        inFromServer = new ObjectInputStream(echoSocket.getInputStream());    
	             
		} catch (IOException e) {
			// TODO Auto-generated catchaa block
			e.printStackTrace();
		}
	}
	
	public static Socket getEchoSocket() {
		return echoSocket;
	}
	
	public void killProcess(String s) {
		out.println(s + ".exe");
	}
	
	public Object getProgramsFromObject() {		
		try {
			if(getObjectInputStream() != null) {
				return getObjectInputStream().readObject();
			} else {
				return new Program("Nullpointer", "NullPointer", "Null", null);
			}
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static void setEchoSocket(Socket echoSocket) {
		Net.echoSocket = echoSocket;
	}

	public PrintWriter getOut() {
		return out;
	}

	public static void setOut(PrintWriter out) {
		Net.out = out;
	}

	public static BufferedReader getIn() {
		return in;
	}
	
    public ObjectInputStream getObjectInputStream() {
		return inFromServer;
	}

	public static void setInFromServer(ObjectInputStream inFromServer) {
		Net.inFromServer = inFromServer;
	}

	public static void setIn(BufferedReader in) {
		Net.in = in;
	}
	
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
