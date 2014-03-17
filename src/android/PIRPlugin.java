package com.ecmxpert.pircontentprovider;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.database.Cursor;
import android.net.Uri;
import android.app.Service;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class PIRPlugin extends CordovaPlugin{

private static final String LOG_TAG = "BatteryManager";

    BroadcastReceiver receiver;

    private CallbackContext batteryCallbackContext = null;
    private Intent broadcastIntent = new Intent("msgIOIO");	
    /**
     * Constructor.
     */
    /**
     * Executes the request.
     *
     * @param action        	The action to execute.
     * @param args          	JSONArry of arguments for the plugin.
     * @param callbackContext 	The callback context used when calling back into JavaScript.
     * @return              	True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        
        if (action.equals("startservice")) {
			System.out.println("startup IOIO service");
			// Setup a method to receive messages broadcast from the IOIO
        	//	LocalBroadcastManager.getInstance(thisContext).registerReceiver(
                //		mMessageReceiver, 
                //		new IntentFilter("returnIOIOdata")
        	//	); 
			callbackContext.success("showStatu");
            		return true;
        }else if (action.equals("readStatus")) {
        //    if (this.batteryCallbackContext != null) {
        //        callbackContext.error( "Battery listener already running.");
         //       return true;
         //   }
            this.batteryCallbackContext = callbackContext;

            // Don't return any result now, since status results will be sent when events come in from broadcast receiver
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                	int count =0;
                	while(true){
                		try{
                			Thread.sleep(2000);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		if(count <= 5){
                			sendUpdate("test", true);
                			count++;
                		}else{
                			sendUpdate("test", false);
                			count =0;
                		}
                		
        			
                	}
                }
            });
            return true;
        }

        return false;
    }

    
    

    /**
     * Create a new plugin result and send it back to JavaScript
     *
     * @param connection the network info to set as navigator.connection
     */
    private void sendUpdate(String message, boolean keepCallback) {
        if (this.batteryCallbackContext != null) {
        	System.out.println("sendupdate");
            PluginResult result = new PluginResult(PluginResult.Status.OK, message);
            result.setKeepCallback(keepCallback);
            this.batteryCallbackContext.sendPluginResult(result);
        }
    }
}
