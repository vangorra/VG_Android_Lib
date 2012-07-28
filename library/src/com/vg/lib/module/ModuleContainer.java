package com.vg.lib.module;

import android.os.Bundle;

/**
 * Provides meta-data about a managed module.
 */
final class ModuleContainer {
	public Module module;
	public Bundle loadArgs;
	public ModuleInfo information;
	public boolean isLoaded = false;
} // class
