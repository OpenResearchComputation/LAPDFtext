package edu.isi.bmkeg.pdf.model.RTree;

import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.Document;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.factory.AbstractModelFactory;

public class RTModelFactory implements AbstractModelFactory{

	@Override
	public ChunkBlock createChunkBlock(int topX, int topY, int bottomX,
			int bottomY) {
		
		ChunkBlock chunky=new RTChunkBlock(topX, topY, bottomX, bottomY);
		
		return chunky;
	}

	@Override
	public PageBlock createPageBlock(int pageNumber, 
			int pageWidth,int pageHeight, Document document) {
		
		return new RTPageBlock(pageNumber, pageWidth, pageHeight, document);
	}

	@Override
	public WordBlock createWordBlock(int topX, int topY, int bottomX,
			int bottomY, int spaceWidth, String font, String style, String word) {
		
		WordBlock wordy=new RTWordBlock(topX, topY, bottomX, bottomY, spaceWidth, font, style, word);
		
		return wordy;
	}

}
