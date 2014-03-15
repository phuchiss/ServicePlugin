package com.ecmxpert.pircontentprovider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;


public class MyService extends Service {
	boolean RunTask = true;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		ReadContentProvider readContent = new ReadContentProvider();
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
					R.drawable.ic_launcher, "service running",
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
		
		
	    return Service.START_NOT_STICKY;
	  }

	
	 
	
	private class ReadContentProvider extends AsyncTask<Void, Void, Void> {


		public ReadContentProvider() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			
			while(RunTask){
				
				try{
					Thread.sleep(3000);
				}catch(Exception ex){
					
				}
				System.out.println(showStatus());
			}
			return null;
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
} 
