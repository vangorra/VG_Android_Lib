package com.vg.lib.os;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * A convenience class that makes building a bundle a little less cumbersome.
 * @author vangorra
 *
 */
public final class BundleBuilder {
	private Bundle bundle;
	
	/*
	 * init the bundle builder.
	 */
	private BundleBuilder() {
		bundle = new Bundle();
	} // method
	
	/**
	 * Create a new BundleBuilder.
	 * @return a new bundle builder.
	 */
	public static BundleBuilder create() {
		return new BundleBuilder();
	} // method
	
	/**
	 * Get the current bundle.
	 * @return The current bundle.
	 */
	public Bundle bundle() {
		return bundle;
	} // method
	
	public BundleBuilder putAll(Bundle map){
		bundle.putAll(map);
		return this;
	} // method

	public BundleBuilder putBoolean(String key, boolean value){
		bundle.putBoolean(key, value);
		return this;
	} // method

	public BundleBuilder putBooleanArray(String key, boolean[] value){
		bundle.putBooleanArray(key, value);
		return this;
	} // method

	public BundleBuilder putBundle(String key, Bundle value){
		bundle.putBundle(key, value);
		return this;
	} // method

	public BundleBuilder putByte(String key, byte value){
		bundle.putByte(key, value);
		return this;
	} // method

	public BundleBuilder putByteArray(String key, byte[] value){
		bundle.putByteArray(key, value);
		return this;
	} // method

	public BundleBuilder putChar(String key, char value){
		bundle.putChar(key, value);
		return this;
	} // method

	public BundleBuilder putCharArray(String key, char[] value){
		bundle.putCharArray(key, value);
		return this;
	} // method

	public BundleBuilder putCharSequence(String key, CharSequence value){
		bundle.putCharSequence(key, value);
		return this;
	} // method

	public BundleBuilder putCharSequenceArray(String key, CharSequence[] value){
		bundle.putCharSequenceArray(key, value);
		return this;
	} // method

	public BundleBuilder putCharSequenceArrayList(String key, ArrayList<CharSequence> value){
		bundle.putCharSequenceArrayList(key, value);
		return this;
	} // method

	public BundleBuilder putDouble(String key, double value){
		bundle.putDouble(key, value);
		return this;
	} // method

	public BundleBuilder putDoubleArray(String key, double[] value){
		bundle.putDoubleArray(key, value);
		return this;
	} // method

	public BundleBuilder putFloat(String key, float value){
		bundle.putFloat(key, value);
		return this;
	} // method

	public BundleBuilder putFloatArray(String key, float[] value){
		bundle.putFloatArray(key, value);
		return this;
	} // method

	public BundleBuilder putInt(String key, int value){
		bundle.putInt(key, value);
		return this;
	} // method

	public BundleBuilder putIntArray(String key, int[] value){
		bundle.putIntArray(key, value);
		return this;
	} // method

	public BundleBuilder putIntegerArrayList(String key, ArrayList<Integer> value){
		bundle.putIntegerArrayList(key, value);
		return this;
	} // method

	public BundleBuilder putLong(String key, long value){
		bundle.putLong(key, value);
		return this;
	} // method

	public BundleBuilder putLongArray(String key, long[] value){
		bundle.putLongArray(key, value);
		return this;
	} // method

	public BundleBuilder putParcelable(String key, Parcelable value){
		bundle.putParcelable(key, value);
		return this;
	} // method

	public BundleBuilder putParcelableArray(String key, Parcelable[] value){
		bundle.putParcelableArray(key, value);
		return this;
	} // method

	public BundleBuilder putParcelableArrayList(String key, ArrayList<? extends Parcelable> value){
		bundle.putParcelableArrayList(key, value);
		return this;
	} // method

	public BundleBuilder putSerializable(String key, Serializable value){
		bundle.putSerializable(key, value);
		return this;
	} // method

	public BundleBuilder putShort(String key, short value){
		bundle.putShort(key, value);
		return this;
	} // method

	public BundleBuilder putShortArray(String key, short[] value){
		bundle.putShortArray(key, value);
		return this;
	} // method

	public BundleBuilder putSparseParcelableArray(String key, SparseArray<? extends Parcelable> value){
		bundle.putSparseParcelableArray(key, value);
		return this;
	} // method

	public BundleBuilder putString(String key, String value){
		bundle.putString(key, value);
		return this;
	} // method

	public BundleBuilder putStringArray(String key, String[] value){
		bundle.putStringArray(key, value);
		return this;
	} // method

	public BundleBuilder putStringArrayList(String key, ArrayList<String> value){
		bundle.putStringArrayList(key, value);
		return this;
	} // method

	public BundleBuilder readFromParcel(Parcel parcel){
		bundle.readFromParcel(parcel);
		return this;
	} // method

	public BundleBuilder remove(String key){
		bundle.remove(key);
		return this;
	} // method

	public BundleBuilder setClassLoader(ClassLoader loader){
		bundle.setClassLoader(loader);
		return this;
	} // method
} // class