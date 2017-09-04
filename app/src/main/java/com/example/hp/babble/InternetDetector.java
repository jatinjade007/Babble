package com.example.hp.babble;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetDetector {
	private Context mcontext;

	public InternetDetector(Context context) {
		this.mcontext = context;
	}

	public boolean checkMobileInternetConn() {
		ConnectivityManager connectivity = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
}
