# ttr-advisor-app
An Android app to help folks play the board game Ticket to Ride, using Java and LibGDX.

### Tester Instructions
#### Download, Examine, and Run the Code
Please download the entire project from
https://github.com/TTR-Advisor-Team/ttr-advisor-app/releases/tag/v1.3
This is an Eclipse project with Gradle. If you want to examine the code, the best way to do it is using Eclipse with the Gradle extension that you can get on the Eclipse Marketplace. Gradle will automatically download dependencies.

The Ticket to Ride Advisor App is a multiplatform project for desktop and Android devices. To get executables, run the following commands in the root directory of the project:
#### gradlew desktop:dist
This works for the desktop distribution.
Generates a .jar file in subdirectory ​desktop/build/libs​ that you can run with java -jar desktop.jar. You must have Java installed.
#### gradlew android:assembleDebug
Creates Android testing distribution. 
Will generate an APK file in subdirectory android/build/outputs/apk. APK files can be copied to an Android device and installed as an app. The generated distribution is not signed and will require the user to confirm installation of the app without a known source.
#### Bug Tracker
To report bugs, please use the GitHub issues on our repo. We would appreciate an example set of inputs to reproduce the bug or a general description of how the bug can be triggered.
