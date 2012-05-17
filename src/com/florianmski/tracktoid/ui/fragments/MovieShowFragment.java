package com.florianmski.tracktoid.ui.fragments;

import com.florianmski.tracktoid.R;
import com.florianmski.tracktoid.ui.fragments.pagers.PagerLibraryFragment;
import com.florianmski.tracktoid.ui.fragments.pagers.PagerTabsFragment;
import com.florianmski.tracktoid.ui.fragments.pagers.items.PI_LibaryMovieFragment;
import com.florianmski.tracktoid.ui.fragments.pagers.items.PI_LibraryShowFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class MovieShowFragment extends PagerTabsFragment
{	
	public MovieShowFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	public abstract Class<?> getShowFragment();
	public abstract Class<?> getMovieFragment();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{		
		mTabsAdapter.addTab("Shows", getShowFragment(), null);
		mTabsAdapter.addTab("Movies", getMovieFragment(), null);
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_tabs_pager, null);
	}
}