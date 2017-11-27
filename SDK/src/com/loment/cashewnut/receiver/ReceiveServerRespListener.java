package com.loment.cashewnut.receiver;

/**
 *
 * @author sekhar
 */
public interface ReceiveServerRespListener {
    public void listenForResponse(String token, String response);
}
