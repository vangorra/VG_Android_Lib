package com.vg.lib.lang.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * Simple reflection tools for dealing with methods.
 * @author vangorra
 *
 */
public class MethodUtils {
	private static final String TAG = MethodUtils.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	private static final MethodMatchComparer methodMatchComparer = new MethodMatchComparer();
	
	/**
	 * Provides a cache for findClosestMethod.
	 */
	private static HashMap<String,Method> findClosestMethodCache = new HashMap<String,Method>();

	/**
	 * Given an object class, return its equivalent primitive class.
	 * @param cls The object class such as Integer.class, Long.class, Double.class, etc..
	 * @return A primitive class int.class, long.class, double.class, etc...
	 */
	public static Class<?> getPrimitiveEquivlent(Class<?> cls) {
		// go from object to primitive.
		if(cls == Byte.class) {
			return byte.class;
		} else if(cls == Short.class) {
			return short.class;
		} else if(cls == Integer.class) {
			return int.class;
		} else if(cls == Long.class) {
			return long.class;
		} else if(cls == Double.class) {
			return double.class;
		} else if(cls == Boolean.class) {
			return boolean.class;
		} else if(cls == Character.class) {
			return char.class;
		} else if(cls == Float.class) {
			return float.class;
		}

		// go from primitive to object.
		if(cls == byte.class) {
			return Byte.class;
		} else if(cls == short.class) {
			return Short.class;
		} else if(cls == int.class) {
			return Integer.class;
		} else if(cls == long.class) {
			return Long.class;
		} else if(cls == double.class) {
			return Double.class;
		} else if(cls == boolean.class) {
			return Boolean.class;
		} else if(cls == char.class) {
			return Character.class;
		} else if(cls == float.class) {
			return Float.class;
		}
		
		return null;
	} // method
	
	/**
	 * Find all methods of a class matching a specific regex pattern.
	 * @param cls THe class to scan.
	 * @param regexPattern The pattern to apply to each method in the class.
	 * @return A list of methods that whose name match the provided regex.
	 */
	public static List<Method> findMethodsMatching(Class<?> cls, Pattern regexPattern) {
		// check for null
		if(cls == null) {
			throw new IllegalArgumentException("cls cannot be null.");
		}
		
		if(regexPattern == null) {
			throw new IllegalArgumentException("regexPattern cannot be null.");
		}
		
		// init
		ArrayList<Method> methods = new ArrayList<Method>();
		
		// iterate all the methods in the class.
		for(Method method: cls.getMethods()) {
			// if the found method matches the pattern, add it to the return list.
			if(regexPattern.matcher(method.getName()).matches()) {
				methods.add(method);
			}
		}
		
		return methods;
	} // method
	
