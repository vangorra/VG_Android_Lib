package com.vg.lib.test.module;

import android.content.Intent;
import android.os.Bundle;

import com.vg.lib.module.ModuleActivity;
import com.vg.lib.module.ModuleManager;
import com.vg.lib.os.BundleBuilder;
import com.vg.lib.test.modules.testmodule.TestModule;

public class ModuleActivityImpl extends ModuleActivity {
	@Override
	public void onLoadModules(ModuleManager moduleManager) {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
	}
}
