# AndroidMaterialViews - RELEASE NOTES

## Version 2.0.0 (Oct. 15th 2015)

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