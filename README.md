# AndroidMaterialViews - README

"AndroidMaterialViews" is an Android-library, which provides various views, which are part of Android's Material Design guidelines, but which are not provided by the official Android SDK. The library's views can also be used on pre-Lollipop devices.

![](doc/images/example_small.png)

The library provides the following views:

- A `CircularProgressBar`, which allows to visualize an indeterminate progress as an animated circle.
- A `FloatingActionButton`, which is a round, colored button with a shadow and an icon. The button supports to become shown or hidden in an animated manner.

## License Agreement

This project is distributed under the GNU Lesser Public License version 3.0 (GLPLv3). For further information about this license agreement's content please refer to its full version, which is available at http://www.gnu.org/licenses/lgpl.txt.

## Download

The latest release of this library can be downloaded as a zip archive from the download section of the project's Github page, which is available [here](https://github.com/michael-rapp/AndroidMaterialViews/releases). Furthermore, the library's source code is available as a Git repository, which can be cloned using the URL https://github.com/michael-rapp/AndroidMaterialViews.git.

Alternatively, the library can be added to your Android app as a Gradle dependency by adding the following to the respective module's `build.gradle` file:

```
dependencies {
    compile 'com.github.michael-rapp:android-material-views:2.0.0'
}
```

Before version 2.0.0 this project was hosted on [Sourceforge](https://sourceforge.net/projects/androidmaterialviews). These older versions used the legacy Eclipse ADT folder structure and are not available as Gradle artifacts.

## Examples

The following examples provide a quick overview on how to use the views, which are provided by the library, in your own Android app. This project also contains the source code of an example app, which implements an use case of the library for demonstration purposes, as well as a more detailed documentation and auto-generated javadoc files.

### CircularProgressBar

The XML code below shows how the view `CircularProgressBar` can be added to a XML layout resource. This example contains all of the view's custom attributes.

```
<?xml version="1.0" encoding="utf-8"?> 
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android" 
    android:layout_width="match_parent" 
    android:layout_height="match_parent" >

    <de.mrapp.android.view.CircularProgressBar 
        android:id="@+id/circular_progress_bar" 
        android:layout_width="@dimen/circular_progress_bar_size_normal" 
        android:layout_height="@dimen/circular_progress_bar_size_normal" 
        android:color="@color/circular_progress_bar_color" 
        android:thickness="@dimen/circular_progress_bar_thickness_normal" /> 

</RelativeLayout>
```

### FloatingActionButton

The following example demonstrates how a `FloatingActionButton` can be declared within a XML layout resource. The example contains all of the view's custom attributes.

```
<?xml version="1.0" encoding="utf-8"?> 
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android" 
    xmlns:custom="http://schemas.android.com/apk/res-auto" 
    android:layout_width="match_parent" 
    android:layout_height="match_parent" >

    <de.mrapp.android.view.FloatingActionButton 
        android:id="@+id/floating_action_button" 
        android:layout_width="wrap_content" 
        android:layout_height="wrap_content" 
        android:color="@color/floating_action_button_color" 
        android:icon="@drawable/floating_action_button_icon" 
        custom:size="normal" 
        custom:activatedColor="@color/floating_action_button_activated_color" 
        custom:pressedColor="@color/floating_action_button_pressed_color" 
        custom:disabledColor="@color/floating_action_button_disabled_color" 
        custom:visibilityAnimationDuration="500" /> 

</RelativeLayout>
```

## Contact information

For personal feedback or questions feel free to contact me via the mail address, which is mentioned on my [Github profile](https://github.com/michael-rapp). If you have found any bugs or want to post a feature request please use the [bugtracker](https://github.com/michael-rapp/AndroidMaterialViews/issues) to report them.