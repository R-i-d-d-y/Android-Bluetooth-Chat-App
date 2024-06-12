package com.example.bluetoothchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ThirdActivity extends AppCompatActivity {
    TextView full2, display2;
    ImageView img;
    Intent inte;
    private AlertDialog.Builder alertbuilder;
    private ImageButton backImage;
    private ImageButton profileImageButton;
    private SwitchCompat bluetoothSwitch;
    DrawerLayout drawerLayout1;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Uri uri;
    TextView userName;
    AlertDialog dialog;
    Button button;

    ImageView userPhoto;

    BluetoothAdapter  adapter;
    Set<BluetoothDevice> ad;
    private  static  final int REQUEST_ENABLE_BLUETOOTH=0;
    //sarthi code
   Button listen,send, listDevices;
    ListView listView,listView2;
    TextView msg_box,status;
    EditText writeMsg;
    BluetoothDevice[] btArray;

    SendReceive sendReceive;
    private ArrayAdapter<String> adapterMainChat;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    public static final int MESSAGE_WRITE = 5;
    public static final int MESSAGE_READ = 6;
    public static final int MESSAGE_DEVICE_NAME = 7;
   // public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "deviceName";

   // int REQUEST_ENABLE_BLUETOOTH=1;
   private Context context;
    private String connectedDevice;
    private static final String APP_NAME = "BTChat";
    private static final UUID MY_UUID=UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        img = (ImageView) findViewById(R.id.profileid);
        display2=(TextView)findViewById(R.id.appid) ;
        inte = getIntent();
        Uri uri = (Uri) inte.getParcelableExtra("img");
        img.setImageURI(uri);
        // Bundle b1=getIntent().getExtras();
        //full2.setText(b1.getString("fullnme"));


        Toolbar toolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        Bundle b2=getIntent().getExtras();
        display2.setText(b2.getString("dispnme"));

        backImage = (ImageButton) findViewById(R.id.backid);
        bluetoothSwitch = findViewById(R.id.switchid);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        profileImageButton = (ImageButton) findViewById(R.id.imagebuttonid);


       /* listView = (ListView) findViewById(R.id.listid);
        available=(Button)findViewById(R.id.paired);
        exeButton();*/

        drawerLayout1 = findViewById(R.id.drawerlayout);
        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout1.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.navigationid);

         View headerView=navigationView.getHeaderView(0);
          userPhoto=(ImageView) headerView.findViewById(R.id.profile3id);
          userName=headerView.findViewById(R.id.nameid);
         Intent inte1=getIntent();
        Uri urii = (Uri) inte1.getParcelableExtra("img");
         userPhoto.setImageURI(uri);
        Bundle b1=getIntent().getExtras();
         userName.setText(b1.getString("fullnme"));
        drawerToggle = new ActionBarDrawerToggle(ThirdActivity.this, drawerLayout1, R.string.open, R.string.close);
        drawerLayout1.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
//sarthi code
        findViewByIdes();
        adapter= BluetoothAdapter.getDefaultAdapter();
        if(adapter==null)
        {
            Toast.makeText(this,"Bluetooth is not supported",Toast.LENGTH_SHORT);
            finish();
        }
        else {
            Toast.makeText(this,"Bluetooth is supported",Toast.LENGTH_SHORT);
        }
        bluetoothSwitch.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if(!adapter.isEnabled())
                    {
                        Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(i,0);

                    }

                }
                else {
                    adapter.disable();
                    Toast.makeText(ThirdActivity.this,"Turned Off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        implementListeners();



    }
/*
    private void exeButton() {
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad = ad= adapter.getBondedDevices();
                String[] strings=new String[ad.size()];
                int index=0;
                if(ad.size()>0)
                {
                    for(BluetoothDevice device:ad)
                    {
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
    }
*/
    public void profile(View view)
    {
        Intent i1=new Intent(ThirdActivity.this,EditProfile.class);
        Intent inte1=getIntent();
        Uri urii = (Uri) inte1.getParcelableExtra("img");
        i1.putExtra("image",urii);
        startActivityForResult(i1,1);
    }
    public void onActivityResult(int requestcode,int resultcode,Intent data) {

        super.onActivityResult(requestcode, resultcode, data);

        if(requestcode==1)
        {
            if(resultcode==RESULT_OK)
            {
                navigationView = findViewById(R.id.navigationid);
                View headerView=navigationView.getHeaderView(0);
                userPhoto=(ImageView) headerView.findViewById(R.id.profile3id);
                userName=headerView.findViewById(R.id.nameid);
                Intent inte1=getIntent();
                Uri urii = (Uri) data.getParcelableExtra("imgg");
                userPhoto.setImageURI(urii);
                Bundle bundle=data.getExtras();
                userName.setText(bundle.getString("full"));
                display2.setText(bundle.getString("disp"));
                img.setImageURI(urii);

            }
        }
    }
    public void onClick2(View view)
    {

    }
    public void onClick3(View view)
    {
        Intent intent=new Intent(this,About.class);
        startActivity(intent);
    }
    public void onClick4(View view)
    {
        alertbuilder=new AlertDialog.Builder(ThirdActivity.this);
        alertbuilder.setCancelable(false);
        alertbuilder.setTitle("Log Out");
        alertbuilder.setMessage("Do you want to log out....?");
        alertbuilder.setIcon(R.drawable.what);
        alertbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

            }

        });

        alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        AlertDialog alert= alertbuilder.create();
        alert.show();
    }

    //sarthi code
    private void implementListeners() {

        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView2=(ListView)findViewById(R.id.list2) ;
                Set<BluetoothDevice> bt=adapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;

                if( bt.size()>0)
                {
                    for(BluetoothDevice device : bt)
                    {
                        btArray[index]= device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass=new ClientClass(btArray[i]);
                clientClass.start();

                status.setText("Connecting");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string= String.valueOf(writeMsg.getText());
                sendReceive.write(string.getBytes());
            }
        });
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case MESSAGE_WRITE:
                    byte[] buffer1 = (byte[]) msg.obj;
                    String outputBuffer = new String(buffer1);
                    adapterMainChat.add("Me: " + outputBuffer);
                    break;
                case MESSAGE_READ:
                    byte[] buffer = (byte[]) msg.obj;
                    String inputBuffer = new String(buffer, 0, msg.arg1);
                    adapterMainChat.add(connectedDevice + ": " + inputBuffer);
                    break;
                case MESSAGE_DEVICE_NAME:
                    connectedDevice = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    private void findViewByIdes() {
        listen=(Button) findViewById(R.id.listen);
        send=(Button) findViewById(R.id.send);
        listView=(ListView) findViewById(R.id.listview);
        //msg_box =(TextView) findViewById(R.id.msg);
        status=(TextView) findViewById(R.id.status);
        writeMsg=(EditText) findViewById(R.id.writemsg);
        listDevices=(Button) findViewById(R.id.listDevices);
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=adapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null)
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

     /*   public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
*/
        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}