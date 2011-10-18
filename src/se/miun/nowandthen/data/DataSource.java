/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package se.miun.nowandthen.data;

import se.miun.nowandthen.R;
import se.miun.nowandthen.reality.PhysicalPlace;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * @author hannes
 *
 */
public class DataSource {
	
	// Datasource and dataformat are not the same. datasource is where the data comes from
	// and dataformat is how the data is formatted. 
	// this is necessary for example when you have multiple datasources with the same
	// dataformat

	public enum DATASOURCE { WIKIPEDIA, BUZZ, TWITTER, OSM, OWNURL, TEST};
	public enum DATAFORMAT { WIKIPEDIA, BUZZ, TWITTER, OSM, MIXARE, TEST};	


	/** default URL */
	private static final String WIKI_BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";
	//private static final String WIKI_BASE_URL =	"file:///sdcard/wiki.json";
	private static final String TWITTER_BASE_URL = "http://search.twitter.com/search.json";
	private static final String BUZZ_BASE_URL = "https://www.googleapis.com/buzz/v1/activities/search?alt=json&max-results=20";
	// OpenStreetMap API see http://wiki.openstreetmap.org/wiki/Xapi
	// eg. only railway stations:
	//private static final String OSM_BASE_URL =	"http://www.informationfreeway.org/api/0.6/node[railway=station]";
	//private static final String OSM_BASE_URL =	"http://xapi.openstreetmap.org/api/0.6/node[railway=station]";
	private static final String OSM_BASE_URL =		"http://osmxapi.hypercube.telascience.org/api/0.6/node[railway=station]";
	//private static final String TEST_BASE_URL =		"http://www.kulturarvsdata.se/ksamsok/api?method=search"; // TODO L?gg in l?nk h?r
	private static final String TEST_BASE_URL =		"http://www.kulturarvsdata.se/ksamsok/api"; // TODO L?gg in l?nk h?r
	private static final String TEST_API_KEY = 		"test";
	private static final String TEST_HITS_PER_PAGE = 		"50";
	//all objects that have names: 
	//String OSM_URL = "http://xapi.openstreetmap.org/api/0.6/node[name=*]"; 
	//caution! produces hugh amount of data (megabytes), only use with very small radii or specific queries

	public static Bitmap twitterIcon;
	public static Bitmap buzzIcon;
	public static Bitmap testIcon;
	public static Bitmap bat;
	public static Bitmap bytomt;
	public static Bitmap fyndplats;
	public static Bitmap minnesmarke;
	public static Bitmap ovrigt;
	public static Bitmap vardkase;
	public static Bitmap hog;
	
	public DataSource() {
		
	}
	
	public static void createIcons(Resources res) {
		twitterIcon=BitmapFactory.decodeResource(res, R.drawable.twitter);
		buzzIcon=BitmapFactory.decodeResource(res, R.drawable.buzz);
		testIcon=BitmapFactory.decodeResource(res, R.drawable.buzz);
		bat=BitmapFactory.decodeResource(res, R.drawable.bat);
		bytomt=BitmapFactory.decodeResource(res, R.drawable.bytomt);
		fyndplats=BitmapFactory.decodeResource(res, R.drawable.fyndplats);
		minnesmarke=BitmapFactory.decodeResource(res, R.drawable.minnesmarke);
		ovrigt=BitmapFactory.decodeResource(res, R.drawable.ovrigt);
		vardkase=BitmapFactory.decodeResource(res, R.drawable.vardkase);
		hog=BitmapFactory.decodeResource(res, R.drawable.hog);
	}
	
	public static Bitmap getBitmap(String ds) {
		Bitmap bitmap=null;
		if("Kyrka/kapell".equals(ds))
			bitmap=bat;
		else if(NameHelper.NAMES[22].equals(ds))  // Bytomt
			bitmap=bytomt;
		else if(NameHelper.NAMES[38].equals(ds))  // Fyndplats
			bitmap=fyndplats;
		else if(NameHelper.NAMES[107].equals(ds))  // Minnesmärke
			bitmap=minnesmarke;
		else if(NameHelper.NAMES[164].equals(ds)) // Vårdkase
			bitmap=vardkase;
		else if(NameHelper.NAMES[84].equals(ds)) // Hög
			bitmap=hog;
		
		else if ("Labyrint".equals(ds)) {
			bitmap = fyndplats;
		} else if ("Boplats".equals(ds)) {
			bitmap = minnesmarke;
		}
		else
			bitmap=ovrigt;
		
//		switch (ds) {
//			case TWITTER: bitmap=twitterIcon; break;
//			case BUZZ: bitmap=buzzIcon; break;
//			case TEST: bitmap=testIcon; break;
//		}
//		bitmap = testIcon;
		return bitmap;
	}
	
