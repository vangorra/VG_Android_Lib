### Example

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
