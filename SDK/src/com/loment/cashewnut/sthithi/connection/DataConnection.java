/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.sthithi.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author bharath
 */
public class DataConnection {

	// private static final String BASE_URL =
    // "https://api-sthithi.loment.net/";//PResourceHandler.get("string_dataconnection_baseurl");
    private static DataConnection instance = null;
    DigestAuthHandler dah = new DigestAuthHandler("info@loment.net",
            "c85e22e3fa2ba99c425db4bef6cb1a21431ab6f6");

    public static DataConnection getInstance() throws IOException {
        if (instance == null) {
            instance = new DataConnection();
        }
        return instance;
    }

    public String sendGetRequest(String request_url, String query)
            throws Exception {
        InputStream is = null;
        HttpConnection conn = null;
        String response = null;
        String requestMethod = HttpConnection.GET;
        boolean reconnect = true;
        while (reconnect) {
            try {

                conn = (HttpConnection) Connector.open(request_url);

                conn.setRequestMethod(requestMethod);
                dah.prepareHeaders(conn,
                        request_url.substring(request_url.indexOf('/', 8)));
                reconnect = dah.processHeaders(conn);
                is = conn.openInputStream();
                int responseLength = (int) conn.getLength();
                if (responseLength != -1) {
                    byte incomingData[] = new byte[responseLength];
                    is.read(incomingData);
                    response = new String(incomingData);
                } else {
                    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = is.read()) != -1) {
                        bytestream.write(ch);
                    }
                    response = new String(bytestream.toByteArray());
                    bytestream.close();
                }
                is.close();
                conn.close();

            } catch (Exception exception) {

            }
        }

        return response;
    }

    public HttpConnection getHttpConnection(String request_url)
            throws Exception {

        HttpConnection connection = null;
        String requestMethod = HttpConnection.POST;
        try {

            connection = (HttpConnection) Connector.open(request_url,
                    Connector.READ_WRITE);

            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("User-Agent",
                    "Profile/MIDP-2.1 Configuration/CLDC-1.0");
            connection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            connection
                    .setRequestProperty("Accept",
                            "application/json,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            connection.setRequestProperty("Accept-Charset",
                    "UTF-8;q=0.7,*;q=0.7");

            connection.setRequestProperty("Pragma", "no-cache");
            connection.setRequestProperty("Cache-Control", "no-cache");

        } catch (Exception exception) {

        }
        return connection;
    }

    public String sendPost(String request_url, byte[] data) throws Exception {
        HttpConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        String responseData = "";
        boolean reconnect = true;
        while (reconnect) {
            try {
                connection = getHttpConnection(request_url);
                dah.prepareHeaders(connection,
                        request_url.substring(request_url.indexOf('/', 8)));
                connection.setRequestProperty("Content-Length", data.length
                        + "");
                if (connection != null) {
                    os = connection.openOutputStream();
                    // user_id, key and Data are sent by Peanut to STHITHI
                    os.write(data);
                    os.flush();
                    reconnect = dah.processHeaders(connection);

                    int responseCode = connection.getResponseCode();
					// Response
                    // if (responseCode == HttpConnection.HTTP_OK) {
                    is = connection.openInputStream();
                    int responseLength = (int) connection.getLength();
                    if (responseLength != -1) {
                        byte[] incomingData = new byte[responseLength];
                        is.read(incomingData);
                        responseData = new String(incomingData);
                    } else {
                        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                        int ch;
                        while ((ch = is.read()) != -1) {
                            bytestream.write(ch);
                        }
                        responseData = new String(bytestream.toByteArray());
                        bytestream.close();
                    }
                } else {
                    responseData = "";
                }
            } catch (Exception exception) {

            } finally {
                if (connection != null) {
                    connection.close();
                }
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
        return responseData;
    }

    public String sendPost1(String request_url, byte[] data) throws Exception {
        HttpConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        String responseData = "";
        boolean reconnect = true;
        while (reconnect) {
            try {
                connection = getHttpConnection(request_url);
                dah.prepareHeaders(connection,
                        request_url.substring(request_url.indexOf('/', 8)));
                connection.setRequestProperty("Content-Length", data.length
                        + "");
                if (connection != null) {
                    os = connection.openOutputStream();
                    // user_id, key and Data are sent by Peanut to STHITHI
                    os.write(data);
                    os.flush();
                    reconnect = dah.processHeaders(connection);

                    //int responseCode = connection.getResponseCode();

                    is = connection.openInputStream();
                    int responseLength = (int) connection.getLength();
                    if (responseLength != -1) {
                        byte[] incomingData = new byte[responseLength];
                        is.read(incomingData);
                        responseData = new String(incomingData);
                    } else {
                        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                        int ch;
                        while ((ch = is.read()) != -1) {
                            bytestream.write(ch);
                        }
                        responseData = new String(bytestream.toByteArray());
                        bytestream.close();
                    }
                } else {
                    responseData = "";
                }
            } catch (Exception exception) {
                throw exception;
            } finally {
                if (connection != null) {
                    connection.close();
                }
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
        return responseData;
    }

    public HttpConnection getHttpConnection1(String request_url)
            throws Exception {

        HttpConnection connection = null;
        String requestMethod = HttpConnection.POST;
        try {

            connection = (HttpConnection) Connector.open(request_url,
                    Connector.READ_WRITE);
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("User-Agent",
                    "Profile/MIDP-2.1 Configuration/CLDC-1.0");
            connection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            connection
                    .setRequestProperty(
                            "Accept",
                            "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            connection.setRequestProperty("Accept-Charset",
                    "UTF-8;q=0.7,*;q=0.7");

            connection.setRequestProperty("Pragma", "no-cache");
            connection.setRequestProperty("Cache-Control", "no-cache");

        } catch (Exception exception) {
            throw exception;
        }
        return connection;
    }

}
