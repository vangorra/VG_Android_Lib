package com.vg.lib.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.vg.lib.module.ModuleManager;

import android.app.Activity;
import android.os.Bundle;

/**
 * Base activity used for module loading. In a perfect world, all your
 * activities would inherit from this at some point.
 * @author vangorra
 */
public class AbstractBaseActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityCreate(this, savedInstanceState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityPostCreate(this, savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivitySaveInstanceState(this, outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityPause(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityRestart(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityDestroy(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityStop(this);
	}
}
