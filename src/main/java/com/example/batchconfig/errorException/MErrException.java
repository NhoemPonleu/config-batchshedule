package com.example.batchconfig.errorException;

public class MErrException extends MException{
	private static final long	serialVersionUID	= -4454580843735287729L;
//	private String[]			callStack			= new String[5];
//	
//	private void saveCallStack() {
//		LinkedList<String> stack = MRContextUtil.getOperationCallStack();
//		if ( stack == null || stack.size() == 0 )
//			return;
//		int stackSize = stack.size();
//		int cnt = 5;
//		for ( int i = ( stackSize - 1 ); i >= 0 && cnt > 0; i-- ) {
//			callStack[cnt - 1] = stack.get( i );
//			cnt--;
//		}
//	}
	
	public MErrException() {
		super();
		//saveCallStack();
	}
	
	public MErrException(String message) {
		super(message);
		//saveCallStack();
	}
	
	public MErrException(String code, String message) {
		super(code, message);
	}
	
	public MErrException(Throwable cause) {
		super(cause);
		//saveCallStack();
	}
	
	public MErrException(String message, Throwable cause) {
		super(message, cause);
		//saveCallStack();
	}
	
	public MErrException(String code, String message, Throwable cause) {
		super(code, message, cause);
		//saveCallStack();
	}
}
