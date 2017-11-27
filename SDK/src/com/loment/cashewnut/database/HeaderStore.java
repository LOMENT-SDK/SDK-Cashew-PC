package com.loment.cashewnut.database;

import java.sql.ResultSet;

import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;

public class HeaderStore {
	
	public  int saveHeader(MessageModel messageModel) {
		int localHeaderId;
		DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
		HeaderModel headerModel = messageModel.getHeaderModel();
		localHeaderId = (int) headerMapper.insert(headerModel);
		return localHeaderId;
	}
	
	public  HeaderModel getHeaderById(int i, boolean isLocalID) {
		try {
			DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
			HeaderModel storedHeader = headerMapper.getMessageHeaderById(i, isLocalID);
			return storedHeader;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void updateScheduleMessages(HeaderModel headerModel,int id)
	{
		DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();	
		headerMapper.updateScheduleMessages(headerModel, id);
	}
	public  void updateHeaderById(HeaderModel headerModel, int id){
		DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
		headerMapper.update(headerModel, id);
	}
	public void removeMessageByHeaderId(int localHeaderId) {
		DBHeaderMapper header = DBHeaderMapper.getInstance();
		header.deleteByHeaderId(localHeaderId);
	}
	
	public boolean isMessageExist(int localId) {
		try {
			DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
			ResultSet cursor = headerMapper.getCursorById(localId);
			cursor.last();
			int rowcount = cursor.getRow();
			return rowcount > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
