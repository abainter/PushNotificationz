package cornez.com.pushnotificationz;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by andrewbainter13 on 12/9/2016.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    //if the token is changed registering the device again
    @Override
    public void onTokenRefresh(){
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
