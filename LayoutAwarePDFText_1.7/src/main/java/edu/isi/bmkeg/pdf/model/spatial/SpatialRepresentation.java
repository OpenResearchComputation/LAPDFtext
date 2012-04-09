package edu.isi.bmkeg.pdf.model.spatial;

import java.util.Collection;
import java.util.List;

import edu.isi.bmkeg.pdf.extraction.exceptions.InvalidPopularSpaceValueException;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;

public interface SpatialRepresentation {
	
	public void add(SpatialEntity entity,int id);

	public List<SpatialEntity> intersects(SpatialEntity entity,String ordering);
	public int addAll(List<SpatialEntity> list, int startId);
	public List<ChunkBlock> getAllChunkBlocks(String ordering);
	public List<WordBlock> getAllWordBlocks(String ordering);
	 public int getMedian();
     public int[] getMargin();
     public List<SpatialEntity> contains(SpatialEntity entity, String ordering);
     public List<SpatialEntity> containsByType(SpatialEntity entity, String ordering,Class classType);
     public boolean delete(SpatialEntity entity,int id);
     public List<SpatialEntity> intersectsByType(SpatialEntity entity,String ordering,Class classType);
     public SpatialEntity getEntity(int id);
    
     public int getMostPopularWordHeightPage(); 
     public int getMostPopularHorizontalSpaceBetweenWordsPage() throws InvalidPopularSpaceValueException;
     public int getMostPopularWordWidthPage();
     public int getMostPopularVerticalSpaceBetweenWordsPage() throws InvalidPopularSpaceValueException;
     

}
