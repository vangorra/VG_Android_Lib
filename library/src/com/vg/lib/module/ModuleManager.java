package com.vg.lib.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Provides management and event notification of modules. This class is not particularly useful on its own.
 * It is intended to be used with the ModuleActivity.
 * @author vangorra
 *
 */
public final class ModuleManager {
	private static ModuleManager moduleManager;
	private HashMap<String,ModuleContainer> containers = new HashMap<String,ModuleContainer>();
	private Context appContext;
	private ModuleDispatcher dispatcher = new ModuleDispatcher(); 
	
	/**
	 * Provides meta-data about a managed module.
	 */
	private static final class ModuleContainer {
		public Module module;
		public Bundle loadArgs;
	} // class
	
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
	 * The the current instance of the module manager. Will throw an IllegalStateException if the module manager 
	 * has not been initialized via ModuleManager.getInstance(Context)
	 * @return The current module manager instance.
	 */
	public static ModuleManager getInstance() {
		if(moduleManager == null) {
			throw new IllegalStateException("ModuleManager was never initialized. You must call ModuleManager.getInstance(Context) to get it initialized.");
		}
		return moduleManager;
	} // method
	
	/**
	 * Get the dispatcher for dispatching events to modules.
	 * @return The module dispatcher.
	 */
	ModuleDispatcher getDispatcher() {
		return dispatcher;
	} // method
	
	/**
	 * Returns true if the specified module is loaded.
	 * @param modName The name of the module.
	 * @return True if the module is loaded, false otherwise.
	 */
	public boolean isLoaded(String modName) {
		// check for null.
		if(modName == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// return if a module was returned.
		return this.get(modName) != null;
	} // method

	/**
	 * Load a module with the given name and referenced by its class.
	 * Note: All child modules should have a public constructor with no arguments. This is how the module manager instantiates your module.
	 * @param modName The name of the module (arbitrary)
	 * @param moduleClass The class of the module.
	 * @param args Arguments for the module.
	 * @return Returns true if the module was successfully loaded. False otherwise.
	 */
	public boolean load(String modName, Class<? extends Module> moduleClass, Bundle args) {
		// check for null
		if(modName == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// check for null
		if(moduleClass == null) {
			throw new IllegalArgumentException("moduleClass cannot be null.");
		}
		
		// check for null
		if(args == null) {
			throw new IllegalArgumentException("args cannot be null.");
		}
		
		// check if the provided module is already loaded.
		if(this.isLoaded(modName)) {
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
			
			// add the container to the list of containers.
			containers.put(modName, mc);
			
			// call the load method on the module.
			module.load(appContext, args);
			
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
		}
		
		return false;
	} // method
	
	/**
	 * Unload a module by name.
	 * @param modName
	 * @return Return true if the module was unloaded. False if the module could not be unloaded or the module wasn't loaded in the first place.
	 */
	public boolean unload(String modName) {
		// check for null
		if(modName == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// get the container.
		ModuleContainer mc = containers.get(modName);
		
		// if the container is null, the module isn't loaded, return false.
		if(mc == null) {
			return false;
		}
		
		// tell the module to unload.
		mc.module.unLoad();
		
		// remove the container from the containers.
		containers.remove(modName);
		
		// return
		return true;
	} // method
	
	/**
	 * Gets a loaded module.
	 * @param modName The name of the module.
	 * @return The loaded module or null if the module is not loaded.
	 */
	public Module get(String modName) {
		// check for null
		if(modName == null) {
			throw new IllegalArgumentException("modName cannot be null.");
		}
		
		// get the container.
		ModuleContainer mc = containers.get(modName);
		
		// if the container is null, the module isn't loaded, return false.
		if(mc == null) {
			return null;
		}
		
		// return the module.
		return mc.module;
	} // method
	
	/**
	 * Provides a class for dispatching an event to all modules.
	 * @author vangorra
	 *
	 */
	final class ModuleDispatcher implements ModuleHooks {
		@Override
		public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityCreate(activity, savedInstanceState);
			}
		} // method

		@Override
		public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityPostCreate(activity, savedInstanceState);
			}
		} // method
		
		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivitySaveInstanceState(activity, outState);
			}
		} // method

		@Override
		public void onActivityPause(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityPause(activity);
			}
		} // method

		@Override
		public void onActivityRestart(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityRestart(activity);
			}
		} // method

		@Override
		public void onActivityDestroy(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityDestroy(activity);
			}
		} // method

		@Override
		public void onActivityResume(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityResume(activity);
			}
		} // method

		@Override
		public void onActivityStart(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityPause(activity);
			}
		} // method

		@Override
		public void onActivityStop(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityStop(activity);
			}
		} // method
		
		@Override
		public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityResult(activity, requestCode, resultCode, data);
			}
		} // method

		@Override
		public void onActivityUserInteraction(Activity activity) {
			// iterate trough the loaded modules and call the event.
			for(ModuleContainer mc: containers.values()) {
				mc.module.onActivityUserInteraction(activity);
			}
		} // method
	} // class
} // class
