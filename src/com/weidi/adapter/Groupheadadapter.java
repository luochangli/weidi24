package com.weidi.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.weidi.R;
import com.weidi.bean.Groupmember;

public class Groupheadadapter extends BaseAdapter {
	Context context;
	ArrayList<Groupmember> datasourcelist;
	public Groupheadadapter(Context context,ArrayList<Groupmember> datasourcelist){
		this.context=context;
		this.datasourcelist=datasourcelist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datasourcelist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datasourcelist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(view==null){
			viewHolder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(
					R.layout.grouphead, null);
			
			viewHolder.mImageView = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.mImageView.setImageResource(R.drawable.default_useravatar);
	
		return view;
		
	}
	
	class ViewHolder {
		
		ImageView mImageView;
		
	}

}
