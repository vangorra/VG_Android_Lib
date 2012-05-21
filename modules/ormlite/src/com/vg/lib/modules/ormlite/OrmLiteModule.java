package com.vg.lib.modules.ormlite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.support.ConnectionSource;
import com.vg.lib.module.BaseModuleImpl;
import com.vg.lib.module.Module;
import com.vg.lib.module.ModuleRuntimeException;

public class OrmLiteModule extends BaseModuleImpl {
	public static final String DATABASE_NAME = "databaseName";
	public static final String DATABASE_VERSION = "databaseVersion";
	
	private volatile DatabaseOpenHelper helper;
	private volatile boolean created = false;
	private volatile boolean destroyed = false;
	private Context context;
	private int loadCount = 0;
	private Bundle args;
	
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
	public void load(Context context, Bundle args) {
		this.context = context;
		
		String databaseName = args.getString(DATABASE_NAME);
		int databaseVersion = args.getInt(DATABASE_VERSION);
		
		if(databaseName == null) {
			throw new ModuleRuntimeException("You must provide a database name.");
		}
		if(databaseVersion <= 0) {
			throw new ModuleRuntimeException("Invalid version number. Must be >= 0");
		}
		
		this.args = args;
		this.createHelper();
		//OpenHelperManager.setHelper(this.helper);
		//created = true;
	}
	
	private void createHelper() {
		if(this.helper == null) {
			this.helper = new DatabaseOpenHelper(
					this.context,
					this.args.getString(DATABASE_NAME),
					null,
					this.args.getInt(DATABASE_VERSION)
			);
		}
	}

	@Override
	public void unLoad() {

	}

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		if(loadCount == 0) {
			this.createHelper();
		}
		loadCount++;
		/*if (helper == null) {
			helper = getHelperInternal(this.context);
			created = true;
		}*/
	}

	@Override
	public void onActivityDestroy(Activity activity) {
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
