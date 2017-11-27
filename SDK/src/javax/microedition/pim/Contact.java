package javax.microedition.pim;

import java.util.ArrayList;

public class Contact implements PIMItem{
	
	public static final int NAME_GIVEN = 0;
	public static final int NAME_FAMILY = 1;
	
	public static String ATTR_NONE = "none";
	
	public static final int ATTR_HOME = 1;
	public static final int ATTR_WORK = 2;
	public static final int ATTR_MOBILE = 3;
	public static final int ATTR_OTHER = 4;
	public static final int ATTR_SMS = 5;
	
	
	public static final int TEL = 0xf0;
	public static final int EMAIL = 0xf1;
	public static final int ADDR = 0xf2;
	public static final int NAME = 0xf3;
	public static final int BIRTHDAY = 0xf4;
	public static final int ORG = 0xf5;
	public static final int NOTES = 0xf6;
	public static final int IM = 0xf7;
	
	private String id;
 	private String displayName;
 	private ArrayList<Phone> phone;
 	private ArrayList<Email> email;
 	private ArrayList<String> notes;
 	private ArrayList<Address> addresses = new ArrayList<Address>();
 	private ArrayList<IM> imAddresses;
 	private Organization organization;
  	
 	
 	public Organization getOrganization() {
 		return organization;
 	}
 	public void setOrganization(Organization organization) {
 		this.organization = organization;
 	}
 	public ArrayList<IM> getImAddresses() {
 		return imAddresses;
 	}
 	public void setImAddresses(ArrayList<IM> imAddresses) {
 		this.imAddresses = imAddresses;
  	}
 	public void addImAddresses(IM imAddr) {
 		this.imAddresses.add(imAddr);
 	}
 	public ArrayList<String> getNotes() {
 		return notes;
 	}
 	public void setNotes(ArrayList<String> notes) {
 		this.notes = notes;
 	}
 	public void addNote(String note) {
 		this.notes.add(note);
 	}
 	public ArrayList<Address> getAddresses() {
 		return addresses;
 	}
 	public void setAddresses(ArrayList<Address> addresses) {
 		this.addresses = addresses;
 	}
 	public void addAddress(Address address) {
 		this.addresses.add(address);
 	}
 	public ArrayList<Email> getEmail() {
 		return email;
 	}
 	public void setEmail(ArrayList<Email> email) {
 		this.email = email;
 	}
 	public void addEmail(Email e) {
 		this.email.add(e);
 	}	
 	public String getId() {
 		return id;
 	}
 	public void setId(String id) {
  		this.id = id;
 	}
 	public String getDisplayName() {
 		return displayName;
 	}
 	public void setDisplayName(String dName) {
 		this.displayName = dName;
 	}
 	public ArrayList<Phone> getPhone() {
 		return phone;
 	}
 	public void setPhone(ArrayList<Phone> phone) {
 		this.phone = phone;
 	}
 	public void addPhone(Phone phone) {
 		this.phone.add(phone);
 	}


	public void addString(String tel2, String attrNone, String tel3) {
		// TODO Auto-generated method stub

	}

	public void addStringArray(String name2, String attrNone, String[] name3) {
		// TODO Auto-generated method stub

	}

	public void commit() {
		// TODO Auto-generated method stub

	}

	public int countValues(int tEL2) {
		switch (tEL2){
		case TEL:
			return phone==null?0:phone.size();
		case NAME:
			return 1;
		case EMAIL:
			return email==null?0:email.size();
		case ADDR:
			return addresses==null?0:addresses.size();
		case NOTES:
			return notes==null?0:notes.size();
		case ORG:
			return 1;
		case IM:
			return imAddresses==null?0:imAddresses.size();
		case BIRTHDAY:
			return 0;
		}
		return 0;
	}

	public int getAttributes(int tEL2, int i) {
		// TODO Auto-generated method stub
		int ret = i<<16;
		ret = ret|tEL2;
		return ret;
	}

	public String getString(int tEL2, int i) {
		switch(tEL2){
		case Contact.TEL:
			return phone.get(i).getNumber();
		case Contact.EMAIL:
			return email.get(i).getAddress();
		case Contact.ADDR:
			return addresses.get(i).toString();
		case Contact.NOTES:
			return notes.get(i);
		case Contact.ORG:
			return organization.getOrganization();
		case Contact.IM:
			return imAddresses.get(i).getName();
		case Contact.BIRTHDAY:
			return "BIRTHDAY";
		}
		
		return null;
	}

	public String[] getStringArray(int fields, int i) {
		// TODO Auto-generated method stub
		if(fields==(NAME)){
			if(displayName==null)
				return new String[]{" "," "};
			else if(displayName.contains(" "))
				return displayName.split(" ");
			else
				return new String[]{displayName," "};
		}else if(fields==ADDR){
			return addresses.get(i).toStringArray();
		}
		return null;
	}
	public long getDate(int i, int j) {
		// TODO Auto-generated method stub
		return -1;
	}
	public int[] getFields() {
		// TODO Auto-generated method stub
		int count = 0;
		int[] tmp = new int[8];
		if(displayName!=null){
			tmp[count]=NAME;
			count++;
		}
		if(phone!=null && phone.size()>0){
			tmp[count]=TEL;
			count++;
		}
		if(email!=null && email.size()>0){
			tmp[count]=EMAIL;
			count++;
		}
		if(addresses!=null && addresses.size()>0){
			tmp[count]=ADDR;
			count++;
		}
		if(notes!=null && notes.size()>0){
			tmp[count]=NOTES;
			count++;
		}
		if(imAddresses!=null && imAddresses.size()>0){
			tmp[count]=IM;
			count++;
		}
		if(organization!=null){
			tmp[count]=ORG;
			count++;
		}
		if(count>0){
			int[] ret = new int[count];
			System.arraycopy(tmp, 0, ret, 0, count);
			return ret;
		}else{
			return new int[0];
		}
	}

}
