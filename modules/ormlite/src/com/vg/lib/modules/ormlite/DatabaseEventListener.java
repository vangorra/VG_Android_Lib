package com.vg.lib.modules.ormlite;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

public interface DatabaseEventListener {
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void onDatabaseCreated(SQLiteDatabase arg0, ConnectionSource arg1);
	
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void onDatabaseUpgraded(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3);
}
