package com.florianmski.tracktoid.ui.fragments.pagers;

import java.util.List;

import android.os.Bundle;

import com.florianmski.tracktoid.TraktoidConstants;
import com.florianmski.tracktoid.adapters.pagers.PagerShowAdapter;
import com.jakewharton.trakt.entities.TvShow;

public class ShowPagerFragment extends PagerFragment
{
	//TODO onShowUpdated()
	private TvShow show;
	private boolean isExist;
	
	public static ShowPagerFragment newInstance(Bundle args)
	{
		ShowPagerFragment f = new ShowPagerFragment();
		f.setArguments(args);
		return f;
	}
	
	public ShowPagerFragment() {}
	
	public ShowPagerFragment(FragmentListener listener) 
	{
		super(listener);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setPageIndicatorType(PagerFragment.IT_CIRCLE);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		getStatusView().show().text("Loading shows,\nPlease wait...");
		setData();
	}
	
	public void setData()
	{
		new Thread()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void run()
			{
				final List<TvShow> shows = (List<TvShow>)getArguments().getSerializable(TraktoidConstants.BUNDLE_RESULTS);

				getActivity().runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						adapter = new PagerShowAdapter(shows, getFragmentManager(), getActivity());
						
						if(((PagerShowAdapter)adapter).isEmpty())
							getStatusView().hide().text("No shows, this is strange...");
						else
							getStatusView().hide().text(null);
						
						initPagerFragment(adapter);
					}
				});
			}
		}.start();
	}

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//	{
//		super.onCreateOptionsMenu(menu, inflater);
//		if(!isExist)
//		{
//			menu.add(0, R.id.action_bar_add, 0, "Add")
//			.setIcon(R.drawable.ab_icon_add)
//			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		}
//		menu.add(0, R.id.action_bar_shouts, 0, "Shouts")
//		.setIcon(R.drawable.ab_icon_shouts)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) 
//	{
//		switch(item.getItemId())
//		{
//		case R.id.action_bar_add :
//			ArrayList<TvShow> shows = new ArrayList<TvShow>();
//			shows.add(show);
//			tm.addToQueue(new UpdateShowsTask(tm, this, shows));
//			return true;
//		case R.id.action_bar_shouts :
//			Intent i = new Intent(getActivity(), ShoutsActivity.class);
//			i.putExtra(TraktoidConstants.BUNDLE_TVDB_ID, show.tvdbId);
//			startActivity(i);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
	@Override
	public void onPageSelected(int position) 
	{
		super.onPageSelected(position);

		show = ((PagerShowAdapter)adapter).getTvShow(currentPagerPosition);
		setTitle(show.title);
	}
}
