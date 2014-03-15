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
	private Intent broadcastIntent = new Intent("msgIOIO");
	private String interval="";
	private String dulation="";
	public int PIRDetect =2;
	private CallbackContext connectionCallbackContext; // for callback startup IOIO
	private CallbackContext connectionCallbackMotion; // for callback Detect Motion sensor
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		if (action.equals("startservice")) {
		//	WriteFile write = new WriteFile();
            	//	String data = write.writeToFile("Hello world");
			System.out.println("startup IOIO service");
			startService(new Intent(this,MyService.class));
			finish();
			callbackContext.success("data");
            	//	this.ioioStartup(callbackContext); 
            return true;
        }
		if (action.equals("ioiogetdata")) {
            this.ioioGetdata(callbackContext); 
            return true;
        }
        if (action.equals("ioioSendMessage")) {
            this.ioioSendMessage(args.getString(0),args.getString(1),args.getString(2) ,callbackContext); 
            return true;
        }        
        return false;
	}

    // Initialise IOIO service (Called from Javascript)
    private void ioioStartup(CallbackContext callbackContext) {
    	// Initialise the service variables and start it it up
    	try{
    	thisContext = this.cordova.getActivity().getApplicationContext();
    	ioioService = new Intent(thisContext, PIRProvider.class);
        thisContext.startService(ioioService); 
        System.out.println("start service");
        
        callbackContext.success("status up");
        
       			                	 	
        }catch(Exception e){
        	callbackContext.success("status error :"+e.toString());
        }

    	
    }
    
    private void ioioGetdata(CallbackContext callbackContext) {
    	String message = String.valueOf(PIRDetect);
    //	System.out.println("PIR Detect :"+message);
    	

    	if (message != null && message.length() > 0) { 
            //callbackContext.success(message);
            
            this.connectionCallbackMotion = callbackContext;
    		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        	pluginResult.setKeepCallback(true);
        	callbackContext.sendPluginResult(pluginResult);
        	cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                	while(true){
                		PluginResult result = new PluginResult(PluginResult.Status.OK, String.valueOf(PIRDetect));
                    	result.setKeepCallback(true);
                    	connectionCallbackMotion.sendPluginResult(result);
                		try{
                			Thread.sleep(300);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                	}
                }
            });
        	//return true;
        } else {
            callbackContext.error("IOIO.java Expected one non-empty string argument.");
        }
    }
    

    

}
