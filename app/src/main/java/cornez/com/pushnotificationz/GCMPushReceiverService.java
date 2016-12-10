package cornez.com.pushnotificationz;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by andrewbainter13 on 12/9/2016.
 */

public class GCMPushReceiverService extends GcmListenerService {

    //this method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data){
        //getting the message from the bundle
        String message = data.getString("message");
        //displaying a notification with the data
        sendNotification(message);
        Log.w("MessageReceived","Holy shit we got a message?");
    }

    //this method is generating a notification and displaying the notification
    private void sendNotification(String message){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

}
