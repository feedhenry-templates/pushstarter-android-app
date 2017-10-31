# Android Push Template

[![circle-ci](https://img.shields.io/circleci/project/github/feedhenry-templates/pushstarter-android-app/master.svg)](https://circleci.com/gh/feedhenry-templates/pushstarter-android-app)

Author: Daniel Passos (dpassos@redhat.com)        
Level: Intermediate    
Technologies: Java, Android, RHMAP, FCM Push Messages     
Summary: A demonstration of how to receive a GCM push message from RHMAP     
Community Project : [Feed Henry](http://feedhenry.org/)    
Target Product: RedHat Mobile Application Platform aka RHMAP       
Product Versions: RHMAP 3.8.0+   
Source: https://github.com/feedhenry-templates/pushstarter-android-app     
Prerequisites: fh-android-sdk: 4.0.+, Android Studio: 3.0.0 or newer, Android SDK: 26+ or newer   

## What is it?

This application will subscribe to a push service running in a RHMAP instance. The user can send messages to the device using RHMAP and view them on the device.  

If you do not have access to a RHMAP instance, you can sign up for a free instance at [https://openshift.feedhenry.com/](https://openshift.feedhenry.com/).

## How do I run it?  

### RHMAP Studio

You can create this project as a template inside of RHMAP Studio.  The full tutorial for setting up AeroGear UPS, registering on the Google Cloud Messaging network, and using the app can be found on the [RedHat Mobile docs](http://docs.feedhenry.com/v3/guides/using_push_notifications.html) site.  

### Firebase Cloud Messaging

 * You need to have a valid Firebase Cloud Messaging project setup. Download the `google-services.json` file from the Firebase Console and put it into the `app` directory of your Android Application.
 * You also need to use the `package` from the Firebase Cloud Messaging configuration as the package for your application.

### Build instructions for Open Source Development
If you wish to contribute to this template, the following information may be helpful; otherwise, RHMAP and its build facilities are the preferred solution.

## Build instructions
 * Edit `app/src/main/assets/fhconfig.properties` to include the relevant information from RHMAP.  
 * Attach running Android Device with API 16+ running  
 * ./gradlew installDebug  
 
## How does it work?

### Registration With Messaging Services

In `app/src/main/java/org/feedhenry/pushstarter/activities/RegisterActivity.java` there is the following block of code : 

```java
FH.pushRegister(new FHActCallback() {
            @Override
            public void success(FHResponse fhResponse) {
                startActivity(new Intent(RegisterActivity.this, MessagesActivity.class));
            }

            @Override
            public void fail(FHResponse fhResponse) {
                Toast.makeText(getApplicationContext(),
                        fhResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
```

This code will register the device with both AeroGear UnifiedPush Service AND Google's GCM network.  If both actions are successful then it will launch the main activity.  Otherwise it will display an error and quit.

### Handling messages

Messages are handled by implementations of the [MessageHandler](https://aerogear.org/docs/specs/aerogear-android-push/org/jboss/aerogear/android/unifiedpush/MessageHandler.html) interface which are registered with the `RegistrarManager`. These classes are `/app/src/main/java/org/feedhenry/pushstarter/activities/MessagesActivity.java` and `app/src/main/java/org/feedhenry/pushstarter/handler/NotificationBarMessageHandler.java`.

In the `MessagesActivity` class the Activity switches between itself and the `NotificationBarMessageHandler` in the *onPause* and *onResume* methods.

```java
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
```
