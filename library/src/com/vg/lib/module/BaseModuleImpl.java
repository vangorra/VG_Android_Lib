package com.vg.lib.module;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * A convince class so the module developer doens't have to have so many
 * empty method stubs.
 * @author vangorra
 *
 */
public abstract class BaseModuleImpl implements Module {
	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {

	}

	@Override
	public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityPause(Activity activity) {

	}

	@Override
	public void onActivityRestart(Activity activity) {

	}

	@Override
	public void onActivityDestroy(Activity activity) {

	}

	@Override
	public void onActivityResume(Activity activity) {

	}

	@Override
	public void onActivityStart(Activity activity) {

	}

	@Override
	public void onActivityStop(Activity activity) {

	}
} // class
