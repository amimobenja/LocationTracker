package com.amimobenja.locationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	LocationManager lm;
	LocationListener locationListener;
	String senderTel;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//---get the SMS message that was received---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str="";
		if (bundle != null) {
			senderTel = "";
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				if (i==0) {
					//---get the sender address/phone number---
					senderTel = msgs[i].getOriginatingAddress();
				}
				//---get the message body---
				str += msgs[i].getMessageBody().toString();
			}
			
			if (str.startsWith("Where are you?")) {
				//---use the LocationManager class to obtain locations data---
				lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
				
				//---request location updates---
				locationListener = new MyLocationListener();
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						60000,
						1000, 
						locationListener);
				
				//---abort the broadcast; SMS messages won't be broadcasted---
				this.abortBroadcast();
			}
		}
	}
	
	private class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				//---send a SMS containing the current location---
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(senderTel, null, "http://maps.google.com/maps?q=" + loc.getLatitude() + "," +
				loc.getLongitude(),	null, null);
				
				//---stop listening for location changes---
				lm.removeUpdates(locationListener);
			}
		}
		
		public void onProviderDisabled(String provider) {
			
		}
		
		public void onProviderEnabled(String provider) {
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	}

}
