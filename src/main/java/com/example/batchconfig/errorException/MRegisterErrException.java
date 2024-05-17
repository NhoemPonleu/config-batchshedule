package com.example.batchconfig.errorException;

import java.io.Serial;

public class MRegisterErrException extends MException {
@Serial
private static final long serialVersionUID = -4668068470041155355L;
	
	public MRegisterErrException() {
		super();
	}
	
	public MRegisterErrException(String message) {
		super(message);
	}
	
	public MRegisterErrException(String code, String message) {
		super(code, message);
	}
	
	public MRegisterErrException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MRegisterErrException(Throwable cause) {
		super(cause);
	}
	
	public MRegisterErrException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
