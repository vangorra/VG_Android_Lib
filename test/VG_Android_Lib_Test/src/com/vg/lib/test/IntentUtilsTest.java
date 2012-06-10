package com.vg.lib.test;

import java.util.List;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.test.AndroidTestCase;

import com.vg.lib.test.activity.TestActivity;
import com.vg.lib.util.IntentUtils;

public class IntentUtilsTest extends AndroidTestCase {
	public void testQueryAppsSupportingIntent() {
		
		
		try {
			IntentUtils.queryAppsSupportingIntent(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			IntentUtils.queryAppsSupportingIntent(getContext(), null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		List<ResolveInfo> info; 
		
		info = IntentUtils.queryAppsSupportingIntent(
				getContext(),
				new Intent("There.should.be.nothing.here.but.who.knows.dfds.34353")
		);
		assertNotNull(info);
		assertTrue(info.size() == 0);
		
		info = IntentUtils.queryAppsSupportingIntent(
				getContext(),
				new Intent(getContext(), TestActivity.class)
		);
		assertNotNull(info);
		assertTrue(info.size() == 1);
	}
}
