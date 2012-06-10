## Example

Goto your project properties and add VGMod_Admob as a library.

Create your activity:
```java
	public class MyActivity extends ModuleActivity {
		@Override
		protected abstract void onLoadModules(ModuleManager moduleManager) {
			moduleManager.load(
				AdmobModule.class,
				BundleBuilder.create()
					.putString(AdmobModule.KEY, "This is my key")
					.bundle()
			);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.myActivity);
		}
	}
```

Create your layout file:
```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
		xmlns:android="http://schemas.android.com/apk/res/android"
		android:layout_width="fill_parent"
		android:layout_height="fill_parent"
		android:orientation="vertical">

		<LinearLayout
			xmlns:android="http://schemas.android.com/apk/res/android"
			android:id="@+id/adsWrapper"
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:gravity="center">
		</LinearLayout>

		<TextView
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:text="This is my activity"
		/>
	</LinearLayout>
```
