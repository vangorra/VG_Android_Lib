package com.vg.lib.test;

import com.vg.lib.util.ApplicationInfoUtils;

import android.test.AndroidTestCase;

public class ApplicationInfoUtilsTest extends AndroidTestCase {
	public void testMetaDataGetInt() {
		try {
			ApplicationInfoUtils.MetaData.getIntegerMeta(null, null, 0);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), null, 0);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), "nothing", 0),
			0
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), "var1", 0),
			1
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), "var2", -1),
			-1
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), "var3", 1),
			1
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getIntegerMeta(getContext(), "var4", 2),
			2
		);
	}
	
	public void testMetaDataGetFloat() {
		try {
			ApplicationInfoUtils.MetaData.getFloatMeta(null, null, 0.0f);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), null, 0.0f);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), "nothing", 0.0f),
			0.0f
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), "var1", 0.0f),
			0.0f
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), "var2", -1.0f),
			1.23f
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), "var3", 1.0f),
			1.0f
		);
		
		assertEquals(
			ApplicationInfoUtils.MetaData.getFloatMeta(getContext(), "var4", 2.0f),
			2.0f
		);
	}
	
	public void testMetaDataGetString() {
		try {
			ApplicationInfoUtils.MetaData.getStringMeta(null, null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "nothing", null)
				== null
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "nothing", "nil")
				.equals("nil")
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "var1", "rrr")
				.equals("rrr")
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "var2", "rrr")
				.equals("rrr")
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "var3", "rrr")
				.equals("test")
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getStringMeta(getContext(), "var4", "rrr")
				.equals("rrr")
		);
	}
	
	public void testMetaDataGetBoolean() {
		try {
			ApplicationInfoUtils.MetaData.getBooleanMeta(null, null, false);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), null, false);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertFalse(
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), "nothing", false)
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), "var1", true)
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), "var2", true)
		);
		
		assertFalse(
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), "var3", false)
		);
		
		assertTrue(
			ApplicationInfoUtils.MetaData.getBooleanMeta(getContext(), "var4", false)
		);		
	}
}
