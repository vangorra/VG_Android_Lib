package com.vg.lib.test.module;

import junit.framework.Assert;

import com.vg.lib.module.ModuleManager;
import com.vg.lib.test.modules.testmodule.TestModule;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

public class ModuleManagerTest extends AndroidTestCase {
	public void testGetInstance() {
		try {
			assertNotNull(ModuleManager.getInstance(null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertNotNull(ModuleManager.getInstance(getContext()));
		assertNotNull(ModuleManager.getInstance(getContext()));
	}
	
	public void testIsLoaded() {
		try {
			assertTrue(
				ModuleManager.getInstance(getContext()).isLoaded(null)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					TestModule.class, 
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance(getContext()).isLoaded(TestModule.class)
		);
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(TestModule.class));
	}
	
	public void testLoad() {
		try {
			assertTrue(
				ModuleManager.getInstance(getContext())
					.load(
						null,
						null
					)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			assertTrue(
				ModuleManager.getInstance(getContext())
					.load(
						null,
						null
					)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			assertTrue(
				ModuleManager.getInstance(getContext())
					.load(
						null,
						null
					)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			assertTrue(
				ModuleManager.getInstance(getContext())
					.load(
						TestModule.class,
						null
					)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					TestModule.class,
					new Bundle()
				)
		);		
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(TestModule.class));
	}
	
	public void testUnload() {
		try {
			ModuleManager.getInstance(getContext()).unload(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertFalse(
			ModuleManager.getInstance(getContext()).unload(TestModule.class)
		);		
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance(getContext()).unload(TestModule.class)
		);
	}
	
	public void testGet() {
		try {
			ModuleManager.getInstance(getContext()).get(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertNull(ModuleManager.getInstance(getContext()).get(TestModule.class));

		ModuleManager.getInstance(getContext())
			.load(
				TestModule.class,
				new Bundle()
			);
		
		assertNotNull(ModuleManager.getInstance(getContext()).get(TestModule.class));
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(TestModule.class));
	}
	
	public void testInvoke() {
		ModuleManager mm = ModuleManager.getInstance(getContext());
		
		mm
			.load(
				TestModule.class,
				new Bundle()
			);
		
		TestModule mod = mm.get(TestModule.class);
		
		mm.invoke("ActivityOnResume");
		Assert.assertTrue(mod.isSimpleMethodCalled());
		
		mm.invoke("ActivityOnActivityResult", null, 0, 0, new Intent());
		Assert.assertTrue(mod.isComplicatedMethodCalled());
	}
}
