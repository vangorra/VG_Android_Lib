package com.vg.lib.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Defines the basic structure of a module. It has the basic methods needed by the ModuleManager
 * and the basic activity events callbacks.
 * Note: All child modules should have a public constructor with no arguments. This is how the module manager instantiates your module.
 * @author vangorra
 *
 */
public interface Module extends ModuleHooks {
	
	/**
	 * Called when a module instance is being loaded.
	 * This is specific to the module system.
	 * @param context the current activity context.
	 * @param args The arguments to send to the module.
	 */
	public void load(Context context, Bundle args);
	
	/**
	 * Called when the module is being unloaded.
	 * This is specific to the module system.
	 */
	public void unLoad();
} //interface
