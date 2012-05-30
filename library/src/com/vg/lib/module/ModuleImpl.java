package com.vg.lib.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * A convince class so the module developer doesn't have to have so many empty method stubs.
 * Just override the method you want to use.
 * Note: All child modules should have a public constructor with no arguments. This is how the module manager instantiates your module.
 * @author vangorra
 *
 */
public abstract class ModuleImpl implements Module {
	/**
	 * Empty constructor. This is needed for the module manager to instantiate the module.
	 */
	public ModuleImpl() {
		// empty
	}
	
	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		// empty
	}

	@Override
	public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
		// empty
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		// empty
	}

	@Override
	public void onActivityPause(Activity activity) {
		// empty
	}

	@Override
	public void onActivityRestart(Activity activity) {
		// empty
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		// empty
	}

	@Override
	public void onActivityResume(Activity activity) {
		// empty
	}

	@Override
	public void onActivityStart(Activity activity) {
		// empty
	}

	@Override
	public void onActivityStop(Activity activity) {
		// empty
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		// empty
	}

	@Override
	public void onActivityUserInteraction(Activity activity) {
		// empty
	}
} // class
