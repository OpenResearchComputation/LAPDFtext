package edu.isi.bmkeg.pdf.extraction;

import java.util.Iterator;
import java.util.List;

import edu.isi.bmkeg.pdf.model.WordBlock;

public interface Extractor extends Iterator<List<WordBlock>>{
   public int getCurrentPageBoxHeight();
   public int getCurrentPageBoxWidth();
}
