package se.miun.nowandthen;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView.SavedState;

public class ObjectInfo extends Activity {
	
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		
		savedInstance = getIntent().getExtras();
				
		String title = (String)savedInstance.getString("title");
		if(title == null)
			title = "Ingen titel.";
		
		String position = (String)savedInstance.getString("location");
		if(position == null)
			position = "Ingen position.";
		
		String beskrivning = (String)savedInstance.getString("description");
		if(beskrivning == null)
			beskrivning = "Ingen beskrivning.";
		
		String html = "<html>" +
					  "	<body>" +
					  "		<h1>"+title+"</h1>" +
					  "		<h2>Position</h2>" +
					  "		<p>"+position+"</p>" +
					  "		<h2>Beskrivning</h2>" +
					  "		<p>"+beskrivning+"</p>" +
					  "	</body>" +
					  "</html>";
		
		WebView webview = new WebView(this);
		setContentView(webview);
		
		//webview.loadData(html, "text/html", "utf-8");
		webview.loadDataWithBaseURL("http://www.sjoviks-sbh.se/", html, "text/html", "utf-8", null);
		
	}

}
