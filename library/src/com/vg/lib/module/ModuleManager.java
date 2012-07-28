package com.vg.lib.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.vg.lib.lang.reflect.MethodUtils;

/**
 * Provides management and event notification of modules. This class is not particularly useful on its own.
 * It is intended to be used with the ModuleActivity.
 * @author vangorra
 *
 */
public final class ModuleManager {
	private static final String TAG = ModuleManager.class.getSimpleName();
	private static final int DEBUG = 0;
	static final String INVOKE_METHOD_PREFIX = "onInvoke";
	
	private static ModuleManager moduleManager;
	private HashMap<Class<? extends Module>,ModuleContainer> containers = new HashMap<Class<? extends Module>,ModuleContainer>();
	//private Context appContext;
	private ArrayList<ModuleContainer> getOrderedContainersCache = null;
	private WeightComparer weightComparer = new WeightComparer();
	private InvokableMethodsCache invokeableMethodsCache = new InvokableMethodsCache();
	private LinkedHashMap<String, Method> invokedMethodsCache = new LinkedHashMap<String, Method>();
	
	/**
	 * Initializes a new module manager.
	 */
	private ModuleManager() {
		
	} // method
	
	/**
	 * Initializes a new module manager.
	 * @param context Activity context.
	 * @deprecated
	 */
	private ModuleManager(Context context) {
		// check for null.
		if(context == null) {
			throw new IllegalArgumentException("Context cannot be null.");
		}
		
		// set the app context.
		//this.appContext = context.getApplicationContext();
	} // method
	
	/**
	 * Load a module with the given name and referenced by its class.
	 * Note: All child modules should have a public constructor with no arguments. This is how the module manager instantiates your module.
	 * @param moduleClass The class of the module.
	 * @param args Arguments for the module.
	 * @return Returns true if the module was successfully loaded. False otherwise.
	 */
	public boolean load(Class<? extends Module> moduleClass, Bundle args) {
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null.");
		}
		
		// check for null
		if(args == null) {
			throw new IllegalArgumentException("args cannot be null.");
		}
		
		// check if the provided module is already loaded.
		if(this.isLoaded(moduleClass)) {
			return true;
		}
		
		// prepare variables.
		ModuleContainer mc;
		try {
			if(DEBUG > 0) Log.d(TAG, "Loading Module:"+ moduleClass.getSimpleName());
			
			// instantiate a new module based on an empty constructor.
			Constructor<?> c = moduleClass.getConstructor();
			Module module = (Module) c.newInstance();
			module.load(args);
			
			// create a new module container and set the variables.
			mc = new ModuleContainer();
			mc.module = module;
			mc.loadArgs = args;
			mc.information = (ModuleInfo)moduleClass.getField("INFO").get(ModuleInfo.class);
			mc.isLoaded = true;
			
			// add the container to the list of containers.
			containers.put(moduleClass, mc);
			
			// clear the ordered modules cache.
			getOrderedContainersCache = null;
			
			if(DEBUG > 0) Log.d(TAG, "Successfully loaded module:"+ moduleClass.getSimpleName());
			
			// pre-cache the invoke methods for this module.
			invokeableMethodsCache.cacheInvokeMethods(moduleClass);
			
			return true;
			
		// handle exceptions for this module.
		} catch (NoSuchMethodException e) {
			System.err.println("Module "+ moduleClass.getName() +" does not have an empty constructor. That is needed in order to instantiate the module.");
			e.printStackTrace();
			
		} catch (IllegalArgumentException e) {
			System.err.println("Module "+ moduleClass.getName() +" does not have an empty constructor. That is needed in order to instantiate the module.");
			e.printStackTrace();
			
		} catch (InstantiationException e) {
			System.err.println("Couldn't instantiate "+ moduleClass.getName() +" does it have an empty constructor? That is needed in order to instantiate the module.");
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			System.err.println("Couldn't instantiate "+ moduleClass.getName() +". Is the empty constructor for the specified module public?.");
			e.printStackTrace();
			
		} catch (InvocationTargetException e) {
			System.err.println("Couldn't instantiate "+ moduleClass.getName() +". Is there another class with the same class path?.");
			e.printStackTrace();
			
		} catch (NoSuchFieldException e) {
			System.err.println("Couldn't instantiate "+ moduleClass.getName() +". Could not find public static final int WEIGHT.");
			e.printStackTrace();
		}
		