	/**
	 * From the given set of methods, based on parameter count and type, find the closest match and return it.
	 * This does take into account that primitives can cast to Objects.
	 * @param methods The methods to search against.
	 * @param targetParamTypes The parameter types to look for.
	 * @return The found method or null if nothing was found suitable.
	 */
	public static Method findClosestMethod(List<Method> methods, Class<?>[] targetParamTypes) {
		// check for null
		if(methods == null) {
			throw new IllegalArgumentException("methods cannot be null.");
		}
		
		if(targetParamTypes == null) {
			throw new IllegalArgumentException("targetParamTypes cannot be null.");
		}
		
		/*
		 * Find the closest method match.
		 */
		ArrayList<MethodMatch> possibleMatches = new ArrayList<MethodMatch>();
		
		// iterate through the class methods.
		for(Method method: methods) {
			// get the parameter types.
			Class<?>[] methodParameterTypes = method.getParameterTypes();
			
			// method and target method don't have the same number of parameters.
			if(methodParameterTypes.length != targetParamTypes.length) {
				continue;
			}

			//if(DEBUG) Log.v(TAG, "Target found:"+ classType.getSimpleName() +"."+ method.getName());			
			
			// check the type of the method parameters.
			boolean possibleMatch = true;
			MethodMatch methodMatch = new MethodMatch(method);
			for(int i = 0; i < methodParameterTypes.length; ++i) {
				Class<?> methodParamType = methodParameterTypes[i];
				Class<?> targetParamType = targetParamTypes[i];
				
				// target param is null, cannot compare, but take that into account.
				if(targetParamType == null) {
					++methodMatch.nullCount;
					if(DEBUG) Log.v(TAG, "\tparam: null");
					continue;
				}
				
				if(DEBUG) Log.v(TAG, "\tparam: "+ targetParamType);
				
				// the class of the param and the target param don't match, this is not the method we are looking for.
				while(targetParamType != null) {
					if(DEBUG) Log.v(TAG, "\t\t "+ targetParamType.getSimpleName() +" == "+ methodParamType.getSimpleName());
					
					// not an exact match.
					if(targetParamType == methodParamType) {
						break;
					}
					
					// try to find a primitive/object map to match.
					if(methodParamType.isPrimitive() && methodParamType == getPrimitiveEquivlent(targetParamType)) {
						++methodMatch.primitiveMapCount;
						break;
					}
					
					targetParamType = targetParamType.getSuperclass();
				}
				
				if(targetParamType == null) {
					possibleMatch = false;
					if(DEBUG) Log.v(TAG, "\t\tFailed to find a param match.");
					break;
				}
				
				if(DEBUG) Log.v(TAG, "\t\tFound a param match.");
			} // for
			
			// couldn't find a match based on params, goto next method.
			if(!possibleMatch) {
				if(DEBUG) Log.v(TAG, "\tNot a possible match");
				continue;
			}
			
			if(DEBUG) Log.v(TAG, "\tFound a possible match");
			
			// found a possible match, add it to the list of possible matches.
			possibleMatches.add(methodMatch);
		} // for
		
		// get the number of matches.
		int possibleMatchesCount = possibleMatches.size();
		
		// no matches were found, return null;
		if(possibleMatchesCount == 0) {
			if(DEBUG) Log.v(TAG, "\tNo method matches found");
			return null;
			
		// only 1 match was found, return that match.
		} else if(possibleMatchesCount == 1) {
			if(DEBUG) Log.v(TAG, "\t1 method matches found");
			return possibleMatches.get(0).method;
		}
		
		// sort the possible matches, best match is on top.
		Collections.sort(possibleMatches, methodMatchComparer);
		
		if(DEBUG) Log.v(TAG, "\t> 1 method matches found, returning most likely.");
		
		// return the best match.
		return possibleMatches.get(0).method;
	} // method
	
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
	 * @return The best matching method or null if a method could not be found.d
	 * @deprecated
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
		StringBuffer sb = new StringBuffer();
		sb.append(targetClass.getName());
		sb.append(".");
		sb.append(targetMethodName);
		sb.append("(");
		for(Class<?> cls : targetParamTypes) {
			sb.append(cls == null? "null": cls.getName());
			sb.append(",");
		}
		sb.insert(sb.length()-1, ")");
		
		String cacheKey = sb.toString();
		
		// return the cached method.
		if(findClosestMethodCache.containsKey(cacheKey)) {
			if(DEBUG)Log.v(TAG, "From cache: "+ cacheKey);
			return findClosestMethodCache.get(cacheKey);
		}
		
		if(DEBUG)Log.v(TAG, "Not from cache: "+ cacheKey);
		
		// get a method, cache it, and return it.
		Method method = _findClosestMethod(targetClass, targetMethodName, targetParamTypes);
		findClosestMethodCache.put(
				cacheKey, 
				method
		);
		return method;
	} // method

	/**
	 * Worker method for findClosestMethod.
	 * @param classType
	 * @param targetMethodName
	 * @param targetParamTypes
	 * @return
	 * @deprecated
	 */
	private static Method _findClosestMethod(Class<?> classType, String targetMethodName, Class<?>[] targetParamTypes) {
		if(DEBUG) Log.v(TAG, MethodUtils.class.getSimpleName() +"._findClosestMatch("+ classType.getSimpleName() +", \""+ targetMethodName +"\", "+ targetParamTypes.toString() +")");
		
		// try to get the method directly. 
		try {
			Method m = classType.getMethod(targetMethodName, targetParamTypes);
			if(DEBUG) Log.v(TAG, "\tMethod found directly");
			return m;
		
		// one of the params is probably null. 
		} catch (Exception e) {
			// ignore
		}
		
		ArrayList<Method> methods = new ArrayList<Method>();
		for(Method method: classType.getMethods()) {
			// if the method name is not the same as our target method name, goto next loop item.
			if(!method.getName().equals(targetMethodName)) {
				continue;
			}
			
			methods.add(method);
		}
		
		return findClosestMethod(methods, targetParamTypes);
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
		/*
		public MethodMatch(Method method, int nullCount, int primitiveMapCount) {
			this.method = method;
			this.nullCount = nullCount;
			this.primitiveMapCount = primitiveMapCount;
		}
		*/
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
