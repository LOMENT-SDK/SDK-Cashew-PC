package com.loment.cashewnut.activity.controller;

import com.loment.cashewnut.sthithi.connection.AccountHandler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UserDetailsForm extends Service<String> {

        private final StringProperty userName = new SimpleStringProperty(this, "userName");
        private final StringProperty password = new SimpleStringProperty(this, "password");
         private final StringProperty cashewId = new SimpleStringProperty(this, "cashewId");
         private int status=0;
         
    

		public int getStatus() {
			return status;
		}
		public final void setStatus(int value) {
            status=value;
        }
		
		public final void setUseName(String value) {
            userName.set(value);
        }

        public final String getUserName() {
            return userName.get();
        }

        public final StringProperty userNameProperty() {
            return userName;
        }

        public final void setPassword(String value) {
            password.set(value);
        }

        public final String getPassword() {
            return password.get();
        }
        
        public final StringProperty userPasswordProperty() {
            return password;
        }
        public final void setCashewId(String value) {
            cashewId.set(value);
        }

        public final String getCashewId() {
            return cashewId.get();
        }



        @Override
        protected Task createTask() {
            final String _uname = getUserName();
            final String _pass = getPassword();
            final String _cid = getCashewId();
            final int  _status=getStatus();
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {

                    if (!AccountHandler.getInstance()
                            .getNameFromSthithi(_uname, _pass,_cid,_status)) {
                        System.out.println("Cashew id not exist .... ");
                        return false;
                    }
                    return true;
                }
            };
        }

}
