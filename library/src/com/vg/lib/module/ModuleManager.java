package com.vg.lib.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Provides management and event notification of modules.
 * @author vangorra
 *
 */
public class ModuleManager {
	private static ModuleManager moduleManager;
	private HashMap<String,ModuleContainer> containers = new HashMap<String,ModuleContainer>();
	private Context appContext;
	
	/**
	 * 
	 */
	private static final class ModuleContainer {
		public Module module;
		public boolean isLoaded;
		public Bundle loadArgs;
	}
	
	/**
	 * Initializes a new module manager.
	 * @param context Activity context.
	 */
	private ModuleManager(Context context) {
		this.appContext = context;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ModuleManager getInstance(Context context) {
		if(moduleManager == null) {
			moduleManager = new ModuleManager(context.getApplicationContext());
		}
		return moduleManager;
	}
	
	public boolean isLoaded(String modName) {
		return this.get(modName) != null;
	}
	
	public void load(String modName, Class<? extends BaseModuleImpl> moduleClass, Bundle args) {
		if(this.isLoaded(modName)) {
			return;
		}
		
		ModuleContainer mc;
		try {
			/*if(!moduleClass.isInstance(Module.class)) {
				throw new ModuleManagerException(moduleClass.getName()+" is not an instance of Module");
//				return;
			}*/
			
			Constructor<?> c = moduleClass.getConstructor();
			Module module = (Module) c.newInstance();
			mc = new ModuleContainer();
			mc.module = module;
			mc.isLoaded = true;
			mc.loadArgs = args;
			
			containers.put(modName, mc);
			
			module.load(appContext, args);
			
		} catch (NoSuchMethodException e) {
			// ignore
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// ignore
//			e.printStackTrace();
		} catch (InstantiationException e) {
			// ignore
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// ignore
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// ignore
//			e.printStackTrace();
		}
	}
	
	public void unload(String modName) {
		ModuleContainer mc = containers.get(modName);
		if(mc == null) {
			//throw new ModuleManagerException("A module with the name '"+ modName +"' is not loaded.");
			return;
		}
		
		mc.module.unLoad();
		containers.remove(modName);
	}
	
	public Module get(String modName) {
		ModuleContainer mc = containers.get(modName);
		if(mc == null) {
			//throw new ModuleManagerException("A module with the name '"+ modName +"' is not loaded.");
			return null;
		}
		
		return mc.module;
	}
	
	
	
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityCreate(activity, savedInstanceState);
		}
	}

	public void onActivityPostCreate(Activity activity, Bundle savedInstanceState) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityPostCreate(activity, savedInstanceState);
		}
	}
	
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivitySaveInstanceState(activity, outState);
		}
	}

	public void onActivityPause(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityPause(activity);
		}
	}

	public void onActivityRestart(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityRestart(activity);
		}
	}

	public void onActivityDestroy(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityDestroy(activity);
		}
	}

	public void onActivityResume(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityResume(activity);
		}
	}

	public void onActivityStart(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityPause(activity);
		}
	}

	public void onActivityStop(Activity activity) {
		for(ModuleContainer mc: containers.values()) {
			mc.module.onActivityStop(activity);
		}
	}
}
