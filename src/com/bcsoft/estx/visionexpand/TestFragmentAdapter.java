package com.bcsoft.estx.visionexpand;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	protected static String[] CONTENT = null;
	private int mCount;

	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);

	}
	
	public void setContent (Activity activity) {
		CONTENT = new String[] { activity.getString(R.string.help_page1), activity.getString(R.string.help_page2), activity.getString(R.string.help_faq),};
		mCount = CONTENT.length;
	}

	@Override
	public Fragment getItem(int position) {
		return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TestFragmentAdapter.CONTENT[position % CONTENT.length];
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

}