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
package org.feedhenry.pushstarter;

import android.app.Application;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.utils.FHLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PushStarterApplication extends Application {

    private static final String TAG = PushStarterApplication.class.getName();

    public static final String PUSH_MESSAGE_FROM_BACKGROUND = "PUSH_MESSAGE_FROM_BACKGROUND";

    private List<String> messages;

    @Override
    public void onCreate() {
        super.onCreate();
        messages = new ArrayList<String>();
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(String newMessage) {
        messages.add(newMessage);
    }

    public void sendMetric(String pushMessageId) {
        FH.sendPushMetrics(pushMessageId, new FHActCallback() {

            @Override
            public void success(FHResponse fhResponse) {
                FHLog.i(TAG, "Push message was marked as open");
            }

            @Override
            public void fail(FHResponse fhResponse) {
                FHLog.w(TAG, "Push message could not marked as open");
            }
        });
    }

}
