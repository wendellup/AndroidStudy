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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.demo.R;
import com.example.egame.constvar.ConstVar;
import com.example.egame.data.NewsAdapter;
import com.example.egame.entity.GameBean;

public class MainActivity extends Activity {

	private ListView mListView;

	private View loadMoreView;
	private Button loadMoreButton;
	NewsAdapter adapter = null;
	private int currentPage = 0;
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        adapter.notifyDataSetChanged();
			loadMoreButton.setText("load more"); // 恢复按钮文字
	    }
	};

	public static void main(String[] args) {
		System.out.println("test...");
		// MainActivity.NewAsyncTask task = new MainActivity().new
		// NewAsyncTask();
		// List<GameBean> list = task.getJsonData(ConstVar.URL_RECOMMEND);
		// System.out.println(list);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_data);
		// Log.i("test", "begin");
		mListView = (ListView) findViewById(R.id.lv_main);
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);
		mListView.addFooterView(loadMoreView); // 设置列表底部视图
		new NewAsyncTask().execute(ConstVar.URL_RECOMMEND);
	}

	public void loadMore(View view) {
		loadMoreButton.setText("loading..."); // 设置按钮文字loading
		currentPage++;
//		new LoadMoreAsyncTask().execute(ConstVar.URL_RECOMMEND
//				+ currentPage);
		new Thread(runnable).start();
	}
	
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	    	List<GameBean> list = getJsonData(ConstVar.URL_RECOMMEND
					+ currentPage);
	    	adapter.addItem(list);
	        Message msg = new Message();
//	        Bundle data = new Bundle();
//	        data.putString("value","请求结果");
//	        msg.setData(data);
	        handler.sendMessage(msg);
	    }
	};
	
	
	/**
	 * 实现网络的异步访问
	 */
	class NewAsyncTask extends AsyncTask<String, Void, List<GameBean>> {
		@Override
		protected void onPostExecute(List<GameBean> newsBeams) {
			super.onPostExecute(newsBeams);
			adapter = new NewsAdapter(MainActivity.this, newsBeams, mListView);
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
			adapter.addItem(getJsonData(ConstVar.URL_RECOMMEND
					+ currentPage));
			adapter.notifyDataSetChanged();
			loadMoreButton.setText("load more"); // 恢复按钮文字
		}

		@Override
		protected List<GameBean> doInBackground(String... params) {
			return getJsonData(params[0]);
		}

	}

}
