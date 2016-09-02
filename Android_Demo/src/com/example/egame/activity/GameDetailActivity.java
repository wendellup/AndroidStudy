package com.example.egame.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.egame.constvar.ConstVar;
import com.example.egame.data.GameDetailImgLoader;
import com.example.egame.entity.GameDetailBean;

public class GameDetailActivity extends Activity {

	private TextView gameName;
	private TextView gameIntroduction;
	private ImageView gameIcon;
	private GameDetailImgLoader mImgLoader;
//	private static final String Path="http://cdn.4g.play.cn/f/pkg/gm/000/001/945/d329bf20h1dafcd7/5016561_2.apk";
//	private static final String Path = "http://open.4g.play.cn/api/v2/mobile/down.json?client_id=8888015&terminal_id=1&game_id=5016561";
	private String path = null;
	
	private ProgressBar progressBar;
	private Button button;
	private Button installBtn;
	private TextView textView;
	private long FileLength;
	private long DownedFileLength = 0;
	private InputStream inputStream;
	private URLConnection connection;
	private String fileName;
//	private OutputStream outputStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);
		gameName = (TextView) findViewById(R.id.id_game_name);
		gameIntroduction = (TextView) findViewById(R.id.id_game_intro);
		gameIcon = (ImageView) findViewById(R.id.id_icon);
		mImgLoader = new GameDetailImgLoader(gameIcon);
		progressBar = (ProgressBar) findViewById(R.id.id_game_down_progressBar);
		button = (Button) findViewById(R.id.id_game_down_btn);
		installBtn = (Button) findViewById(R.id.id_game_install_btn);
		textView = (TextView) findViewById(R.id.id_game_down_textView);
		
		button.setOnClickListener(new ButtonListener());
		installBtn.setOnClickListener(new IntallButtonListener());

		// Intent intent = this.getIntent();
		Bundle bl = getIntent().getExtras();
		int gameId = bl.getInt("gameId");
		// String gameId = intent.getStringExtra("gameId");

		new GameDetailAsyncTask().execute(ConstVar.URL_GAME_DETAIL + gameId);
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			DownedFileLength = 0;
			// TODO Auto-generated method stub
			Thread thread = new Thread() {
				public void run() {
					try {
						DownFile(path);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
			thread.start();
		}
	}
	
	class IntallButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			File file = null;
			String savePathString = Environment.getExternalStorageDirectory()
					+ "/DownFile/" + fileName;
			file = new File(savePathString);
			openFile(file);
		}
	}
	

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					progressBar.setMax(100);
					Log.i("文件长度----------->", progressBar.getMax() + "");
					break;
				case 1:
					progressBar.setProgress((int)(DownedFileLength*1.0/FileLength*100));
					long x =(long) (DownedFileLength*1.0/FileLength*100);
