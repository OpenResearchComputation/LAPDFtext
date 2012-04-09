// -*- mode: Java;  tab-width: 2; c-basic-offset: 2; -*-
//
// $Id: LogStream.java 7033 2008-07-15 01:05:25Z tar $
//
//  Copyright (C) 2007 University of Southern California.
//  All Rights Reserved.
//

package edu.isi.bmkeg.pdf.scripts;

import java.io.*;
import java.util.Date;

/** A log stream, which handles log messages going to a PrintStream
 *  or PrintWriter.
 *
 *  @author  University of Southern California
 *  @version $Revision: 7033 $
 */
public class LogStream implements LogTarget {

  PrintWriter logOutput;

  /** Construct a default log stream, with output to System.out
   */
  public LogStream () {
    this(new PrintWriter(System.out));
  }

  /** Construct a log stream with output to the given stream.
   *
   * @param stream The OutputStream to which to write the output.
   */
  public LogStream (OutputStream stream) {
    this(new PrintWriter(stream, true));
  }

  /** Construct a log stream with output to the given stream.
   *
   * @param out The Writer to which to write the output.
   */
  public LogStream (Writer out) {
    super();
    if (out instanceof PrintWriter) {
      logOutput = (PrintWriter) out;
    } else {
      logOutput = new PrintWriter(out, true);
    }
  }

  /** Log the message, attaching a timestamp to the log.
   *  Adds the entry to the end of the text area and
   *  scrolls to that position.
   *
   *  @param message The log message to output.
   */
  public void logString (String message) {
    logOutput.println(new Date().toString() + ": " + message);
		logOutput.flush();
  }

  /** Append the message without a timestamp.
   *
   *  @param message The log message to output.
   */
  public void append (String message) {
		logOutput.print(message);
		logOutput.flush();
	}

  /** Log the exception, logging it and the stack trace to the log
	 *  target.
   *
   *  @param title The title for the exception.
   *  @param ex The exception to log.
   */
  public void logException (String title, Throwable ex) {
		if (title != null && !title.equals("") && !title.endsWith(": ")) {
			title = title + ": ";
		}
		logString(title + ex);
		ex.printStackTrace(logOutput);
		logOutput.flush();
  }


  /** Clears log entries.  This has no effect.
   */
  public void clearLog () {
    // no-op
  }
}
