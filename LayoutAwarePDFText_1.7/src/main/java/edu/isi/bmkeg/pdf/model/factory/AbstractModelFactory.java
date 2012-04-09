package edu.isi.bmkeg.pdf.model.factory;

import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.Document;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;

public interface AbstractModelFactory {

public ChunkBlock createChunkBlock(int topX,int topY, int bottomX,int bottomY);
public WordBlock createWordBlock(int topX,int topY, int bottomX,int bottomY,int spaceWidth,String font,String style,String word);
public PageBlock createPageBlock(int pageNumber,int pageHeight,int pageWidth,Document document);
	
}
