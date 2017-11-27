package com.loment.cashewnut;


import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;

public abstract class CashewnutActivity extends Application {

    public static long idleTime;
    public static boolean isHibernate = false;
    public static boolean close = false;
    public static boolean onPauseHibernate = false;
    private static boolean log = false;
    public static Application currentActivity;
    public static final String HEADER_VERSION = "V1"; // "V1";
    public static final String HEADER_VERSION_V2 = "V2";
    protected static final int OTHER_ID = 101;
    public static Map cache = new HashMap();
    public static Map group_cache=new HashMap();
    public static Map group_cache1=new HashMap();
    public static int colourIndex = 1;
    public static boolean isListUpdate = false;
    public static boolean recentEmoji=false;
    public static boolean isNetworkConnectionOn() {
        return CashewnutApplication.isInternetOn();
    }

    public void processMessage(int status) {
        if (log) {
            System.out.println("Activity " + 31);
        }
    }

    public void processMessage(final Object object, int type) {
        // fire to list
    }
	public  void checkHibernate(long time) {
		
	}	
	public  void setHibernate() {
		
	}
	
	
    public void onLocaleChange() {
    	// fire to list
    }
}
