package com.vg.lib.module;

import android.content.Intent;
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
	 * @param moduleManager The current instance of the module manager.
	 */
	protected abstract void onLoadModules(ModuleManager moduleManager);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		onLoadModules(ModuleManager.getInstance(this));
		
		super.onCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityCreate(this, savedInstanceState);
	} // method

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityPostCreate(this, savedInstanceState);
	} // method

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivitySaveInstanceState(this, outState);
	} // method

	@Override
	protected void onPause() {
		super.onPause();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityPause(this);
	} // method

	@Override
	protected void onRestart() {
		super.onRestart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityRestart(this);
	} // method

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityDestroy(this);
	} // method

	@Override
	protected void onResume() {
		super.onResume();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityResume(this);
	} // method

	@Override
	protected void onStart() {
		super.onStart();

		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityStart(this);
	} // method

	@Override
	protected void onStop() {
		super.onStop();
		
		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityStop(this);
	} // method

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityStop(this);
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		
		//notify the module manager of the event.
		ModuleManager.getInstance(this).getDispatcher().onActivityUserInteraction(this);
	}
	
} // class
