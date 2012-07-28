package com.vg.lib.test.module;

import com.vg.lib.module.Module;
import com.vg.lib.module.ModuleManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

public class ModuleActivityTest extends ActivityInstrumentationTestCase2<ModuleActivityImpl> implements Module {
	private boolean callRecieved = false;
	
	public ModuleActivityTest(Class<ModuleActivityImpl> activityClass) {
		super(activityClass);
	}
	
	public void testEventDispatch() {
		ModuleActivityImpl activity = getActivity();
		ModuleManager.getInstance()
			.load(this.getClass(), new Bundle())
		;
		
		callRecieved = false;
		activity.onCreate(null);
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onPostCreate(null);
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onSaveInstanceState(null);
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onPause();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onRestart();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onDestroy();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onResume();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onStart();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onStop();
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onActivityResult(0, 0, null);
		assertTrue(callRecieved);
		
		callRecieved = false;
		activity.onUserInteraction();
		assertTrue(callRecieved);
	}
	
	public void onInvokeActivityOnCreate(Activity activity, Bundle savedInstanceState) {
		callRecieved = true;
	}

	public void onInvokeActivityOnPostCreate(Activity activity, Bundle savedInstanceState) {
		callRecieved = true;
	}

	public void onInvokeActivityOnSaveInstanceState(Activity activity, Bundle outState) {
		callRecieved = true;
	}

	public void onInvokeActivityOnPause(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnRestart(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnDestroy(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnResume(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnStart(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnStop(Activity activity) {
		callRecieved = true;
	}

	public void onInvokeActivityOnActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		callRecieved = true;
	}

	public void onInvokeActivityOnUserInteraction(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void load(Bundle args) {
		callRecieved = true;
	}

	@Override
	public void unLoad() {
		callRecieved = true;
	}
}
