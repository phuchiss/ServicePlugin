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
	private CallbackContext connectionCallbackMotion = null; // for callback Detect Motion sensor
	
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
        		//callbackContext.success("MotionStatus : "+MotionStatus);
        		
        		if (connectionCallbackMotion != null) {
                		callbackContext.error( "motion service already running.");
                		return true;
            		}
            		connectionCallbackMotion = callbackContext;
        		
        		PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            		pluginResult.setKeepCallback(false);
            		callbackContext.sendPluginResult(pluginResult);
            		ioioGetdata();
            		return true;
        		
        	}
        return false;
	}
	
	private void ioioGetdata() {
    	final String message = String.valueOf(MotionStatus);
    //	System.out.println("PIR Detect :"+message);
    	

    	if (message != null && message.length() > 0) { 
            //callbackContext.success(message);

    	//	PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        //	pluginResult.setKeepCallback(true);
        //	callbackContext.sendPluginResult(pluginResult);
        	cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                	while(true){
                		//PluginResult result = new PluginResult(PluginResult.Status.OK, String.valueOf(message));
                    	//result.setKeepCallback(true);
                    //	connectionCallbackMotion.sendPluginResult(result);
                    		if (connectionCallbackMotion != null) {
            				PluginResult result = new PluginResult(PluginResult.Status.OK, message);
            				result.setKeepCallback(true);
            				connectionCallbackMotion.sendPluginResult(result);
        			}
                		try{
                			Thread.sleep(1000);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                	}
                }
            });
            
        	//return true;
        } else {
            
        }
    }
	
	// Receive message from the IOIO device
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		System.out.println("receiver message :"+intent.getStringExtra("MotionStatus"));
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
