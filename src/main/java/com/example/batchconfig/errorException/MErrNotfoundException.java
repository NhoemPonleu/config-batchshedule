package com.example.batchconfig.errorException;

public class MErrNotfoundException extends MException {
private static final long serialVersionUID = 4590938470775174864L;
	
	public MErrNotfoundException() {
		super();
	}
	
	public MErrNotfoundException(String message) {
		super(message);
	}
	
	public MErrNotfoundException(String code, Throwable cause) {
		super(code, cause);
	}
	
	public MErrNotfoundException(Throwable cause) {
		super(cause);
	}
	
	public MErrNotfoundException(String code, String message) {
		super(code, message);
	}
	
	public MErrNotfoundException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
