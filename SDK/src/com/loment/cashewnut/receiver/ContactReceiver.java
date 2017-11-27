package com.loment.cashewnut.receiver;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.HeaderModel;
public class ContactReceiver implements ReceiveServerRespListener {

	private HashMap<String, String> tokenMapper = new HashMap<String, String>();
	private String cashewnutId = "";
	public static String RECEIVED_HEADER_TOKEN_AMQP = "cashew_header";
	public static String RECEIVED_CONTACT_HEADER_TOKEN_AMQP="contact_header";
	long lastMessageTime = 0;
	int size = 0;

	public ContactReceiver() {
		try {
			Receiver.getInstance().addReceiverListener(this);
			AccountsModel accountsModel = DBAccountsMapper.getInstance()
					.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void listenForResponse(String token, String response) {
		// TODO Auto-generated method stub
		if (token.equals(RECEIVED_CONTACT_HEADER_TOKEN_AMQP)) {
			try {
				JSONObject jsonContactObject = new JSONObject(response);
				if (jsonContactObject.has("header")) {
					JSONObject header = (JSONObject) jsonContactObject
							.get("header");
					if (header.length() > 0) {
						int serverMessageId = Integer.parseInt(header
								.getString("id"));
						HeaderModel headerModel = new HeaderStore()
								.getHeaderById(serverMessageId, false);
						if (headerModel == null
								|| headerModel.getLocalHeaderId() == -1) {
							ContactsModel contactReqModel =getContactRequestDataAmqp(response,cashewnutId);
							DBContactsMapper profileMapper = DBContactsMapper.getInstance();
							profileMapper.insertRequest(contactReqModel.getCashewnutId(), contactReqModel.firstName, contactReqModel.getPhone(), contactReqModel.getEmailId(),contactReqModel.getStatus(),contactReqModel.getNotes(),contactReqModel.getContactServerId());
						}
						Thread.sleep(300);
					}
					
				}
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		
	}
	public ContactsModel getContactRequestDataAmqp(String response,String cashewId)
	{
		ContactsModel requestModel = new ContactsModel();
		 int contactId;
		 int contactServerId;
		 String from;
		 JSONArray to=null;
		 String type;
		 int status;
		 String comments=null;
		 String creationTime;
		 String lastUpdatedTime;
		 String phone=null;
		 String email=null;
		 String serverName; 
		 try
		 {
			 JSONObject contactJson = new JSONObject(response);
				JSONObject contactRequest = (JSONObject) contactJson.get("header");
				contactServerId = Integer.parseInt((contactRequest.getString("id")));
				from = contactRequest.getString("from");
				to = contactRequest.getJSONArray("to");
				//type = contactRequest.getString("type");
				creationTime = contactRequest.getString("creation_timestamp");	
				status = contactRequest.getInt("status");
				if(contactRequest.has("subject")) {
					JSONObject subject = (JSONObject) contactRequest.get("subject");
					
					if(subject.has("content")) {
						comments = subject.getString("content");
					}
					
				}
				
				if (contactRequest.has("client_params")) {
					Object parameters = contactRequest.get("client_params");
					if (parameters instanceof JSONObject) {
						JSONObject clientParams = (JSONObject)parameters;
						if (clientParams.has("email")) {
							email = clientParams.getString("email");
						}
						if (clientParams.has("phone_number")) {
							phone = clientParams.getString("phone_number");
						}
					}


				}

				//JSONArray itemList = new JSONArray(to);
				String item = null;
				for (int i = 0; i < to.length(); i++) {
					item = to.getString(i); 
				}
				if(from.equals(cashewId))
				{
					status=3;
					from=item;
				}
				else
				{
					status=1;
					
				}
				
				//String type1 = type;
				requestModel.setContactServerId(contactServerId);
				requestModel.setCashewnutId(from);
				//requestModel.setType(type1);
				requestModel.setStatus(status);
				requestModel.setNotes(comments);
				//requestModel.setTo(item);
				requestModel.setPhone(phone);
				requestModel.setEmailId(email);
				requestModel.setLastUpdateDate(creationTime);
				
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		
		return requestModel;
	}
}
