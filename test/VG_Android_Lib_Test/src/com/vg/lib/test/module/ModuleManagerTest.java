package com.vg.lib.test.module;

import junit.framework.Assert;

import com.vg.lib.module.ModuleManager;
import com.vg.lib.test.modules.testmodule.TestModule;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

public class ModuleManagerTest extends AndroidTestCase {
	public void testGetInstance() {
		assertNotNull(ModuleManager.getInstance());
		assertNotNull(ModuleManager.getInstance());
	}
	
	public void testIsLoaded() {
		try {
			assertTrue(
				ModuleManager.getInstance().isLoaded(null)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(
			ModuleManager.getInstance()
				.load(
					TestModule.class, 
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance().isLoaded(TestModule.class)
		);
		
		assertTrue(ModuleManager.getInstance().unload(TestModule.class));
	}
	
	public void testLoad() {
		try {
			assertTrue(
				ModuleManager.getInstance()
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
				ModuleManager.getInstance()
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
				ModuleManager.getInstance()
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
				ModuleManager.getInstance()
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
			ModuleManager.getInstance()
				.load(
					TestModule.class,
					new Bundle()
				)
		);		
		
		assertTrue(
			ModuleManager.getInstance()
				.load(
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(ModuleManager.getInstance().unload(TestModule.class));
	}
	
	public void testUnload() {
		try {
			ModuleManager.getInstance().unload(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertFalse(
			ModuleManager.getInstance().unload(TestModule.class)
		);		
		
		assertTrue(
			ModuleManager.getInstance()
				.load(
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance().unload(TestModule.class)
		);
	}
	
	public void testGet() {
		try {
			ModuleManager.getInstance().get(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertNull(ModuleManager.getInstance().get(TestModule.class));

		ModuleManager.getInstance()
			.load(
				TestModule.class,
				new Bundle()
			);
		
		assertNotNull(ModuleManager.getInstance().get(TestModule.class));
		
		assertTrue(ModuleManager.getInstance().unload(TestModule.class));
	}
	
	public void testInvokeAll() {
		ModuleManager mm = ModuleManager.getInstance();
		
		mm
			.load(
				TestModule.class,
				new Bundle()
			);
		
		TestModule mod = mm.get(TestModule.class);
		
		mm.invokeAll("ActivityOnResume");
		Assert.assertTrue(mod.isSimpleMethodCalled());
		
		mm.invokeAll("ActivityOnActivityResult", null, 0, 0, new Intent());
		Assert.assertTrue(mod.isComplicatedMethodCalled());
	}
}
