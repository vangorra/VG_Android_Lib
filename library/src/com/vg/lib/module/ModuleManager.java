package com.vg.lib.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
	private static final boolean DEBUG = false;
	
	private static ModuleManager moduleManager;
	private HashMap<Class<? extends Module>,ModuleContainer> containers = new HashMap<Class<? extends Module>,ModuleContainer>();
	private Context appContext;
	private ArrayList<ModuleContainer> getOrderedContainersCache = null;
	private WeightComparer weightComparer = new WeightComparer();
	
	/**
	 * Initializes a new module manager.
	 * @param context Activity context.
	 */
	private ModuleManager(Context context) {
		// check for null.
		if(context == null) {
			throw new IllegalArgumentException("Context cannot be null.");
		}
		
		// set the app context.
		this.appContext = context.getApplicationContext();
	} // method
	
	/**
	 * Gets the current instance of the module manager or create a new instance if one doesn't exist yet.
	 * @param context The context calling the module manager.
	 * @return The current module manager instance.
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
	 * Returns true if the specified module is loaded.
	 * @param moduleClass The class of the module.
	 * @return True if the module is loaded, false otherwise.
	 */
	public <T extends Module> boolean isLoaded(Class<T> moduleClass) {
		// check for null.
		if(moduleClass == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// return if a module was returned.
		return this.get(moduleClass) != null;
	} // method

	/**
	 * Load a module with the given name and referenced by its class.
	 * Note: All child modules should have a public constructor with no arguments. This is how the module manager instantiates your module.
	 * @param moduleClass The class of the module.
	 * @param args Arguments for the module.
	 * @return Returns true if the module was successfully loaded. False otherwise.
	 */
	public <T extends Module> boolean load(Class<T> moduleClass, Bundle args) {
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
			// instantiate a new module based on an empty constructor.
			Constructor<?> c = moduleClass.getConstructor();
			Module module = (Module) c.newInstance();
			
			// create a new module container and set the variables.
			mc = new ModuleContainer();
			mc.module = module;
			mc.loadArgs = args;
			mc.information = (ModuleInfo)moduleClass.getField("INFO").get(ModuleInfo.class);
			
			// add the container to the list of containers.
			containers.put(moduleClass, mc);
			
			// call the load method on the module.
			module.load(appContext, args);
			
			// clear the ordered modules cache.
			getOrderedContainersCache = null;
			
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
	public <T extends Module> boolean unload(Class<T> moduleClass) {
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
	 * Gets a loaded module.
	 * @param modName The name of the module.
	 * @return The loaded module or null if the module is not loaded.
	 */
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
		if(mc.module.getClass().getName().equals(moduleClass.getName())) {
			return (T)mc.module;
		}
		
		return null;
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
		
		// update the cahe and return.
		getOrderedContainersCache = sortedContainers;
		return getOrderedContainersCache;
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
		
		return "onInvoke"+ event.substring(0, 1).toUpperCase() + event.substring(1);
	} // method.
	
	/**
	 * Invoke an event to all loaded modules. Invoke OnInvoke<EventStr> to all loaded modules. 
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
	 */
	public void invoke(String event) {
		// check for null
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		// iterate trough the loaded modules and call the event.
		for(ModuleContainer mc: getOrderedContainers()) {
			try {
				// attempt to get the method and invoke.
				Method method = mc.module.getClass().getMethod(
						getInvokeMethodName(event)
				);
				method.invoke(mc.module);
			} catch (NoSuchMethodException e) {
				// ignore
			} catch (IllegalArgumentException e) {
				// ignore
			} catch (IllegalAccessException e) {
				// ignore
			} catch (InvocationTargetException e) {
				// ignore
			}
		} // for
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
	 * @param event
	 * @param params
	 */
	public void invoke(String event, Object... params) {
		// check for null
		if(event == null) {
			throw new IllegalArgumentException("event cannot be null");
		}
		
		// check for null
		if(params == null) {
			throw new IllegalArgumentException("params cannot be null");
		}
		
		// child the list of classes.
		Class<?>[] classesArr = new Class<?>[params.length];
		for(int i = 0; i < classesArr.length; ++i) {
			classesArr[i] = params[i] == null? null: params[i].getClass();
		}
		
		// iterate trough the loaded modules and call the event.
		for(ModuleContainer mc: getOrderedContainers()) {
			try {
				// attempt to get the method and invoke.
				Method method = MethodUtils.findClosestMethod(
						mc.module.getClass(),
						getInvokeMethodName(event),
						classesArr
				);
				
				String argsStr = "";
				for(Class<?> cls: classesArr) {
					argsStr += cls == null? "null,": cls.getSimpleName()+",";
				}
				if(DEBUG)Log.v("TESTME", mc.module.getClass().getSimpleName() +"."+getInvokeMethodName(event)+"("+ argsStr +")");
				
				
				// method could not be found,
				if(method == null) {
					if(DEBUG)Log.v("TESTME", "\tmethod not found.");
					continue;
				}
				
				// invoke the method.
				if(DEBUG)Log.v("TESTME", "\tinvoke.");
				method.invoke(mc.module, params);
				if(DEBUG)Log.v("TESTME", "\tinvoke successful.");
				continue;
				
			} catch (IllegalArgumentException e) {
				// ignore
				if(DEBUG)Log.v("TESTME", "\tinvoke failed."+e.getMessage());
				
			} catch (IllegalAccessException e) {
				// ignore
				if(DEBUG)Log.v("TESTME", "\tinvoke failed."+e.getMessage());
				
			} catch (InvocationTargetException e) {
				// ignore
				if(DEBUG)Log.v("TESTME", "\tinvoke failed."+e.getMessage());
			}
		} // for
	} // method
	
} // class
