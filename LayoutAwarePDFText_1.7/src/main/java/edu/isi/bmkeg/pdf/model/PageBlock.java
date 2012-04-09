package edu.isi.bmkeg.pdf.model;

import java.util.List;

import edu.isi.bmkeg.pdf.model.spatial.SpatialRepresentation;

public interface PageBlock extends Block,SpatialRepresentation{
         public int getPageNumber();
         public int initialize(List<WordBlock> list, int startId);
         public int getPageBoxHeight();
         public int getPageBoxWidth();
         public Document getDocument();
}
