package se.miun.nowandthen.data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.miun.nowandthen.Marker;
import se.miun.nowandthen.MixView;
import se.miun.nowandthen.SiteMarker;
import se.miun.nowandthen.reality.PhysicalPlace;

import android.util.Log;

public class KSSXMLHandler extends DataHandler {
	

	private List<Marker> processKSS(Element root) {

    	List<Marker> markers = new ArrayList<Marker>();
        NodeList nodes = root.getElementsByTagName("record");
        
        for(int recIdx = 0; recIdx < nodes.getLength(); recIdx++) {
        	
        	Element nodeElem = ((Element)nodes.item(recIdx));
        	NodeList itemLables = nodeElem.getElementsByTagNameNS("*", "itemLabel");
            NodeList itemDescrips = nodeElem.getElementsByTagNameNS("*", "description");
            NodeList itemCoords = nodeElem.getElementsByTagNameNS("*", "coordinates");
            
        	String name = "";
        	if (itemLables.getLength() > 0)
        		name = itemLables.item(0).getChildNodes().item(0).getNodeValue().toString();
        	
        	String description = "";
        	if (itemDescrips.getLength() > 0)
        		description = itemDescrips.item(0).getChildNodes().item(0).getNodeValue().toString();
        	
        	double lat = Double.parseDouble(itemCoords.item(0).getChildNodes().item(0).getNodeValue().toString().split(",")[1]);
        	double lon = Double.parseDouble(itemCoords.item(0).getChildNodes().item(0).getNodeValue().toString().split(",")[0]);
        	
        	Marker ma = new SiteMarker(
    				name, 
    				lat, 
    				lon,
    				description, 
    				"");

    			markers.add(ma);
        }
        
        
        
        
        
        /*for (int i =0; i< nodes.getLength(); i++) {
        	Node nodeRDF = nodes.item(i).getChildNodes().item(0);
        	NamedNodeMap att = nodeRDF.getAttributes();
        	NodeList nodeDescList = nodeRDF.getChildNodes();
        	for(int j=0;j<nodeDescList.getLength();j++) {
        		Element nodeDesc = (Element)nodeDescList.item(j);
        		if(nodeDesc.getNodeType() == Node.TEXT_NODE) continue;
	        	
        		
        		// Ignore those without itemLabel
        		if ()
    			Node n = ((Element)nodeDesc).getElementsByTagNameNS("ns5", "itemLabel").item(0);
    			if (n != null) {
        			String name = n.getNodeValue();
        			String[] coordinates = ((Element)nodeDesc).getElementsByTagNameNS("gml", "coordinates").item(0).getNodeValue().toString().split(",");
                	double lat = Double.valueOf(coordinates[0]);
                	double lon = Double.valueOf(coordinates[1]);
        			
                	Log.v(MixView.TAG,"KSS Node: "+name+" lat "+lat+" lon "+lon+"\n");

                	// This check will be done inside the createMarker method 
                	//if(markers.size()<MAX_OBJECTS)
                	
                	Marker ma = new SiteMarker(
        				name, 
        				lat, 
        				lon, 
        				"http://www.openstreetmap.org/?node="+att.getNamedItem("id").getNodeValue(), 
        				DataSource.DATASOURCE.TEST);
        			markers.add(ma);

//	                	//skip to next node
//	        			continue;
        		}
        	}
        }*/
        return markers;
	}
	
	public static String getOSMBoundingBox(double lat, double lon, double radius) {
		String bbox = "[bbox=";
		PhysicalPlace lb = new PhysicalPlace(); // left bottom
		PhysicalPlace rt = new PhysicalPlace(); // right top
		PhysicalPlace.calcDestination(lat, lon, 225, radius*1414, lb); // 1414: sqrt(2)*1000
		PhysicalPlace.calcDestination(lat, lon, 45, radius*1414, rt);
		bbox+=lb.getLongitude()+","+lb.getLatitude()+","+rt.getLongitude()+","+rt.getLatitude()+"]";
		return bbox;

		//return "[bbox=16.365,48.193,16.374,48.199]";
	}
	
	public List<Marker> load(Document doc) {
        Element root = doc.getDocumentElement();
        
        // If the root tag is called "osm" we got an 
        // openstreetmap .osm xml document
        if ("result".equals(root.getTagName()))
        	return processKSS(root);
        return null;
	}

}
