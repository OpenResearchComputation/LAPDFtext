package edu.isi.bmkeg.pdf.extraction;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Strip;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.isi.bmkeg.pdf.extraction.exceptions.AccessException;
import edu.isi.bmkeg.pdf.extraction.exceptions.EmptyPDFException;
import edu.isi.bmkeg.pdf.extraction.exceptions.EncryptionException;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.factory.AbstractModelFactory;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;

public class JPedalExtractor implements Extractor {

	Set<WordBlock> wordListPerPage = null;
	PdfDecoder PDFDecoder = null;
	int currentPage = 1;
	int pageCount;
	private static Document xmlDocument;
	private static DocumentBuilder docBuilder;

	private static int pageHeight;
	private static int pageWidth;
	private AbstractModelFactory modelFactory;
	static {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JPedalExtractor(AbstractModelFactory modelFactory)
			throws FileNotFoundException, IOException, PdfException, Exception {
		this.modelFactory = modelFactory;
		this.PDFDecoder = new PdfDecoder(false);

		PDFDecoder.setExtractionMode(PdfDecoder.TEXT); // extract just text
		PDFDecoder.init(true);
		PdfGroupingAlgorithms.useUnrotatedCoords = true;
		// if you do not require XML content, pure text extraction is much
		// faster.
		PDFDecoder.useXMLExtraction();

		System.setProperty("hacked", "true");

	}

	public void init(String fileName) throws PdfException, AccessException,
			EncryptionException {
		if (PDFDecoder.isOpen()) {
			PDFDecoder.flushObjectValues(true);
			PDFDecoder.closePdfFile();

		}
		PDFDecoder.openPdfFile(fileName);
		currentPage = 1;
		pageCount = PDFDecoder.getPageCount();
		if (!PDFDecoder.isExtractionAllowed()) {
			throw new AccessException(fileName);
		} else if (PDFDecoder.isEncrypted()) {
			throw new EncryptionException(fileName);
		}

	}

	private int[] generatePageBoundaries(PdfPageData currentPageData) {
		int[] dimensions = new int[4];
		// 0:TLX, 1:TLY, 2:BRX, 3:BRY

		// Using just cropbox
		if (currentPageData.getCropBoxHeight(currentPage) != currentPageData
				.getMediaBoxHeight(currentPage)) {
			dimensions[0] = currentPageData.getCropBoxX(currentPage);
			dimensions[2] = currentPageData.getCropBoxWidth(currentPage)
					+ dimensions[0];

			dimensions[3] = currentPageData.getCropBoxY(currentPage);
			dimensions[1] = currentPageData.getCropBoxHeight(currentPage)
					+ dimensions[3];
		} else {
			dimensions[0] = currentPageData.getMediaBoxX(currentPage);
			dimensions[2] = currentPageData.getMediaBoxWidth(currentPage)
					+ dimensions[0];

			dimensions[3] = currentPageData.getMediaBoxY(currentPage);
			dimensions[1] = currentPageData.getMediaBoxHeight(currentPage)
					+ dimensions[3];
		}
		return dimensions;

	}

	private String getFontData(String xml, String item)
			throws UnsupportedEncodingException, IOException {
		xml = "<root>" + xml + "</root>";

		try {
			xmlDocument = docBuilder.parse(new ByteArrayInputStream(xml
					.getBytes("UTF-8")));
		} catch (SAXException e) {

			// e.printStackTrace();
			return null;
		}

		Element font = (Element) xmlDocument.getElementsByTagName("font").item(
				0);
		return font.getAttribute(item);

	}

	private void decodeFile() throws Exception {

		String font = null;
		String currentWord;
		String style = null;

		PDFDecoder.decodePage(currentPage);
		
		PdfGroupingAlgorithms currentGrouping = PDFDecoder.getGroupingObject();

		PdfPageData currentPageData = PDFDecoder.getPdfPageData();
		int[] dimensions;

		// pageHeight.add(currentPageData.getCropBoxHeight(page));
		// pageWidth.add(currentPageData.getCropBoxWidth(page));

		dimensions = generatePageBoundaries(currentPageData);
		pageWidth = Math.abs(dimensions[2] - dimensions[0]);
		pageHeight = Math.abs(dimensions[1] - dimensions[3]);
		List words = null;
		//currentGrouping.extractTextAsWordlist(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7)
		words = currentGrouping.extractTextAsWordlist(dimensions[0],
				dimensions[1], dimensions[2], dimensions[3], currentPage,
				true, "&:=()!;\\/\"\"\'\'");

		if (words == null)
			throw new EmptyPDFException("filename");

		Iterator wordIterator = words.iterator();

		if (wordListPerPage == null)
			wordListPerPage = new TreeSet<WordBlock>(new SpatialOrdering(SpatialOrdering.MIXED_MODE_ABSOLUTE));
		else {
			wordListPerPage.clear();
		}

		while (wordIterator.hasNext()) {

			currentWord = (String) wordIterator.next();

			font = getFontData(currentWord, "face");
			style = getFontData(currentWord, "style");
			currentWord = Strip.convertToText(currentWord);
			
			int wx1 = roundUp(Float.parseFloat((wordIterator.next() + "")));
			int wy1 = roundUp(Float.parseFloat((wordIterator.next() + "")));
			int wx2 = roundUp(Float.parseFloat((wordIterator.next() + "")));
			int wy2 = roundUp(Float.parseFloat((wordIterator.next() + "")));

			wy1 = dimensions[1] - wy1;
			wy2 = dimensions[1] - wy2;

			WordBlock wordBlock = modelFactory.createWordBlock(wx1, wy1, wx2,
					wy2, 1, font, style, currentWord);

			wordListPerPage.add(wordBlock);
		}
		currentPage++;
		PDFDecoder.flushObjectValues(false);
	}

	@Override
	public boolean hasNext() {
		if (currentPage <= pageCount) {

			try {
				decodeFile();
			} catch (EmptyPDFException e) {
				return false;
			} catch (Exception e)
			{
				return false;
			}

			return true;
		} else {
			PDFDecoder.flushObjectValues(true);
			PDFDecoder.closePdfFile();
			return false;
		}

	}

	@Override
	public List<WordBlock> next() {
		// TODO Auto-generated method stub
		return new ArrayList<WordBlock>(wordListPerPage);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCurrentPageBoxHeight() {

		return pageHeight;
	}

	@Override
	public int getCurrentPageBoxWidth() {

		return pageWidth;
	}

	private int roundUp(double value) {
		
		value = Math.floor(value);

		return (int) value;
	}

}
