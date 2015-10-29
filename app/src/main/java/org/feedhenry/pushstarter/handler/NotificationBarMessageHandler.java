/**
 * Copyright (c) 2015 FeedHenry Ltd, All Rights Reserved.
 *
 * Please refer to your contract with FeedHenry for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package org.feedhenry.pushstarter.handler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import org.feedhenry.pushstarter.PushStarterApplication;
import org.feedhenry.pushstarter.R;
import org.feedhenry.pushstarter.activities.MessagesActivity;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.gcm.UnifiedPushMessage;

public class NotificationBarMessageHandler implements MessageHandler {

    public static final int NOTIFICATION_ID = 1;
    private Context context;

    public static final NotificationBarMessageHandler instance = new NotificationBarMessageHandler();

    public NotificationBarMessageHandler() {
    }

    @Override
    public void onMessage(Context context, Bundle bundle) {
        this.context = context;

        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);

        PushStarterApplication application = (PushStarterApplication) context.getApplicationContext();
        application.addMessage(message);

        notify(bundle);
    }

    private void notify(Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);
        String pushMessageId = bundle.getString(UnifiedPushMessage.PUSH_MESSAGE_ID);

        Intent intent = new Intent(context, MessagesActivity.class)
                .addFlags(PendingIntent.FLAG_UPDATE_CURRENT)
                .putExtra(UnifiedPushMessage.ALERT_KEY, message)
                .putExtra(UnifiedPushMessage.PUSH_MESSAGE_ID, pushMessageId)
                .putExtra(PushStarterApplication.PUSH_MESSAGE_FROM_BACKGROUND, true);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDeleteMessage(Context context, Bundle arg0) {
    }

    @Override
    public void onError() {
    }

}
