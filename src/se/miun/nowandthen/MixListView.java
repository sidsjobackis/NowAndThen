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
package se.miun.nowandthen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import se.miun.nowandthen.data.DataHandler;
import se.miun.nowandthen.data.NameHelper;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MixListView extends ListActivity {

	private static int list;

	private Vector<SpannableString> listViewMenu;
	private Vector<Marker> selectedItem;
	private Vector<String> dataSourceMenu;
	private Vector<String> dataSourceDescription;
	private Vector<Boolean> dataSourceChecked;
	private MixContext mixContext;
	private DataView dataView;
	//private static String selectedDataSource = "Wikipedia";
	/*to check which data source is active*/
	//	private int clickedDataSourceItem = 0;
	private ListItemAdapter adapter;
	public static String customizedURL="http://mixare.org/geotest.php";
	private static Context ctx;
	private static String searchQuery = "";
	private static SpannableString underlinedTitle;
	public static List<Marker> searchResultMarkers;
	public static List<Marker> originalMarkerList;

	public Vector<String> getDataSourceMenu() {
		return dataSourceMenu;
	}
	
	public Vector<String> getDataSourceDescription() {
		return dataSourceDescription;
	}

	public Vector<Boolean> getDataSourceChecked() {
		return dataSourceChecked;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//		mixCtx = MixView.ctx;
		dataView = MixView.dataView;	
		ctx = this;
		mixContext = dataView.getContext();

		switch(list){
		case 1:
			
			//TODO: this needs some cleanup
			dataSourceMenu = new Vector<String>();

			dataSourceDescription = new Vector<String>();
			
			dataSourceChecked = new Vector<Boolean>();
			
			for(String name : NameHelper.NAMES) {
				dataSourceMenu.add(name);
				dataSourceDescription.add("");
				dataSourceChecked.add(true);
			}

			adapter = new ListItemAdapter(this);
			//adapter.colorSource(getDataSource());
			getListView().setTextFilterEnabled(true);

			setListAdapter(adapter);
			break;

		case 2:
			selectedItem = new Vector<Marker>();
			listViewMenu = new Vector<SpannableString>();
			DataHandler jLayer = dataView.getDataHandler();
			if (dataView.isFrozen() && jLayer.getMarkerCount() > 0){
				selectedItem.add(new SiteMarker("search", 0, 0, null, null));
			}
			/*add all marker items to a title and a URL Vector*/
			for (int i = 0; i < jLayer.getMarkerCount(); i++) {
				Marker ma = jLayer.getMarker(i);
				if(ma.isActive()) {
					if (ma.getURL()!=null) {
						/* Underline the title if website is available*/
							underlinedTitle = new SpannableString(ma.getTitle());
							underlinedTitle.setSpan(new UnderlineSpan(), 0, underlinedTitle.length(), 0);
							listViewMenu.add(underlinedTitle);
						} else {
							listViewMenu.add(new SpannableString(ma.getTitle()));
						}
					/*the website for the corresponding title*/
					selectedItem.add(ma);
				}
			}

			if (dataView.isFrozen()) {

				TextView searchNotificationTxt = new TextView(this);
				searchNotificationTxt.setVisibility(View.VISIBLE);
				searchNotificationTxt.setText(getString(DataView.SEARCH_ACTIVE_1)+" "+ mixContext.getDataSourcesStringList() + getString(DataView.SEARCH_ACTIVE_2));
				searchNotificationTxt.setWidth(MixView.dWindow.getWidth());

				searchNotificationTxt.setPadding(10, 2, 0, 0);
				searchNotificationTxt.setBackgroundColor(Color.DKGRAY);
				searchNotificationTxt.setTextColor(Color.WHITE);

				getListView().addHeaderView(searchNotificationTxt);

			}

			setListAdapter(new ArrayAdapter<SpannableString>(this, android.R.layout.simple_list_item_1,listViewMenu));
			getListView().setTextFilterEnabled(true);
			break;

		}
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMixSearch(query);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void doMixSearch(String query) {
		DataHandler jLayer = dataView.getDataHandler();
		if (!dataView.isFrozen()) {
			originalMarkerList = jLayer.getMarkerList();
			MixMap.originalMarkerList = jLayer.getMarkerList();
		}
		originalMarkerList = jLayer.getMarkerList();
		searchResultMarkers = new ArrayList<Marker>();
		Log.d("SEARCH-------------------0", ""+query);
		setSearchQuery(query);

		selectedItem = new Vector<Marker>();
		listViewMenu = new Vector<SpannableString>();
		for(int i = 0; i < jLayer.getMarkerCount();i++){
			Marker ma = jLayer.getMarker(i);

			if (ma.getTitle().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
				searchResultMarkers.add(ma);
				listViewMenu.add(new SpannableString(ma.getTitle()));
				/*the website for the corresponding title*/
//				if (ma.getURL() != null)
					selectedItem.add(ma);
//				/*if no website is available for a specific title*/
//				else
//					selectedItemURL.add("");
			}
		}
		if (listViewMenu.size() == 0) {
			Toast.makeText( this, getString(DataView.SEARCH_FAILED_NOTIFICATION), Toast.LENGTH_LONG ).show();
		}
		else {
			jLayer.setMarkerList(searchResultMarkers);
			dataView.setFrozen(true);
			setList(2);
			finish();
			Intent intent1 = new Intent(this, MixListView.class); 
			startActivityForResult(intent1, 42);
		}
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch(list){
		/*Data Sources*/  
		case 1:
			//clickOnDataSource(position);	
			CheckBox cb = (CheckBox) v.findViewById(R.id.list_checkbox);
			cb.toggle();
			break;

			/*List View*/
		case 2:
			clickOnListView(position);
			break;
		}

	}

	public void clickOnListView(int position){
		/*if no website is available for this item*/
//		String selectedURL = position < selectedItemURL.size() ? selectedItemURL.get(position) : null;
//		if (selectedURL == null || selectedURL.length() <= 0)
//			Toast.makeText( this, getString(DataView.NO_WEBINFO_AVAILABLE), Toast.LENGTH_LONG ).show();			
		/*else*/ if("search".equals(selectedItem.get(position))){
			dataView.setFrozen(false);
			dataView.getDataHandler().setMarkerList(originalMarkerList);
			setList(2);
			finish();
			Intent intent1 = new Intent(this, MixListView.class); 
			startActivityForResult(intent1, 42);
		}
//		else {
//			try {
//				if (selectedURL.startsWith("webpage")) {
//					String newUrl = MixUtils.parseAction(selectedURL);
//					dataView.getContext().loadWebPage(newUrl, this);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		String title = selectedItem.get(position).title;
		String location = selectedItem.get(position).getLongitude() + " " + selectedItem.get(position).getLatitude();
		String description = selectedItem.get(position).description;
		
		Intent showInfo = new Intent(this.mixContext, ObjectInfo.class);
		showInfo.putExtra("title", title);
		showInfo.putExtra("location", location);
		showInfo.putExtra("description", description);
		this.mixContext.startActivity(showInfo);
		
	}

	public static void createContextMenu(ImageView icon) {
		icon.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {				
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				int index=0;
				switch(ListItemAdapter.itemPosition){
				case 0:
					menu.setHeaderTitle("Wiki Menu");
					menu.add(index, index, index, "We are working on it...");			
					break;
				case 1:
					menu.setHeaderTitle("Twitter Menu");
					menu.add(index, index, index, "We are working on it...");
					break;
				case 2:
					menu.setHeaderTitle("Buzz Menu");
					menu.add(index, index, index, "We are working on it...");
					break;
				case 3:
					menu.setHeaderTitle("OpenStreetMap Menu");
					menu.add(index, index, index, "We are working on it...");
					break;
				case 4:
					AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
					alert.setTitle("insert your own URL:");

					final EditText input = new EditText(ctx); 
					input.setText(customizedURL);
					alert.setView(input);

					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {       		
							Editable value = input.getText();
							customizedURL = ""+value;
						}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {       		
							dialog.dismiss();
						}
					});
					alert.show();
					break;
				}
			}
		});

	}

	public void clickOnDataSource(int position){ // TODO h�r finns konstiga grejer
		if(dataView.isFrozen())
			dataView.setFrozen(false);
		mixContext.toogleDataSource(NameHelper.NAMES[position]);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int base = Menu.FIRST;

		/*define menu items*/
		MenuItem item1 = menu.add(base, base, base, getString(DataView.MENU_ITEM_3)); 
		MenuItem item2 = menu.add(base, base+1, base+1, getString(DataView.MENU_CAM_MODE));

		/*assign icons to the menu items*/
		item1.setIcon(android.R.drawable.ic_menu_mapmode);
		item2.setIcon(android.R.drawable.ic_menu_camera);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		/*Map View*/
		case 1:
			createMixMap();
			finish();
			break;
			/*back to Camera View*/
		case 2:
			finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1: 
			break;
		case 2: 
			break;
		}
		return false;
	}

	public void createMixMap(){
		Intent intent2 = new Intent(MixListView.this, MixMap.class); 
		startActivityForResult(intent2, 20);
	}

	/*public void setDataSource(String source){
		selectedDataSource = source;
	}

	public static String getDataSource(){
		return selectedDataSource;
	}*/

	public static void setList(int l){
		list = l;
	}

	public static String getSearchQuery(){
		return searchQuery;
	}

	public static void setSearchQuery(String query){
		searchQuery = query;
	}
}

