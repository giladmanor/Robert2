package co.quizic.robert2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommanderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);
    }



    public void addCommand(View v){
        String t = ((Button)v).getText().toString();
        LinearLayout ll = (LinearLayout) findViewById(R.id.commandList);
        TextView tv = new TextView(this);

        tv.setText(t);
        ll.addView(tv);

    }


    public void sendBT(View v){

        LinearLayout ll = (LinearLayout) findViewById(R.id.commandList);
        for(int i=0;i<ll.getChildCount();i++){
            String t = ((TextView)ll.getChildAt(i)).getText().toString();
            BluetoothConnector.getInstance("").sendMessage(t);
        }

    }
}
