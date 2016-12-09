package cornez.com.pushnotificationz;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by andrewbainter13 on 12/9/2016.
 */

public class GCMRegistrationIntentService extends IntentService {

    //Constants for success and error
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR="RegistrationError";

    //Class constructor
    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent Intent){
        //registering GCM to device
        registerGCM();
    }

    private void registerGCM(){
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try{
            //creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //getting token from instance id
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //displaying the token in the log so that we can copy it to send push notification
            //you can also extend the app by storing the token to your server
            Log.w("GCMRegIntentService", "token:"+token);

            //on registration complete creating instance with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //putting the token to the intent
            registrationComplete.putExtra("token", token);
        } catch (Exception e){
            Log.w("GCMRegIntentService", "Registration Error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //sending the broadcast that the registration is complete
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