class ListItemAdapter extends BaseAdapter {

	private MixListView mixListView;

	private LayoutInflater myInflater;
	static ViewHolder holder;
	private int[] bgcolors = new int[] {0,0,0,0,0};
	private int[] textcolors = new int[] {Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE};
	private int[] descriptioncolors = new int[] {Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY};

	public static boolean icon_clicked = false;

	public static int itemPosition =0;

	public ListItemAdapter(MixListView mixListView) {
		this.mixListView = mixListView;
		myInflater = LayoutInflater.from(mixListView);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		itemPosition = position;
		if (convertView==null) {
			convertView = myInflater.inflate(R.layout.main, null);

			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.list_text);
			holder.description = (TextView) convertView.findViewById(R.id.description_text);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.list_checkbox);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}


		holder.icon.setPadding(20, 8, 20, 8);
		holder.icon.setClickable(true);        

		holder.icon.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				icon_clicked = true;
				itemPosition = position;

				return false;
			}
		});
		MixListView.createContextMenu(holder.icon);

		if(position!=4){
			holder.icon.setVisibility(View.INVISIBLE);
		}
		holder.checkbox.setChecked(mixListView.getDataSourceChecked().get(position));

		holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
					mixListView.clickOnDataSource(position);
			}
			
		});
		
		holder.text.setPadding(20, 8, 0, 0);
		holder.description.setPadding(20, 40, 0, 0);

		holder.text.setText(mixListView.getDataSourceMenu().get(position));
		holder.description.setText(mixListView.getDataSourceDescription().get(position));

		int colorPos = position % bgcolors.length;
		convertView.setBackgroundColor(bgcolors[colorPos]);
		holder.text.setTextColor(textcolors[colorPos]);
		holder.description.setTextColor(descriptioncolors[colorPos]);

		return convertView;
	}

	public void changeColor(int index, int bgcolor, int textcolor){
		if (index < bgcolors.length) {
			bgcolors[index]=bgcolor;
			textcolors[index]= textcolor;
		}
		else
			Log.d("Color Error", "too large index");
	}

	public void colorSource(String source){
		for (int i = 0; i < bgcolors.length; i++) {
			bgcolors[i]=0;
			textcolors[i]=Color.WHITE;
		}
		
		if (source.equals("Wikipedia"))
			changeColor(0, Color.WHITE, Color.DKGRAY);
		else if (source.equals("Twitter"))
			changeColor(1, Color.WHITE, Color.DKGRAY);
		else if (source.equals("Buzz"))
			changeColor(2, Color.WHITE, Color.DKGRAY);
		else if (source.equals("OpenStreetMap"))
			changeColor(3, Color.WHITE, Color.DKGRAY);
		else if (source.equals("OwnURL"))
			changeColor(4, Color.WHITE, Color.DKGRAY);
	}

	public int getCount() {
		return mixListView.getDataSourceMenu().size();
	}

	public Object getItem(int position) {
		return this;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView text;
		TextView description;
		CheckBox checkbox;
		ImageView icon;
	}
}
