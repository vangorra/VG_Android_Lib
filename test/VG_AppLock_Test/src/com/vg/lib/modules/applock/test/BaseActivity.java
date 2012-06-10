package com.vg.lib.modules.applock.test;

import com.vg.lib.module.ModuleActivity;
import com.vg.lib.module.ModuleManager;
import com.vg.lib.modules.applock.AppLockModule;
import com.vg.lib.os.BundleBuilder;

public class BaseActivity extends ModuleActivity {

	@Override
	protected void onLoadModules(ModuleManager moduleManager) {
		moduleManager.load(
				AppLockModule.class, 
				BundleBuilder.create()
					.putString(
							AppLockModule.LOCKSCREEN_ACTIVITY_CLASS, 
							LockScreenActivity.class.getName()
					)
					.putStringArray(
							AppLockModule.WHITELIST_ACTIVITY_CLASSES, 
							new String[]{
									PersistentUnlockedActivity.class.getName(),
							}
					)
					.putLong(
							AppLockModule.INACTIVITY_TIMEOUT, 
							5000
					)
					.bundle()
		);
	}
}