	public static DATAFORMAT dataFormatFromDataSource(String ds) {
		return DATAFORMAT.TEST;
//		DATAFORMAT ret;
//		switch (ds) {
//			case TEST: ret=DATAFORMAT.TEST; break;	
//			case WIKIPEDIA: ret=DATAFORMAT.WIKIPEDIA; break;
//			case BUZZ: ret=DATAFORMAT.BUZZ; break;
//			case TWITTER: ret=DATAFORMAT.TWITTER; break;
//			case OSM: ret=DATAFORMAT.OSM; break;
//			case OWNURL: ret=DATAFORMAT.MIXARE; break;
//						default: ret=DATAFORMAT.MIXARE; break;
//		}
//		return ret;
	}
	
	public static String createRequestURL(double lat, double lon, double alt, float radius, String searchQuery) {
		String ret=TEST_BASE_URL;
//		switch(source) {
//			case TEST:
//				ret = TEST_BASE_URL;
//			break;
//				
//			case WIKIPEDIA: 
//				ret= WIKI_BASE_URL;
//			break;
//			
//			case BUZZ: 
//				ret= BUZZ_BASE_URL;
//			break;
//			
//			case TWITTER: 
//				ret = TWITTER_BASE_URL;			
//			break;
//				
//			case OSM: 
//				ret = OSM_BASE_URL;
//			break;
//			
//			case OWNURL:
//				ret = MixListView.customizedURL;
//			break;
//				
//		}
		if (!ret.startsWith("file://")) {
//			switch(source) {
//			
//			case TEST:
				ret	+=
				"?method=search" +
				"&hitsPerPage=" + TEST_HITS_PER_PAGE + 
				"&x-api=" + TEST_API_KEY +
				"&query=" + getBoundingBox(lat, lon, radius) + searchQuery +
				"&recordSchema=presentation";
				Log.d("OMGLOL", ret);
//			break;
//			
//			case WIKIPEDIA: 
//				ret+=
//				"?lat=" + lat +
//				"&lng=" + lon + 
//				"&radius="+ radius +
//				"&maxRows=50" +
//				"&lang=" + locale; 
//			break;
//			
//			case BUZZ: 
//				ret+= 
//				"&lat=" + lat+
//				"&lon=" + lon + 
//				"&radius="+ radius*1000;
//			break;
//			
//			case TWITTER: 
//				ret+=
//				"?geocode=" + lat + "%2C" + lon + "%2C" + 
//				Math.max(radius, 1.0) + "km" ;				
//			break;
//				
//			case OSM: 
//				ret+= XMLHandler.getOSMBoundingBox(lat, lon, radius);
//			break;
//			
//			case OWNURL:
//				ret+=
//				"?latitude=" + Double.toString(lat) + 
//				"&longitude=" + Double.toString(lon) + 
//				"&altitude=" + Double.toString(alt) +
//				"&radius=" + Double.toString(radius);
//			break;
//			
//
//		
//			}
			
		}
		
		return ret;
	}
	
	// TODO Radar dot color
	public static int getColor(String datasource) {
		int ret = Color.RED;
//		switch(datasource) {
//			case TEST:		ret=Color.MAGENTA; break;
//			case BUZZ:		ret=Color.rgb(4, 228, 20); break;
//			case TWITTER:	ret=Color.rgb(50, 204, 255); break;
//			case OSM:		ret=Color.rgb(255, 168, 0); break;
//			case WIKIPEDIA:	ret=Color.RED; break;
//			default:		ret=Color.WHITE; break;
//		}
		return ret;
	}
	
	/**
	 * Calculates coordinates for boundingbox
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return coordinates for boundingbox
	 */
	private static String getBoundingBox(double lat, double lon, double radius) {
		String bbox = "boundingBox=/WGS84%20\"";
		PhysicalPlace lb = new PhysicalPlace(); // left bottom
		PhysicalPlace rt = new PhysicalPlace(); // right top
		PhysicalPlace.calcDestination(lat, lon, 225, radius*1414, lb); // 1414: sqrt(2)*1000
		PhysicalPlace.calcDestination(lat, lon, 45, radius*1414, rt);
		bbox += lb.getLongitude() + "%20" + lb.getLatitude() + "%20" + rt.getLongitude() + "%20" + rt.getLatitude() + "\"";
		return bbox;

		//return "[bbox=16.365,48.193,16.374,48.199]";
	}

}
