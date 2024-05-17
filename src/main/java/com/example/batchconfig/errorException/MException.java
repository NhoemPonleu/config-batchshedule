package com.example.batchconfig.errorException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MException extends RuntimeException {
	private static final long	serialVersionUID		= 1437917247176570420L;
	private Map<String, Object>	causeInfo				= new HashMap<>();
	public static final String	REPLACRE_PARAMS			= "replace_params";
	public static final String	MESSAGE_ADD_CONTENTS	= "msg_add_ctt";
	public static final String	MESSAGE_SVC_ID			= "msg_svc_id";
	public static final String	MESSAGE_FILE_NAME		= "msg_file_name";
	public static final String	MESSAGE_METHOD_NAME		= "msg_method_name";
	
	public MException() {}
	
	public MException(String s) {
		super(s);
	}
	
	public MException(String mCode, Throwable cause) {
		super(mCode, cause);
	}
	
	public MException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
	
	public Throwable getRootCause() {
		Throwable tempCause = getCause();
		
		while (tempCause != null) {
			
			if (tempCause.getCause() == null) {
				break;
			}
			
			tempCause = tempCause.getCause();
		}
		
		return tempCause;
	}
	
	public String getStackTraceString() {
		StringWriter s = new StringWriter();
		super.printStackTrace(new PrintWriter(s));
		return s.toString();
	}
	
	public void setCauseInfo(String causeInfoKey, Object causeInfoValue) {
		this.causeInfo.put(causeInfoKey, causeInfoValue);
	}
	
	public Object getCauseInfo(String causeInfoKey) {
		return this.causeInfo.get(causeInfoKey);
	}
	
	public void setCauseInfo(Map<String, Object> causeInfo) {
		this.causeInfo = causeInfo;
	}
	
	public Map<String, Object> getCauseInfo() {
		return this.causeInfo;
	}
	
	public void setReplaceParams(String[] replaceParams) {
		causeInfo.put(REPLACRE_PARAMS, replaceParams);
	}
	
	public String[] getReplaceParams() {
		return (String[])causeInfo.get(REPLACRE_PARAMS);
	}
	
	public void setMessageAddContents(String msgAddCtnt) {
		causeInfo.put(MESSAGE_ADD_CONTENTS, msgAddCtnt);
	}
	
	public String getMessageAddContents() {
		return (String)causeInfo.get(MESSAGE_ADD_CONTENTS);
	}
	
	public String getMessageSvcId() {
		return (String)causeInfo.get(MESSAGE_SVC_ID);
	}
	
	public void setMessageSvcId(String msgSvcId) {
		causeInfo.put(MESSAGE_SVC_ID, msgSvcId);
	}
	
	public void setMessageFileName(String fileName) {
		this.causeInfo.put(MESSAGE_FILE_NAME, fileName);
	}
	
	public String getMessageFileName() {
		return (String)causeInfo.get(MESSAGE_FILE_NAME);
	}
	
	public void setMessageMethodName(String methodName) {
		this.causeInfo.put(MESSAGE_METHOD_NAME, methodName);
	}
	
	public String getMessageMethodName() {
		return (String)causeInfo.get(MESSAGE_METHOD_NAME);
	}
	
	public MException(String msgCode, String[] replaceParams) {
		super(msgCode);
		setReplaceParams(replaceParams);
	}
	
	public MException(String msgCode, String[] replaceParams, Throwable cause) {
		super(msgCode, cause);
		setReplaceParams(replaceParams);
	}
	
	public MException(String msgCode, String msgAddCtt) {
		super(msgCode);
		setMessageAddContents(msgAddCtt);
	}
	
	public MException(String msgCode, String msgAddCtt, Throwable cause) {
		super(msgCode, cause);
		setMessageAddContents(msgAddCtt);
	}
	
	public MException(String msgCode, String[] replaceParams, String msgAddCtt) {
		super(msgCode);
		setReplaceParams(replaceParams);
		setMessageAddContents(msgAddCtt);
	}
	
	public MException(String msgCode, String[] replaceParams, String msgAddCtt, Throwable cause) {
		super(msgCode, cause);
		setReplaceParams(replaceParams);
		setMessageAddContents(msgAddCtt);
	}
	
	public String getMCode() {
		return super.getMessage();
	}
	
	public String getMMessage() {
		return this.getMessageAddContents();
	}
	
}
