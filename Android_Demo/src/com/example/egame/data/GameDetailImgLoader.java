package com.example.egame.data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GameDetailImgLoader {
	private ImageView mImageView;

	public GameDetailImgLoader(ImageView imageView) {
		this.mImageView = imageView;
	}

	/**
	 * 从网络中获取图片
	 *
	 * @param urlString
	 *            图片网址
	 * @return 获取到的图片
	 */
	public Bitmap getBitmapFromURL(String urlString) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			is = new BufferedInputStream(conn.getInputStream());

			bitmap = BitmapFactory.decodeStream(is);
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return bitmap;
	}

	/**
	 * 展示图片 AysncTask() 这种方法
	 */
	public void showImgByAysncTask(String url) {
		// 从缓存中取出对应的图片
		// 如果没有,就从网线上下载图片
//		new NewsAynsTaskImgView(url);
		NewsAynsTaskImgView task = new NewsAynsTaskImgView(url);
		task.execute(url);

	}

	/**
	 * 停止加载图片 ,滑动时
	 */
//	public void cancelAllTasks() {
//		if (mtask != null) {
//			for (NewsAynsTaskImgView task : mtask) {
//				task.cancel(false);
//			}
//		}
//	}

	private class NewsAynsTaskImgView extends AsyncTask<String, Void, Bitmap> {
		// private ImageView mImageView;
		public NewsAynsTaskImgView(String url){
			
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			Bitmap bitmap = getBitmapFromURL(url);
			/**
			 * 从网络获取图片
			 */
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			mImageView.setImageBitmap(bitmap);
		}
	}


}
