/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author sekhar
 */
public class NetworkConnection extends Service<Boolean> {
    @Override
    protected Task createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                return isInternetReachable();
            }
        };
    }

    public boolean isInternetReachable() throws IOException {
        try {
            //make a URL to a known source
            URL url = new URL("https://www.google.com");

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();
            //System.out.println("SUCCESSFUL INTERNET CONNECTION");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block

            //System.out.println("CONNECTION FAILED");
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //System.out.println("CONNECTION FAILED");
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println("CONNECTION FAILED");
            return false;
        }
        return true;
    }
}
