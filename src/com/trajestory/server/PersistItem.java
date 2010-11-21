package com.trajestory.server;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gwt.maps.client.geom.LatLng;
import wu.geostory.GeoStoryItem;
import wu.geostory.Interval;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "false")
public class PersistItem implements Serializable{

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String email;

	@Persistent
	private String locationName;

	@Persistent
	private Date start;

	@Persistent
	private Date end;

	@Persistent
	private GeoPt place;

	@Persistent
	private String description;

	public PersistItem(String item, String email){
		try{
			JSONObject json = new JSONObject(item);
			this.description = json.getString("description");
			this.locationName = json.getString("place");
			Long a = json.getLong("begin");
			this.start = new Date(a);
			Long b = json.getLong("end");
			this.end = new Date(b);
			this.email = email;
			float lat =  (float) json.getDouble("latitude");
			float lng =  (float) json.getDouble("longitude");
			GeoPt point = new GeoPt(lat,lng); 
			this.place = point;  
		}catch(Throwable t){
			throw new IllegalArgumentException();
		}
	}


	public JSONObject toJSON() {
		try{
			JSONObject result = new JSONObject();
			result.put("description", this.description);
			result.put("place", this.locationName);
			result.put("begin", this.start.getTime());
			result.put("end", this.end.getTime());
			result.put("latitude",this.place.getLatitude());
			result.put("longitude", this.place.getLongitude());
			return result;
		}catch(Throwable t){
			t.printStackTrace();
			return null;
		}
	}

}
