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

	private Context thisContext;
	private Intent ioioService;
	private Intent broadcastIntent = new Intent("msgIOIO");

	private String MotionStatus ="";
	private CallbackContext connectionCallbackContext; // for callback startup IOIO
	private CallbackContext connectionCallbackMotion; // for callback Detect Motion sensor
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		if (action.equals("startservice")) {
			System.out.println("startup IOIO service");
			// Setup a method to receive messages broadcast from the IOIO
        		LocalBroadcastManager.getInstance(thisContext).registerReceiver(
                		mMessageReceiver, 
                		new IntentFilter("returnIOIOdata")
        		); 
			callbackContext.success("showStatu");
            		return true;
        	}
        	else if (action.equals("readStatus")) {
        		callbackContext.success("MotionStatus : "+MotionStatus);
        	}
        return false;
	}
	
	// Receive message from the IOIO device
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		if(intent.getStringExtra("MotionStatus")!= null){
    			MotionStatus = intent.getStringExtra("MotionStatus");
    		}
    	}
    };
    
    // Send a message to IOIO service
    private void ioioSendMessage(String command,String dulation,String interval, CallbackContext callbackContext){
    	// Which vars to send  
    	broadcastIntent.putExtra("command", command);
    	broadcastIntent.putExtra("dulation", dulation);
    	broadcastIntent.putExtra("interval", interval);
    	// Send the message to the IOIO
        LocalBroadcastManager.getInstance(thisContext).sendBroadcast(broadcastIntent);
    }
	
	

	


}
