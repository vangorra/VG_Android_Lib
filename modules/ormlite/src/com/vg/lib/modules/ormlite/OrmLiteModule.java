package com.vg.lib.modules.ormlite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.vg.lib.module.Module;

public class OrmLiteModule implements Module {
	private static final String TAG = OrmLiteModule.class.getSimpleName();
	private static final int DEBUG = 0;
	public static final String DATABASE_NAME = "databaseName";
	public static final String DATABASE_VERSION = "databaseVersion";
	
	private volatile DatabaseOpenHelper helper;
	private int loadCount = 0;
	private String databaseName;
	private int databaseVersion;
	
	/**
	 * Get a helper for this action.
	 */
	public DatabaseOpenHelper getHelper() {
		/*if (helper == null) {
			if (!created) {
				throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
			} else if (destroyed) {
				throw new IllegalStateException(
						"A call to onDestroy has already been made and the helper cannot be used after that point");
			} else {
				throw new IllegalStateException("Helper is null for some unknown reason");
			}
		} else {
			return helper;
		}*/
		if(DEBUG > 0) Log.d(TAG, "getHelper: "+ this.helper);
		return this.helper;
	}

	/**
	 * Get a connection source for this action.
	 */
	public ConnectionSource getConnectionSource() {
		return getHelper().getConnectionSource();
	}
	
	/**
	 * This is called internally by the class to populate the helper object instance. This should not be called directly
	 * by client code unless you know what you are doing. Use {@link #getHelper()} to get a helper instance. If you are
	 * managing your own helper creation, override this method to supply this activity with a helper instance.
	 * 
	 * <p>
	 * <b> NOTE: </b> If you override this method, you most likely will need to override the
	 * {@link #releaseHelper(OrmLiteSqliteOpenHelper)} method as well.
	 * </p>
	 */
	/*protected DatabaseOpenHelper getHelperInternal(Context context) {
		@SuppressWarnings({ "unchecked", "deprecation" })
		DatabaseOpenHelper newHelper = (DatabaseOpenHelper) OpenHelperManager.getHelper(context, DatabaseOpenHelper.class);
		return newHelper;
	}*/

	/**
	 * Release the helper instance created in {@link #getHelperInternal(Context)}. You most likely will not need to call
	 * this directly since {@link #onDestroy()} does it for you.
	 * 
	 * <p>
	 * <b> NOTE: </b> If you override this method, you most likely will need to override the
	 * {@link #getHelperInternal(Context)} method as well.
	 * </p>
	 */
	/*protected void releaseHelper(DatabaseOpenHelper helper) {
		OpenHelperManager.releaseHelper();
		this.helper = null;
	}*/
	
	@Override
	public void load(Bundle args) {
		if(DEBUG > 0) Log.d(TAG, "load: "+ args);
		/*// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}*/

		// check for null
		if(args == null) {
			throw new IllegalArgumentException("args cannot be null");
		}
		
		//this.context = context;
		
		/*
		 * Set the database name.
		 */
		// get the variable.
		databaseName = args.getString(DATABASE_NAME);
		
		// check for null.
		if(databaseName == null) {
			throw new IllegalArgumentException("DATABASE_NAME is required.");
		}
		
		// check for empty string.
		if(databaseName.length() <= 0) {
			throw new IllegalArgumentException("DATABASE_NAME cannot be an empty string.");
		}
		
		/**
		 * Set the database version.
		 */
		databaseVersion = args.getInt(DATABASE_VERSION);
		if(databaseVersion <= 0) {
			throw new IllegalArgumentException("Invalid version number. Must be >= 0");
		}
		
		//this.args = args;
		//this.createHelper();
		//OpenHelperManager.setHelper(this.helper);
		//created = true;
	}
	
	private void createHelper(Context context) {
		if(this.helper == null) {
			if(DEBUG > 0) Log.d(TAG, "createHelper: "+ context);
			this.helper = new DatabaseOpenHelper(
					context.getApplicationContext(),
					databaseName,
					null,
					databaseVersion
			);
		}
	}

	@Override
	public void unLoad() {

	}

	public void onInvokeActivityOnCreate(Activity activity, Bundle savedInstanceState) {
		if(DEBUG > 0) Log.d(TAG, "onInvokeActivityOnCreate: "+ activity);
		
		this.createHelper(activity);
		loadCount++;
		
		/*if (helper == null) {
			helper = getHelperInternal(this.context);
			created = true;
		}*/
	}

	public void onInvokeActivityOnDestroy(Activity activity) {
		loadCount--;
		
		if(loadCount <= 0) {
			if(helper != null) {
				helper.close();
				helper = null;
			}
			loadCount = 0;
		}
//		releaseHelper(helper);
//		destroyed = true;
	}
}
