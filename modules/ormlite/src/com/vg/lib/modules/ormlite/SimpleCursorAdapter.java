package com.vg.lib.modules.ormlite;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.view.View;
import android.widget.ResourceCursorAdapter;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectIterator;

public class SimpleCursorAdapter<T> extends ResourceCursorAdapter {
	protected final PreparedQuery<T> preparedQuery;
	protected final ViewBinder<T> viewBinder;
	protected final Dao<T, ?> dao;

	/** This constructor gets the cursor from the prepared query
	 * @param activity
	 * @param layout
	 * @param dao
	 * @param preparedQuery
	 * @param viewBinder
	 * @throws SQLException
	 */
	public SimpleCursorAdapter(Activity activity, int layout, Dao<T, ?> dao, PreparedQuery<T> preparedQuery, ViewBinder<T> viewBinder) throws SQLException {
		this(activity, layout, dao, preparedQuery, ((AndroidDatabaseResults) ((SelectIterator<T, ?>)dao.iterator(preparedQuery)).getRawResults()).getRawCursor(), viewBinder);
	}

	/** This constructor allows you to create the cursor in another thread then pass it in
	 * @param activity
	 * @param layout
	 * @param dao
	 * @param preparedQuery
	 * @param cursor
	 * @param viewBinder
	 * @throws SQLException
	 */
	public SimpleCursorAdapter(Activity activity, int layout, Dao<T, ?> dao, PreparedQuery<T> preparedQuery, Cursor cursor, ViewBinder<T> viewBinder) throws SQLException {
		super(
				activity,
				layout,
				cursor
		);
		this.preparedQuery = preparedQuery;
		this.viewBinder = viewBinder;
		this.dao = dao;
		activity.startManagingCursor(getCursor());
	}

	@Override
	public T getItem(int position) {
		try {
			return cursorToObject((Cursor) super.getItem(position));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getItemId(int position) {
		// ORMLite object can have any kind of ID - not just int. So this method doesn't make sense.
		//throw new UnsupportedOperationException();
		return -1;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		try {
			//TODO instead of just passing null, it would be nice to pass the objectCache for the dao
			viewBinder.bindView(view, cursorToObject(cursor));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected T cursorToObject(Cursor cursor) throws SQLException {
		return preparedQuery.mapRow(new AndroidDatabaseResults(cursor, null));
	}
	
	public static interface ViewBinder<U> {
		void bindView(View view, U data);
	}
	/*
	private static class IgnoreIdColumnCursor extends CursorWrapper {
		public IgnoreIdColumnCursor(Cursor wrapped){
			super(wrapped);
		}

		@Override
		public int getColumnIndexOrThrow(String columnName)
				throws IllegalArgumentException {
			//here's the hack :-)
			if("_id".equals(columnName)){
				//return a dummy value - we know by knowing the implementations that this will be ignored
				return -1;
			}else{
				return super.getColumnIndexOrThrow(columnName);
			}
		}
		
	}*/
}