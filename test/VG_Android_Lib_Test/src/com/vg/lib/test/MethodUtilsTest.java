package com.vg.lib.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import android.app.Activity;
import android.content.Intent;
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
		
		
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"onInvokeActivityOnActivityResult", 
				new Class<?>[]{
					null,
					null,
					null,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"onInvokeActivityOnActivityResult", 
				new Class<?>[]{
					Activity.class,
					null,
					null,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"onInvokeActivityOnActivityResult", 
				new Class<?>[]{
					Activity.class,
					int.class,
					null,
					null,
				}
		));
		

		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"onInvokeActivityOnActivityResult", 
				new Class<?>[]{
					Activity.class,
					int.class,
					int.class,
					null,
				}
		));
		
		Assert.assertNotNull(MethodUtils.findClosestMethod(
				MethodUtilsTest.class, 
				"onInvokeActivityOnActivityResult", 
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
	
	public void anotherMethod(ArrayList list) {
		
	}
	
	public void anotherMethod(List list) {
		
	}
	
	public void onInvokeActivityOnActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		
	}
	
	private class ArrayList2 extends ArrayList {
		
	}
}
