package com.imooc._27_FrameLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imooc.toast.R;

public class FrameLayoutTest extends Activity {

	private ImageView one = null;
	private ImageView two = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_frame);
		one = (ImageView) findViewById(R.id.imageViewOne);
		two = (ImageView) findViewById(R.id.imageViewTwo);
		one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				two.setVisibility(View.VISIBLE);
				one.setVisibility(View.GONE);
			}
		});

		two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				one.setVisibility(View.VISIBLE);
				two.setVisibility(View.GONE);
			}
		});
	}
}
