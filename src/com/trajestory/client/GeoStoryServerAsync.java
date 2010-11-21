package com.trajestory.client;

import java.util.Date;
import java.util.List;


import com.google.gwt.user.client.rpc.AsyncCallback;
import wu.geostory.GeoStoryItem;
import wu.geostory.Interval;

/**
 * The async counterpart of <code>GeoStoryServer</code>.
 */
public interface GeoStoryServerAsync {

	void pullItems(String email, Date start, Date end,
			AsyncCallback<List<String>> callback);

	void pushItem(String email, String jsonItem,
			AsyncCallback<Boolean> callback);
}
