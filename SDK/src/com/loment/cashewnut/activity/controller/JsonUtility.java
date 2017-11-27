/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.controller;

import java.util.ArrayList;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author sekhar
 */
public class JsonUtility {

    public static ArrayList<String> getCashewnutIdForThisDevice(String responseText) {
        ArrayList<String> deviseAccounts = new ArrayList<String>();
        try {
            JSONObject responseJson = new JSONObject(responseText);
            Integer status = (Integer) responseJson.get("status");
            int val = status.intValue();

            // System.out.println("account===" + responseJson);
            JSONArray jsoDeviceAccounts = (JSONArray) responseJson
                    .get("device_accounts");
            if (jsoDeviceAccounts.length() > 0) {
				// we have accounts for this device
                // parse json and create mailaccounts
                for (int i = 0; i < jsoDeviceAccounts.length(); i++) {
                    if (!jsoDeviceAccounts.isNull(i)) {
                        JSONObject account = jsoDeviceAccounts.getJSONObject(i);
                        deviseAccounts.add(account.getString("username"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviseAccounts;
    }

    public static ArrayList<String> getCashewnutIdForOtherDevice(String responseText) {
        ArrayList<String> otherAccounts = new ArrayList<String>();
        try {
            JSONObject responseJson = new JSONObject(responseText);

            Integer status = (Integer) responseJson.get("status");
            int val = status.intValue();

            // System.out.println("account===" + responseJson);
            JSONArray jsoOtherAccounts = (JSONArray) responseJson
                    .get("other_accounts");
            if (jsoOtherAccounts.length() > 0) {
				// we have accounts from other devices
                // parse json and create mailaccounts
                for (int i = 0; i < jsoOtherAccounts.length(); i++) {
                    if (!jsoOtherAccounts.isNull(i)) {
                        JSONObject account = jsoOtherAccounts.getJSONObject(i);
                        otherAccounts.add(account.getString("username"));
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return otherAccounts;
    }
}
