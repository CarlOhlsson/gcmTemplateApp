# What is it?
The app contains a template application that can receive push notifications from google either by subscribing to a topic or receiving a device message. The message itself is only show in the UI and not processed in any way.

This app was created by modifying Google's own startup template that can be found by following the link below.
https://developers.google.com/cloud-messaging/android/start

# Please note
This is just the absolute basics implementation of GCM without security or anything, it just simply works. The intention of the template is just to give an introduction to what is required to make it work and should absolutely not be used in any end product without appropriate modifications.

# Setup
1: Download your google-services.json file and place in gcmTemplate/app/

https://developers.google.com/cloud-messaging/android/client#get-config

2: Update Gradle files according to official documentation

https://developers.google.com/cloud-messaging/android/client#add-config

https://developers.google.com/cloud-messaging/android/client#play-services

