## Overview

This is a reference app for the AndroidBeaconLibrary supporting AltBeacon compatible devices

## Project Setup

1. [Install Gradle](http://www.gradle.org/installation) version 1.10
2. Install [Android Studio](https://developer.android.com/sdk/installing/studio.html) 0.5.2
3. Install Google Android SDKs (or install Android Studio which includes them)
4. In this project directory, edit `local.properties`, and give it a line that
   specifies the path to your Android SDK. (e.g. `sdk.dir=/Applications/Android
   Studio.app/sdk`)
5. Install and build the open-source `android-beacon-library` in a parallel
   directory. (See below)

## Installing the `AndroidBeaconLibrary`

This project is dependent on the open source AndroidBeaconLibrary (from altbeacon.org).  It must be
installed immediately underneath this project directory with a folder name androidBeaconLibrary, or
be linked with a symbolic link so it appears to be immediately under this project directory.
