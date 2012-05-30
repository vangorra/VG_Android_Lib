package com.vg.lib.modules.ormlite;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseOpenHelper extends OrmLiteSqliteOpenHelper {
	/**
	 * The name of the meta-data tag that should be in the application tag of the manifest file.
	 */
	public static final String CONFIG_DATABASE_NAME = "orm_database_name";
	
	/**
	 * The name of the meta-data tag that should be in the application tag of the manifest file.
	 */
	public static final String CONFIG_DATABASE_VERSION = "orm_database_version";
	
	private DatabaseEventListener eventListener;

	private AndroidDatabaseConnection databaseConnection;
	
	
	public DatabaseOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	/**
	 * Same as the other constructor with the addition of a file-id of the table config-file. See
	 * {@link OrmLiteConfigUtil} for details.
	 * 
	 * @param configFileId
	 *            file-id which probably should be a R.raw.ormlite_config.txt or some static value.
	 */
	public DatabaseOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion,
			int configFileId) {
		super(context, databaseName, factory, databaseVersion, configFileId);
	}

	/**
	 * Same as the other constructor with the addition of a config-file. See {@link OrmLiteConfigUtil} for details.
	 * 
	 * @param configFile
	 *            Configuration file to be loaded.
	 */
	public DatabaseOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion,
			File configFile) {
		super(context, databaseName, factory, databaseVersion, configFile);
	}

	/**
	 * Same as the other constructor with the addition of a input stream to the table config-file. See
	 * {@link OrmLiteConfigUtil} for details.
	 * 
	 * @param stream
	 *            Stream opened to the configuration file to be loaded.
	 */
	public DatabaseOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion,
			InputStream stream) {
		super(context, databaseName, factory, databaseVersion, stream);
	}
	
	public AndroidDatabaseConnection getConnection() {
		if(this.databaseConnection == null) {
			this.databaseConnection = new AndroidDatabaseConnection(
					this.getReadableDatabase(),
					false
			);
		}
		return this.databaseConnection;
	}
	
	public void setDatabaseEventListener(DatabaseEventListener del) {
		this.eventListener = del;
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		if(this.eventListener != null) {
			this.eventListener.onDatabaseCreated(arg0, arg1);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		if(this.eventListener != null) {
			this.eventListener.onDatabaseUpgraded(arg0, arg1, arg2, arg3);
		}
	}
}
