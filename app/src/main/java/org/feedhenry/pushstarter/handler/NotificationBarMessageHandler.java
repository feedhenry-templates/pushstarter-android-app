/**
 * Copyright 2015 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.feedhenry.pushstarter.handler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;

import org.feedhenry.pushstarter.PushStarterApplication;
import org.feedhenry.pushstarter.R;
import org.feedhenry.pushstarter.activities.MessagesActivity;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.fcm.UnifiedPushMessage;

public class NotificationBarMessageHandler implements MessageHandler {

    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "pushstarter_channel";
    private Context context;

    public static final NotificationBarMessageHandler instance = new NotificationBarMessageHandler();

    public NotificationBarMessageHandler() {
    }

    @Override
    public void onMessage(final Context context, final Bundle bundle) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        final String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);

        FH.pushRegister(new FHActCallback() {
            @Override
            public void success(FHResponse fhResponse) {
                PushStarterApplication application = (PushStarterApplication) context.getApplicationContext();
                application.sendMetric(extractPushMessageId(bundle));

                application.addMessage(message);

                showMessage(bundle);
            }

            @Override
            public void fail(FHResponse fhResponse) {
                Log.e("NOTIFICATION", fhResponse.getErrorMessage());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            throw new IllegalStateException("This function should not be called on < Android O");
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel_name);
        // The user-visible description of the channel.
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

        // Configure the notification channel.
        channel.setDescription(description);
        channel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);

    }

    private void showMessage(Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);
        String pushMessageId = extractPushMessageId(bundle);

        Intent intent = new Intent(context, MessagesActivity.class)
                .putExtra(UnifiedPushMessage.ALERT_KEY, message)
                .putExtra(UnifiedPushMessage.PUSH_MESSAGE_ID, pushMessageId)
                .putExtra(PushStarterApplication.PUSH_MESSAGE_FROM_BACKGROUND, true);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private String extractPushMessageId(Bundle bundle) {
        return bundle.getString(UnifiedPushMessage.PUSH_MESSAGE_ID);
    }

}
