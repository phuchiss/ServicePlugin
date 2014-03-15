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
public class PIRPlugin extends CordovaPlugin{

	private Context thisContext;
	private Intent ioioService;
	private String interval="";
	private String dulation="";
	public int PIRDetect =2;
	private CallbackContext connectionCallbackContext; // for callback startup IOIO
	private CallbackContext connectionCallbackMotion; // for callback Detect Motion sensor
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
	
		if (action.equals("startservice")) {
			System.out.println("startup IOIO service");
		//	runservice();
			callbackContext.success(showStatus());
            		return true;
        	}
	
        return false;
	}
	
	public String showStatus() {
		      // Show all the birthdays sorted by friend's name
		      String URL = "content://com.example.pircontentprovider.PIRProvider/Motion";
		      Uri motion = Uri.parse(URL);
		      Cursor c = getContentResolver().query(motion, null, null, null, "");
		      String result = "Status :";
		      if (!c.moveToFirst()) {
		      }else{
		    	  do{
		            result = result + "\n" + c.getString(c.getColumnIndex("id")) + 
		    	            " with id " +  c.getString(c.getColumnIndex("status"));
		          } while (c.moveToNext());
		      }
		      return result;
	 
	}

	


}
