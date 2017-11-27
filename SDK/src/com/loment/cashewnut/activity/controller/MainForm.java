package com.loment.cashewnut.activity.controller;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.activity.view.javafx.LoginFormJfx;
import com.loment.cashewnut.activity.view.javafx.RegisterFormJfx;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.util.Helper;

import javafx.application.Application;

public class MainForm {
	private static boolean lockInstance(final String lockFile) {
	    try {
	      	String test=Helper.getCashewnutFolderPath("");
	        final File file = new File(test+lockFile);
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
	        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
	        if (fileLock != null) {
	            Runtime.getRuntime().addShutdownHook(new Thread() {
	                public void run() {
	                    try {
	                        fileLock.release();
	                        randomAccessFile.close();
	                        file.delete();
	                    } catch (Exception e) {
	                     e.getStackTrace();
	                    }
	                }
	            });
	            return true;
	        }
	    } catch (Exception e) {
	    	 e.getStackTrace();
	        //log.error("Unable to create and/or lock file: " + lockFile, e);
	    }
	    return false;
	}

    public static void main(String[] args) {
        onCreate();
    }
    
    
    
    // Splash screen timer
    protected static void onCreate() {
        // sortJsonArray();
    	System.setProperty("file.encoding","UTF-8");
    	Field charset;
		try {
			charset = Charset.class.getDeclaredField("defaultCharset");
		 	charset.setAccessible(true);
	    	charset.set(null,null);
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   
    	if(!lockInstance("test"))
    	{
    		System.exit(0);
    	}
    	
        LomentDataModel lomentData = null;
        AccountsModel accountData = null;
        SettingsModel settingsModel =  null;
        try {
            DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
            lomentData = headerMapper.getLomentData();

            DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            accountData = accountsMapper.getAccount();
            
            settingsModel = new SettingsStore().getSettingsData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This method will be executed once the timer is over
        // Start your app main activity
        if (lomentData != null && lomentData.getLomentId() != null
                && lomentData.getPassword() != null) {
        	
        	if (settingsModel != null) {
        		AppConfiguration.onLocaleChange(settingsModel.getLanguage());
        	}

            SettingsModel model = new SettingsStore().getSettingsData();
            
            if (model != null && model.getRememberStatus() > 0) {
                if (accountData.getCashewnutId() != null
                        && !accountData.getCashewnutId().trim().equals("")) {
                    // ConversationListActivity
                     Application.launch(ConversationView.class, "");                 
                } else {
                    // LoginActivity
                    Application.launch(LoginFormJfx.class, "");
                }
            } else {
                // LoginActivity
              Application.launch(LoginFormJfx.class, "");             
            }
        } else {
            Application.launch(RegisterFormJfx.class, "");
        }
    }
    
   
}
