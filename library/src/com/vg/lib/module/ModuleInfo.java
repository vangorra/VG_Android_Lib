package com.vg.lib.module;

/**
 * Provides information about module.
 * @author vangorra
 *
 */
public abstract class ModuleInfo {
	/**
	 * The weight of the module which determines the order in which invokes are called.
	 * <p>Suggestions of module weight can be found as constants in the ModuleWeight class.</p>
	 */
	public final int weight = ModuleWeight.NORMAL;
	
	/**
	 * The name of the module.
	 */
	public final String name = "";
	
	/**
	 * The description of the module.
	 */
	public final String description = "";
	
	/**
	 * The version of the module.
	 */
	public final int version = 0;
	
	/**
	 * Provides a list of module this module depends on.
	 */
	@SuppressWarnings("unchecked")
	public final Class<? extends Module>[] dependencies = new Class[]{};
}
