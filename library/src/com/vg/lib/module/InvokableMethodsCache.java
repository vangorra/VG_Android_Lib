package com.vg.lib.module;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.util.Log;

import com.vg.lib.lang.reflect.MethodUtils;

/**
 * Caches invoke methods for modules.
 * @author vangorra
 *
 */
class InvokableMethodsCache {
	private static String TAG = InvokableMethodsCache.class.getSimpleName();
	private static int DEBUG = 0;
	private static Pattern invokeMethodRegex = Pattern.compile("^"+ ModuleManager.INVOKE_METHOD_PREFIX +".*$");
	private HashMap<String, ArrayList<Method>> invokeMethodCache = new HashMap<String, ArrayList<Method>>();
	
	/**
	 * Create a cache key.
	 * @param method
	 * @return
	 */
	private static String generateClassInvokeMethodKey(Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		return generateClassInvokeMethodKey(
				method.getDeclaringClass().getName(),
				method.getName(),
				paramTypes == null? 0: paramTypes.length
		);
	} // method
	
	/**
	 * Create a cache key.
	 * @param className
	 * @param methodName
	 * @param parameterCount
	 * @return
	 */
	private static String generateClassInvokeMethodKey(String className, String methodName, int parameterCount) {
		return className +":"+ methodName +":"+ parameterCount;
	} // method
	
	/**
	 * Cache all invoke methods for a given module class.
	 * @param moduleClass
	 */
	public <T extends Module> void cacheInvokeMethods(Class<T> moduleClass) {
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null.");
		}
		
		if(DEBUG > 0) Log.d(TAG, "cacheInvokeMethods: "+moduleClass.getSimpleName());
		if(DEBUG > 0) Log.d(TAG, "  total invokeable methods:"+ MethodUtils.findMethodsMatching(moduleClass, invokeMethodRegex).size());
		
		for(Method method: MethodUtils.findMethodsMatching(moduleClass, invokeMethodRegex)) {
			int modifiers = method.getModifiers();
			
			// if the method is not accessible, it is not worth working with.
			if(!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			}
			
			// add the method to the cache.
			addInvokeMethod(method);
		}
	} // method
	
	/**
	 * Determine if a module with the given parameters is in the cache.
	 * @param moduleClass
	 * @param methodName
	 * @param parameterCount
	 * @return True if there is an invokable method, false otherwise.
	 */
	public boolean hasInvokeableMethod(Class<? extends Module> moduleClass, String methodName, int parameterCount) {
		List<Method> methods = getInvokeMethods(moduleClass, methodName, parameterCount);
		return methods != null && methods.size() > 0;
	} // method
	
	/**
	 * Get all the cached invokable method for the given class, method name and parameter count.
	 * @param moduleClass
	 * @param methodName
	 * @param parameterCount
	 * @return The cached methods, null otherwise.
	 */
	public List<Method> getInvokeMethods(Class<? extends Module> moduleClass, String methodName, int parameterCount) {
		// get the cache key.
		String key = generateClassInvokeMethodKey(moduleClass.getName(), methodName, parameterCount);
		
		// return the cache for the given key.
		return invokeMethodCache.get(key);
	} // method
	
	/**
	 * Add an invokable method to the cache.
	 * @param method
	 */
	private void addInvokeMethod(Method method) {
		String key = generateClassInvokeMethodKey(method);
		
		if(DEBUG > 0) Log.d(TAG, "addInvokeMethod: "+key);
		
		// the list of methods doesn't exist yet, create it.
		if(!invokeMethodCache.containsKey(key)) {
			invokeMethodCache.put(key, new ArrayList<Method>());
		}
		
		// add to the cache.
		invokeMethodCache.get(key).add(
				method
		);
	} // method
} // class
