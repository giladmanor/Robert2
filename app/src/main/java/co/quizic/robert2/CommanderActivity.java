package co.quizic.robert2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class CommanderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);
        setCommandMap();
    }



    public void addCommand(View v){
        String t = ((Button)v).getText().toString() + " 1";
        EditText ll = (EditText) findViewById(R.id.editText);
        ll.setText(ll.getText() + "\n" + t);

    }


    public void sendBT(View v){

        EditText ll = (EditText) findViewById(R.id.editText);
        String[] commands = ll.getText().toString().split("\n");

        ArrayList<String> sequence = new ArrayList<>();
        for(int i=0;i<commands.length;i++){
            String t = compile(commands[i]);
            Log.d("SENDING:", t);
            sequence.add(t);

        }
        sequence.add(".");

        final ArrayList<String> seq = sequence;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(String s : seq){
                    BluetoothConnector.getInstance("").sendMessage(s);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();



    }
    private HashMap<String,String> commandMap;
    private void setCommandMap(){
        commandMap = new HashMap<>();
        commandMap.put("forward","c");
        commandMap.put("back","d");
        commandMap.put("right","a");
        commandMap.put("left", "b");
        commandMap.put("stop",".");

    }

    private String compile(String c){

        String[] cc = c.split(" ");

        try{
            //Log.d("command",cc[0]);
            //Log.d("repeat",cc[1]);

            //Log.d("command",commandMap.get(cc[0]));
            //Log.d("repeat",Integer.parseInt(cc[1].trim())+"");

            int repeat = Integer.parseInt(cc[1]);
            String res = "";
            for(int i=0;i<repeat;i++){
                res+=commandMap.get(cc[0]);
            }
            return res;
        }catch (Exception e){
            Log.e("COMPILE",c);
            return "";
        }


    }
}
