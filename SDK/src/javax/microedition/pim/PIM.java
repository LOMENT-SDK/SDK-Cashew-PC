package javax.microedition.pim;


public class PIM {

	public static final String CONTACT_LIST = null;
	public static final String READ_WRITE = null;
	public static final String READ_ONLY = null;
	
	private static PIM instance= new PIM();

	public static PIM getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public ContactList openPIMList(String contactList, String readWrite) {
		// TODO Auto-generated method stub
//		ContactAPI.getAPI().setCr(LWUITActivity.currentActivity.getContentResolver());
//		return ContactAPI.getAPI().newContactList();
		return null;
	}

}
