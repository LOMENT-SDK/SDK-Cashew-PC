package javax.microedition.pim;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;


public class ContactList {
	
	public Contact c;
	public static final int[] SUPPORTED_FIELDS=new int[]{Contact.TEL,Contact.EMAIL,Contact.ADDR,Contact.NOTES,Contact.ORG,Contact.IM};
	
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	 
 	public ArrayList<Contact> getContacts() {
 		return contacts;
 	}
 
 	public void setContacts(ArrayList<Contact> contacts) {
 		this.contacts = contacts;
 	}
 	
 	public void addContact(Contact contact) {
 		this.contacts.add(contact);
 	}
  	
 	


	public void close() {
		// TODO Auto-generated method stub

	}

	public Contact createContact() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSupportedField(int fields) {
		// TODO Auto-generated method stub
		for(int i=0;i<SUPPORTED_FIELDS.length;i++)
		if(fields==(SUPPORTED_FIELDS[i])){
			return true;
		}
		return false;
	}

	public Enumeration items() {
		// TODO Auto-generated method stub
		
		return new Enumeration<Contact>() {
			int currentIndex = 0;
//			Iterator<Contact> itr=contacts.iterator();
			@Override
			public boolean hasMoreElements() {
				// TODO Auto-generated method stubtion e) {
				if(ContactAPI.getAPI().isLoading()&& contacts.size()<=currentIndex){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(contacts.size()>currentIndex)
					return true;
				else
					return false;
				
//				return itr.hasNext();
			}

			@Override
			public Contact nextElement() {
				// TODO Auto-generated method stub
				c = contacts.get(currentIndex++);
				return c;
//				c = itr.next();
//				return c
			}
		};
	}

//	public int stringArraySize(String name) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	public int[] getSupportedFields() {
		// TODO Auto-generated method stub
		
		return SUPPORTED_FIELDS;
	}

	public String getAttributeLabel(int attrib) {
		// TODO Auto-generated method stub
//		return ContactAPI.getAPI().getLabel(attrib, c, LWUITActivity.currentActivity);
		return "";
	}

	public int getFieldDataType(int i) {
		if(c!=null){
			switch(i){
			case Contact.TEL:
				return PIMItem.STRING;
			case Contact.EMAIL:
				return PIMItem.STRING;
			case Contact.ADDR:
				return PIMItem.STRING_ARRAY;
			case Contact.NOTES:
				return PIMItem.STRING;
			case Contact.ORG:
				return PIMItem.STRING;
			case Contact.IM:
				return PIMItem.STRING;
			case Contact.BIRTHDAY:
				return PIMItem.DATE;
			}
		}
		return 0;
	}

	public boolean isSupportedAttribute(int tel, int attrib) {
		// TODO Auto-generated method stub
		return true;
	}

}
