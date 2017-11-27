package com.loment.cashewnut.jsonSenderModel;

import java.util.LinkedHashMap;
import java.util.Vector;

import org.json.simple.JSONValue;


import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.util.Helper;

/**
 * 
 * @author sekhar
 */
public class Body {
	private static String MESSAGE_ID = "msg_id";
	private static String MESSAGE_BODY_DATA = "data";
	private static String TOKEN = "token";

	public String getMessageBody(MessageModel messageModel, String token) {
		String jsonText = "";
		try {
			HeaderModel headModel = messageModel.getHeaderModel();
			
			LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<String, String>();
			jsonOrderedMap.put(TOKEN, token);
			jsonOrderedMap.put(MESSAGE_ID, headModel.getServerMessageId()+"");
			jsonOrderedMap.put(MESSAGE_BODY_DATA, getData(headModel)); 
			jsonText = JSONValue.toJSONString(jsonOrderedMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonText = Helper.replace(jsonText,",", ",\r\n");
		jsonText = Helper.replace(jsonText,"{", "{\r\n");
		jsonText = Helper.replace(jsonText,"}", "}\r\n");
		return jsonText + "\r\n";
	}
	
	private String getData(HeaderModel headModel) {
		try {
			String body = (new BodyStore()).getBody(headModel.getLocalHeaderId());
			return body;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
