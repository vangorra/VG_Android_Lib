package com.vg.lib.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import android.test.AndroidTestCase;
import android.util.Log;

import com.vg.lib.lang.reflect.MethodUtils;

public class MethodUtilsTest extends AndroidTestCase {
	public void testFindClosesMethod() {
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					String.class,
					int.class
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					String.class,
					null
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					null,
					int.class
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					null,
					null
				}
			)
		);
		
		Assert.assertNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					null,
				}
			)
		);
		
		Assert.assertNull(
			MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"thisIsMySuperTestMethod", 
				new Class<?>[]{
					String.class,
				}
			)
		);
		
		Method method = MethodUtils.findClosestMethod(
			MethodUtilsTest.class, 
			"anotherMethod", 
			new Class<?>[]{
				ArrayList2.class,
			}
		);
		Assert.assertNotNull(
			method
		);
		
		Assert.assertTrue(
			method.getParameterTypes()[0] == ArrayList.class
		);
	}
	
	public void thisIsMySuperTestMethod(String str1, int int1) {
		
	}
	
	public void anotherMethod(ArrayList list) {
		
	}
	
	public void anotherMethod(List list) {
		
	}
	
	private class ArrayList2 extends ArrayList {
		
	}
}
