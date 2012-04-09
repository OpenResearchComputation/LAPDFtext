package edu.isi.bmkeg.pdf.model.RTree;

import java.util.Collections;
import java.util.List;

import edu.isi.bmkeg.pdf.model.Block;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.pdf.model.spatial.SpatialEntity;

public class RTChunkBlock extends RTSpatialEntity implements ChunkBlock {

	private Block container;
	private int mostPopularWordHeight;
	private int mostPopularWordSpaceWidth;
	private String mostPopularWordFont;
	private String mostPopularWordStyle;
	private String allignment = null;
	private String type = Block.TYPE_UNCLASSIFIED;
	private Boolean headerOrFooter=null;

	public RTChunkBlock(int x1, int y1, int x2,int y2) {
		super(x1, y1, x2, y2);

	}

	@Override
	public Block getContainer() {
		return container;
	}

	@Override
	public int getMostPopularWordHeight() {

		return mostPopularWordHeight;
	}

	public int getMostPopularWordSpaceWidth() {
		return mostPopularWordSpaceWidth;
	}

	public void setMostPopularWordSpaceWidth(int mostPopularWordSpaceWidth) {
		this.mostPopularWordSpaceWidth = mostPopularWordSpaceWidth;
	}

	public String getMostPopularWordFont() {
		return mostPopularWordFont;
	}

	public void setMostPopularWordFont(String mostPopularWordFont) {
		this.mostPopularWordFont = mostPopularWordFont;
	}

	public void setMostPopularWordHeight(int height) {
		this.mostPopularWordHeight = height;
	}

	@Override
	public String getLeftRightMedLine() {
		if (allignment != null)
			return allignment;
		PageBlock parent = (PageBlock) this.getContainer();
		int median = parent.getMedian();
		int X1 = this.getX1();
		int width = this.getWidth();
		int averageWordHeightForTheDocument = parent.getDocument().getMostPopularWordHeight();

		// Conditions for left
		if (X1 < median
				&& (X1 + width) < (median + averageWordHeightForTheDocument))
			return LEFT;
		// conditions for right
		if (X1 > median)
			return RIGHT;
		// conditions for midline
		int left = median - X1;
		int right = X1 + width - median;
		/*
		 * Doubtful code if(right <= 0) return LEFT;
		 */
		double leftIsToRight = (double) left / (double) right;
		double rightIsToLeft = (double) right / (double) left;
		if (leftIsToRight < 0.05)
			allignment = RIGHT;
		else if (rightIsToLeft < 0.05)
			allignment = LEFT;
		else
			allignment = MIDLINE;

		return allignment;
	}

	public boolean isFlush(String condition, int value) {
		PageBlock parent = (PageBlock) this.getContainer();
		int median = parent.getMedian();
		String leftRightMidline = this.getLeftRightMedLine();

		int x1 = this.getX1();
		int x2 = this.getX2();
		int marginX1 = parent.getMargin()[0];
		int marginX2 = parent.getMargin()[3];

		if (condition.equals(MIDLINE)) {
			if (leftRightMidline.equals(MIDLINE))
				return false;
			else if (leftRightMidline.equals(LEFT)
					&& Math.abs(x2 - median) < value)
				return true;
			else if (leftRightMidline.equals(RIGHT)
					&& Math.abs(x1 - median) < value)
				return true;
		} else if (condition.equals(LEFT)) {
			if (leftRightMidline.equals(MIDLINE)
					&& Math.abs(x1 - marginX1) < value)
				return true;
			else if (leftRightMidline.equals(LEFT)
					&& Math.abs(x1 - marginX1) < value)
				return true;
			else if (leftRightMidline.equals(RIGHT))
				return false;
		} else if (condition.equals(RIGHT)) {
			if (leftRightMidline.equals(MIDLINE)
					&& Math.abs(x2 - marginX2) < value)
				return true;
			else if (leftRightMidline.equals(LEFT))
				return false;
			else if (leftRightMidline.equals(RIGHT)
					&& Math.abs(x2 - marginX2) < value)
				return true;
		}
		return false;
	}

	@Override
	public int getId() {

		return super.getId();
	}

	@Override
	public int getNumberOfLine() {
		PageBlock parent = (PageBlock) this.container;
		List<SpatialEntity> wordBlockList = parent.containsByType(this,
				SpatialOrdering.MIXED_MODE, WordBlock.class);
		if (wordBlockList.size() == 0)
			return 0;
		WordBlock block = (WordBlock) wordBlockList.get(0);
		int numberOfLines = 1;
		int lastY = block.getY1() + block.getHeight() / 2;
		int currentY = lastY;
		for (SpatialEntity entity : wordBlockList) {
			lastY = currentY;
			block = (WordBlock) entity;
			currentY = block.getY1() + block.getHeight() / 2;
			if (currentY > lastY + block.getHeight() / 2)
				numberOfLines++;

		}
		return numberOfLines;
	}

	@Override
	public String getchunkText() {
		List<SpatialEntity> wordBlockList = ((PageBlock) container)
				.containsByType(this, SpatialOrdering.MIXED_MODE,
						WordBlock.class);
		StringBuilder builder = new StringBuilder();
		for (SpatialEntity entity : wordBlockList) {
			builder.append(((WordBlock) entity).getWord());
			if(!((WordBlock) entity).getWord().endsWith("-"))
			builder.append(" ");
		}
		return builder.toString().trim();

	}

	@Override
	public void setContainer(Block block) {
		this.container = (PageBlock) block;

	}

	@Override
	public String getType() {

		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;

	}

	@Override
	public ChunkBlock getLastChunkBlock() {
		
		List<ChunkBlock> sortedChunkBlockList = ((PageBlock) this
				.getContainer())
				.getAllChunkBlocks(SpatialOrdering.COLUMN_AWARE_MIXED_MODE);

		int index = Collections.binarySearch(sortedChunkBlockList, this,
				new SpatialOrdering(SpatialOrdering.COLUMN_AWARE_MIXED_MODE));

		return (index <= 0) ? null : sortedChunkBlockList.get(index - 1);
	}

	@Override
	public String getMostPopularWordStyle() {
		
		return mostPopularWordStyle;
	}

	@Override
	public void setMostPopularWordStyle(String style) {
		
		this.mostPopularWordStyle=style;
	}

	@Override
	public Boolean isHeaderOrFooter() {
		
		return headerOrFooter;
	}

	@Override
	public void setHeaderOrFooter(boolean headerOrFooter) {
	
		this.headerOrFooter=headerOrFooter;
	}
	

}
