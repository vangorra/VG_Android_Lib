package com.vg.lib.modules.applock.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vg.lib.module.ModuleActivity;
import com.vg.lib.module.ModuleManager;
import com.vg.lib.modules.applock.AppLockModule;

public class PersistentUnlockedActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.persistent_unlocked_activity);
		this.findViewById(R.id.lockScreenActivityButton).setOnClickListener(this);
		this.findViewById(R.id.normalLockableActivity1Button).setOnClickListener(this);
		this.findViewById(R.id.normalLockableActivity2Button).setOnClickListener(this);
		this.findViewById(R.id.toggleLockButton).setOnClickListener(this);
		this.findViewById(R.id.toggleEnabledButton).setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		updateModuleStatus();
	}

	private void updateModuleStatus() {
		AppLockModule mod = (AppLockModule)ModuleManager.getInstance(this).get(MODULE_APPLOCK);

		Button lockButton = (Button)this.findViewById(R.id.toggleLockButton);
		if(mod.getIsLocked()) {
			lockButton.setText("App is locked. Tap to unlock.");
		} else {
			lockButton.setText("App is unlocked. Tap to lock.");
		}
		
		Button enabledButton = (Button)this.findViewById(R.id.toggleEnabledButton);
		if(mod.getIsEnabled()) {
			enabledButton.setText("AppLock is enabled. Tap to disable.");
		} else {
			enabledButton.setText("AppLock is disabled. Tap to enable.");
		}
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		
		if(vId == R.id.lockScreenActivityButton) {
			this.startActivity(new Intent(this, LockScreenActivity.class));
			return;
			
		} else if(vId == R.id.normalLockableActivity1Button) {
			this.startActivity(new Intent(this, NormalLockableActivity1.class));
			return;
			
		} else if(vId == R.id.normalLockableActivity2Button) {
			this.startActivity(new Intent(this, NormalLockableActivity2.class));
			return;
		
		} else if(vId == R.id.toggleLockButton) {
			AppLockModule mod = (AppLockModule)ModuleManager.getInstance(this).get(MODULE_APPLOCK);
			mod.setIsLocked(!mod.getIsLocked());
			updateModuleStatus();
			return;
			
		} else if(vId == R.id.toggleEnabledButton) {
			AppLockModule mod = (AppLockModule)ModuleManager.getInstance(this).get(MODULE_APPLOCK);
			mod.setIsEnabled(!mod.getIsEnabled());
			updateModuleStatus();
			return;
		}
	}
}
