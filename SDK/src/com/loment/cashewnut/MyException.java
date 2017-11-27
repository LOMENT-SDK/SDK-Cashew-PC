package com.loment.cashewnut;

public class MyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyException() {
		// TODO Auto-generated constructor stub
	}

	public MyException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public MyException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public MyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
