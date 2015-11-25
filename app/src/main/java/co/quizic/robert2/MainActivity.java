package co.quizic.robert2;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private ServerBee server;


    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        //TextView ipText = (TextView) findViewById(R.id.ipText);
        //ipText.setText("http://"+ip+":"+ServerBee.SERVER_PORT);

        server = new ServerBee();
        server.addListenner(new ServerBee.Listener() {
            @Override
            public void read(String s) {

            }
        });
        server.start();


        if(!BluetoothConnector.isEnabled()){
            Snackbar.make(this.getCurrentFocus() , "BT is OFF", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }



        final ListView lv = (ListView) findViewById(R.id.btDevices);
        lv.setVisibility(View.VISIBLE);
        final Set<BluetoothDevice> devices = BluetoothConnector.getDeviceList();
        final ArrayList<String> values = new ArrayList<>();
        for(BluetoothDevice device : devices){
            values.add(device.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(), "Sending : " + itemValue, Toast.LENGTH_LONG).show();
                Log.d("SEND BT", values.get(itemPosition));

                for (BluetoothDevice device : devices) {
                    if (device.getName().equals(values.get(itemPosition))) {
                        BluetoothConnector.getInstance(device.getName()).connect();
                    }
                }
                SharedPreferences settings = getSharedPreferences("localStorage", 0);

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("bt_device", values.get(itemPosition));
                editor.commit();

                (findViewById(R.id.btDeviceSelector)).setVisibility(View.GONE);
                (findViewById(R.id.commandPane)).setVisibility(View.VISIBLE);


                //TextView btTV = (TextView) findViewById(R.id.textView2);
                //btTV.setText(values.get(itemPosition));

            }
        });



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


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
