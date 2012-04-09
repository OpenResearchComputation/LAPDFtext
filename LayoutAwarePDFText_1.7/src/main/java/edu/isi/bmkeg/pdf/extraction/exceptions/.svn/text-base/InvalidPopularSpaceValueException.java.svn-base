package edu.isi.bmkeg.pdf.extraction.exceptions;

import org.jpedal.exception.PdfException;

public class InvalidPopularSpaceValueException extends PdfException
{
	private String methodName;
	public InvalidPopularSpaceValueException(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public void printStackTrace() {

		System.out.println("Encountered invalid value for space in "+methodName);
	}
}
