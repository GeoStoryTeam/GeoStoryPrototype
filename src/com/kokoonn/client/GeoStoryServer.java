package com.kokoonn.client;

import java.util.Date;
import java.util.List;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import wu.geostory.GeoStoryItem;
import wu.geostory.Interval;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("geo")
public interface GeoStoryServer extends RemoteService {
	
	List<String> pullItems(String email, Date start, Date end);
	
	boolean pushItem(String email, String jsonItem);
}
