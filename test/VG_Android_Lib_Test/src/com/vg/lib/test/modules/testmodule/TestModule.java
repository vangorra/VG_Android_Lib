package com.vg.lib.test.modules.testmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vg.lib.module.Module;

public class TestModule implements Module {

	private boolean mIsComplicatedMethodCalled = false;
	private boolean mIsSimpleMethodCalled = false;

	@Override
	public void load(Bundle args) {
		
	}

	@Override
	public void unLoad() {

	}

	public void onInvokeActivityOnResume() {
		this.mIsSimpleMethodCalled  = true;
	}
	
	public void onInvokeActivityOnActivityResult(Activity activity, int requestCode, int responseCode, Intent data) {
		this.mIsComplicatedMethodCalled = true;
	}
	
	public boolean isComplicatedMethodCalled() {
		return this.mIsComplicatedMethodCalled;
	}
	
	public void setIsComplicatedMethodCalled(boolean b) {
		this.mIsComplicatedMethodCalled = b;
	}
	
	public boolean isSimpleMethodCalled() {
		return this.mIsSimpleMethodCalled;
	}
	
	public void setIsSimpleMethodCalled(boolean b) {
		this.mIsSimpleMethodCalled = b;
	}
}
