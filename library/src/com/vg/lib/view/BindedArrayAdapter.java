package com.vg.lib.view;

import java.util.List;

import com.vg.lib.view.BindedArrayAdapter.ViewBinder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BindedArrayAdapter<T> extends ArrayAdapter<T> {
	private int layoutId;
	private ViewBinder<T> viewBinder;

	public BindedArrayAdapter(Context context, int layoutId,
			List<T> objects) {
		super(context, layoutId, objects);
		this.layoutId = layoutId;
	}

	public void setViewBinder(ViewBinder<T> vb) {
		this.viewBinder = vb;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			v = View.inflate(this.getContext(), this.layoutId, null);
		}
		
		T object = this.getItem(position);
		if(this.viewBinder != null) {
			this.viewBinder.bindView(
					this.getContext(), 
					object, 
					v, 
					null
			);
		}
		
		return v;
	}
	
	public static interface ViewBinder<T> {
		public void bindView(Context c, T object, View convertView, ViewGroup parent);
	}
}