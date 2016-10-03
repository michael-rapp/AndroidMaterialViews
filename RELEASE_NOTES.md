# AndroidMaterialViews - RELEASE NOTES

## Version 2.1.3 (Oct. 3rd 2016)

A minor release, which introduces the following changes:

- For drawing a `CircularProgressBar`, the view's padding is now taken into account.
- The view `FloatingActionButton` is now marked as deprecated.

## Version 2.1.2 (Sep. 12th 2016)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 24 (Android 7.0).
- Updated AppCompat v7 support library to version 24.2.0.
- Updated dependency "AndroidUtil" to version 1.11.1. 

## Version 2.1.1 (Mar. 18th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.4.5.
- Updated AppCompat v7 support library to version 23.2.1.
- Fixed some deprecation warnings.

## Version 2.1.0 (Feb. 26th 2016)

A feature release, which introduces the following changes:

- The class `Chip` has been added. It implements a tag-like view as proposed by the Material design guidelines (http://www.google.com/design/spec/components/chips.html#) 

## Version 2.0.1 (Feb. 24th 2016)

A minor release, which introduces the following changes:

- The library is from now on distributed under the Apache License version 2.0. 
- Updated dependency "AndroidUtil" to version 1.4.3.
- Updated AppCompat support library to version 23.1.1.
- Minor changes of the example app.

## Version 2.0.0 (Oct. 16th 2015)

A major release, which introduces the following changes:

- The project has been migrated from the legacy Eclipse ADT folder structure to Android Studio. It now uses the Gradle build system and the library as well as the example app are contained by one single project.
- The library can now be added to Android apps using the Gradle dependency `com.github.michael-rapp:android-material-views:2.0.0`

## Version 1.0.3 (Oct. 4th 2015)

A minor release, which introduces the following changes:

- Instances of the drawable `CircularProgressDrawable` are now always resized to be square by shortening the longer edge to match the length of the shorter edge.

## Version 1.0.2 (Oct. 3rd 2015)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialViews/issues/1

## Version 1.0.1 (June 26th 2015)

A minor release, which introduces the following changes:

- The image resources have been updated for a better appearance on dark backgrounds.

## Version 1.0.0 (June 25th 2015)

The first stable release, which initially provides the following views: 

- A `CircularProgressBar`, which allows to visualize an indeterminate progress as an animated circle.
- A `FloatingActionButton`, which is a round, colored button with a shadow and an icon. The button supports to become shown or hidden in an animated manner.