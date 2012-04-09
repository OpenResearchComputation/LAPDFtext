package edu.isi.bmkeg.pdf.model;

import java.util.ArrayList;
import java.util.List;

import edu.isi.bmkeg.pdf.extraction.exceptions.InvalidPopularSpaceValueException;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.utils.IntegerFrequencyCounter;

public class Document {
	private ArrayList<PageBlock> pageList;
	private IntegerFrequencyCounter avgHeightFrequencyCounter;
	private int mostPopularWordHeight = -1;
	private boolean jPedalDecodeFailed;
	public boolean hasjPedalDecodeFailed()
	{
		return jPedalDecodeFailed;
	}

	public void setjPedalDecodeFailed(boolean jPedalDecodeFailed)
	{
		this.jPedalDecodeFailed = jPedalDecodeFailed;
	}

	public Document() {
		this.avgHeightFrequencyCounter = new IntegerFrequencyCounter(1);
	}

	public int getTotalNumberOfPages() {
		return this.pageList.size();
	}

	public void addPages(List<PageBlock> pageList) {
		this.pageList=new ArrayList<PageBlock>(pageList);
	}

	public PageBlock getPage(int pageNumber){

		return pageList.get(pageNumber-1);
	}

	public ChunkBlock getLastChunkBlock(ChunkBlock chunk) throws InvalidPopularSpaceValueException {
		int pageNumber = ((PageBlock) chunk.getContainer()).getPageNumber();
		PageBlock page=this.getPage(pageNumber);
		if(page.getMostPopularVerticalSpaceBetweenWordsPage()<0&&page.getMostPopularWordHeightPage()>page.getMostPopularWordWidthPage()*2){//page.getMostPopularWordHeightPage()>page.getMostPopularWordWidthPage()*2
			System.err.println("Possible page with vertical text flow at page number +"+pageNumber);
			//throw new InvalidPopularSpaceValueException("Possible page with vertical text flow at page number +"+pageNumber);
		}

		if (chunk.getLastChunkBlock() != null) {
			//System.out.println("Same page");
			return chunk.getLastChunkBlock();
		} else {
			pageNumber = ((PageBlock) chunk.getContainer()).getPageNumber() - 1;

			if (pageNumber == 0){
				return null;
			}

			page=this.getPage(pageNumber);
			List<ChunkBlock> sortedChunkBlockList = page.getAllChunkBlocks(SpatialOrdering.COLUMN_AWARE_MIXED_MODE);
			//System.out.println("Page:"+ pageNumber);
			return sortedChunkBlockList.get(sortedChunkBlockList.size() - 1);
		}

	}


	public int getMostPopularWordHeight() {
		if (mostPopularWordHeight != -1) {
			avgHeightFrequencyCounter = null;
			return mostPopularWordHeight;
		}

		int mostPopular = avgHeightFrequencyCounter.getMostPopular();
		double mostPopularCount = avgHeightFrequencyCounter
		.getCount(mostPopular);
		int secondMostPopular = avgHeightFrequencyCounter.getNextMostPopular();
		double secondMostPopularCount = avgHeightFrequencyCounter
		.getCount(secondMostPopular);
		double ratio = secondMostPopularCount / mostPopularCount;
		if (secondMostPopular > mostPopular && ratio > 0.8) {
			mostPopularWordHeight = secondMostPopular;
		} else {
			mostPopularWordHeight = mostPopular;
		}

		return mostPopularWordHeight;
	}

	public void addToWordHeightFrequencyCounter(int height){
		avgHeightFrequencyCounter.add(height);
	}

}
