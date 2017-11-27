package com.loment.cashewnut.activity.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.loment.cashewnut.AppConfiguration;


public class ConversetionPrimerUser implements Comparable<Object> {
	public static final String UNKNOWN_USER = AppConfiguration.appConfString.conversation_unknown_contact;
	
	private String firstName;
	private String lastName;
	
	private String from;
	private Vector<String> receipients;

	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public Vector<String> getReceipients() {
		return receipients;
	}


	public void setReceipients(Vector<String> receipients) {
		this.receipients = receipients;
	}
	


	public ConversetionPrimerUser() {
	}


	/**
	 * 
	 * @return Full name of the user
	 */
	public String getFullName() {
		StringBuffer buffer = new StringBuffer();

		if (firstName != null && firstName.length() > 0)
			buffer.append(firstName).append(" ");

		if (lastName != null && lastName.length() > 0)
			buffer.append(lastName);

		if (buffer.length() < 1)
			return UNKNOWN_USER;

		return buffer.toString();
	}
	
	public static int compareTwoVectors(List<String> v1, List<String> v2) {
		if (v1.size() != v2.size()) {
			return -1;
		}
		for (int i = 0; i < v1.size(); i++) {
			boolean isEquals = false;
			for (int j = 0; j < v2.size(); j++) {
				if (v1.get(i).equals(v2.get(j))) {
					isEquals = true;
				}
			}
			if (!isEquals) {
				return -1;
			}
		}
		for (int i = 0; i < v2.size(); i++) {
			boolean isEquals = false;
			for (int j = 0; j < v1.size(); j++) {
				if (v2.get(i).equals(v1.get(j))) {
					isEquals = true;
				}
			}
			if (!isEquals) {
				return -1;
			}
		}
		return 1;
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(Object o) {

		if (o instanceof ConversetionPrimerUser) {
			ConversetionPrimerUser that = (ConversetionPrimerUser) o;
			return compareTwoVectors(this.getReceipients(),
					that.getReceipients());
		}

		if (o instanceof Vector) {
			Vector<String> that = (Vector<String>) o;
			return compareTwoVectors(this.getReceipients(), that);
		}
		
		if (o instanceof ArrayList) {
			ArrayList<String> that = (ArrayList<String>) o;
			return compareTwoVectors(this.getReceipients(), that);
		}

		return -1;
	}
}