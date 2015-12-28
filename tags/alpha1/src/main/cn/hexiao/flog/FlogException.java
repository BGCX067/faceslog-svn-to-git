package cn.hexiao.flog;

import java.io.PrintStream;
import java.io.PrintWriter;
 
public class FlogException extends Exception {
    
    private Throwable mRootCause = null;
    
    /**
     * Construct emtpy exception object.
     */
    public FlogException() {
        super();
    }
    
    
    /**
     * Construct FlogException with message string.
     * @param s Error message string.
     */
    public FlogException(String s) {
        super(s);
    }
    
    
    /**
     * Construct FlogException, wrapping existing throwable.
     * @param s Error message
     * @param t Existing connection to wrap.
     */
    public FlogException(String s, Throwable t) {
        super(s);
        mRootCause = t;
    }
    
    
    /**
     * Construct FlogException, wrapping existing throwable.
     * @param t Existing exception to be wrapped.
     */
    public FlogException(Throwable t) {
        mRootCause = t;
    }
    
    
    /**
     * Get root cause object, or null if none.
     * @return Root cause or null if none.
     */
    public Throwable getRootCause() {
        return mRootCause;
    }
    
    
    /**
     * Get root cause message.
     * @return Root cause message.
     */
    public String getRootCauseMessage() {
        String rcmessage = null;
        if (getRootCause()!=null) {
            if (getRootCause().getCause()!=null) {
                rcmessage = getRootCause().getCause().getMessage();
            }
            rcmessage = (rcmessage == null) ? getRootCause().getMessage() : rcmessage;
            rcmessage = (rcmessage == null) ? super.getMessage() : rcmessage;
            rcmessage = (rcmessage == null) ? "NONE" : rcmessage;
        }
        return rcmessage;
    }
    
    
    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @see java.lang.Throwable#printStackTrace()
     */
    @Override
	public void printStackTrace() {
        super.printStackTrace();
        if (mRootCause != null) {
            System.out.println("--- ROOT CAUSE ---");
            mRootCause.printStackTrace();
        }
    }
    
    
    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @param s Stream to print to.
     */
    @Override
	public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (mRootCause != null) {
            s.println("--- ROOT CAUSE ---");
            mRootCause.printStackTrace(s);
        }
    }
    
    
    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @param s Writer to write to.
     */
    @Override
	public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (null != mRootCause) {
            s.println("--- ROOT CAUSE ---");
            mRootCause.printStackTrace(s);
        }
    }
    
}
