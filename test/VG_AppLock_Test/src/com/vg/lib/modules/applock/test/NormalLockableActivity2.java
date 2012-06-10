package com.vg.lib.modules.applock.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NormalLockableActivity2 extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.normal_lockable_activity2);
		this.findViewById(R.id.normalLockableActivity1Button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		
		if(vId == R.id.normalLockableActivity1Button) {
			this.startActivity(new Intent(this, NormalLockableActivity1.class));
			return;
		}
	}
}
