# SuperNotebook

## Guide to Install OCR Library

Follow this link: http://gaut.am/making-an-ocr-android-app-using-tesseract/

In order to use the commands 'ndk-build' and 'android update ..', You may need to add ndk-build and android into your ~/.bash_profile. Also you need to have ant installed.

While calling 'android update project --path . ' command, you may encounter an error complaining about the SDK target version. Run 'android list target' to find the available SDK versions, then run 'android update project -p . -p (replace with the target ID)'. Or you can have the required lower level SDK installed.

To use this library in Android Studio, follow the link: https://coderwall.com/p/eurvaq/tesseract-with-andoird-and-gradle

CAUTION: In step 'Delete the libs folder in the tess-two directory. If you like, delete the project.properties, build.xml, .classpath, and .project. files as well. You don't need them.'

DO NOT DELETE THE LIBS FOLDER.

