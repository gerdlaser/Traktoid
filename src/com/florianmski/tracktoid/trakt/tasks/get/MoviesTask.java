package com.florianmski.tracktoid.trakt.tasks.get;

import java.util.ArrayList;
import android.support.v4.app.Fragment;

import com.florianmski.tracktoid.Utils;
import com.florianmski.tracktoid.trakt.TraktManager;
import com.florianmski.tracktoid.trakt.tasks.TraktTask;
import com.jakewharton.trakt.TraktApiBuilder;
import com.jakewharton.trakt.entities.Movie;

public class MoviesTask extends TraktTask
{
	private ArrayList<Movie> movies = new ArrayList<Movie>();
	private TraktApiBuilder<?> builder;
	private boolean sort;
	private MoviesListener listener;
	
	public MoviesTask(TraktManager tm, Fragment fragment, MoviesListener listener, TraktApiBuilder<?> builder, boolean sort) 
	{
		super(tm, fragment);
		
		this.builder = builder;
		this.sort = sort;
		this.listener = listener;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean doTraktStuffInBackground()
	{		
		movies = (ArrayList<Movie>) builder.fire();
		
		//TODO
//		if(sort)
//			Collections.sort(movies);
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean success)
	{
		super.onPostExecute(success);
		
		if(success && !Utils.isActivityFinished(fragment.getActivity()))
			listener.onMovies(movies);
	}
	
	public interface MoviesListener
	{
		public void onMovies(ArrayList<Movie> movies);
	}
}