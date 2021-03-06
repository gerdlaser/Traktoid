/*
 * Copyright 2011 Florian Mierzejewski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.florianmski.tracktoid.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.florianmski.tracktoid.Utils;
import com.jakewharton.trakt.entities.CalendarDate;
import com.jakewharton.trakt.entities.CalendarDate.CalendarTvShowEpisode;
import com.jakewharton.trakt.entities.Images;
import com.jakewharton.trakt.entities.Ratings;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.jakewharton.trakt.entities.TvShowSeason.Episodes;
import com.jakewharton.trakt.enumerations.DayOfTheWeek;
import com.jakewharton.trakt.enumerations.Rating;

public class DatabaseWrapper 
{
	private static final String TAG = "DatabaseWrapper";

	// Begin constants:

	private static final String DATABASE_NAME = "tvshows.db";
	private static final int DATABASE_VERSION = 2;

	public static final String KEY_ID = "_id";
	public static final int COLUMN_KEY_ID = 0;

	/************************** TvShow table *******************************/
	private static final String TVSHOWS_TABLE = "tvshows";

	public static final String KEY_TVSHOW_TITLE = "title";
	public static final int COLUMN_TVSHOW_TITLE = 1;

	public static final String KEY_TVSHOW_YEAR = "year";
	public static final int COLUMN_TVSHOW_YEAR = 2;

	public static final String KEY_TVSHOW_URL = "url";
	public static final int COLUMN_TVSHOW_URL = 3;

	public static final String KEY_TVSHOW_FIRST_AIRED = "first_aired";
	public static final int COLUMN_TVSHOW_FIRST_AIRED = 4;

	public static final String KEY_TVSHOW_COUNTRY = "country";
	public static final int COLUMN_TVSHOW_COUNTRY = 5;

	public static final String KEY_TVSHOW_OVERVIEW = "overview";
	public static final int COLUMN_TVSHOW_OVERVIEW = 6;

	public static final String KEY_TVSHOW_RUNTIME = "runtime";
	public static final int COLUMN_TVSHOW_RUNTIME = 7;

	public static final String KEY_TVSHOW_NETWORK = "network";
	public static final int COLUMN_TVSHOW_NETWORK = 8;

	public static final String KEY_TVSHOW_AIR_DAY = "air_day";
	public static final int COLUMN_TVSHOW_AIR_DAY = 9;

	public static final String KEY_TVSHOW_AIR_TIME = "air_time";
	public static final int COLUMN_TVSHOW_AIR_TIME = 10;

	public static final String KEY_TVSHOW_CERTIFICATION = "certification";
	public static final int COLUMN_TVSHOW_CERTIFICATION = 11;

	public static final String KEY_TVSHOW_IMDB_ID = "imdb_id";
	public static final int COLUMN_TVSHOW_IMDB_ID = 12;

	public static final String KEY_TVSHOW_TVDB_ID = "tvdb_id";
	public static final int COLUMN_TVSHOW_TVDB_ID = 13;

	public static final String KEY_TVSHOW_TVRAGE_ID = "tvrage_id";
	public static final int COLUMN_TVSHOW_TVRAGE_ID = 14;

	public static final String KEY_TVSHOW_POSTER = "poster";
	public static final int COLUMN_TVSHOW_POSTER = 15;

	public static final String KEY_TVSHOW_FANART = "fanart";
	public static final int COLUMN_TVSHOW_FANART = 16;

	public static final String KEY_TVSHOW_PERCENTAGE = "percentage";
	public static final int COLUMN_TVSHOW_PERCENTAGE = 17;

	public static final String KEY_TVSHOW_VOTES = "votes";
	public static final int COLUMN_TVSHOW_VOTES = 18;

	public static final String KEY_TVSHOW_LOVED = "loved";
	public static final int COLUMN_TVSHOW_LOVED = 19;

	public static final String KEY_TVSHOW_HATED = "hated";
	public static final int COLUMN_TVSHOW_HATED = 20;

	public static final String KEY_TVSHOW_RATING = "rating";
	public static final int COLUMN_TVSHOW_RATING = 21;

	public static final String KEY_TVSHOW_IN_WATCHLIST = "in_watchlist";
	public static final int COLUMN_TVSHOW_IN_WATCHLIST = 22;

	public static final String KEY_TVSHOW_EPISODES_WATCHED = "episodes_watched";
	public static final int COLUMN_TVSHOW_EPISODES_WATCHED = 23;

	public static final String KEY_TVSHOW_EPISODES = "episodes";
	public static final int COLUMN_TVSHOW_EPISODES = 24;

	public static final String KEY_TVSHOW_PROGRESS = "progress";
	public static final int COLUMN_TVSHOW_PROGRESS = 25;

	private final static String SELECT_TVSHOW = 
			KEY_ID + "," +
					KEY_TVSHOW_TITLE + "," +
					KEY_TVSHOW_YEAR + "," +
					KEY_TVSHOW_URL + "," +
					KEY_TVSHOW_FIRST_AIRED + "," +
					KEY_TVSHOW_COUNTRY + "," +
					KEY_TVSHOW_OVERVIEW + "," +
					KEY_TVSHOW_RUNTIME + "," +
					KEY_TVSHOW_NETWORK + "," +
					KEY_TVSHOW_AIR_DAY + "," +
					KEY_TVSHOW_AIR_TIME + "," +
					KEY_TVSHOW_CERTIFICATION + "," +
					KEY_TVSHOW_IMDB_ID + "," +
					KEY_TVSHOW_TVDB_ID + "," +
					KEY_TVSHOW_TVRAGE_ID + "," +
					KEY_TVSHOW_POSTER + "," +
					KEY_TVSHOW_FANART + "," +
					KEY_TVSHOW_PERCENTAGE + "," +
					KEY_TVSHOW_VOTES + "," +
					KEY_TVSHOW_LOVED + "," +
					KEY_TVSHOW_HATED + "," +
					KEY_TVSHOW_RATING + "," +
					KEY_TVSHOW_IN_WATCHLIST + "," +
					KEY_TVSHOW_EPISODES_WATCHED + "," +
					KEY_TVSHOW_EPISODES + "," +
					KEY_TVSHOW_PROGRESS;

	private static final String TVSHOWS_TABLE_CREATE = "create table " +
			TVSHOWS_TABLE + " (" + 
			KEY_ID + " integer primary key, " + 
			KEY_TVSHOW_TITLE + " text not null, " +
			KEY_TVSHOW_YEAR + " integer, " +
			KEY_TVSHOW_URL + " text, " +
			KEY_TVSHOW_FIRST_AIRED + " integer, " +
			KEY_TVSHOW_COUNTRY + " text, " +
			KEY_TVSHOW_OVERVIEW + " text, " +
			KEY_TVSHOW_RUNTIME + " integer, " +
			KEY_TVSHOW_NETWORK + " text, " +
			KEY_TVSHOW_AIR_DAY + " text," +
			KEY_TVSHOW_AIR_TIME + " text, " +
			KEY_TVSHOW_CERTIFICATION + " text, " +
			KEY_TVSHOW_IMDB_ID + " text, " +
			KEY_TVSHOW_TVDB_ID + " integer, " +
			KEY_TVSHOW_TVRAGE_ID + " integer, " +
			KEY_TVSHOW_POSTER + " text, " +
			KEY_TVSHOW_FANART + " text, " +
			KEY_TVSHOW_PERCENTAGE + " integer, " +
			KEY_TVSHOW_VOTES + " integer, " +
			KEY_TVSHOW_LOVED + " integer, " +
			KEY_TVSHOW_HATED + " integer, " +
			KEY_TVSHOW_RATING + " text, " +
			KEY_TVSHOW_IN_WATCHLIST + " boolean default 0, " + 
			KEY_TVSHOW_EPISODES_WATCHED + " integer default 0, " + 
			KEY_TVSHOW_EPISODES + " integer default 0, " + 
			KEY_TVSHOW_PROGRESS + " integer default 0 " + // No comma in the end!
			");";


	/************************** Seasons table *******************************/
	private static final String SEASONS_TABLE = "seasons";

	public static final String KEY_SEASON_SEASON = "season";
	public static final int COLUMN_SEASON_SEASON = 1;

	public static final String KEY_SEASON_EPISODES = "episodes";
	public static final int COLUMN_SEASON_EPISODES = 2;

	public static final String KEY_SEASON_EPISODES_WATCHED = "episodes_watched";
	public static final int COLUMN_SEASON_EPISODES_WATCHED = 3;

	public static final String KEY_SEASON_URL = "url";
	public static final int COLUMN_SEASON_URL = 4;

	public static final String KEY_SEASON_TVSHOW_ID = "tvshow_id";
	public static final int COLUMN_SEASON_TVSHOW_ID = 5;

	private final static String SELECT_SEASON =
			KEY_ID + "," +
					KEY_SEASON_SEASON + "," +
					KEY_SEASON_EPISODES + "," +
					KEY_SEASON_EPISODES_WATCHED + "," +
					KEY_SEASON_URL + "," +
					KEY_SEASON_TVSHOW_ID;

	private static final String SEASONS_TABLE_CREATE = "create table " +
			SEASONS_TABLE + " (" + 
			KEY_ID + " integer primary key, " + 
			KEY_SEASON_SEASON + " integer, " +
			KEY_SEASON_EPISODES + " integer, " +
			KEY_SEASON_EPISODES_WATCHED + " integer default 0, " +
			KEY_SEASON_URL + " text, " + 
			KEY_SEASON_TVSHOW_ID + " integer REFERENCES " + TVSHOWS_TABLE + " (" + KEY_TVSHOW_TVDB_ID + ") " + // No comma in the end!
			");";


	/************************** Episodes table *******************************/
	private static final String EPISODES_TABLE = "episodes";

	public static final String KEY_EPISODE_SEASON = "season";
	public static final int COLUMN_EPISODE_SEASON = 1;

	public static final String KEY_EPISODE_EPISODE = "episode";
	public static final int COLUMN_EPISODE_EPISODE = 2;

	public static final String KEY_EPISODE_TITLE = "title";
	public static final int COLUMN_EPISODE_TITLE = 3;

	public static final String KEY_EPISODE_OVERVIEW = "overview";
	public static final int COLUMN_EPISODE_OVERVIEW = 4;

	public static final String KEY_EPISODE_FIRST_AIRED = "first_aired";
	public static final int COLUMN_EPISODE_FIRST_AIRED = 5;

	public static final String KEY_EPISODE_URL = "url";
	public static final int COLUMN_EPISODE_URL = 6;

	public static final String KEY_EPISODE_SCREEN = "screen";
	public static final int COLUMN_EPISODE_SCREEN = 7;

	public static final String KEY_EPISODE_PERCENTAGE = "percentage";
	public static final int COLUMN_EPISODE_PERCENTAGE = 8;

	public static final String KEY_EPISODE_VOTES = "votes";
	public static final int COLUMN_EPISODE_VOTES = 9;

	public static final String KEY_EPISODE_LOVED = "loved";
	public static final int COLUMN_EPISODE_LOVED = 10;

	public static final String KEY_EPISODE_HATED = "hated";
	public static final int COLUMN_EPISODE_HATED = 11;

	public static final String KEY_EPISODE_WATCHED = "watched";
	public static final int COLUMN_EPISODE_WATCHED = 12;

	public static final String KEY_EPISODE_SEASON_ID = "season_id";
	public static final int COLUMN_EPISODE_SEASON_ID = 13;

	private final static String SELECT_EPISODE = 
			EPISODES_TABLE+"."+KEY_ID + "," +
					EPISODES_TABLE+"."+KEY_SEASON_SEASON + "," +
					KEY_EPISODE_EPISODE + "," +
					KEY_EPISODE_TITLE + "," +
					KEY_EPISODE_OVERVIEW + "," +
					KEY_EPISODE_FIRST_AIRED + "," +
					EPISODES_TABLE+"."+KEY_EPISODE_URL + "," +
					KEY_EPISODE_SCREEN + "," +
					KEY_EPISODE_PERCENTAGE + "," +
					KEY_EPISODE_VOTES + "," +
					KEY_EPISODE_LOVED + "," +
					KEY_EPISODE_HATED + "," +
					KEY_EPISODE_WATCHED + "," +
					KEY_EPISODE_SEASON_ID;

	private static final String EPISODES_TABLE_CREATE = "create table " +
			EPISODES_TABLE + " (" + 
			KEY_ID + " integer primary key, " + 
			KEY_EPISODE_SEASON + " integer, " +
			KEY_EPISODE_EPISODE + " integer, " +
			KEY_EPISODE_TITLE + " text, " +
			KEY_EPISODE_OVERVIEW + " text, " +
			KEY_EPISODE_FIRST_AIRED + " integer, " +
			KEY_EPISODE_URL + " text, " +
			KEY_EPISODE_SCREEN + " text, " +
			KEY_EPISODE_PERCENTAGE + " integer, " +
			KEY_EPISODE_VOTES + " integer, " +
			KEY_EPISODE_LOVED + " integer, " +
			KEY_EPISODE_HATED + " integer, " +
			KEY_EPISODE_WATCHED + " boolean default 0, " +
			KEY_EPISODE_SEASON_ID + "  REFERENCES " + SEASONS_TABLE + " (" + KEY_SEASON_URL + ")" +	// No comma in the end!
			");";



	/******************************* Triggers ***********************************/

	/** Update season table */
	private static final String EPISODES_WATCHED_INSERT_TRIGGER = "episodes_watched_insert_trigger";

	//update watched episodes in season table (insert case)
	private static final String EPISODES_WATCHED_INSERT_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_WATCHED_INSERT_TRIGGER + " " +
					"AFTER INSERT ON " + EPISODES_TABLE + " " +
					"WHEN " + "new."+KEY_EPISODE_WATCHED + "=1 " +
					"BEGIN " +
					"UPDATE " + SEASONS_TABLE + " " +
					"SET " + KEY_SEASON_EPISODES_WATCHED + " = " + KEY_SEASON_EPISODES_WATCHED + "+1 " +
					"WHERE " + SEASONS_TABLE+"."+KEY_SEASON_URL + " = " + "new."+KEY_EPISODE_SEASON_ID + "; " +
					"END" + ";";

	//update watched episodes in season table (update case when watched value is updated to 1)
	private static final String EPISODES_WATCHED_UPDATE_1_TRIGGER = "episodes_watched_update_1_trigger";

	private static final String EPISODES_WATCHED_UPDATE_1_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_WATCHED_UPDATE_1_TRIGGER + " " +
					"AFTER UPDATE OF " + KEY_EPISODE_WATCHED + " ON " + EPISODES_TABLE + " " +
					"WHEN " + "new."+KEY_EPISODE_WATCHED + "=1 AND " + "old."+KEY_EPISODE_WATCHED + "=0 " +
					"BEGIN " +
					"UPDATE " + SEASONS_TABLE + " " +
					"SET " + KEY_SEASON_EPISODES_WATCHED + " = " + KEY_SEASON_EPISODES_WATCHED + "+1 " +
					"WHERE " + SEASONS_TABLE+"."+KEY_SEASON_URL + " = " + "new."+KEY_EPISODE_SEASON_ID + ";" +
					"END" + ";";

	//update watched episodes in season table (update case when watched value is updated to 0)
	private static final String EPISODES_WATCHED_UPDATE_0_TRIGGER = "episodes_watched_update_0_trigger";

	private static final String EPISODES_WATCHED_UPDATE_0_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_WATCHED_UPDATE_0_TRIGGER + " " +
					"AFTER UPDATE OF " + KEY_EPISODE_WATCHED + " ON " + EPISODES_TABLE + " " +
					"WHEN " + "new."+KEY_EPISODE_WATCHED + "=0 AND " + "old."+KEY_EPISODE_WATCHED + "=1 " +
					"BEGIN " +
					"UPDATE " + SEASONS_TABLE + " SET " + KEY_SEASON_EPISODES_WATCHED + " = " + KEY_SEASON_EPISODES_WATCHED + "-1 " +
					"WHERE " + SEASONS_TABLE+"."+KEY_SEASON_URL + " = " + "new."+KEY_EPISODE_SEASON_ID + ";" +
					"END" + ";";

	/** Update tvshow table */
	//update number of watched episodes in tvshow table (don't count watched episode in specials) (update case only, it's based on season's trigger)
	private static final String EPISODES_WATCHED_UPDATE_TRIGGER = "episodes_watched_update_trigger";

	private static final String EPISODES_WATCHED_UPDATE_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_WATCHED_UPDATE_TRIGGER + " " +
					"AFTER UPDATE OF " + KEY_SEASON_EPISODES_WATCHED + " ON " + SEASONS_TABLE + " " +
					"WHEN " + "new."+KEY_SEASON_SEASON + "!=0 " +
					"BEGIN " +
					"UPDATE " + TVSHOWS_TABLE + " " +
					"SET " + KEY_TVSHOW_EPISODES_WATCHED + " = " + KEY_TVSHOW_EPISODES_WATCHED + " + new." + KEY_SEASON_EPISODES_WATCHED + "- old." + KEY_SEASON_EPISODES_WATCHED + " " +
					"WHERE " + KEY_TVSHOW_TVDB_ID + " = " + "new."+KEY_SEASON_TVSHOW_ID + "; " +
					"END" + ";";

	/** don't count episodes in "specials" */
	//update number of episodes in tvshow table (don't count episode in specials) (insert case)
	private static final String EPISODES_INSERT_TRIGGER = "episodes_insert_trigger";

	private static final String EPISODES_INSERT_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_INSERT_TRIGGER + " AFTER INSERT ON " + SEASONS_TABLE + " " +
					"WHEN " + "new."+KEY_SEASON_SEASON + "!=0 " + 
					"BEGIN " +
					"UPDATE " + TVSHOWS_TABLE + " SET " + KEY_TVSHOW_EPISODES + " = " + KEY_TVSHOW_EPISODES + "+ new." + KEY_SEASON_EPISODES + " " +
					"WHERE " + KEY_TVSHOW_TVDB_ID + " = " + "new."+KEY_SEASON_TVSHOW_ID + "; " +
					"END" + ";";

	//update number of episodes in tvshow table (don't count episode in specials) (update case)
	private static final String EPISODES_UPDATE_TRIGGER = "episodes_update_trigger";

	private static final String EPISODES_UPDATE_TRIGGER_CREATE = 
			"CREATE TRIGGER " +	EPISODES_UPDATE_TRIGGER + " " +
					"AFTER UPDATE OF " + KEY_SEASON_EPISODES + " ON " + SEASONS_TABLE + " " +
					"WHEN " + "new."+KEY_SEASON_SEASON + "!=0 " +
					"BEGIN " +
					"UPDATE " + TVSHOWS_TABLE + " SET " + KEY_TVSHOW_EPISODES + " = " + KEY_TVSHOW_EPISODES + "+ new." + KEY_SEASON_EPISODES + "- old." + KEY_SEASON_EPISODES + " " +
					"WHERE " + KEY_TVSHOW_TVDB_ID + " = " + "new."+KEY_SEASON_TVSHOW_ID + "; " +
					"END" + ";";


	/******************************************************************************/
	/**
	 * Helper class to create/open/migrate the database
	 */
	private static class DataBaseOpener extends SQLiteOpenHelper
	{
		private Context context;

		public DataBaseOpener(Context context, String name, CursorFactory factory, int version) 
		{
			super(context, name, factory, version); 
			this.context = context;
		}
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(TVSHOWS_TABLE_CREATE);
			db.execSQL(SEASONS_TABLE_CREATE);
			db.execSQL(EPISODES_TABLE_CREATE);

			db.execSQL(EPISODES_WATCHED_INSERT_TRIGGER_CREATE);
			db.execSQL(EPISODES_WATCHED_UPDATE_1_TRIGGER_CREATE);
			db.execSQL(EPISODES_WATCHED_UPDATE_0_TRIGGER_CREATE);

			db.execSQL(EPISODES_WATCHED_UPDATE_TRIGGER_CREATE);
			db.execSQL(EPISODES_UPDATE_TRIGGER_CREATE);
			db.execSQL(EPISODES_INSERT_TRIGGER_CREATE);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, int oldVersion, final int newVersion) 
		{
			final ProgressDialog pd;
			pd = ProgressDialog.show(context, "", "Upgrading database from v" + oldVersion + " to v" + newVersion + "\nPlease wait...");

			new Thread()
			{
				@Override
				public void run()
				{
					if(newVersion <= 2)
						upgradeFromV1ToV2(db);		

					pd.dismiss();
				}
			}.start();
		}

		private void upgradeFromV1ToV2(SQLiteDatabase db)
		{
			//adding new columns
			db.execSQL(
					"ALTER TABLE " + TVSHOWS_TABLE + " " +
							"ADD COLUMN " + KEY_TVSHOW_PROGRESS + ";");

			db.execSQL(
					"ALTER TABLE " + SEASONS_TABLE + " " +
							"ADD COLUMN " + KEY_SEASON_TVSHOW_ID + " integer " +
							"REFERENCES " + TVSHOWS_TABLE + " (" + KEY_TVSHOW_TVDB_ID + ");");

			db.execSQL(
					"ALTER TABLE " + EPISODES_TABLE + " " +
							"ADD COLUMN " + KEY_EPISODE_SEASON_ID + " text " +
							"REFERENCES " + SEASONS_TABLE + " (" + KEY_SEASON_URL + ");");

			//transfering data
			db.execSQL("UPDATE " + SEASONS_TABLE + " SET " + KEY_SEASON_TVSHOW_ID + " = (SELECT tvshow_id FROM tvshows_seasons WHERE season_id = " + KEY_SEASON_URL + ");");
			db.execSQL("UPDATE " + EPISODES_TABLE + " SET " + KEY_EPISODE_SEASON_ID + " = (SELECT season_id FROM seasons_episodes WHERE episode_id = " + KEY_EPISODE_URL + ");");

			//drop stupid tables
			db.execSQL("DROP TABLE tvshows_seasons;");
			db.execSQL("DROP TABLE seasons_episodes;");

			//drop and recreate same triggers
			db.execSQL("DROP TRIGGER " + EPISODES_WATCHED_INSERT_TRIGGER + ";");
			db.execSQL("DROP TRIGGER " + EPISODES_WATCHED_UPDATE_0_TRIGGER + ";");
			db.execSQL("DROP TRIGGER " + EPISODES_WATCHED_UPDATE_1_TRIGGER + ";");
			db.execSQL("DROP TRIGGER " + EPISODES_WATCHED_UPDATE_TRIGGER + ";");
			db.execSQL("DROP TRIGGER " + EPISODES_INSERT_TRIGGER + ";");
			db.execSQL("DROP TRIGGER " + EPISODES_UPDATE_TRIGGER + ";");

			db.execSQL(EPISODES_WATCHED_INSERT_TRIGGER_CREATE);
			db.execSQL(EPISODES_WATCHED_UPDATE_1_TRIGGER_CREATE);
			db.execSQL(EPISODES_WATCHED_UPDATE_0_TRIGGER_CREATE);
			db.execSQL(EPISODES_WATCHED_UPDATE_TRIGGER_CREATE);
			db.execSQL(EPISODES_UPDATE_TRIGGER_CREATE);
			db.execSQL(EPISODES_INSERT_TRIGGER_CREATE);

			DatabaseWrapper dbw = new DatabaseWrapper(context);
			dbw.open();

			for(TvShow s : dbw.getShows())
				dbw.refreshPercentage(s.tvdbId);

			dbw.close();
		}
	}

	/******************************************************************************/
	private final Context context;
	private static SQLiteDatabase db = null;
	private static Integer nbOpenedInstances = new Integer(0);

	public DatabaseWrapper(Context context) 
	{
		this.context = context;
	}

	public void open() throws SQLException 
	{
		synchronized(nbOpenedInstances) 
		{
			if(nbOpenedInstances.intValue() == 0) 
			{
				DataBaseOpener dbOpener = new DataBaseOpener(this.context, DATABASE_NAME, null, DATABASE_VERSION);
				db = dbOpener.getWritableDatabase();
			}
			nbOpenedInstances++;
		}
	}

	public void close()
	{
		synchronized (nbOpenedInstances) 
		{
			if(nbOpenedInstances.intValue() > 0) 
				nbOpenedInstances--;
			if(nbOpenedInstances.intValue() == 0 && db.isOpen()) 
				db.close();
		}
	}

	/******************************************************************************/

	/**
	 *  Helper for inserting or updating an entry:
	 * @param table
	 * @param values
	 * @param id
	 */
	private void insertOrUpdate(String table, ContentValues values, String id) 
	{
		String key_id = "";

		if(table.equals(TVSHOWS_TABLE))
			key_id = KEY_TVSHOW_TVDB_ID;
		else if(table.equals(SEASONS_TABLE))
			key_id = KEY_SEASON_URL;
		else if(table.equals(EPISODES_TABLE))
			key_id = KEY_EPISODE_URL;

		// Try to update the entry!
		int nbRowsAffected = db.update(
				table,
				values,
				key_id + "=?",
				new String[]{id});

		// If nothing has been updated, insert a new entry
		if(nbRowsAffected == 0)
			db.insert(table, null, values);
	}

	/************************** Shows methods *******************************/

	/**
	 *  Insert or update a tvshow
	 */
	public void insertOrUpdateShow(TvShow s) 
	{
		ContentValues values = new ContentValues();

		if(s.ratings != null)
		{
			values.put(KEY_TVSHOW_HATED, s.ratings.hated);
			values.put(KEY_TVSHOW_PERCENTAGE, s.ratings.percentage);
			values.put(KEY_TVSHOW_LOVED, s.ratings.loved);
			values.put(KEY_TVSHOW_VOTES, s.ratings.votes);			
		}

		if(s.rating != null)
			values.put(KEY_TVSHOW_RATING, s.rating.toString());

		if(s.airDay != null)
			values.put(KEY_TVSHOW_AIR_DAY, s.airDay.toString());
		else
			values.put(KEY_TVSHOW_AIR_DAY, "");

		values.put(KEY_TVSHOW_AIR_TIME, s.airTime);
		values.put(KEY_TVSHOW_CERTIFICATION, s.certification);
		values.put(KEY_TVSHOW_COUNTRY, s.country);

		if(s.images != null)
		{
			values.put(KEY_TVSHOW_FANART, s.images.fanart);
			values.put(KEY_TVSHOW_POSTER, s.images.poster);
		}

		if(s.firstAired != null)
			values.put(KEY_TVSHOW_FIRST_AIRED, s.firstAired.toString());

		values.put(KEY_TVSHOW_IMDB_ID, s.imdbId);
		values.put(KEY_TVSHOW_IN_WATCHLIST, s.inWatchlist);
		values.put(KEY_TVSHOW_NETWORK, s.network);
		values.put(KEY_TVSHOW_OVERVIEW, s.overview);
		values.put(KEY_TVSHOW_RUNTIME, s.runtime);
		values.put(KEY_TVSHOW_TITLE, s.title);
		values.put(KEY_TVSHOW_TVDB_ID, s.tvdbId);
		values.put(KEY_TVSHOW_TVRAGE_ID, s.tvrageId);
		values.put(KEY_TVSHOW_URL, s.url);
		values.put(KEY_TVSHOW_YEAR, Integer.valueOf(s.year));

		//		values.put(KEY_TVSHOW_PROGRESS, s.getProgress());

		insertOrUpdate(TVSHOWS_TABLE, values, s.tvdbId);
	}

	/**
	 *  Insert or update a list of tvshow
	 */
	public void insertOrUpdateShows(List<TvShow> shows) 
	{
		for(TvShow s : shows)
			insertOrUpdateShow(s);
	}

	private TvShow getShowFromCursor(Cursor c)
	{
		TvShow show = new TvShow();
		Images i = new Images();
		Ratings r = new Ratings();

		i.fanart = c.getString(COLUMN_TVSHOW_FANART);
		i.poster = c.getString(COLUMN_TVSHOW_POSTER);

		r.hated = c.getInt(COLUMN_TVSHOW_HATED);
		r.loved = c.getInt(COLUMN_TVSHOW_LOVED);
		r.percentage = c.getInt(COLUMN_TVSHOW_PERCENTAGE);
		r.votes = c.getInt(COLUMN_TVSHOW_VOTES);

		show.airDay = DayOfTheWeek.fromValue(c.getString(COLUMN_TVSHOW_AIR_DAY));
		show.airTime = c.getString(COLUMN_TVSHOW_AIR_TIME);
		show.certification = c.getString(COLUMN_TVSHOW_CERTIFICATION);
		show.country = c.getString(COLUMN_TVSHOW_COUNTRY);
		show.network = c.getString(COLUMN_TVSHOW_NETWORK);
		show.overview = c.getString(COLUMN_TVSHOW_OVERVIEW);
		show.runtime = c.getInt(COLUMN_TVSHOW_RUNTIME);
		show.tvdbId = c.getString(COLUMN_TVSHOW_TVDB_ID);
		show.tvrageId = c.getString(COLUMN_TVSHOW_TVRAGE_ID);
		show.rating = c.getString(COLUMN_TVSHOW_RATING) == null ? null : Rating.fromValue(c.getString(COLUMN_TVSHOW_RATING));
		show.inWatchlist = c.getInt(COLUMN_TVSHOW_IN_WATCHLIST) != 0;
		show.images = i;
		show.imdbId = c.getString(COLUMN_TVSHOW_IMDB_ID);
		show.ratings = r;
		show.title = c.getString(COLUMN_TVSHOW_TITLE);
		show.url = c.getString(COLUMN_TVSHOW_URL);
		show.year = c.getInt(COLUMN_TVSHOW_YEAR);

		show.progress = c.getInt(COLUMN_TVSHOW_PROGRESS);

		return show;
	}

	public List<TvShow> getShows()
	{
		List<TvShow> shows = new ArrayList<TvShow>();
		Cursor c = db.rawQuery(
				"SELECT * " +
						"FROM " + TVSHOWS_TABLE + " " +
						"ORDER BY " + KEY_TVSHOW_TITLE, 
						null);
		c.moveToFirst();
		for(int i = 0; i < c.getCount(); i++)
		{
			shows.add(getShowFromCursor(c));
			c.moveToNext();
		}

		c.close();

		Collections.sort(shows);

		return shows;
	}

	public TvShow getShow(String tvdbId)
	{
		Cursor c = db.rawQuery(
				"SELECT * " +
						"FROM " + TVSHOWS_TABLE + " " +
						"WHERE " + KEY_TVSHOW_TVDB_ID + "=?", 
						new String[]{tvdbId});

		TvShow show = null;

		if(c.moveToFirst())
			show = getShowFromCursor(c);

		c.close();

		return show;
	}

	public void removeShow(String tvdbId)
	{
		boolean showFound = db.delete(
				TVSHOWS_TABLE,
				KEY_TVSHOW_TVDB_ID + "=?",
				new String[]{tvdbId}) > 0;

				if(showFound) 
				{
					List<TvShowSeason> seasons = getSeasons(tvdbId, true, true);
					for(TvShowSeason season : seasons)
					{
						List<TvShowEpisode> episodes = season.episodes.episodes;
						db.delete(
								SEASONS_TABLE,
								KEY_SEASON_URL + "=?",
								new String[]{season.url});

						for(TvShowEpisode episode : episodes)
						{
							db.delete(
									EPISODES_TABLE,
									KEY_EPISODE_URL + "=?",
									new String[]{episode.url});
						}

					}
				}
	}

	public boolean showExist(String tvdbId)
	{
		Cursor c = db.rawQuery(
				"SELECT " + KEY_TVSHOW_TVDB_ID + " " +
						"FROM " + TVSHOWS_TABLE + " " +
						"WHERE " + KEY_TVSHOW_TVDB_ID + "=?", 
						new String[]{tvdbId});

		boolean exist = c.moveToFirst();

		return exist;
	}

	/************************** Seasons methods *******************************/	

	/**
	 *  Insert or update a season
	 */
	public void insertOrUpdateSeason(TvShowSeason s, String tvshowId) 
	{
		ContentValues values = new ContentValues();

		if(s.episodes != null)
		{
			int episodes = s.episodes.episodes.size();
			values.put(KEY_SEASON_EPISODES, episodes);
		}

		int season = s.season;
		String url = s.url;

		values.put(KEY_SEASON_SEASON, season);
		values.put(KEY_SEASON_URL, url);
		values.put(KEY_SEASON_TVSHOW_ID, tvshowId);

		insertOrUpdate(SEASONS_TABLE, values, url);

	}

	/**
	 *  Insert or update a list of seasons
	 */
	public void insertOrUpdateSeasons(List<TvShowSeason> seasons, String tvshowId)
	{
		for(TvShowSeason s : seasons)
			insertOrUpdateSeason(s, tvshowId);
	}

	private TvShowSeason getSeasonFromCursor(Cursor c, boolean getEpisodesToo)
	{
		TvShowSeason s = new TvShowSeason();
		Episodes episodes = new Episodes();

		if(getEpisodesToo)
			episodes.episodes = getEpisodes(c.getString(COLUMN_SEASON_URL));

		s.episodes = episodes;

		s.season = c.getInt(COLUMN_SEASON_SEASON);
		s.episodesWatched = c.getInt(COLUMN_SEASON_EPISODES_WATCHED);
		s.episodes.count = (c.getInt(COLUMN_SEASON_EPISODES));
		s.url = c.getString(COLUMN_SEASON_URL);

		return s;
	}

	public List<TvShowSeason> getSeasons(String tvdbId, boolean getEpisodesToo, boolean orderByASC)
	{
		ArrayList<TvShowSeason> seasons = new ArrayList<TvShowSeason>();
		String sql = 
				"SELECT * " + 
						"FROM " + SEASONS_TABLE + " " +
						"WHERE " + KEY_SEASON_TVSHOW_ID	+ "=? " +
						"ORDER BY " + KEY_SEASON_SEASON + (orderByASC ? " ASC" : " DESC");
		Cursor c = db.rawQuery(sql, new String[]{tvdbId});
		c.moveToFirst();
		for(int i = 0; i < c.getCount(); i++)
		{
			seasons.add(getSeasonFromCursor(c, getEpisodesToo));
			c.moveToNext();
		}

		c.close();

		return seasons;
	}

	public TvShowSeason getSeason(String tvdbId, int season, boolean getEpisodesToo)
	{
		String sql = 
				"SELECT * "+
						"FROM " + SEASONS_TABLE + " " + 
						"WHERE " + KEY_SEASON_TVSHOW_ID + "=? " +
						"AND s." + KEY_SEASON_SEASON + "=? " +
						"ORDER BY " + KEY_SEASON_SEASON + " DESC";
		Cursor c = db.rawQuery(sql, new String[]{tvdbId, String.valueOf(season)});
		c.moveToFirst();

		TvShowSeason tvSeason = getSeasonFromCursor(c, getEpisodesToo);

		c.close();

		return tvSeason;
	}

	/************************** Episodes methods *******************************/

	/**
	 *  Insert or update an episode
	 */
	public void insertOrUpdateEpisode(TvShowEpisode e, String seasonId) 
	{
		ContentValues values = new ContentValues();

		String url = e.url;

		values.put(KEY_EPISODE_EPISODE, e.number);
		values.put(KEY_EPISODE_FIRST_AIRED, e.firstAired.getTime());
		values.put(KEY_EPISODE_HATED, e.ratings.hated);
		values.put(KEY_EPISODE_LOVED, e.ratings.loved);
		values.put(KEY_EPISODE_OVERVIEW, e.overview);
		values.put(KEY_EPISODE_PERCENTAGE, e.ratings.percentage);
		values.put(KEY_EPISODE_SCREEN, e.images.screen);
		values.put(KEY_EPISODE_SEASON, e.season);
		values.put(KEY_EPISODE_TITLE, e.title);
		values.put(KEY_EPISODE_URL, url);
		values.put(KEY_EPISODE_VOTES, e.ratings.votes);
		values.put(KEY_EPISODE_WATCHED, e.watched);
		values.put(KEY_EPISODE_SEASON_ID, seasonId);

		insertOrUpdate(EPISODES_TABLE, values, url);
	}

	/**
	 *  Insert or update an episode
	 *  /!\ be careful, this episode MUST BE ALREADY IN DB /!\
	 */
	public boolean insertOrUpdateEpisode(TvShowEpisode e) 
	{
		//this episode is not in db, cancel insertion
		if(getEpisode(e.url) == null)
			return false;

		ContentValues values = new ContentValues();

		String url = e.url;

		values.put(KEY_EPISODE_EPISODE, e.number);
		values.put(KEY_EPISODE_FIRST_AIRED, e.firstAired.getTime());
		values.put(KEY_EPISODE_HATED, e.ratings.hated);
		values.put(KEY_EPISODE_LOVED, e.ratings.loved);
		values.put(KEY_EPISODE_OVERVIEW, e.overview);
		values.put(KEY_EPISODE_PERCENTAGE, e.ratings.percentage);
		values.put(KEY_EPISODE_SCREEN, e.images.screen);
		values.put(KEY_EPISODE_SEASON, e.season);
		values.put(KEY_EPISODE_TITLE, e.title);
		values.put(KEY_EPISODE_URL, url);
		values.put(KEY_EPISODE_VOTES, e.ratings.votes);
		values.put(KEY_EPISODE_WATCHED, e.watched);

		insertOrUpdate(EPISODES_TABLE, values, url);

		return true;
	}

	/**
	 *  Insert or update a list of seasons
	 */
	public void insertOrUpdateEpisodes(List<TvShowEpisode> episodes, String seasonId)
	{
		for(TvShowEpisode e : episodes)
			insertOrUpdateEpisode(e, seasonId);
	}

	private TvShowEpisode getEpisodeFromCursor(Cursor c)
	{
		if(c.getCount() == 0)
			return null;

		TvShowEpisode e = new TvShowEpisode();

		Ratings r = new Ratings();
		r.hated = c.getInt(COLUMN_EPISODE_HATED);
		r.loved = c.getInt(COLUMN_EPISODE_LOVED);
		r.percentage = c.getInt(COLUMN_EPISODE_PERCENTAGE);
		r.votes = c.getInt(COLUMN_EPISODE_VOTES);

		Images i = new Images();
		i.screen = c.getString(COLUMN_EPISODE_SCREEN);

		e.number = c.getInt(COLUMN_EPISODE_EPISODE);
		e.firstAired = new Date(c.getLong(COLUMN_EPISODE_FIRST_AIRED));
		e.overview = c.getString(COLUMN_EPISODE_OVERVIEW);
		e.ratings = r;
		e.images = i;
		e.season = c.getInt(COLUMN_EPISODE_SEASON);
		e.title = c.getString(COLUMN_EPISODE_TITLE);
		e.url = c.getString(COLUMN_EPISODE_URL);
		e.watched = c.getInt(COLUMN_EPISODE_WATCHED) != 0;

		return e;
	}

	public List<TvShowEpisode> getEpisodes(String seasonId)
	{
		ArrayList<TvShowEpisode> episodes = new ArrayList<TvShowEpisode>();
		String sql = 
				"SELECT * " + 
						"FROM " + EPISODES_TABLE + " " +
						"WHERE " + KEY_EPISODE_SEASON_ID + "=? " + 
						"ORDER BY " + KEY_EPISODE_EPISODE;
		Cursor c = db.rawQuery(sql, new String[]{seasonId});
		c.moveToFirst();
		for(int i = 0; i < c.getCount(); i++)
		{
			episodes.add(getEpisodeFromCursor(c));
			c.moveToNext();
		}

		c.close();

		return episodes;
	}

	public TvShowEpisode getEpisode(String seasonId, int episode)
	{
		String sql = 
				"SELECT * " + 
						"FROM " + EPISODES_TABLE + " " +
						"WHERE " + KEY_EPISODE_SEASON_ID + "=? " + 
						"AND " + KEY_EPISODE_EPISODE + "=? " +
						"ORDER BY " + KEY_EPISODE_EPISODE;
		Cursor c = db.rawQuery(sql, new String[]{seasonId, String.valueOf(episode)});
		c.moveToFirst();

		TvShowEpisode tvEpisode = getEpisodeFromCursor(c);

		c.close();

		return tvEpisode;
	}

	public TvShowEpisode getEpisode(String url)
	{
		String sql = 
				"SELECT * " + 
						"FROM " + EPISODES_TABLE + " " +
						"WHERE " + KEY_EPISODE_URL + "=? " + 
						"ORDER BY " + KEY_EPISODE_EPISODE;
		Cursor c = db.rawQuery(sql, new String[]{url});

		TvShowEpisode tvEpisode = null;

		if(c.moveToFirst())
			tvEpisode = getEpisodeFromCursor(c);

		c.close();

		return tvEpisode;
	}

	public void markEpisodeAsWatched(boolean watched, String tvdbId, int season, int episode)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_EPISODE_WATCHED, watched);

		db.update(
				EPISODES_TABLE, 
				cv, 
				KEY_EPISODE_SEASON + "=? AND " + KEY_EPISODE_EPISODE + "=? " +
						"AND " + KEY_EPISODE_SEASON_ID + " " +
						"IN (SELECT " + KEY_SEASON_URL + " " +
						"FROM " + SEASONS_TABLE + " " +
						"WHERE " + KEY_SEASON_TVSHOW_ID + "=?)", 
						new String[]{String.valueOf(season), String.valueOf(episode), tvdbId});
	}


	/************************** Other methods *******************************/

	public boolean isEmpty()
	{
		String sql = "SELECT * FROM " + TVSHOWS_TABLE;
		Cursor c = db.rawQuery(sql, null);
		return !c.moveToFirst();
	}

	public TvShowEpisode getNextEpisode(String tvdbId)
	{
		String sql = 
				"SELECT " + SELECT_EPISODE + " " +
						"FROM " + EPISODES_TABLE + "," + SEASONS_TABLE + " " +
						"WHERE " + SEASONS_TABLE+"."+KEY_SEASON_URL + "=" + KEY_EPISODE_SEASON_ID + " " +
						"AND " + SEASONS_TABLE+"."+KEY_SEASON_SEASON + "!=? " +
						"AND " + KEY_EPISODE_WATCHED + "=? " + 
						"AND " + KEY_SEASON_TVSHOW_ID + "=? " +
						"ORDER BY " + EPISODES_TABLE+"."+KEY_EPISODE_SEASON + "," + KEY_EPISODE_EPISODE + " ASC LIMIT 1";

		Cursor c = db.rawQuery(sql, new String[]{String.valueOf(0), String.valueOf(0), tvdbId});
		c.moveToFirst();

		TvShowEpisode tvEpisode = getEpisodeFromCursor(c);

		c.close();

		return tvEpisode;		
	}

	//refresh a show percentage (based on episodes watched, episodes not aired yet, specials episodes...)
	public int refreshPercentage(String tvdbId)
	{
		String sql = 
				"SELECT " + KEY_TVSHOW_EPISODES + "," + KEY_TVSHOW_EPISODES_WATCHED + " " +
						"FROM " + TVSHOWS_TABLE + " " +
						"WHERE " + KEY_TVSHOW_TVDB_ID + "=?";

		Cursor c = db.rawQuery(sql , new String[]{tvdbId});
		c.moveToFirst();

		int numberOfEpisodes = c.getInt(0);
		int numberOfEpisodesSeen = c.getInt(1);

		String sql2 = 
				"SELECT count(*) " +
						"FROM " + EPISODES_TABLE + "," + SEASONS_TABLE + " " +
						"WHERE " + SEASONS_TABLE+"."+KEY_SEASON_URL + "=" + KEY_EPISODE_SEASON_ID + " " + 
						"AND " + KEY_SEASON_TVSHOW_ID + "=? " +
						"AND " + SEASONS_TABLE+"."+KEY_SEASON_SEASON + "!=? " +
						"AND(" + KEY_EPISODE_FIRST_AIRED + "=? " + " " +
						"OR " + KEY_EPISODE_FIRST_AIRED + "> ? " + 
						"OR " + EPISODES_TABLE+"."+KEY_EPISODE_EPISODE + "=? )";

		c.close();

		Cursor c2 = db.rawQuery(sql2 , new String[]{tvdbId, String.valueOf(0), String.valueOf(0), String.valueOf(new Date().getTime()), String.valueOf(0)});
		c2.moveToFirst();
		numberOfEpisodes -= c2.getInt(0);

		c2.close();

		int realPercentage = (int) ((numberOfEpisodesSeen*1.0/numberOfEpisodes*1.0)*100);
		realPercentage = (realPercentage > 100) ? 100 : ((realPercentage < 0) ? 0 : realPercentage);

		ContentValues cv = new ContentValues();
		cv.put(KEY_TVSHOW_PROGRESS, realPercentage);

		db.update(
				TVSHOWS_TABLE, 
				cv, 
				KEY_TVSHOW_TVDB_ID + "=?", 
				new String[]{tvdbId});

		return realPercentage;
	}

	public ArrayList<CalendarDate> getFutureEpisodes()
	{
		ArrayList<CalendarDate> episodes = new ArrayList<CalendarDate>();
		String sql = 
				"SELECT * " + 
						"FROM " + EPISODES_TABLE + ", " + SEASONS_TABLE + ", " + TVSHOWS_TABLE + " " +
						"WHERE " + KEY_EPISODE_SEASON_ID + "=" + SEASONS_TABLE+"."+KEY_SEASON_URL + " " +
						"AND " + KEY_SEASON_TVSHOW_ID + "=" + KEY_TVSHOW_TVDB_ID + " " +
						"AND " + EPISODES_TABLE+"."+KEY_EPISODE_FIRST_AIRED + ">=?" +  " " +
						"ORDER BY " + EPISODES_TABLE+"."+KEY_EPISODE_FIRST_AIRED;

		Cursor c = db.rawQuery(sql, new String[]{String.valueOf(new Date().getTime())});
		c.moveToFirst();

		CalendarDate cd = null;
		for(int i = 0; i < c.getCount(); i++)
		{
			TvShowEpisode e = getEpisodeFromCursor(c);
			e.images.screen = null;
			
			if(cd == null || !Utils.isSameDay(cd.date, e.firstAired))
			{
				cd = new CalendarDate();
				cd.date = e.firstAired;
				cd.episodes = new ArrayList<CalendarTvShowEpisode>();
				episodes.add(cd);
			}

			CalendarTvShowEpisode cde = new CalendarTvShowEpisode();

			TvShow s = new TvShow();

			s.airTime = c.getString(c.getColumnIndex(KEY_TVSHOW_AIR_TIME));
			s.title = c.getString(c.getColumnIndex(KEY_TVSHOW_TITLE));
			s.tvdbId = c.getString(c.getColumnIndex(KEY_TVSHOW_TVDB_ID));
			s.network = c.getString(c.getColumnIndex(KEY_TVSHOW_NETWORK));
			
			s.images = new Images();
			s.images.poster = c.getString(c.getColumnIndex(KEY_TVSHOW_POSTER));

			cde.show = s;
			cde.episode = e;
			cd.episodes.add(cde);

			c.moveToNext();
		}

		c.close();

		return episodes;
	}

}
