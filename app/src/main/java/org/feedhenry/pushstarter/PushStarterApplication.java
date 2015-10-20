/**
 * Copyright (c) 2015 FeedHenry Ltd, All Rights Reserved.
 *
 * Please refer to your contract with FeedHenry for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package org.feedhenry.pushstarter;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PushStarterApplication extends Application {

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

}
