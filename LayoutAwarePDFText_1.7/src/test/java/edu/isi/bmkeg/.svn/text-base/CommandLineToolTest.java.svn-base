package edu.isi.bmkeg;

import junit.framework.TestCase;

import org.junit.Test;


import edu.isi.bmkeg.pdf.scripts.CommandLineTool;

public class CommandLineToolTest extends TestCase
{

	protected void setUp() throws Exception
	{ 
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file 
	 * - generate a report file for each input PDF containing page-wise statistics 
	 *   of each block detected. This is meant as a guide for developers to use in 
	 *   the process of developing rules for block classification and evantual
	 *   section-wise text extraction. 
	 */
	@Test
	public void testBlockStats()
	{
		String args[] = {"blockStatistics","src/test/resources/sampleData/plos/8_8"};
		CommandLineTool.main(args);

	}
	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is unspecified and therefore output is written to the input folder. 
	 * 
	 */
	@Test
	public void test1()
	{
		String args[] = {"blockify","src/test/resources/sampleData/plos/8_8"};
		CommandLineTool.main(args);

	}

	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is specified. 
	 * 
	 */
	@Test
	public void test2()
	{
		String args[] = {"blockify","src/test/resources/sampleData/plos/8_8","src/test/resources/sampleData/plos/8_8_OUTPUT"};
		CommandLineTool.main(args);

	}

	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is unspecified and therefore output is written to the input folder.
	 * - Classify the extracted blocks into their corresponding sections. 
	 *   The types of sections that are supported are listed in the 
	 *   java interface edu.isi.bmkeg.pdf.model.Block 
	 */

	@Test
	public void test3()
	{
		String args[] = {"blockifyClassify","src/test/resources/sampleData/plos/8_8","src/main/resources/rules/plosbiology/epoch_7Jun_8.drl","src/test/resources/sampleData/plos/8_8_OUTPUT"};
		CommandLineTool.main(args);

	}
	
	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is unspecified and therefore output is written to the input folder.
	 * - Classify the extracted blocks into their corresponding sections. 
	 *   The types of sections that are supported are listed in the 
	 *   java interface edu.isi.bmkeg.pdf.model.Block 
	 * - The ability of LA-PDFText to accept classification rules written as an excel spreadsheet
	 */
	@Test
	public void test3Excel()
	{
		String args[] = {"blockifyClassify","src/test/resources/sampleData/plos/8_8","src/main/resources/rules/plosbiology/epoch_7Jun_8.csv","src/test/resources/sampleData/plos/8_8_OUTPUT"};
		CommandLineTool.main(args);

	}
	
	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is unspecified and therefore output is written to the input folder.
	 * - Classify the extracted blocks into their corresponding sections. 
	 *   The types of sections that are supported are listed in the 
	 *   java interface edu.isi.bmkeg.pdf.model.Block 
	 * - The ability of LA-PDFText to extract the text in plain text from by using the 
	 *   section classifications to filter out those sections that are not a part of the 
	 *   main narrative of the input article. 
	 */
	@Test
	public void test4()
	{
		String args[] = {"extractFullText","src/test/resources/sampleData/plos/8_8","src/main/resources/rules/plosbiology/epoch_7Jun_8.drl","src/test/resources/sampleData/plos/8_8_OUTPUT"};
		CommandLineTool.main(args);

	}

	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * error check the input modes of operation. Current version does not 
	 * support the extraction of individual sections. 
	 */
	@Test
	public void test5()
	{
		String args[] = {"extractSection","src/test/resources/sampleData/plos/8_8_OUTPUT/pbio.1000441.pdf_rhetorical.xml","src/test/resources/sampleData/plos/8_8_OUTPUT/pbio.1000441.pdf_rhetorical.methods","materials|methods"};
		CommandLineTool.main(args);

	}
	
	/**
	 * This test is designed to demonstrate the capability of LA-PDFText to 
	 * - Extract contiguous blocks from an input PDF file. In this test the output location
	 *   is unspecified and therefore output is written to the input folder.
	 * - Classify the extracted blocks into their corresponding sections. 
	 *   The types of sections that are supported are listed in the 
	 *   java interface edu.isi.bmkeg.pdf.model.Block 
	 * - The ability of LA-PDFText to extract the text in plain text from by using the 
	 *   section classifications to filter out those sections that are not a part of the 
	 *   main narrative of the input article. 
	 * - The rule file used here is a generic journal and publisher format-agnostic 
	 *   rule file which identifies the page footers and headers only. Subsequently,
	 *   the class edu.isi.bmkeg.pdf.text.SpatiallyOrderedChunkTextWriter is used to 
	 *   filter out the header and footer to write text that is not interrupted by 
	 *   their formatting embellishments. 
	 */
	@Test
	public void testGeneral()
	{
		String args[] = {"blockifyClassify","/Users/cartic/Desktop/temp/bloodOriginal/new","src/main/resources/rules/plosbiology/general.drl"};
		CommandLineTool.main(args);

	}


}
