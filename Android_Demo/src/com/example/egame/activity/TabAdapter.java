package com.example.egame.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.egame.constvar.ConstVar;

public class TabAdapter extends FragmentPagerAdapter {

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		TabFragment fragment = new TabFragment(arg0);
		return fragment;
	}

	@Override
	public int getCount() {
		return ConstVar.TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ConstVar.TITLES[position];
	}

}
