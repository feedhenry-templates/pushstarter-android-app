# Android Push Template
---------
Author: Daniel Passos (dpassos@redhat.com, daniel@passos.me)   
Level: Intermediate  
Technologies: Java, Android, RHMAP, GCM Push Messages
Summary: A demonstration of how to receive a GCM push message from RHMAP
Community Project : [Feed Henry](http://feedhenry.org/)
Target Product: RedHat Mobile Application Platform aka RHMAP  
Product Versions: RHMAP 3.8.0+   
Source: https://github.com/feedhenry-templates/pushstarter-android-app


## What is it?

This application will subscribe to a push service running in a RHMAP instance. The user can send messages to the device using RHMAP and view them on the device.  

If you do not have access to a RHMAP instance, you can sign up for a free instance at [https://openshift.feedhenry.com/](https://openshift.feedhenry.com/).

## How do I run it?  

### RHMAP Studio

### Build instructions for Open Source Development
If you wish to contribute to this template, the following information may be helpful; otherwise, RHMAP and its build facilities are the preferred solution.

###  Prerequisites  
 * fh-android-sdk : 3.0.+
 * Android Studio 1.4.0 or newer
 * Android SDK 22+ or newer

## Build instructions
 * Edit [fhconfig.properties](app/src/main/assets/fhconfig.properties) to include the relevant information from RHMAP.  
 * Attach running Android Device with API 16+ running  
 * ./gradlew installDebug  
 
## How does it work?



