package com.vg.lib.lang.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MethodUtils {
	private static final boolean DEBUG = false;
	
	private static final MethodMatchComparer methodMatchComparer = new MethodMatchComparer();
	
	/**
	 * Provides a cache for findClosestMethod.
	 */
	private static HashMap<String,Method> findClosestMethodCache = new HashMap<String,Method>();
	
	/**
	 * <p>Searches a class for a method that best best matches the name and parameter types provided. Some of the 
	 * targetParamTypes can be null, that is where the "best match" comes into play.</p>
	 * Conditions of the search:
	 * <ul>
	 * 	 <li>Class method name must match targetMethodName.</li>
	 * 	 <li>Class method params count must match targetParamTypes count.</li>
	 *   <li>Class method param types must match targetParamType types.</li>
	 * </ul>
	 * @param targetClass The class to scan through.
	 * @param targetMethodName The method to search form.
	 * @param targetParamTypes The method params of the method to find.
	 * @return The best matching method or null if a method could not be found.
	 */
	public static Method findClosestMethod(Class<?> targetClass, String targetMethodName, Class<?>[] targetParamTypes) {
		// check for null
		if(targetClass == null) {
			throw new IllegalArgumentException("classType cannot be null.");
		}
		
		// check for null
		if(targetMethodName == null) {
			throw new IllegalArgumentException("targetMethodName cannot be null.");
		}
		
		// check for empty
		if(targetMethodName.length() == 0) {
			throw new IllegalArgumentException("targetMethodName cannot be an empty string.");
		}
		
		// check for null
		if(targetParamTypes == null) {
			throw new IllegalArgumentException("targetParamTypes cannot be null.");
		}
		
		// check for empty
		if(targetParamTypes.length == 0) {
			throw new IllegalArgumentException("targetParamTypes cannot be an empty array.");
		}
		
		// generate a cache key.
		String cacheKey = (targetMethodName.hashCode()+targetParamTypes.hashCode()) +"";
		
		// return the cached method.
		if(findClosestMethodCache.containsKey(cacheKey)) {
			return findClosestMethodCache.get(cacheKey);
		}
		
		// get a method, cache it, and return it.
		Method method = _findClosestMethod(targetClass, targetMethodName, targetParamTypes);
		findClosestMethodCache.put(
				cacheKey, 
				method
		);
		return method;
	} // method
	
	
	private static HashMap<Class<?>,Class<?>> primitiveObjectMap = new HashMap<Class<?>, Class<?>>();
	private static void initPrimitiveObjectMap() {
		if(primitiveObjectMap.size() > 0) {
			return; 
		}
		
		primitiveObjectMap.put(Byte.class, byte.class);
		primitiveObjectMap.put(Short.class, short.class);
		primitiveObjectMap.put(Integer.class, int.class);
		primitiveObjectMap.put(Long.class, long.class);
		primitiveObjectMap.put(Float.class, float.class);
		primitiveObjectMap.put(Double.class, double.class);
		primitiveObjectMap.put(Boolean.class, boolean.class);
		primitiveObjectMap.put(Character.class, char.class);
	}

	public static Class<?> getPrimitiveEquivlent(Class<?> cls) {
		initPrimitiveObjectMap();
		return primitiveObjectMap.get(cls);
	}
	
	
	/**
	 * Worker method for findClosestMethod.
	 * @param classType
	 * @param targetMethodName
	 * @param targetParamTypes
	 * @return
	 */
	private static Method _findClosestMethod(Class<?> classType, String targetMethodName, Class<?>[] targetParamTypes) {
		// try to get the method directly. 
		try {
			return classType.getMethod(targetMethodName, targetParamTypes);
		
		// one of the params is probably null. 
		} catch (Exception e) {
			// ignore
		}
		
		/*
		 * Find the closest method match.
		 */
		ArrayList<MethodMatch> possibleMatches = new ArrayList<MethodMatch>();
		
		// iterate through the class methods.
		for(Method method: classType.getMethods()) {
			// if the method name is not the same as our target method name, goto next loop item.
			if(!method.getName().equals(targetMethodName)) {
				continue;
			}
			
			// get the parameter types.
			Class<?>[] methodParameterTypes = method.getParameterTypes();
			
			// method and taraget method don't have the same number of parameters.
			if(methodParameterTypes.length != targetParamTypes.length) {
				continue;
			}
			
			// check the type of the method parameters.
			boolean possibleMatch = true;
			MethodMatch methodMatch = new MethodMatch(method);
			for(int i = 0; i < methodParameterTypes.length; ++i) {
				Class<?> methodParamType = methodParameterTypes[i];
				Class<?> targetParamType = targetParamTypes[i];
				
				// target param is null, cannot compare, but take that into account.
				if(targetParamType == null) {
					++methodMatch.nullCount;
					continue;
				}
				
				// the class of the param and the target param don't match, this is not the method we are looking for.
				while(targetParamType != null) {
					// not an exact match.
					if(targetParamType == methodParamType) {
						break;
					}
					
					// try to find a primitave/object map to match.
					if(methodParamType.isPrimitive() && methodParamType == getPrimitiveEquivlent(targetParamType)) {
						++methodMatch.primitiveMapCount;
						break;
					}
					
					targetParamType = targetParamType.getSuperclass();
				}
				
				if(targetParamType == null) {
					possibleMatch = false;
					break;
				}
			} // for
			
			// couldn't find a match based on params, goto next method.
			if(!possibleMatch) {
				continue;
			}
			
			// found a possible match, add it to the list of possible matches.
			possibleMatches.add(methodMatch);
		} // for
		
		// get the number of matches.
		int possibleMatchesCount = possibleMatches.size();
		
		// no matches were found, return null;
		if(possibleMatchesCount == 0) {
			return null;
			
		// only 1 match was found, return that match.
		} else if(possibleMatchesCount == 1) {
			return possibleMatches.get(0).method;
		}
		
		// sort the possible matches, best match is on top.
		Collections.sort(possibleMatches, methodMatchComparer);
		
		// return the best match.
		return possibleMatches.get(0).method;
	} // method
	
	/**
	 * A class used to store possible method matches.
	 * @author vangorra
	 *
	 */
	private static class MethodMatch {
		public Method method;
		public int nullCount;
		public int primitiveMapCount;
		
		public MethodMatch(Method method) {
			this.method = method;
		}
		
		public MethodMatch(Method method, int nullCount, int primitiveMapCount) {
			this.method = method;
			this.nullCount = nullCount;
			this.primitiveMapCount = primitiveMapCount;
		}
	} // class
	
	/**
	 * Compares MethodMatch objects based on their nullCount.
	 * @author vangorra
	 *
	 */
	private static class MethodMatchComparer implements Comparator<MethodMatch> {
		@Override
		public int compare(MethodMatch lhs, MethodMatch rhs) {
			// null count is less accurate and should be treated as such.
			int lhsVal = (lhs.nullCount * 2) + lhs.primitiveMapCount;
			int rhsVal = (rhs.nullCount * 2) + rhs.primitiveMapCount;
			
			if(lhsVal < rhsVal) {
				return -1;
			}
			
			if(lhsVal > rhsVal) {
				return 1;
			}
			
			return 0;
		}
	} // class
} // class
