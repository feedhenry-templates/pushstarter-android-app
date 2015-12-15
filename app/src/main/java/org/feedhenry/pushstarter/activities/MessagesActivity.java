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
package org.feedhenry.pushstarter.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.feedhenry.pushstarter.PushStarterApplication;
import org.feedhenry.pushstarter.R;
import org.feedhenry.pushstarter.handler.NotificationBarMessageHandler;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.UnifiedPushMessage;

public class MessagesActivity extends AppCompatActivity implements MessageHandler {

    private PushStarterApplication application;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        application = (PushStarterApplication) getApplication();

        View emptyView = findViewById(R.id.empty);
        listView = (ListView) findViewById(R.id.messages);
        listView.setEmptyView(emptyView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RegistrarManager.registerMainThreadHandler(this);
        RegistrarManager.unregisterBackgroundThreadHandler(NotificationBarMessageHandler.instance);

        displayMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RegistrarManager.unregisterMainThreadHandler(this);
        RegistrarManager.registerBackgroundThreadHandler(NotificationBarMessageHandler.instance);
    }

    @Override
    public void onMessage(Context context, Bundle bundle) {
        addNewMessage(bundle);
        application.sendMetric(bundle.getString(UnifiedPushMessage.PUSH_MESSAGE_ID));
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {
    }

    @Override
    public void onError() {
    }

    private void addNewMessage(Bundle bundle) {
        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);
        application.addMessage(message);
        displayMessages();
    }

    private void displayMessages() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
            R.layout.message_item, application.getMessages());
        listView.setAdapter(adapter);
    }
}
