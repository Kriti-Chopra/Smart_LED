package kriti.somevalue.com.tryproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btnPaired;
    ListView deviceList;
    BluetoothAdapter myBluetooth;

     static String EXTRA_ADDRESS="device address";

    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPaired=(Button) findViewById(R.id.btnPaired);
        deviceList=(ListView) findViewById(R.id.deviceList);

        myBluetooth=BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth==null){
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            if(myBluetooth.isEnabled()){
                Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent turnBTon=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }

        btnPaired.setOnClickListener(pairedDeviceList);
    }


    private View.OnClickListener pairedDeviceList=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pairedDevices=myBluetooth.getBondedDevices();
            ArrayList list=new ArrayList();

            if(pairedDevices.size()>0){
                for (BluetoothDevice bt:pairedDevices){
                    list.add(bt.getName()+"\n"+bt.getAddress());//name and address of devices
                }
            }
            else{
                Toast.makeText(MainActivity.this, "No paired devices found", Toast.LENGTH_SHORT).show();
            }

            final ArrayAdapter<String> adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,list);
            deviceList.setAdapter(adapter);
            deviceList.setOnItemClickListener(myListClickListener);

        }
    };

    private AdapterView.OnItemClickListener myListClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String info=((TextView)view).getText().toString();
            String address=info.substring(info.length()-17);//mac address has 17 characters

            //if correct item clicked then move to next activity
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            intent.putExtra(EXTRA_ADDRESS,address);
           // Toast.makeText(MainActivity.this, "starting new activity", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    };
}
