package com.vg.lib.test.module;

import com.vg.lib.module.ModuleManager;
import com.vg.lib.test.modules.testmodule.TestModule;

import android.os.Bundle;
import android.test.AndroidTestCase;

public class ModuleManagerTest extends AndroidTestCase {
	public void testGetInstance() {
		/*try {
			ModuleManager.getInstance();
			assertTrue(false);
		} catch (IllegalStateException e) {
			assertTrue(true);
		}*/
		
		try {
			assertNotNull(ModuleManager.getInstance(null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertNotNull(ModuleManager.getInstance(getContext()));
		assertNotNull(ModuleManager.getInstance());
		assertNotNull(ModuleManager.getInstance(getContext()));
	}
	
	public void testIsLoaded() {
		String moduleName = "isLoadedMod";
		
		try {
			assertTrue(
				ModuleManager.getInstance().isLoaded(null)
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			assertTrue(
				ModuleManager.getInstance().isLoaded("")
			);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					moduleName, 
					TestModule.class, 
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance(getContext()).isLoaded(moduleName)
		);
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(moduleName));
	}
	
	public void testLoad() {
		String moduleName = "loadMod";
		
		try {
			assertTrue(
				ModuleManager.getInstance(getContext())
					.load(
						null,
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
						"",
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
						moduleName,
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
						moduleName,
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
					moduleName,
					TestModule.class,
					new Bundle()
				)
		);		
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					moduleName,
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(moduleName));
	}
	
	public void testUnload() {
		String moduleName = "unloadMod";
		
		try {
			ModuleManager.getInstance(getContext()).unload(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ModuleManager.getInstance(getContext()).unload("");
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertFalse(
			ModuleManager.getInstance(getContext()).unload(moduleName)
		);		
		
		assertTrue(
			ModuleManager.getInstance(getContext())
				.load(
					moduleName,
					TestModule.class,
					new Bundle()
				)
		);
		
		assertTrue(
			ModuleManager.getInstance().unload(moduleName)
		);
	}
	
	public void testGet() {
		String moduleName = "getMod";
		
		try {
			ModuleManager.getInstance(getContext()).get(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ModuleManager.getInstance(getContext()).get("");
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertNull(ModuleManager.getInstance(getContext()).get(moduleName));

		ModuleManager.getInstance(getContext())
			.load(
				moduleName,
				TestModule.class,
				new Bundle()
			);
		
		assertNotNull(ModuleManager.getInstance(getContext()).get(moduleName));
		
		assertTrue(ModuleManager.getInstance(getContext()).unload(moduleName));
	}
	
	
}
