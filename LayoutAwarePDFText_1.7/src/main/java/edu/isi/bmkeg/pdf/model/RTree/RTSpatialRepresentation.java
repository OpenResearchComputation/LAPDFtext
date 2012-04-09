package edu.isi.bmkeg.pdf.model.RTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

import edu.isi.bmkeg.pdf.extraction.exceptions.InvalidPopularSpaceValueException;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.pdf.model.spatial.SpatialEntity;
import edu.isi.bmkeg.pdf.model.spatial.SpatialRepresentation;
import edu.isi.bmkeg.utils.IntegerFrequencyCounter;

public class RTSpatialRepresentation implements SpatialRepresentation {

	private HashMap<Integer, WordBlock> indexToWordBlockMap;
	private HashMap<Integer, ChunkBlock> indexToChunkBlockMap;

	private int mostPopularHorizontalSpaceBetweenWords = -1;
	private int mostPopularWordWidth = -1;
	private int mostPopularVerticalSpaceBetweenWords = -1;
	private int mostPopularWordHeightPerPage = -1;
	private int[] margin = null;
	private List<WordBlock> list = null;
	private RTree tree;

	protected RTSpatialRepresentation() {

		this.indexToWordBlockMap = new HashMap<Integer, WordBlock>();
		this.indexToChunkBlockMap = new HashMap<Integer, ChunkBlock>();
		tree = new RTree();

		Properties prp = new Properties();
		prp.setProperty("MaxNodeEntries", "" + 1500);
		prp.setProperty("MinNodeEntries", "" + 1);
		tree.init(prp);

	}

	@Override
	public void add(SpatialEntity entity, int id) {
        
		RTSpatialEntity rtSpatialEntity = (RTSpatialEntity) entity;
		rtSpatialEntity.setId(id);
		if (rtSpatialEntity instanceof ChunkBlock) {
			this.indexToChunkBlockMap.put(id, (ChunkBlock) rtSpatialEntity);
		} else {
			this.indexToWordBlockMap.put(id, (WordBlock) rtSpatialEntity);

		}
		tree.add(rtSpatialEntity, id);

	}

	@Override
	public int addAll(List<SpatialEntity> list, int startId) {
		for (SpatialEntity entity : list)
			this.add(entity, startId++);
		return startId;
	}

	public List<SpatialEntity> intersects(SpatialEntity entity, String ordering) {

		return this.intersectsByType(entity, ordering, null);
	}

	@Override
	public SpatialEntity getEntity(int id) {
		if (indexToWordBlockMap.containsKey(id))
			return indexToWordBlockMap.get(id);

		return indexToChunkBlockMap.get(id);
	}

	@Override
	public List<ChunkBlock> getAllChunkBlocks(String ordering) {

		List<ChunkBlock> list = new ArrayList<ChunkBlock>(indexToChunkBlockMap
				.values());
		if (ordering != null) {
			Collections.sort(list, new SpatialOrdering(ordering));
		}

		return list;
	}

	@Override
	public int[] getMargin() {
		if (margin == null) {
			margin = new int[4];
			Rectangle marginRect = tree.getBounds();

			margin[0] = (int) marginRect.minX;
			margin[1] = (int) marginRect.minY;
			margin[2] = (int) marginRect.maxX;
			margin[3] = (int) marginRect.maxY;

			return margin;
		}

		return margin;
	}

	@Override
	public int getMedian() {
		if (margin == null)
			this.getMargin();
		return margin[0] + (margin[2] - margin[0]) / 2;
	}

	@Override
	public List<SpatialEntity> contains(SpatialEntity entity, String ordering) {

		return this.containsByType(entity, ordering, null);
	}

	@Override
	public boolean delete(SpatialEntity entity, int id) {

		RTSpatialEntity rtSpatialEntity = (RTSpatialEntity) entity;

		if (indexToChunkBlockMap.containsKey(id))
			indexToChunkBlockMap.remove(id);
		else
			indexToWordBlockMap.remove(id);
		return tree.delete(rtSpatialEntity, id);

	}

	@Override
	public List<SpatialEntity> intersectsByType(SpatialEntity entity,
			String ordering, Class classType) {

		RTProcedure procedure = new RTProcedure(this, ordering,
				(RTSpatialEntity) entity, classType, false);

		tree.intersects((RTSpatialEntity) entity, procedure);

		return procedure.getIntersectionList();
	}

