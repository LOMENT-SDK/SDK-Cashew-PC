package javax.microedition.pim;



public abstract class ContactAPI {
	 
 	private static ContactAPI api;
 	
 	
 	public static ContactAPI getAPI() {
// 		if (api == null) {
// 			String apiClass;
// 			if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.ECLAIR) {
// 				apiClass = "javax.microedition.pim.ContactAPISdk5";
// 			} else {
// 				apiClass = "javax.microedition.pim.ContactAPISdk3";
// 			}
// 			
// 			try {
// 				Class<? extends ContactAPI> realClass = Class.forName(apiClass).asSubclass(ContactAPI.class);
// 				api = realClass.newInstance();
// 			} catch (Exception e) {
// 				throw new IllegalStateException(e);
// 			}
// 			
// 		}
 		return api;
 	}
 	
// 	public abstract Intent getContactIntent();
 	
 	public abstract ContactList newContactList();
 	
// 	public abstract Cursor getCur();
// 	public abstract void setCur(Cursor cur);
 	
// 	public abstract ContentResolver getCr();
// 	public abstract void setCr(ContentResolver cr);
	
	public abstract boolean isLoading();
	
//	public abstract String getLabel(int attrib, Contact c, Activity activity);
 }

