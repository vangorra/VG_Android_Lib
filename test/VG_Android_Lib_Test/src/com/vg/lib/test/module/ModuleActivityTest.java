package com.vg.lib.test.module;

import com.vg.lib.module.Module;
import com.vg.lib.module.ModuleManager;

import android.app.Activity;
import android.content.Context;
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
		ModuleManager.getInstance(activity)
			.load("me", this.getClass(), new Bundle())
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
	
	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		callRecieved = true;
	}

	@Override
	public void onActivityPostCreate(Activity activity,
			Bundle savedInstanceState) {
		callRecieved = true;
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		callRecieved = true;
	}

	@Override
	public void onActivityPause(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityRestart(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityResume(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityStart(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityStop(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		callRecieved = true;
	}

	@Override
	public void onActivityUserInteraction(Activity activity) {
		callRecieved = true;
	}

	@Override
	public void load(Context context, Bundle args) {
		callRecieved = true;
	}

	@Override
	public void unLoad() {
		callRecieved = true;
	}
}