	@Override
	public List<WordBlock> getAllWordBlocks(String ordering) {
		List<WordBlock> list = new ArrayList<WordBlock>(indexToWordBlockMap
				.values());
		if (ordering != null) {
			Collections.sort(list, new SpatialOrdering(ordering));
		}

		return list;
	}

	@Override
	public List<SpatialEntity> containsByType(SpatialEntity entity,
			String ordering, Class classType) {

		RTProcedure procedure = new RTProcedure(this, ordering,
				(RTSpatialEntity) entity, classType, true);

		tree.contains((RTSpatialEntity) entity, procedure);
        if(procedure.getIntersectionList().size()==0){
        	List<SpatialEntity> intersectList=this.intersectsByType(entity, ordering, classType);
        	List<SpatialEntity> returnList=new ArrayList<SpatialEntity>();
        	for(SpatialEntity loopEntity:intersectList){
        		  
        		if(entity.getX1()<=loopEntity.getX1()&&entity.getX2()>=loopEntity.getX2()&&entity.getY1()<=loopEntity.getY1()&&entity.getY2()>=loopEntity.getY2())
        			returnList.add(loopEntity);
        		return returnList;
        	}
        	
        }
		return procedure.getIntersectionList();
	}

	@Override
	public int getMostPopularHorizontalSpaceBetweenWordsPage() throws InvalidPopularSpaceValueException {
		if (mostPopularHorizontalSpaceBetweenWords != -1) {

			return mostPopularHorizontalSpaceBetweenWords;
		}
		IntegerFrequencyCounter avgHorizontalSpaceBetweenWordFrequencyCounter = new IntegerFrequencyCounter(
				1);
		if (list == null)
			list = this.getAllWordBlocks(SpatialOrdering.MIXED_MODE);
		int lastX2 = list.get(0).getX2();
		int space;
		for (WordBlock block : list) {
			space = block.getX1() - lastX2;
			if (space > 0) {
				avgHorizontalSpaceBetweenWordFrequencyCounter.add(space);
			}
			lastX2 = block.getX2();
		}

		int mostPopular = avgHorizontalSpaceBetweenWordFrequencyCounter
				.getMostPopular();
		double mostPopularCount = avgHorizontalSpaceBetweenWordFrequencyCounter
				.getCount(mostPopular);
		int secondMostPopular = avgHorizontalSpaceBetweenWordFrequencyCounter
				.getNextMostPopular();
		double secondMostPopularCount = avgHorizontalSpaceBetweenWordFrequencyCounter
				.getCount(secondMostPopular);
		double ratio = secondMostPopularCount / mostPopularCount;
		if (secondMostPopular > mostPopular && ratio > 0.8) {
			mostPopularHorizontalSpaceBetweenWords = secondMostPopular;
		} else {
			mostPopularHorizontalSpaceBetweenWords = mostPopular;
		}
		if(mostPopularHorizontalSpaceBetweenWords==-1){
			throw new InvalidPopularSpaceValueException("RTSpatialRepresentation.getMostPopularHorizontalSpaceBetweenWordsPage");
		}
		propagateCalculation();
		//System.out.println("Returning mostPopularHorizontalSpaceBetweenWords"+mostPopularHorizontalSpaceBetweenWords);
		return mostPopularHorizontalSpaceBetweenWords;
	}

	private void propagateCalculation() throws InvalidPopularSpaceValueException {
		if (mostPopularHorizontalSpaceBetweenWords == -1) {
			getMostPopularHorizontalSpaceBetweenWordsPage();
		}
		if (mostPopularWordWidth == -1) {
			getMostPopularWordWidthPage();
		}
		if (mostPopularVerticalSpaceBetweenWords == -1) {
			getMostPopularVerticalSpaceBetweenWordsPage();
		}
		if (mostPopularWordHeightPerPage == -1) {
			getMostPopularWordHeightPage();
		}
		list = null;
	}

