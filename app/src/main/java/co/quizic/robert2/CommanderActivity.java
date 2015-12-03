package co.quizic.robert2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        for(int i=0;i<commands.length;i++){
            String t = compile(commands[i]);
            BluetoothConnector.getInstance("").sendMessage(t);
        }

    }
    private HashMap<String,String> commandMap;
    private void setCommandMap(){
        commandMap = new HashMap<>();
        commandMap.put("forward","A");
        commandMap.put("back","B");
        commandMap.put("right","C");
        commandMap.put("left","D");

    }

    private String compile(String c){
        String[] cc = c.split(" ");
        int repeat = Integer.getInteger(cc[1]);
        String res = "";
        for(int i=0;i<repeat;i++){
            res+=commandMap.get(cc[0]);
        }
        return res;
    }
}
