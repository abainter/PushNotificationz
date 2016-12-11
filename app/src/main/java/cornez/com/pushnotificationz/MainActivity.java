package cornez.com.pushnotificationz;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    //creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView tv1;
    private Button b1;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            //When the broadcast is received
            //We are sending the broadcast from GCMRegistrationIntentService
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //displaying the token as toast
                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();
                }
            }
        };
                et=(EditText)findViewById(R.id.body);
                tv1=(TextView)findViewById(R.id.name);
                b1=(Button)findViewById(R.id.send);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title=tv1.getText().toString().trim();
                        String body=et.getText().toString().trim();
                        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notify=new Notification.Builder(getApplicationContext())
                                .setContentTitle(title)
                                .setContentText(body)
                                .setSmallIcon(R.mipmap.ic_launcher).build();
                        notif.notify(0, notify);
                    }
                });


        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode){
            //if play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                //displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not installed/enabled in this device!", Toast.LENGTH_LONG).show();
                //if play is not supported
                //displaying an error message
            } else{
                Toast.makeText(getApplicationContext(), "This device does not support Google Play Services!", Toast.LENGTH_LONG).show();
            }
        //if play service is available
        } else {
            //starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume(){
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause(){
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
