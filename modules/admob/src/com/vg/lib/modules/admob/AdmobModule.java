package com.vg.lib.modules.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.vg.lib.module.Module;

/**
 * Provides support for admob ads. By default this module will search your
 * layout for com.vg.lib.modules.admob.R.id.adsWrapper and place the ads in there.
 * This can be overridden by loading the module with different arguments.
 * Note: admob does require permissions to be set in order to run. Consult
 * the admob docs.
 * @author vangorra
 *
 */
public class AdmobModule implements Module {
	/**
	 * Argument specifying the api key to use.
	 * Required for loading this module.
	 */
	public static final String KEY = "key";
	
	/**
	 * Argument specifying the size of the ad.
	 * Used in conjunction with AdmobModule.AdSizes
	 * Required for loading this module.
	 */
	public static final String AD_SIZE = "adSize";
	
	/**
	 * Argument specifying the layout id to place an ad in.
	 * The layout is expected to be a LinearLayout.
	 * This argument is optional, com.vg.lib.modules.admob.R.id.adsWrapper
	 * is the default.
	 */
	public static final String AD_RES_ID = "adResourceId";
	
	/**
	 * Defines the sizes to request from admob.
	 * @author vangorra
	 *
	 */
	public static final class AdSizes {
		public static final int BANNER = 0;
		public static final int IAB_BANNER = 1;
		public static final int IAB_LEADERBOARD = 2;
		public static final int IAB_MRECT = 3;
	}

	private String apiKey;
	private AdSize adSize;
	private int adsWrapperRes = R.id.adsWrapper;
	
	@Override
	public void load(Bundle args) {
		/*
		 * Configure the API key.
		 */
		// get the key from the args.
		apiKey = args.getString(KEY);
		
		// check for null
		if(apiKey == null) {
			throw new IllegalArgumentException("Ad key cannot be null.");
		}
		
		// check for empty string.
		if(apiKey.length() <= 0) {
			throw new IllegalArgumentException("Ad key cannot be an empty string.");
		}
		
		/**
		 * Configure the wrapper layout.
		 */
		// get the ad wrapper layout id.
		int resId = args.getInt(AD_RES_ID);
		
		// if something was sent, then use it.
		if(resId > 0) {
			adsWrapperRes = resId;
		}
		
		/**
		 * configure the ad size.
		 */
		// get the ad size from the args and check.
		switch(args.getInt(AD_SIZE)) {
			case AdSizes.BANNER:
				this.adSize = AdSize.BANNER;
				break;
				
			case AdSizes.IAB_BANNER:
				adSize = AdSize.BANNER;
				break;
				
			case AdSizes.IAB_LEADERBOARD:
				adSize = AdSize.BANNER;
				break;
				
			case AdSizes.IAB_MRECT:
				adSize = AdSize.BANNER;
				break;
				
			// a proper size was not specified.
			default:
				throw new IllegalArgumentException("Invalid size provided.");
		} // switch
	} // method

	@Override
	public void unLoad() {
		// nothing to do here.
	}

	public void onInvokeActivityOnPostCreate(Activity activity, Bundle savedInstanceState) {
		// query for the ad wrapper from the activity's layout.
		View adsWrapper = activity.findViewById(this.adsWrapperRes);
		
		// if a layout was found and it is suitable for containing the ads.
		if(adsWrapper != null && adsWrapper instanceof LinearLayout) {
			// create a new admob adview.
			AdView adView = new AdView(activity, this.adSize, this.apiKey);
			
			// ad the adview inside the wrapper.
			((LinearLayout)adsWrapper).addView(adView);
			
			// configure emulator support for ads.
			AdRequest ar = new AdRequest();
			ar.addTestDevice(AdRequest.TEST_EMULATOR);
			
			// load the ad
			adView.loadAd(ar);
		} // if
	} // method
} // class
