/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.controller;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sthithi.connection.DataConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sekhar
 */
public class ValidatePasswordTask {

    String newpass = "";
    String primaryEmail = "";
    boolean isPrimary = false;

    public ValidatePasswordTask(String primaryEmail, String newpass, boolean isPrimary) {
        this.newpass = newpass;
        this.primaryEmail = primaryEmail;
        this.isPrimary = isPrimary;
    }

    public int onPostExecute(String res) {
        if (res != null && !res.trim().equals("")) {
            try {
                JSONObject responseJson = new JSONObject(res);
                Integer status = (Integer) responseJson.get("status");
                if (status == 0) {
                    if (isPrimary) {
                        try {
                            DBLomentDataMapper headerMapper = DBLomentDataMapper
                                    .getInstance();
                            LomentDataModel data = new LomentDataModel();
                            data.setLomentId(primaryEmail);
                            data.setPassword(newpass);
                            headerMapper.updateLomentAccountPassword(data);
                             return 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return 0;
                }
                if (status == 10203) {
                    //invalid pass
                    return 2;
                }
                if (status == 10202) {
                    //invalid user name
                    return 3;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String execute() {
        try {
            String url = AppConfiguration.getSthithiApi()+"/user/" + primaryEmail
                    + "/authenticate";
            String post = "password=" + newpass;
            DataConnection conn = DataConnection.getInstance();
            String response = conn.sendPost(url, post.getBytes());
            return response;
        } catch (Exception ex) {
        }
        return null;
    }
}
