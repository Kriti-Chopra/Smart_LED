package kriti.somevalue.com.tryproject;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class SecondActivity extends AppCompatActivity {

    Button btnOn,btnOff,btnDisconnect;
    SeekBar brightness;
    TextView txtSeekBar;

    String address=null;
    private ProgressDialog progress;

    BluetoothSocket btSocket=null;
    private boolean isBtConnected=false;

    BluetoothAdapter myBluetooth=null;


    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newIntent=getIntent();
        address=newIntent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        setContentView(R.layout.activity_second);

        btnOn=(Button) findViewById(R.id.btnOn);
        btnOff=(Button) findViewById(R.id.btnOff);
        btnDisconnect=(Button) findViewById(R.id.btnDisconnect);
        brightness=(SeekBar) findViewById(R.id.brightness);
        txtSeekBar=(TextView) findViewById(R.id.txtSeekBar);

        new ConnectBT().execute();

        btnOn.setOnClickListener(turnOnLed);
        btnOff.setOnClickListener(turnOffled);
        btnDisconnect.setOnClickListener(disconnect);

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b==true){
                    txtSeekBar.setText(String.valueOf(i));
                    try{
                        btSocket.getOutputStream().write(String.valueOf(i).getBytes());

                    }
                    catch (IOException e){
                        Log.e("LOG","problem here");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private View.OnClickListener disconnect=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(btSocket!=null){
                try{
                    btSocket.close();
                }catch (IOException e){
                    Toast.makeText(SecondActivity.this, "Error in disconneccting", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    };

    private View.OnClickListener turnOffled=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(btSocket!=null){
                try{
                    btSocket.getOutputStream().write("TF".toString().getBytes());
                }
                catch (IOException e){
                    Toast.makeText(SecondActivity.this, "Error in switching OFF", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private View.OnClickListener turnOnLed=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(btSocket!=null){
                try{
                    btSocket.getOutputStream().write("TO".toString().getBytes());
                }
                catch (IOException e){
                    Toast.makeText(SecondActivity.this, "Error in switching ON", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    //class to create connection
    private class ConnectBT extends AsyncTask<Void,Void ,Void>{

        private boolean ConnectSuccess=true;

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(SecondActivity.this,"Connecting..","Please wait!..");

        }

        @Override
        protected Void doInBackground(Void... devices) {
            try{

                if(btSocket==null || !isBtConnected){

                    myBluetooth=BluetoothAdapter.getDefaultAdapter();


                    BluetoothDevice dispositivo=myBluetooth.getRemoteDevice(address);


                    btSocket=dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);

                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

                }

            }catch (IOException e){
                ConnectSuccess=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            if(!ConnectSuccess){
                Toast.makeText(SecondActivity.this, "Connection Failed! not right bluetooth device", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(SecondActivity.this, "Connected! Good to Go.", Toast.LENGTH_SHORT).show();
                isBtConnected=true;
            }
            progress.dismiss();
        }
    }



}
