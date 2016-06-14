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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;

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
    public void onMessage(final Context context, final Bundle bundle) {
        this.context = context;

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

    private void showMessage(Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);
        String pushMessageId = extractPushMessageId(bundle);

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

    private String extractPushMessageId(Bundle bundle) {
        return bundle.getString(UnifiedPushMessage.PUSH_MESSAGE_ID);
    }

}
