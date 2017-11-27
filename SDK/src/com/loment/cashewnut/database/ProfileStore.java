package com.loment.cashewnut.database;

import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.LomentDataModel;

/**
 *
 * @author sekhar
 */
public class ProfileStore {

    private static ProfileStore instance;
    private String lomentId = "";
    private String password = "";
    private String cashewId = "";
    String deviceId = "";

    private ProfileStore() {
    	getProfileData();
    }

    public static ProfileStore getInstance() {
        if (instance == null) {
            try {
                instance = new ProfileStore();
            } catch (Exception ex) {
            }
        }
        return instance;
    }

    public String getLomentId() {
        if (lomentId == null || lomentId.trim().equals("")) {
        	getProfileData();
        }
        return lomentId;
    }

    public String getPassword() {
        if (password == null || password.trim().equals("")) {
        	getProfileData();
        }
        return password;
    }

    public String getCashewId() {
        if (cashewId == null || cashewId.trim().equals("")) {
            setCashewId();
        }
        return cashewId;
    }

    public String getDeviceId() {
        if (deviceId == null || deviceId.trim().equals("")) {
        	getProfileData();
        }
        return deviceId;
    }

    public LomentDataModel getProfileData() {
        try {
            DBLomentDataMapper lomentDataMapper = DBLomentDataMapper.getInstance();
            LomentDataModel lomentDataModel = lomentDataMapper.getLomentData();

            deviceId = lomentDataModel.getDeviceId();
            password = lomentDataModel.getPassword();
            lomentId = lomentDataModel.getLomentId();
            
            return lomentDataModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private void setCashewId() {
        try {
            DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            AccountsModel accountsModel = accountsMapper.getAccount();
            cashewId = accountsModel.getCashewnutId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
