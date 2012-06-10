VG Android Lib
===
Every Android library has its own requirements to run. Some libraries have to be run onCreate other, onResume or even onDestroy. Devs mitigate the issue by creating a base activity for their project where all the custom events are handled.

Why can't this automated? 
In a way it has.

VG Android Lib aims to solve this problem by providing a flexible framework where external libraries are wrapped up into a simple module. As the developer, all you have to do is load the module with the settings you like and enjoy.

### Features
* ICS styles and functionality.
* Module system that allows modules to react to activity events.

### Core modules
* ORM Lite - A lightweight Android ORM. No more messy SQL all over the place.
* Google Analytics - Will track each activity based on its class name.
* Admob - Allows for easy integration with admob.
* AppLock - Provides the functionality to lock the app after inactivty or loss of focus. Still being worked on. Dev needs to provide interface for unlocking.

### Community modules
Nothing yet. :(

### How do I use a module? EASY!
Here is an example for using the GoogleAnalytics module.
Goto your project properties and add VGMod_GoogleAnalytics as an Android library.

Have your activity extend ModuleActivity:
```java
	public class MyBaseActivity extends ModuleActivity {
		@Override
		protected abstract void onLoadModules(ModuleManager moduleManager) {
			moduleManager.load(
				GoogleAnalyticsModule.class,
				BundleBuilder.create()
					.putString(GoogleAnalyticsModule.TRACKER_ID, "my tracker code")
					.bundle()
			);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// have fun.

			// do custom stuff too!
			// ModuleManager.getInstance().get("GoogleAnalytics").getTracker();
		}

		...
	}
```
Now each time your activiy is called, GoogleAnalytics will kick off and track the activity based on class.

## But I want to do more with original code. Okay.
In the example above just run:
```java
	ModuleManager.getInstance().get("GoogleAnalytics").getTracker();
```

### How to use
* Clone the project to <dir>.
* Import and choose <dir>/library
* Unckeck copy contents to workspace.
* Import both ActionBarSherlock and VG_Android_Lib
* In your project properties, click the Android tab and add VG_Android_Lib as a library.
* Be sure to change your theme to be a sherlock theme. <application android:theme="Theme.Sherlock" ...>
* Enjoy!

## Theming
This library makes use of the ActionBar Sherlock library. Sherlock comes complete with many features and options. Check out their site for more information.
