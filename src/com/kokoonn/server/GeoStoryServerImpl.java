package com.kokoonn.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import com.kokoonn.client.GeoStoryServer;
import wu.geostory.Interval;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GeoStoryServerImpl extends RemoteServiceServlet implements
GeoStoryServer {

	public List<String> pullItems(String email, Date start, Date end){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(PersistItem.class);
	    query.setFilter("email == emailParam");
	    //query.setFilter("end > startParam");
	    // TODO query.setFilter("upperRight > lowLeftParam");
	    query.declareParameters("String emailParam");
	    //query.declareParameters("Date startParam");
	    
	    List<String> result = new ArrayList<String>();
	    try {
	        List<PersistItem> results = (List<PersistItem>) query.execute(email);
	        if (results.iterator().hasNext()) {
	        	// convert the result into GeoStoryItem
	            for (PersistItem e : results) {
	               result.add(convert(e).toString());
	            }
	        } else {
	            // ... no results ...
	        }
	    } finally {
	        query.closeAll();
	    }
		return result;
	}

	public boolean pushItem(String email, String jsonItem){
		System.out.println("SERVER starts pushing item "+jsonItem);
		PersistItem persist = convert(jsonItem, email);
		
		System.out.println("SERVER storing persistent one "+persist);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		System.out.println("SERVER recovered Persistence Manager "+pm);
        
        try {
            pm.makePersistent(persist);
        } catch (Throwable t){
        	System.out.println("--------------------------------------");
        	t.printStackTrace();
        	System.out.println("--------------------------------------");
        } finally {
            pm.close();
        }
        System.out.println("SERVER ends pushing items");
        
		return true;
	}

	JSONObject convert(PersistItem item){
		return item.toJSON(); //TODO
	}

	PersistItem convert(String item, String email){
		return new PersistItem(item,email); //TODO	
	}

}
