package javax.wireless.messaging;

import java.util.Date;

public interface Message {
	public Date getTimestamp();
	String getAddress();
}
