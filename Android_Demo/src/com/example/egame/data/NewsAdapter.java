package com.example.egame.data;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.egame.activity.GameDetailActivity;
import com.example.egame.entity.GameBean;

public class NewsAdapter extends BaseAdapter implements
		AbsListView.OnScrollListener, OnItemClickListener {
	private List<GameBean> mList;
	private LayoutInflater mInflater;

	private ImgLoader mImgLoader;

	private int mStart;
	private int mEnd;
	public static String[] URLS;

	private boolean mFirstIn;
	
	private Context context;

	public NewsAdapter(Context context, List<GameBean> data, ListView listView) {
		mList = data;
		mInflater = LayoutInflater.from(context);
		mImgLoader = new ImgLoader(listView);
		URLS = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			URLS[i] = data.get(i).gameIcon;
		}
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		mFirstIn = true;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_layout, null);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.id_icon);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.id_title);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.id_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.icon.setImageResource(R.drawable.ic_launcher);
		String url = mList.get(position).gameIcon;

		/**
		 * 第1 种方法: 绑定 listView ,避免 图片加载错乱
		 */
		viewHolder.icon.setTag(url);

		// 多线程方式
//		mImgLoader.showImgByThread(viewHolder.icon, url);

		// AysncTask 方式加载图片
		mImgLoader.showImgByAysncTask(viewHolder.icon, url);

		viewHolder.title.setText(mList.get(position).gameName);
		viewHolder.content.setText(mList.get(position).gameRecommendWord);
		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView content;
		public ImageView icon;
	}

	/**
	 * 下面是滑动状态监听
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 处于停止状态
		if (scrollState == SCROLL_STATE_IDLE) {
			// 加载可见项
			mImgLoader.loadImages(mStart, mEnd);
		} else {
			// 停止加载
			mImgLoader.cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mStart = firstVisibleItem;
		mEnd = firstVisibleItem + visibleItemCount;

		if(mEnd>=URLS.length){
			mEnd = URLS.length;
		}
		
		// 首次加载时 使用的,
		if (mFirstIn == true && visibleItemCount > 0) {
			mImgLoader.loadImages(mStart, mEnd);
			mFirstIn = false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		Toast.makeText(context, "你点了第"+position+"项", Toast.LENGTH_LONG).show();
//		System.out.println("position--->"+position);
//		Log.i("test", mList.get(position).gameId+"");
		Intent intent = new Intent(context, GameDetailActivity.class);
		Bundle bl=new Bundle();
		bl.putInt("gameId", mList.get(position).gameId);
//		intent.putExtra("gameId", mList.get(position).gameId);
		intent.putExtras(bl);
		context.startActivity(intent);
		
	}

	/**
     * 添加列表项
     * @param item
     */
    public void addItem(List<GameBean> list) {
    	mList.addAll(list);
    	URLS = new String[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			URLS[i] = mList.get(i).gameIcon;
		}
    }
}
