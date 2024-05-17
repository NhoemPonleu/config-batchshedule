package com.example.batchconfig.errorException;

import java.io.Serial;

public class MCatchException extends RuntimeException{
	public MCatchException(String msg) {
		super(msg);
	}
	@Serial
	private static final long serialVersionUID = -7234632514251157752L;
}
