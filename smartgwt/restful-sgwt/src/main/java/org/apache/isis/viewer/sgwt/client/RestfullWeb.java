package org.apache.isis.viewer.sgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.smartgwt.client.types.DeviceMode;
import com.smartgwt.client.util.Browser;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.layout.SplitPane;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestfullWeb implements EntryPoint, HistoryListener {

	private final Messages messages = GWT.create(Messages.class);

    public static native boolean websiteModeSetOnPage() /*-{
        return $wnd.isc_websiteMode === true || ($wnd.isc.params.isc_websiteMode === "true");
    }-*/;
    public static boolean isc_websiteMode = websiteModeSetOnPage();
	
	private SplitPane splitPane;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
    	
        final boolean useDesktopMode = Browser.getIsDesktop();
        
        final String initToken = History.getToken();
        // If the request contains param "websiteMode", override the configured value
        {
            final String isc_websiteModeParam = com.google.gwt.user.client.Window.Location.getParameter("isc_websiteMode");
            if (isc_websiteModeParam != null) {
                isc_websiteMode = isc_websiteModeParam.isEmpty() || 
                    Boolean.parseBoolean(isc_websiteModeParam);
            }
        }
        isc_websiteMode = isc_websiteMode && useDesktopMode;

		SC.askforValue("Restful API URL", "Please enter URL of Restfull viewer: ", "http://localhost:8080/restful/api/", new ValueCallback() {

			@Override
			public void execute(String value) {
		        // setup overall layout
		        final VLayout main = new VLayout() {
		            {
		                setID("isc_Showcase_1_0");
		                setWidth100();
		                setHeight100();
		            }

		            @Override
		            protected void onInit() {
		                super.onInit();
		                if (initToken.length() != 0) {
		                    onHistoryChanged(initToken);
		                }
		            }
		        };
		        
		        splitPane = new SplitPane();
		        if (useDesktopMode) splitPane.setDeviceMode(DeviceMode.DESKTOP);

		        if (isc_websiteMode) {
		            splitPane.setShowResizeBar(true);
		            splitPane.setResizeBarTarget("next");
		        }

		        splitPane.setShowMiniNav(false);
		        splitPane.setWidth100();
		        splitPane.setHeight100();
		        splitPane.setAddHistoryEntries(false);

		        if (useDesktopMode) splitPane.setShowNavigationBar(false);
		        else splitPane.setNavigationTitle("Navigation Pane");
		        
		        VLayout sideNavLayout = new VLayout();
		        sideNavLayout.setHeight100();
		        sideNavLayout.setWidth(215);

		        splitPane.setNavigationPane(sideNavLayout);
		        
		        main.addMember(splitPane);
		        main.draw();
			}
		}, new Dialog());

        // Add history listener
        History.addHistoryListener(this);

	}

	@Override
	public void onHistoryChanged(String historyToken) {
		SC.say("Hello World from SmartGWT for Apache ISIS Restful Viewer");
	}
}
