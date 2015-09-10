# InstaNote

## Inspiration
The inspiration is from the latest version of Google translator. Google translator nowadays has a great feature that can translate everything through the camera. As a user, you do not have to type everything into the mobile in order to get a translation. All you need to do is use your camera and swipe select the area you want to be translated.

## What it does
Our InstaNote used a similar strategy but focus more on the note tag side. People nowadays rely more and more on the electronic notes. But many would feel inefficient and waste of time if you have to type everything you have seen actually into your mobile, especially when you have a rather smaller screen. Thus we offer people a solution that you do not have to record everything with typing. Instead of that, with a mobile camera and a few swipes, you have all you want to remember stored inside your cell phone. It actually make people's life much easier. Besides, we added many advanced features such as share and tag in order to make the note more social.

## How we built it
The whole App is build on Android SDK, the language we used to accomplish the idea is mainly Java and XML. We have our front end layout design and icon. We also have image processing algorithm with which we can convert the image information into text form. Besides, we have back end database to support the application storage system.

## Challenges we ran into
This is almost the fist time for all the team members to actually build a android App, everything of the project was new and great challenge for us. The distortion and noise caused by the environment affected the result of image process severely. We also have some problem to adjust the database in order to make sure every updates for the note is consistent.

## Accomplishments that we're proud of
We have completed most features of our design. The accuracy and robustness of the image process was much better than we thought. Every part of the note functions well and it can even send SMG to other contacts about the event you have recorded and use Google maps to tag the location of the event.

## What we learned
Android SDK skills which is obvious. Great team members.

## What's next for InstaNote
Sponsorship is needed for further improvement.

## Guide to Install OCR Library

Follow this link: http://gaut.am/making-an-ocr-android-app-using-tesseract/

In order to use the commands 'ndk-build' and 'android update ..', You may need to add ndk-build and android into your ~/.bash_profile. Also you need to have ant installed.

While calling 'android update project --path . ' command, you may encounter an error complaining about the SDK target version. Run 'android list target' to find the available SDK versions, then run 'android update project -p . -t (replace with the target ID)'. Or you can have the required lower level SDK installed.

To use this library in Android Studio, follow the link: https://coderwall.com/p/eurvaq/tesseract-with-andoird-and-gradle

CAUTION: In step 'Delete the libs folder in the tess-two directory. If you like, delete the project.properties, build.xml, .classpath, and .project. files as well. You don't need them.'

DO NOT DELETE THE LIBS FOLDER.

