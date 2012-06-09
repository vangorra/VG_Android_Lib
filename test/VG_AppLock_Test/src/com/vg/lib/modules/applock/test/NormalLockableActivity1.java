package com.vg.lib.modules.applock.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.vg.lib.module.ModuleActivity;
import com.vg.lib.module.ModuleManager;

public class NormalLockableActivity1 extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.normal_lockable_activity1);
		this.findViewById(R.id.normalLockableActivity2Button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		
		if(vId == R.id.normalLockableActivity2Button) {
			this.startActivity(new Intent(this, NormalLockableActivity2.class));
			return;
		}
	}

}
