package com.example.batchconfig.errorException;

import java.io.Serial;

public class MNotAffectedErrException extends MException {
@Serial
private static final long serialVersionUID = -6167372328797543749L;
	
	public MNotAffectedErrException() {
		super();
	}
	
	public MNotAffectedErrException(String message) {
		super(message);
	}
	
	public MNotAffectedErrException(String code, Throwable cause) {
		super(code, cause);
	}
	
	public MNotAffectedErrException(Throwable cause) {
		super(cause);
	}
	
	public MNotAffectedErrException(String code, String message) {
		super(code, message);
	}
	
	public MNotAffectedErrException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
