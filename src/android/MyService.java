package com.ecmxpert.pircontentprovider;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.IBinder;

import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;

public class MyService extends Service {
	boolean RunTask = true;
	private Intent broadcastIntent = new Intent("returnIOIOdata");
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
	    try{
	    	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.example.pirmotionex");
            	startActivity(LaunchIntent);
	    }catch(Exception ex){
	    	System.out.println("error :"+ex.toString());
	    }
	    	
		ReadContentProvider readContent = new ReadContentProvider(this);
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (intent != null && intent.getAction() != null
				&& intent.getAction().equals("stop")) {
			// User clicked the notification. Need to stop the service.
			RunTask = false;
			nm.cancel(0);
			stopSelf();
		} else {
			// Service starting. Create a notification.
			Notification notification = new Notification(
					0x7f020000, "service running",
					System.currentTimeMillis());
			notification
					.setLatestEventInfo(this, "Service", "Click to stop",
							PendingIntent.getService(this, 0, new Intent(
									"stop", null, this, this.getClass()), 0));
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			nm.notify(0, notification);
			RunTask = true;
		}
		
		readContent.execute();
		broadcastVars("test");
		
	    return Service.START_NOT_STICKY;
	  }

	// Broadcast a message to the IOIO plugin
	private void broadcastVars(String status) {
		// write var to send
		System.out.println("broadcastVars : "+status);
		broadcastIntent.putExtra("MotionStatus", status);
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

	}
	 
	
	private class ReadContentProvider extends AsyncTask<Void, Void, Void> {
		
		private  Context context;

		public ReadContentProvider(Context data) {
			this.context = data;
		}
		

		@Override
		protected Void doInBackground(Void... params) {
			
			while(RunTask){
				
				try{
					Thread.sleep(300);
				}catch(Exception ex){
				
				}
				String status = showStatus();
				System.out.println(status);
				broadcastVars(status);
			}
			return null;
		}

		public String showStatus() {
		      // Show all the birthdays sorted by friend's name
		      String result = "";
		      try{
		      	String URL = "content://com.ecmxpert.pircontentprovider.PIRProvider/Motion";
		      	Uri motion = Uri.parse(URL);
		      	Cursor c = getContentResolver().query(motion, null, null, null, "");
		      
		      	if (!c.moveToFirst()) {
		      	}else{
		    	  	do{
		            	result =c.getString(c.getColumnIndex("status"));
		          	} while (c.moveToNext());
		      	}		      	
		      }catch(Exception ex){
		      	result = "error :" +ex.toString();
		      }

		      return result;
	 
		   }


		// Broadcast a message to the IOIO plugin
		private void broadcastVars(String status) {
			// write var to send
			System.out.println("broadcastVars : "+status);
			broadcastIntent.putExtra("MotionStatus", status);
			LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

		}
		
		// Receive message from the phonegap plugin
		private BroadcastReceiver mIOIOReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// Received a message

			}
		};

	}
} 

