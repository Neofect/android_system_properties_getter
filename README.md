# Android System Properties Getter
This is an Android app to list all the system properties of Android device.

### Implementation
Manipulation of Android system properties is managed by `android.os.SystemProperties`. But it doesn't provide a way to get the list of properties. So the list is retrieved through running the `getprop` of adb shell script.

### Project structure
The project is structured as an Android Studio project.
