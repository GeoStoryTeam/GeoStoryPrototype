package com.trajestory.client;

import java.util.List;

import wu.events.WEvent;
import wu.events.WHandler;
import wu.geostory.GeoStory;
import wu.geostory.GeoStoryItem;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Trajestory implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GeoStoryServerAsync facade = GWT
	.create(GeoStoryServer.class);


	public void onModuleLoad() {
		/*
		 * Asynchronously loads the Maps API.
		 *
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from localhost.
		 */
		RootPanel root = RootPanel.get();
		root.setSize("100%", "100%");
		{
			VerticalPanel tabs = new VerticalPanel();
			tabs.setSize("100%", "100%");
			root.add(tabs);
			{
				final GeoStory geostory = new GeoStory();
				// Push event when an item is added
				geostory.getTypes().itemAdded.registerHandler(new WHandler<GeoStoryItem>(){
					@Override
					public void onEvent(WEvent<GeoStoryItem> elt) {
						GWT.log("Access to the server facade");
						GeoStoryItem item = elt.getElement();
						GWT.log(geostory.getEmail()+" email ");
						facade.pushItem(geostory.getEmail(), item.toJSON().toString(), new AsyncCallback<Boolean>(){
							@Override
							public void onFailure(Throwable caught) {
								GWT.log("Failure datastore attempt ");
								caught.printStackTrace();
							}
							@Override
							public void onSuccess(Boolean result) {
								GWT.log("Success Element successfully stored in datastore");
							}});
					}});
				geostory.getTypes().getEventFromServer.registerHandler(new WHandler<String>(){

					@Override
					public void onEvent(final WEvent<String> elt) {
						facade.pullItems(elt.getElement(), null, null, new AsyncCallback<List<String>>(){
							@Override
							public void onFailure(Throwable caught) {
								GWT.log("Pull item failure");
							}
							@Override
							public void onSuccess(List<String> result) {
								GWT.log("Pull success for "+ elt.getElement());
								for(String item : result){
									JSONValue json = JSONParser.parse(item);
									JSONObject object = json.isObject();
									GeoStoryItem gsi = GeoStoryItem.fromJSON(object);
									geostory.getTypes().itemFromServer.shareEvent(gsi);
									System.out.println(gsi);
								}
								geostory.getTypes().centerEvent.shareEvent(null);
							}});
					}});
				geostory.setSize("100%", "100%");
				tabs.add(geostory);	
			}
		}
	}
}
