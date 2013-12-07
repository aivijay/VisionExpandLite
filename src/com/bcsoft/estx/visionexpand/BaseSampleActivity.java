package com.bcsoft.estx.visionexpand;

import java.util.Random;

import com.viewpagerindicator.PageIndicator;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseSampleActivity extends FragmentActivity {
    private static final Random RANDOM = new Random();

    TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

}
