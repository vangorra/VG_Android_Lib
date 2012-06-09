package com.vg.lib.modules.applock.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LockScreenActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.lock_screen_activity);
		this.findViewById(R.id.unlockButton).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		
		// unlock button was pressed.
		if(vId == R.id.unlockButton) {
			// tell the calling activity the unlock was successful.
			this.setResult(Activity.RESULT_OK);
			this.finish();
			return;
		}
	}

}
