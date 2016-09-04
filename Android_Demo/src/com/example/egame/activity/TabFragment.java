package com.example.egame.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.demo.R;
import com.example.egame.activity.RecommendMainActivity.LoadMoreAsyncTask;
import com.example.egame.activity.RecommendMainActivity.NewAsyncTask;
import com.example.egame.constvar.ConstVar;
import com.example.egame.data.NewsAdapter;
import com.example.egame.entity.GameBean;

@SuppressLint("ValidFragment")
public class TabFragment extends Fragment
{
	private int pos;
	
	private ListView mListView;
	private View loadMoreView;
	private Button loadMoreButton;
	NewsAdapter adapter = null;
	private int currentPage = 0;

	@SuppressLint("ValidFragment")
	public TabFragment(int pos)
	{
		this.pos = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
//		View view = inflater.inflate(R.layout.egame_frag, container, false);
//		TextView tv = (TextView) view.findViewById(R.id.id_tv);
//		tv.setText(TabAdapter.TITLES[pos]);
//		return view;
		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main_data);
		View view = inflater.inflate(R.layout.activity_main_data, container, false);
		// Log.i("test", "begin");
		mListView = (ListView) view.findViewById(R.id.lv_main);
		loadMoreView = inflater.inflate(R.layout.load_more, null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);
		mListView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setText("loading..."); // 设置按钮文字loading
				currentPage++;
				new LoadMoreAsyncTask().execute(ConstVar.TITLE_URLS[pos]
						+ currentPage);
			}
		});
		new NewAsyncTask().execute(ConstVar.TITLE_URLS[pos]);
		return view;
	}
	
//	public void loadMore(View view) {
//		loadMoreButton.setText("loading..."); // 设置按钮文字loading
//		currentPage++;
////		new Thread(runnable).start();
//		
//		new LoadMoreAsyncTask().execute(ConstVar.TITLE_URLS[pos]
//				+ currentPage);
//	}
	
	/**
	 * 实现网络的异步访问
	 */
	class NewAsyncTask extends AsyncTask<String, Void, List<GameBean>> {
		@Override
		protected void onPostExecute(List<GameBean> newsBeams) {
			super.onPostExecute(newsBeams);
			adapter = new NewsAdapter(getActivity(), newsBeams, mListView);
			mListView.setAdapter(adapter);
		}

		@Override
		protected List<GameBean> doInBackground(String... params) {
			// Log.i("test", params[0]);
			return getJsonData(params[0]);
		}
	}
	
	/**
	 * 从 inpustStream 获取的信息
	 * 
	 * @param is
	 * @return
	 */
	private String readStream(InputStream is) {
		InputStreamReader isr;
		String result = "";
		try {
			isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 从 URL 中获取数据
	 * 
	 * @param url
	 * @return
	 */
	public List<GameBean> getJsonData(String url) {
		List<GameBean> newsBeamsList = new ArrayList<GameBean>();
		try {
			String jsonString = readStream(new URL(url).openStream());

			JSONObject jsonObject;
			GameBean newsBeam;

			jsonObject = new JSONObject(jsonString);
			if (jsonObject != null) {
				JSONObject extObj = jsonObject.getJSONObject("ext");
				if (extObj != null) {
					JSONObject mainObj = extObj.getJSONObject("main");
					if (mainObj != null) {
						JSONObject contentObj = mainObj
								.getJSONObject("content");
						if (contentObj != null) {
							JSONArray jsonArray = contentObj
									.getJSONArray("game_list");
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject = jsonArray.getJSONObject(i);
								newsBeam = new GameBean();
								newsBeam.gameName = jsonObject
										.getString("game_name");
								newsBeam.gameRecommendWord = jsonObject
										.getString("game_recommend_word");
								newsBeam.gameIcon = jsonObject
										.getString("game_icon");
								newsBeam.gameId = Integer
										.parseInt(jsonObject
												.getString("game_id"));
								newsBeamsList.add(newsBeam);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Log.i("test", newsBeamsList.toString());
		return newsBeamsList;
	}
	
	class LoadMoreAsyncTask extends AsyncTask<String, Void, List<GameBean>> {

		@Override
		protected void onPostExecute(List<GameBean> result) {
			super.onPostExecute(result);
			adapter.addItem(result);
			adapter.notifyDataSetChanged();
			loadMoreButton.setText("load more"); // 恢复按钮文字
		}

		@Override
		protected List<GameBean> doInBackground(String... params) {
			return getJsonData(params[0]);
		}

	}
}
