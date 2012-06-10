package com.vg.lib.module;

import java.util.Comparator;

/**
 * Compares 2 module ModuleContainers based on the Module's weight.
 * @author vangorra
 *
 */
final class WeightComparer implements Comparator<ModuleContainer> {
	@Override
	public int compare(ModuleContainer lhs, ModuleContainer rhs) {
		ModuleInfo lInfo = lhs.information;
		ModuleInfo rInfo = rhs.information;
		
		if(lInfo.weight < rInfo.weight) {
			return -1;
		}
		
		if(lInfo.weight > rInfo.weight ){
			return 1;
		}
		
		return 0;
	}
}
