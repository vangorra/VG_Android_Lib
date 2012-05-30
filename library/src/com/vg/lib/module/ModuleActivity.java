package com.vg.lib.module;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Base activity used for module loading. This activity needs to be extended at some point for modules to be used.
 * @author vangorra
 */
public abstract class ModuleActivity extends SherlockActivity {

	/**
	 * Called before onCreate and in intended for your modules to be initialized and loaded
	 * into the ModuleManager.
	 */
	protected abstract void onLoadModules();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		onLoadModules();
		
		// call super
		super.onCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityCreate(this, savedInstanceState);
	} // method

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// call super
		super.onPostCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityPostCreate(this, savedInstanceState);
	} // method

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// call super
		super.onSaveInstanceState(outState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivitySaveInstanceState(this, outState);
	} // method

	@Override
	protected void onPause() {
		// call super
		super.onPause();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityPause(this);
	} // method

	@Override
	protected void onRestart() {
		// call super
		super.onRestart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityRestart(this);
	} // method

	@Override
	protected void onDestroy() {
		// call super
		super.onDestroy();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityDestroy(this);
	} // method

	@Override
	protected void onResume() {
		// call super
		super.onResume();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityResume(this);
	} // method

	@Override
	protected void onStart() {
		// call super
		super.onStart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityStart(this);
	} // method

	@Override
	protected void onStop() {
		// call super
		super.onStop();
		
		// notify the module manager of the event.
		ModuleManager.getInstance(this).onActivityStop(this);
	} // method
} // class
