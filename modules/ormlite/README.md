### Example

Goto your project properties and add VGMod_OrmLite as a library.

```java
	public class MyActivity extends ModuleActivity {
		@Override
		protected abstract void onLoadModules(ModuleManager moduleManager) {
			moduleManager.load(
				"OrmLite",
				OrmLiteModule.class,
				BundleBuilder.create()
					.putString(OrmLiteModule.DATABASE_NAME, "main.db")
					.bundle()
			);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// have fun.
			// Start coding with ORMLite just as you had before.
			// ModuleManager.getInstance().get("OrmLite").getHelper();
		}

		...
	}
```
