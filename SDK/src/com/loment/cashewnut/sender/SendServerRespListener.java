package com.loment.cashewnut.sender;

/**
 *
 * @author sekhar
 */
public interface SendServerRespListener {
    public void listenForResponse(String token, String response);
}