//					Log.i("x----------->", "DownedFileLength:"
//							+ DownedFileLength * 100 + ", FileLength:"
//							+ FileLength+", x:" + x);
					textView.setText(x + "%");
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "下载完成",
							Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			}
		}

	};

	private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
        startActivity(intent);
	}
	
	private void DownFile(String urlString) {

		/*
		 * 连接到服务器
		 */
		FileOutputStream fos = null;
		
		try {
			URL url = new URL(urlString);
			connection = url.openConnection();
			if (connection.getReadTimeout() == 5) {
				Log.i("---------->", "当前网络有问题");
				// return;
			}
			inputStream = connection.getInputStream();

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
		 */
		URL url = connection.getURL();
		Log.e("URL", url.toString());
		String fileAbUrl = url.toString();
		 // 从路径中获取
        if (fileAbUrl != null && !"".equals(fileAbUrl)) {
        	fileName = fileAbUrl.substring(fileAbUrl.lastIndexOf("/") + 1);
        }
		
        if(fileName==null){
        	Log.e("fileName", fileName);
        }
        
		String savePAth = Environment.getExternalStorageDirectory()
				+ "/DownFile";
		File file1 = new File(savePAth);
		if (!file1.exists()) {
			file1.mkdir();
		}
		String savePathString = Environment.getExternalStorageDirectory()
				+ "/DownFile/" + fileName;
		File file = new File(savePathString);
		
		
		if(file.exists()){
			file.delete();
		}
//		try {
//			file.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		/*
		 * 向SD卡中写入文件,用Handle传递线程
		 */
		Message message = new Message();
		try {
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024 * 10];
			FileLength = connection.getContentLength();
			
			message.what = 0;
			handler.sendMessage(message);
//			while (DownedFileLength < FileLength) {
//				DownedFileLength += inputStream.read(buffer);
//				outputStream.write(buffer);
//				Log.i("-------->", DownedFileLength + "");
//				Message message1 = new Message();
//				message1.what = 1;
//				handler.sendMessage(message1);
//			}
			int n = 0;
			while ((n = inputStream.read(buffer))!=-1) {
				DownedFileLength += n;
				fos.write(buffer, 0, n);
				Log.i("-------->", DownedFileLength + "");
				Message message1 = new Message();
				message1.what = 1;
				handler.sendMessage(message1);
			}
			Message message2 = new Message();
			message2.what = 2;
			handler.sendMessage(message2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 实现网络的异步访问
	 */
	class GameDetailAsyncTask extends AsyncTask<String, Void, GameDetailBean> {

		@Override
		protected void onPostExecute(GameDetailBean result) {
			super.onPostExecute(result);
			if (result != null) {
				gameName.setText(result.getGameName());
				gameIntroduction.setText(result.getGameIntroduction());
				mImgLoader.showImgByAysncTask(result.getGameIcon());
				button.setVisibility(android.view.View.VISIBLE);
				installBtn.setVisibility(android.view.View.VISIBLE);
			}

		}

		@Override
		protected GameDetailBean doInBackground(String... params) {
			GameDetailBean gameDetailBean =  getJsonData(params[0]);
			if(fileName==null){
				try {
					URL url = new URL(path);
					connection = url.openConnection();
					if (connection.getReadTimeout() == 5) {
						Log.i("---------->", "当前网络有问题");
					}
					inputStream = connection.getInputStream();
					inputStream.close();
					URL abUrl = connection.getURL();
					Log.e("URL onClick", abUrl.toString());
					String fileAbUrl = abUrl.toString();
					 // 从路径中获取
			        if (fileAbUrl != null && !"".equals(fileAbUrl)) {
			        	fileName = fileAbUrl.substring(fileAbUrl.lastIndexOf("/") + 1);
			        }
					
			        if(fileName==null){
			        	Log.e("fileName", fileName);
			        }

				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return gameDetailBean;
			
		}

		/**
		 * 从 URL 中获取数据
		 * 
		 * @param url
		 * @return
		 */
		public GameDetailBean getJsonData(String url) {
			GameDetailBean gameDetailBean = null;
			try {
				String jsonString = readStream(new URL(url).openStream());
				JSONObject jsonObject;
				jsonObject = new JSONObject(jsonString);
				if (jsonObject != null) {
					JSONObject extObj = jsonObject.getJSONObject("ext");
					if (extObj != null) {
						JSONObject gameDetailObj = extObj
								.getJSONObject("game_detail");
						if (gameDetailObj != null) {
							gameDetailBean = new GameDetailBean();
							gameDetailBean.setGameIcon(gameDetailObj
									.getString("game_icon"));
							gameDetailBean.setGameName(gameDetailObj
									.getString("game_name"));
							gameDetailBean.setGameId(Integer
									.parseInt(gameDetailObj
											.getString("game_id")));
							gameDetailBean.setGameIntroduction(gameDetailObj
									.getString("game_introduction"));
							gameDetailBean.setGameDownloadUrl(gameDetailObj
									.getString("game_download_url"));
							path = gameDetailBean.getGameDownloadUrl();
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Log.i("test", newsBeamsList.toString());
			return gameDetailBean;
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

	}
}
