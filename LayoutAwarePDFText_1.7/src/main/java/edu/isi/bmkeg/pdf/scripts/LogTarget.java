// -*- mode: Java;  tab-width: 2; c-basic-offset: 2; -*-
//
// $Id: LogTarget.java 7033 2008-07-15 01:05:25Z tar $
//
//  Copyright (C) 2007 University of Southern California.
//  All Rights Reserved.
//

package edu.isi.bmkeg.pdf.scripts;

/** Interface for generalized logging.
 *
 *  @author  University of Southern California
 *  @version $Revision: 7033 $
 */
public interface LogTarget {

  /** Log the message, attaching a timestamp to the log.
   *
   *  @param message The log message to output.
   */
  public void logString (String message);

  /** Append the message without a timestamp.
   *
   *  @param message The log message to output.
   */
  public void append (String message);

  /** Log the exception, logging it and the stack trace to the log
	 *  target.
   *
   *  @param title The title for the exception.
   *  @param ex The exception to log.
   */
  public void logException (String title, Throwable ex);


  /** Clears log entries.  This may, depending on the
   *  implementing class, not do anything.
   */
  public void clearLog ();

	
}
