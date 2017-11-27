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
public class ChangePasswordTask {

    String newpass = "";
    String primaryEmail = "";

    public ChangePasswordTask(String newpass, String primaryEmail) {
        this.newpass = newpass;
        this.primaryEmail = primaryEmail;

    }

    public boolean onPostExecute(String res) {
        if (res != null && !res.trim().equals("")) {
            try {
                JSONObject responseJson = new JSONObject(res);
                Integer status = (Integer) responseJson.get("status");
                if (status == 0) {
                    try {
                        DBLomentDataMapper headerMapper = DBLomentDataMapper
                                .getInstance();

                        LomentDataModel data = new LomentDataModel();
                        data.setLomentId(primaryEmail);
                        data.setPassword(newpass);
                        headerMapper.updateLomentAccountPassword(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return status == 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String execute() {
        try {
            String post = "password=" + newpass;
            DataConnection conn = new DataConnection();
            String url = AppConfiguration.getSthithiApi()+"/user/"
                    + primaryEmail + "/password/change";
            String response = conn.sendPost(url, post.getBytes());
            return response;
        } catch (Exception ex) {
        }
        return null;
    }
}
