package co.quizic.robert2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnector extends Thread {
    private static BluetoothConnector thisInstance = null;
    OutputStream outputStream;
    InputStream inputStream;
    private String TAG = "BluetoothConnector";
    private BluetoothAdapter adapter = null;
    private BluetoothDevice device = null;
    private boolean remoteDeviceFound = false;
    private boolean connected = false;
    private List<Listener> listeners = new ArrayList<>();
    private char READ_LINE_DELIMITER = '#';

    private BluetoothConnector(String name){
        thisInstance = this;
        try {
            String remoteDeviceName = name;
            adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                Log.e(TAG,"Phone does not support bluetooth!!");
                return;
            }
            if (!adapter.isEnabled()) {
                Log.e(TAG, "Bluetooth is not activated!!");
            }

            Set<BluetoothDevice> paired = adapter.getBondedDevices();
            if (paired.size() > 0) {
                for (BluetoothDevice d : paired) {
                    if (d.getName().equals(remoteDeviceName)) {
                        device = d;
                        remoteDeviceFound = true;
                        break;
                    }
                }
            }

        }catch (Exception e){
            Log.e(TAG,"[#]Error Connecting to Bluetooth device! : " , e);
        }
    }

    public static BluetoothConnector getInstance(String n){
        return thisInstance == null ? new BluetoothConnector(n) : thisInstance;
    }

    public static Set<BluetoothDevice> getDeviceList(){
        return BluetoothAdapter.getDefaultAdapter().getBondedDevices();
    }

    public static boolean isEnabled(){
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public void addListener(Listener l){
        listeners.add(l);
    }

    public boolean connect(){
        if(!remoteDeviceFound)
            return false;
        try{
            Log.d(TAG, "Connecting to remote device...");

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            connected = true;
            this.start();
            Log.d(TAG, adapter.getName());
            return true;


        }catch (Exception e){
            Log.e(TAG, "Error while connecting: ", e);
            return false;
        }
    }

    public void run(){
        while (connected) {

            try {
                byte ch, buffer[] = new byte[100];
                int i = 0;

                while((ch=(byte) inputStream.read()) != READ_LINE_DELIMITER){
                    buffer[i++] = ch;
                }
                buffer[i] = '-';
                final String msg = new String(buffer);

                //Log.d(TAG, "message::["+msg.trim()+"]");

                for(Listener l : listeners){
                    l.set(msg.trim());
                }

                //Log.d(TAG, "[BT " + inputStream.available() + " ]:" + msg.trim());



            } catch (IOException e) {
                Log.e(TAG, "IOException in receive message: " , e);
                connect();
            }catch (Exception e){
                Log.e(TAG, "Exception in receive message: " , e);
                e.printStackTrace();
                connect();
            }
        }

    }

    public void sendMessage(String msg){
        try {
            if(connected) {
                outputStream.write(msg.getBytes());
            }

        } catch (IOException e){
            Log.e(TAG, "Error while sending message: " ,e);
        }
    }

    public char getDelimiter(){
        return READ_LINE_DELIMITER;
    }

    public void setDelimiter(char d){
        READ_LINE_DELIMITER = d;
    }
    public interface Listener{
        void set(String s);
    }

}