	@Override
	public int getMostPopularVerticalSpaceBetweenWordsPage() throws InvalidPopularSpaceValueException {
		if (mostPopularVerticalSpaceBetweenWords != -1) {

			return mostPopularVerticalSpaceBetweenWords;
		}
		IntegerFrequencyCounter verticalSpaceBetweenWordFrequencyCounter = new IntegerFrequencyCounter(
				1);
		if (list == null)
			list = this.getAllWordBlocks(SpatialOrdering.MIXED_MODE);
		int lastX2 = list.get(0).getX2();
		int firstY2 = list.get(0).getY2();
		int space;
		for (WordBlock block : list) {
			space = block.getX1() - lastX2;
			if (space < 0) {
				verticalSpaceBetweenWordFrequencyCounter.add(block.getY1()
						- firstY2);
				firstY2 = block.getY2();

			}
			lastX2 = block.getX2();
		}

		int mostPopular = verticalSpaceBetweenWordFrequencyCounter
				.getMostPopular();
		double mostPopularCount = verticalSpaceBetweenWordFrequencyCounter
				.getCount(mostPopular);
		int secondMostPopular = verticalSpaceBetweenWordFrequencyCounter
				.getNextMostPopular();
		double secondMostPopularCount = verticalSpaceBetweenWordFrequencyCounter
				.getCount(secondMostPopular);
		double ratio = secondMostPopularCount / mostPopularCount;
		if (secondMostPopular > mostPopular && ratio > 0.8) {
			mostPopularVerticalSpaceBetweenWords = secondMostPopular;
		} else {
			mostPopularVerticalSpaceBetweenWords = mostPopular;
		}
		if(mostPopularVerticalSpaceBetweenWords==-1){
			throw new InvalidPopularSpaceValueException("RTSpatialRepresentation.getMostPopularVerticalSpaceBetweenWordsPage");
		}
		propagateCalculation();
		//System.out.println("Returning mostPopularVerticalSpaceBetweenWords"+mostPopularVerticalSpaceBetweenWords);
		return mostPopularVerticalSpaceBetweenWords;
	}

	@Override
	public int getMostPopularWordWidthPage() {
		if (mostPopularWordWidth != -1) {
			return mostPopularWordWidth;
		}
		IntegerFrequencyCounter avgWordWidthFrequencyCounter = new IntegerFrequencyCounter(
				1);
		if (list == null)
			list = this.getAllWordBlocks(null);

		for (WordBlock block : list)
			avgWordWidthFrequencyCounter.add(block.getWidth());

		int mostPopular = avgWordWidthFrequencyCounter.getMostPopular();
		double mostPopularCount = avgWordWidthFrequencyCounter
				.getCount(mostPopular);
		int secondMostPopular = avgWordWidthFrequencyCounter
				.getNextMostPopular();
		double secondMostPopularCount = avgWordWidthFrequencyCounter
				.getCount(secondMostPopular);
		double ratio = secondMostPopularCount / mostPopularCount;
		if (secondMostPopular > mostPopular && ratio > 0.8) {
			mostPopularWordWidth = secondMostPopular;
		} else {
			mostPopularWordWidth = mostPopular;
		}
		propagateWordBasedCalculation();
		return mostPopularWordWidth;
	}

	@Override
	public int getMostPopularWordHeightPage() {
		if (mostPopularWordHeightPerPage != -1) {
			return mostPopularWordHeightPerPage;
		}
		IntegerFrequencyCounter avgWordWidthFrequencyCounter = new IntegerFrequencyCounter(
				1);
		if (list == null)
			list = this.getAllWordBlocks(null);

		for (WordBlock block : list)
			avgWordWidthFrequencyCounter.add(block.getHeight());

		int mostPopular = avgWordWidthFrequencyCounter.getMostPopular();
		double mostPopularCount = avgWordWidthFrequencyCounter
				.getCount(mostPopular);
		int secondMostPopular = avgWordWidthFrequencyCounter
				.getNextMostPopular();
		double secondMostPopularCount = avgWordWidthFrequencyCounter
				.getCount(secondMostPopular);
		double ratio = secondMostPopularCount / mostPopularCount;
		if (secondMostPopular > mostPopular && ratio > 0.8) {
			mostPopularWordHeightPerPage = secondMostPopular;
		} else {
			mostPopularWordHeightPerPage = mostPopular;
		}
		propagateWordBasedCalculation();
		return mostPopularWordHeightPerPage;
	}

	private void propagateWordBasedCalculation() {
		if (mostPopularWordWidth == -1) {
			getMostPopularWordWidthPage();
		}
		
		if (mostPopularWordHeightPerPage == -1) {
			getMostPopularWordHeightPage();
		}
		list = null;
		
	}
	
	

}
