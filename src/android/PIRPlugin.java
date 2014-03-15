package com.ecmxpert.pircontentprovider;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;




import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PIRPlugin extends CordovaPlugin{

	private Context thisContext;
	private Intent ioioService;
	private String interval="";
	private String dulation="";
	public int PIRDetect =2;
	private CallbackContext connectionCallbackContext; // for callback startup IOIO
	private CallbackContext connectionCallbackMotion; // for callback Detect Motion sensor
	final Intent service = new Intent(this,MyService.class);
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		if (action.equals("startservice")) {
			System.out.println("startup IOIO service");
			startService(service);
			callbackContext.success("data");
            return true;
        }
	
        return false;
	}

}
