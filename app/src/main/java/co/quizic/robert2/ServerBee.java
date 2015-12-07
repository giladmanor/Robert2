package co.quizic.robert2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Gilad Manor on 10/16/2015.
 */
public class ServerBee extends Thread{
    public static final int SERVER_PORT = 8874;
    private ServerSocket serverSocket;
    private ArrayList<Listener> listeners = new ArrayList();
    private Socket socket = null;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = serverSocket.accept();
                CommunicationThread commThread = new CommunicationThread(socket);
                new Thread(commThread).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void write(String s) throws IOException {
        OutputStream ops = this.socket.getOutputStream();
        ops.write(s.getBytes());
    }


    private class CommunicationThread implements Runnable{
        private Socket socket;
        private BufferedReader input;

        public CommunicationThread(Socket s){
            this.socket = s;

            try {
                this.input = new BufferedReader((new InputStreamReader(this.socket.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            while (!Thread.currentThread().isInterrupted()){
                try{
                    String line = input.readLine();
                    for(Listener l : listeners){
                        l.read(line);
                    }
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }

        }
    }

    public interface Listener {
        void read(String s);
    }

    public void addListenner(Listener l){
        this.listeners.add(l);
    }

}
