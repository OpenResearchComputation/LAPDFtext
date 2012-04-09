// -*- mode: Java;  tab-width: 2; c-basic-offset: 2; -*-
//
// $Id: LogPanel.java 7033 2008-07-15 01:05:25Z tar $
//
//  Copyright (C) 2007 University of Southern California.
//  All Rights Reserved.
//

package edu.isi.bmkeg.pdf.scripts;

import java.util.Date;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/** A log panel, consisting of a scrolling text area and a button
 *  to clear the log.
 *
 *  @author  University of Southern California
 *  @version $Revision: 7033 $
 */
public class LogPanel extends TextPanel implements LogTarget {

	final static String DEFAULT_CLEAR_BUTTON_NAME = "Clear Log";
	final static String DEFAULT_SAVE_BUTTON_NAME  = "Save Log";
	final static int DEFAULT_HEIGHT = 12;
	final static int DEFAULT_WIDTH  = 80;

	/** Construct a default sized log panel.
	 */
	public LogPanel () {
		this(DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_SAVE_BUTTON_NAME, DEFAULT_CLEAR_BUTTON_NAME);
	}

	/** Construct a logPanel with given button names.
	 *  If no name is given, then the specified button will not appear.
	 *
	 * @param saveText  Text string for labeling the save button.
	 * @param clearText Text string for labeling the clear button.
	 */
	public LogPanel (String saveText, String clearText) {
		this(DEFAULT_HEIGHT, DEFAULT_WIDTH, saveText, clearText);
	}


	/** Construct a logPanel of the given size.
	 *
	 * @param height The height of the log panel's text area.
	 * @param width  The width of the log panel's text area.
	 */
	public LogPanel (int height, int width) {
		this(height, width, DEFAULT_SAVE_BUTTON_NAME, DEFAULT_CLEAR_BUTTON_NAME);
	}

	/** Construct a logPanel with given size and button names.
	 *  If no name is given, then the specified button will not appear.
	 *
	 * @param height The height of the log panel's text area.
	 * @param width  The width of the log panel's text area.
	 * @param saveText  Text string for labeling the save button.
	 * @param clearText Text string for labeling the clear button.
	 */
	public LogPanel (int height, int width, String saveText, String clearText) {
		super(height, width, saveText, clearText);
	}

	protected String getSaveDialogTitle() {
		return "Save Log File";
	}

	/** Log the message, attaching a timestamp to the log.
	 *  Adds the entry to the end of the text area and
	 *  scrolls to that position.
	 *
	 *  @param message The log message to output.
	 */
	public void logString (String message) {
		append(new Date().toString() + ": " + message + "\n");
	}

	/** Append the message without a timestamp.
	 *  Note.  This uses an inherited method from TextPanel.
	 *
	 *  @param message The log message to output.
	 */


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
		for (StackTraceElement elem: ex.getStackTrace()) {
			append(elem.toString());
			append("\n");
		}
	}

	/** Clears log entries.  This may, depending on the
	 *  implementing class, not do anything.
	 */
	public void clearLog () {
		clear();
	}
}
