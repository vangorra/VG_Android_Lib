package com.vg.lib.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import android.app.Activity;
import android.content.Intent;
import android.test.AndroidTestCase;

import com.vg.lib.lang.reflect.MethodUtils;

public class MethodUtilsTest extends AndroidTestCase {
	
	public void testGetPrimitiveEquivlent() {
		Assert.assertNull(MethodUtils.getPrimitiveEquivlent(null));
		
		HashMap<Class<?>,Class<?>> primToNonMap = new HashMap<Class<?>, Class<?>>();
		primToNonMap.put(Byte.class, byte.class);
		primToNonMap.put(Short.class, short.class);
		primToNonMap.put(Integer.class, int.class);
		primToNonMap.put(Long.class, long.class);
		primToNonMap.put(Double.class, double.class);
		primToNonMap.put(Boolean.class, boolean.class);
		primToNonMap.put(Character.class, char.class);
		primToNonMap.put(Float.class, float.class);
		
		for(Class<?> nonPrim: primToNonMap.keySet()) {
			Assert.assertTrue(
					MethodUtils.getPrimitiveEquivlent(nonPrim) == primToNonMap.get(nonPrim)
			);
			Assert.assertTrue(
					MethodUtils.getPrimitiveEquivlent(primToNonMap.get(nonPrim)) == nonPrim
			);
		}
	}
	
	public void testFindMethodsMatching() {
		try {
			MethodUtils.findMethodsMatching(null, null);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			MethodUtils.findMethodsMatching(MethodUtilsTest.class, null);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		int cnt = MethodUtils.findMethodsMatching(MethodUtilsTest.class, Pattern.compile("^test.*$")).size();
		Assert.assertTrue(cnt > 1);
		
		cnt = MethodUtils.findMethodsMatching(MethodUtilsTest.class, Pattern.compile("^testFindMethodsMatching$")).size();
		Assert.assertTrue(cnt == 1);
		
		cnt = MethodUtils.findMethodsMatching(MethodUtilsTest.class, Pattern.compile("^sdf sd lss fodfjsdoifoiwhifdkslfdsklfsd fsf$")).size();
		Assert.assertTrue(cnt == 0);
	}
	
	public void testFindClosestMethod() {
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					String.class,
					int.class
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					String.class,
					null
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					null,
					int.class
				}
			)
		);
		
		Assert.assertNotNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					null,
					null
				}
			)
		);
		
		Assert.assertNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					null,
				}
			)
		);
		
		Assert.assertNull(
			MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^thisIsMySuperTestMethod$")
				),
				new Class<?>[]{
					String.class,
				}
			)
		);
		
		Method method = MethodUtils.findClosestMethod(
			MethodUtils.findMethodsMatching(
				MethodUtilsTest.class, 
				Pattern.compile("^anotherMethod$")
			),
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
		
		
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^onInvokeActivityOnActivityResult$")
				),
				new Class<?>[]{
					null,
					null,
					null,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^onInvokeActivityOnActivityResult$")
				),
				new Class<?>[]{
					Activity.class,
					null,
					null,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^onInvokeActivityOnActivityResult$")
				),
				new Class<?>[]{
					Activity.class,
					int.class,
					null,
					null,
				}
		));
		

		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^onInvokeActivityOnActivityResult$")
				),
				new Class<?>[]{
					Activity.class,
					int.class,
					int.class,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtils.findMethodsMatching(
					MethodUtilsTest.class, 
					Pattern.compile("^onInvokeActivityOnActivityResult$")
				),
				new Class<?>[]{
					Activity.class,
					int.class,
					int.class,
					Intent.class,
				}
		));
	}
	
	public void thisIsMySuperTestMethod(String str1, int int1) {
		
	}
	
	@SuppressWarnings("rawtypes")
	public void anotherMethod(ArrayList list) {
		
	}
	
	@SuppressWarnings("rawtypes")
	public void anotherMethod(List list) {
		
	}
	
	public void onInvokeActivityOnActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	private class ArrayList2 extends ArrayList {
		
	}
}