		return false;
	} // method
	
	/**
	 * Unload a module by name.
	 * @param modName
	 * @return Return true if the module was unloaded. False if the module could not be unloaded or the module wasn't loaded in the first place.
	 */
	public boolean unload(Class<? extends Module> moduleClass) {
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null.");
		}
		
		// get the container.
		ModuleContainer mc = containers.get(moduleClass);
		
		// if the container is null, the module isn't loaded, return false.
		if(mc == null) {
			return false;
		}
		
		// tell the module to unload.
		mc.module.unLoad();
		
		// remove the container from the containers.
		containers.remove(moduleClass);
		
		// clear the ordered modules cache.
		getOrderedContainersCache = null;
		
		// return
		return true;
	} // method
	
	/**
	 * Returns true if the specified module is loaded.
	 * @param moduleClass The class of the module.
	 * @return True if the module is loaded, false otherwise.
	 */
	public boolean isLoaded(Class<? extends Module> moduleClass) {
		// check for null.
		if(moduleClass == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// return if a module was returned.
		return this.get(moduleClass) != null;
	} // method

	/**
	 * Gets a loaded module.
	 * @param modName The name of the module.
	 * @return The loaded module or null if the module is not loaded.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Module> T get(Class<T> moduleClass) {
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null.");
		}
		
		// get the container.
		ModuleContainer mc = containers.get(moduleClass);
		
		// if the container is null, the module isn't loaded, return false.
		if(mc == null) {
			return null;
		}
		
		// if the class names are the same, cast and return the module.
		if(mc.module.getClass() == moduleClass) {
			return (T)mc.module;
		}
		
		return null;
	} // method
	
	/**
	 * This method is deprecated, use invokeAll instead.
	 * @param event
	 * @deprecated
	 */
	public void invoke(String event) {
		invoke(event, new Object[]{});
	} // method
	
	/**
	 * This method is depricated, use invokeAll instead.
	 * @param event
	 * @param params
	 * @deprecated
	 */
	public void invoke(String event, Object... params) {
		invokeAll(event, params);
	}

	/**
	 * Check if a specific module event can be invoked with the given parameters. 
	 * @param moduleClass The module class.
	 * @param event The invoke event.
	 * @param params The parameters to call on the event.
	 * @return True if the invoke can occur, false otherwise.
	 */
	public boolean isInvokeable(Class<? extends Module> moduleClass, String event, Object... params) {
		return invokeableMethodsCache.hasInvokeableMethod(
				moduleClass, 
				getInvokeMethodName(event), 
				params.length
		); 
	} // method
	
	/**
	 * Check if a specific module event can be invoked without parameters. 
	 * @param moduleClass The module class.
	 * @param event The invoke event.
	 * @param params The parameters to call on the event.
	 * @return True if the invoke can occur, false otherwise.
	 */
	public boolean isInvokeable(Class<? extends Module> moduleClass, String event) {
		return isInvokeable(moduleClass, event, 0);
	} // method
	
	/**
	 * Invoke an event on a specific module with parameters.
	 * @param moduleClass The module class
	 * @param event The event to invoke.
	 * @param params The parameters to send to the event.
	 * @return The object returned by the module's method or null if the event could not be invoked for that module. Note: you can use isInvokeable to check if a module can be invoked with particular parameters.
	 */
	public Object invoke(Class<? extends Module> moduleClass, String event, Object... params) {
		// check for null.
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null");
		}
		
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		if(params == null) {
			throw new IllegalArgumentException("params cannot be null");
		}
		
		// get the module.
		Module module = get(moduleClass);
		if(module == null) {
			return null;
		}
		
		// invoke the event.
		return invoke(
				module,
				getInvokeMethodName(event),
				params,
				getClassTypes(params)
		);
	} // method

	/**
	 * Invoke an event on a specific module.
	 * @param moduleClass The module class.
	 * @param event The event to invoke.
	 * @return The object returned by the module's method or null if the event could not be invoked for that module. Note: you can use isInvokeable to check if a module can be invoked with particular parameters.
	 */
	public Object invoke(Class<? extends Module> moduleClass, String event) {
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null");
		}
		
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		return invoke(moduleClass, event, new Object[]{});
	} // method.

	/**
	 * Invoke an event to all loaded modules without parameters. Invoke OnInvoke<EventStr> to all loaded modules. 
	 * <p>This will iterate through all modules (ordered by Module.INFO.weight) and invoke onInvoke<Event>
	 * if it exists.</p>
	 * <h3>Examples:</h3>
	 * <p>
	 * Will attempt to call onInvokeTestMethod on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("testMethod");
	 * </p>
	 * <p>
	 * Will attempt to call onInvokeTestmethod on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("testmethod");
	 * </p>
	 * 
	 * @param event The event to invoke.
	 * @return An ordered hash map that is keyed on the module class and the return type that module provided.
	 */
	public LinkedHashMap<Class<? extends Module>, Object> invokeAll(String event) {
		return invokeAll(event, new Object[]{});
	} // method
	
	/**
	 * Invoke an event to all loaded modules. Invoke OnInvoke<EventStr> with the same datatypes as
	 * the arguments passed to this method. 
	 * <p>This will iterate through all modules (ordered by Module.INFO.weight) and invoke onInvoke<Event>
	 * if it exists.</p>
	 * <h3>Examples:</h3>
	 * <p>
	 * Will attempt to call onInvokeTestMethod on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("testMethod");
	 * </p>
	 * <p>
	 * Will attempt to call onInvokeTestmethod on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("testmethod");
	 * </p>
	 * <p>
	 * Will attempt to call onInvokeTestMethod(String str) on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("TestMethod", "hello there");
	 * </p>
	 * <p>
	 * Will attempt to call onInvokeTestMethod(String str, int num) on all loaded modules.<br />
	 * ModuleManager.getInstance(this).invoke("TestMethod", "hello there", 5);
	 * </p>
	 * @param event The event to invoke.
	 * @param params The parameters to pass to the module method.
	 * @return An ordered hash map that is keyed on the module class and the return type that module provided.
	 */
	public LinkedHashMap<Class<? extends Module>, Object> invokeAll(String event, Object... params) {
		// check for null
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		if(params == null) {
			throw new IllegalArgumentException("params cannot be null");
		}
		
		// get the invoke method.
		String methodName = getInvokeMethodName(event);
		
		// child the list of classes.
		Class<?>[] paramTypes = getClassTypes(params);
		
		if(DEBUG > 0) Log.d(TAG, "Invoking "+ event +", "+ params.toString());
		
		// iterate through the loaded modules and call the event.
		LinkedHashMap<Class<? extends Module>, Object> returnObjects = new LinkedHashMap<Class<? extends Module>, Object>();
		for(ModuleContainer mc: getOrderedContainers()) {
			boolean isInvokeable = invokeableMethodsCache.hasInvokeableMethod(
					mc.module.getClass(), 
					methodName, 
					params.length
			);
			
			// no method exists, don't include a result.
			if(!isInvokeable) {
				if(DEBUG > 2) Log.d(TAG, "  "+ mc.module.getClass() +", not invokeable.");
				continue;
			}
			
			returnObjects.put(
					mc.module.getClass(),
					invoke(
							mc.module, 
							methodName, 
							params, 
							paramTypes
					)
			);
		} // for
		
		return returnObjects;
	} // method
	
	/**
	 * Get a list of the loaded ModuleContainer objects ordered by the module's weight.
	 * @return The ordered list of ModuleContainer objects.
	 */
	private ArrayList<ModuleContainer> getOrderedContainers() {
		// attempt to pull the ordered containers from cache.
		if(getOrderedContainersCache != null) {
			return getOrderedContainersCache;
		}
		
		// create the list.
		ArrayList<ModuleContainer> sortedContainers = new ArrayList<ModuleContainer>(containers.values());
		
		// sort the list.
		Collections.sort(sortedContainers, weightComparer);
		
		// update the cache and return.
		getOrderedContainersCache = sortedContainers;
		return getOrderedContainersCache;
	} // method

	/**
	 * Invoke a specific method on a specific module with specific parameters. This will still
	 * perform a fuzzy search based on parameter type.
	 * @param module
	 * @param methodName
	 * @param params
	 * @param paramTypes
	 */
	private Object invoke(Module module, String methodName, Object[] params, Class<?>[] paramTypes) {
		// check for null
		if(module == null) {
			throw new IllegalArgumentException("module cannot be null");
		}
		
		if(methodName == null) {
			throw new IllegalArgumentException("methodName cannot be null");
		}
		
		if(params == null) {
			throw new IllegalArgumentException("params cannot be null");
		}
		
		if(paramTypes == null) {
			throw new IllegalArgumentException("paramTypes cannot be null");
		}
		
		// init the method.
		Method method;
		
		// pull method from cache if it is there.
		if(isCachedInvokedMethod(module, methodName, paramTypes)) {
			method = getCachedInvokedMethod(module, methodName, paramTypes);
			
		// doesn't look like a method  is in cache.
		} else {
			// get a list of possible methods for this event with the same number of parameters.
			List<Method> methods = invokeableMethodsCache.getInvokeMethods(
					module.getClass(), 
					methodName, 
					params.length
			);
			
			// find the method.
			if(params.length > 0) {
				// find the closest method match.
				method = MethodUtils.findClosestMethod(methods, paramTypes);
				
			// we are looking at a method with 0 parameters. Only 1 such method can exist.
			} else {
				method = methods.size() > 0 ? methods.get(0): null;
			}
			
			// cache this method for later use.
			setCachedInvokedMethod(module, methodName, paramTypes, method);			
		}
		
		if(DEBUG > 0) Log.d(TAG, "ModuleContainer.Module: "+ module.getClass().getSimpleName());
	
		// method could not be found,
		if(method == null) {
			if(DEBUG > 0)Log.d(TAG, "\tmethod not found.");
			return null;
		}
		
		// invoke the method.
		try {
			if(DEBUG > 0)Log.d(TAG, "\tinvoke.");
			Object val = method.invoke(module, params);
			if(DEBUG > 0)Log.d(TAG, "\tinvoke successful.");
			return val;
			
		} catch (IllegalArgumentException e) {
			// ignore
			if(DEBUG > 0)Log.d(TAG, "\tinvoke failed."+e.getMessage());
			
		} catch (IllegalAccessException e) {
			// ignore
			if(DEBUG > 0)Log.d(TAG, "\tinvoke failed."+e.getMessage());
			
		} catch (InvocationTargetException e) {
			// ignore
			if(DEBUG > 0)Log.d(TAG, "\tinvoke failed."+e.getMessage());
		}
		
		return null;
	} // method
	
	/**
	 * Get an invoked method from the cache.
	 * @param module
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	private Method getCachedInvokedMethod(Module module, String methodName, Class<?>[] paramTypes) {
		// check for null.
		if(module == null) {
			throw new IllegalArgumentException("module cannot be null.");
		}
		
		if(methodName == null) {
			throw new IllegalArgumentException("methodName cannot be null.");
		}
		
		if(paramTypes == null) {
			throw new IllegalArgumentException("paramTypes cannot be null.");
		}
		
		String cacheKey = getCachedInvokedMethodKey(module, methodName, paramTypes);
		
		// get the cache.
		Method method = invokedMethodsCache.get(cacheKey);
		if(method == null) {
			return null;
		}
		
		// this was recently used, move it to the end of the cache.
		// this will ensure that only least used cache items will be purged.
		invokedMethodsCache.remove(cacheKey);
		invokedMethodsCache.put(cacheKey, method);
		return method;
	} // method
	
	/**
	 * Set an invoked method into the cache.
	 * @param module
	 * @param methodName
	 * @param paramTypes
	 * @param method
	 */
	private void setCachedInvokedMethod(Module module, String methodName, Class<?>[] paramTypes, Method method) {
		// check for null.
		if(module == null) {
			throw new IllegalArgumentException("module cannot be null.");
		}
		
		if(methodName == null) {
			throw new IllegalArgumentException("methodName cannot be null.");
		}
		
		if(paramTypes == null) {
			throw new IllegalArgumentException("paramTypes cannot be null.");
		}
		
		if(method == null) {
			throw new IllegalArgumentException("method cannot be null.");
		}
		
		// limit the size of the cache.
		if(invokedMethodsCache.size() > 200) {
			// remove the first (oldest) item in the cache.
			List<String> keys = new ArrayList<String>(invokedMethodsCache.keySet());
			invokedMethodsCache.remove(keys.get(0));
		}
		
		String cacheKey = getCachedInvokedMethodKey(module, methodName, paramTypes);
		invokedMethodsCache.put(cacheKey, method);
	} //method
	
	/**
	 * Check if an invoked method is in the cache based on cache key.
	 * @param module
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	private boolean isCachedInvokedMethod(Module module, String methodName, Class<?>[] paramTypes) {
		// check for null.
		if(module == null) {
			throw new IllegalArgumentException("module cannot be null.");
		}
		
		if(methodName == null) {
			throw new IllegalArgumentException("methodName cannot be null.");
		}
		
		if(paramTypes == null) {
			throw new IllegalArgumentException("paramTypes cannot be null.");
		}
		
		String cacheKey = getCachedInvokedMethodKey(module, methodName, paramTypes);
		return invokedMethodsCache.containsKey(cacheKey);
	} // method
	
	/**
	 * Generate a cache key used for invoked methods cache.
	 * @param module
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	private static String getCachedInvokedMethodKey(Module module, String methodName, Class<?>[] paramTypes) {
		// check for null.
		if(module == null) {
			throw new IllegalArgumentException("module cannot be null.");
		}
		
		if(methodName == null) {
			throw new IllegalArgumentException("methodName cannot be null.");
		}
		
		if(paramTypes == null) {
			throw new IllegalArgumentException("paramTypes cannot be null.");
		}
		
		StringBuilder sb = new StringBuilder()
			.append(module.getClass().getName())
			.append(".")
			.append(methodName)
			.append("(")
		;
		for(Class<?> cls: paramTypes) {
			String clsName = "";
			
			if(cls == null) {
				clsName = "null";
			} else {
				if(cls.isPrimitive()) {
					clsName = MethodUtils.getPrimitiveEquivlent(cls).getName();
				} else {
					clsName = cls.getName();
				}
			}
			sb.append(clsName);
			sb.append(",");
		}
		sb.append(")");
		return sb.toString();
	} // method
	
	/**
	 * Gets the current instance of the module manager or create a new instance if one doesn't exist yet.
	 * @param context The context calling the module manager.
	 * @return The current module manager instance.
	 * @deprecated
	 */
	public static ModuleManager getInstance(Context context) {
		// check for null.
		if(context == null) {
			throw new IllegalArgumentException("Context cannot be null.");
		}
		
		// if module manager is null.
		if(moduleManager == null) {
			// create a new static instance of module manager.
			moduleManager = new ModuleManager(context);
		}
		
		// return the static instance of the module manager.
		return moduleManager;
	} // method

	/**
	 * Gets the current instance of the module manager or create a new instance if one doesn't exist yet.
	 * @return The current module manager instance.
	 */
	public static ModuleManager getInstance() {
		// if module manager is null.
		if(moduleManager == null) {
			// create a new static instance of module manager.
			moduleManager = new ModuleManager();
		}
		
		// return the static instance of the module manager.
		return moduleManager;
	} // method
	
	/**
	 * Creates an invoke method name based on the given string.
	 * @param event The event to invoke.
	 * @return The method name to invoke on a module.
	 */
	private static final String getInvokeMethodName(String event) {
		// check for null
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		return new StringBuilder(INVOKE_METHOD_PREFIX.length() + event.length())
			.append(INVOKE_METHOD_PREFIX)
			.append(event.substring(0, 1).toUpperCase())
			.append(event.substring(1))
			.toString()
		;
	} // method.

	/**
	 * Get an array of class types that represent the object array passed in.
	 * @param args
	 * @return
	 */
	private static Class<?>[] getClassTypes(Object[] args) {
		// check for null.
		if(args == null) {
			throw new IllegalArgumentException("args cannot be null");
		}
		
		Class<?>[] paramTypes = new Class<?>[args.length];
		for(int i = 0; i < paramTypes.length; ++i) {
			paramTypes[i] = args[i] == null? null: args[i].getClass();
		}
		return paramTypes;
	} // method
} // class
