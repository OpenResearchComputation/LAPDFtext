package edu.isi.bmkeg.pdf.features;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.isi.bmkeg.pdf.extraction.exceptions.InvalidPopularSpaceValueException;
import edu.isi.bmkeg.pdf.model.Block;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.factory.AbstractModelFactory;
import edu.isi.bmkeg.pdf.model.spatial.SpatialEntity;

public class ChunkFeatures {

	private ChunkBlock chunk;
	private PageBlock parent;
	private static Pattern patternLowerCase = Pattern.compile("[a-z]");
	private static Pattern patternUpperCase = Pattern.compile("[A-Z]");
	public static int CENTER = -1;
	public static int NORTH = 0;
	public static int SOUTH = 1;
	public static int EAST = 2;
	public static int WEST = 3;
	public static int NORTH_SOUTH = 4;
	public static int EAST_WEST = 5;
	private static AbstractModelFactory modelFactory;

	public ChunkFeatures(ChunkBlock chunk, AbstractModelFactory modelFactory) {
		this.chunk = chunk;
		this.parent = (PageBlock) chunk.getContainer();
		this.modelFactory = modelFactory;
	}
	/**
	 * returns the difference between the most popular font size in the in the current chunk 
	 * and the most popular font size in the document.
	 * @return
	 */
	public int getHeightDifferenceBetweenChunkWordAndDocumentWord() {
		return chunk.getMostPopularWordHeight()
		- parent.getDocument().getMostPopularWordHeight();
	}
	/**
	 * returns true if chunk block is left aligned
	 * @return
	 */
	public boolean isAllignedLeft() {
		if (Block.LEFT.equalsIgnoreCase(chunk.getLeftRightMedLine()))
			return true;
		return false;
	}
	/**
	 * returns the most popular font size in the chunk block
	 * @return
	 */
	public int getMostPopularFontSize() {
		String fontStyle = chunk.getMostPopularWordStyle();
		if(fontStyle==null)
			return chunk.getMostPopularWordHeight();
		int fontSizeIndex = fontStyle.indexOf("font-size");
		int colonIndex = fontStyle.indexOf(":", fontSizeIndex);
		int ptIndex = fontStyle.indexOf("pt", colonIndex);
		return Integer.parseInt(fontStyle.substring(colonIndex + 1, ptIndex));
	}
	/**
	 * returns true if chunk block is right aligned
	 * @return
	 */
	public boolean isAllignedRight() {
		if (Block.RIGHT.equalsIgnoreCase(chunk.getLeftRightMedLine()))
			return true;
		return false;
	}
	/**
	 * returns true if chunk block is center aligned
	 * @return
	 */
	public boolean isAllignedMiddle() {
		if (Block.MIDLINE.equalsIgnoreCase(chunk.getLeftRightMedLine()))
			return true;
		return false;
	}
	/**
	 * returns true if chunk block contains mostly capitalized text
	 * @return
	 */
	public boolean isAllCapitals() {
		String chunkText = chunk.getchunkText();
		Matcher matcher = patternLowerCase.matcher(chunkText);
		if (matcher.find()) {

			return false;
		} else {
			matcher = patternUpperCase.matcher(chunkText);
			if (matcher.find()) {
				return true;
			} else {
				return false;
			}

		}
	}
	/**
	 * returns true if chunk block contains mostly bold face text
	 * @return
	 */
	public boolean isMostPopularFontModifierBold() {

		if ((chunk.getMostPopularWordStyle() != null && chunk
				.getMostPopularWordStyle().indexOf("Bold") != -1)
				|| (chunk.getMostPopularWordFont() != null && (chunk
						.getMostPopularWordFont().indexOf("Bold") != -1 || chunk
						.getMostPopularWordFont().indexOf("-B") != -1))) {
			return true;
		}
		return false;
	}
	/**
	 * returns true if chunk block contains mostly italicized  text
	 * @return
	 */
	public boolean isMostPopularFontModifierItalic() {
		if ((chunk.getMostPopularWordStyle() != null && chunk
				.getMostPopularWordStyle().indexOf("Italic") != -1)
				|| (chunk.getMostPopularWordFont() != null && chunk
						.getMostPopularWordFont().indexOf("Italic") != -1)) {
			return true;
		}
		return false;
	}
	/**
	 * returns true if chunk block contains the first line of a page's text
	 * @return
	 */
	public boolean isContainingFirstLineOfPage() {

		if (Math.abs(chunk.getY1() - parent.getMargin()[1]) < parent
				.getDocument().getMostPopularWordHeight())
			return true;
		else
			return false;
	}

	/**
	 * returns true if chunk block contains the last line of a page's text
	 * @return
	 */
	public boolean isContainingLastLineOfPage() {
		if (Math.abs(chunk.getY2() - parent.getMargin()[3]) < parent
				.getDocument().getMostPopularWordHeight())
			return true;
		else
			return false;
	}

	/**
	 * returns true if chunk block is an outlier or stray block
	 * @return
	 */
	public boolean isOutlier() {
		// TODOUseReflections
		ChunkBlock block = modelFactory.createChunkBlock(chunk.getX1(), chunk
				.getY1() - 30, chunk.getX2(), chunk.getY2() + 60);
		int neighbouringChunksCount = parent.intersectsByType(block, null,
				ChunkBlock.class).size();
		int wordBlockCount = parent
		.containsByType(chunk, null, WordBlock.class).size();
		int chunkTextSizeAfterAlphaNumericTruncation = chunk.getchunkText()
		.replaceAll("[A-Za-z0-9]", "").length();
		if ((wordBlockCount < 10 && neighbouringChunksCount < 10)
				|| (chunkTextSizeAfterAlphaNumericTruncation < 10 && neighbouringChunksCount < 10)
				|| chunk.getMostPopularWordHeight() > 50)
			return true;
		return false;
	}

	public int getChunkTextLength() {
		return chunk.getchunkText().length();
	}

	/**
	 * returns the word block density in a chunk block
	 * @return
	 */
	public double getDensity() {
		List<SpatialEntity> wordBlockList = parent.containsByType(chunk, null,
				WordBlock.class);
		double areaCoveredByWordBlocks = 0;
		for (SpatialEntity entity : wordBlockList)
			areaCoveredByWordBlocks = areaCoveredByWordBlocks
			+ (entity.getHeight() * entity.getWidth());
		return areaCoveredByWordBlocks / (chunk.getHeight() * chunk.getWidth());
	}

	/**
	 * returns true if the chunk block is aligned with column boundaries
	 * @return
	 */
	public boolean isAllignedWithColumnBoundaries() {
		String lrm = chunk.getLeftRightMedLine();
		int columnLeft = 0;
		int columnRight = 0;
		double threshold = chunk.getMostPopularWordHeight() * 1.5;
		if (Block.MIDLINE.equalsIgnoreCase(lrm)) {
			return false;
		} else if (Block.LEFT.equalsIgnoreCase(lrm)) {
			columnLeft = parent.getMargin()[0];
			columnRight = parent.getMedian();
		} else if (Block.RIGHT.equalsIgnoreCase(lrm)) {
			columnLeft = parent.getMedian();
			columnRight = parent.getMargin()[2];
		}

		if (chunk.getNumberOfLine() > 1
				&& Math.abs(chunk.getX1() - columnLeft) < threshold
				&& Math.abs(chunk.getX2() - columnRight) < threshold) {
			return true;
		} else if (chunk.getNumberOfLine() == 1
				&& Math.abs(chunk.getX1() - columnLeft) < threshold) {
			return true;
		}
		return false;
	}

	/**
	 * returns the classification assigned to previous chunk block
	 * @return
	 */
	public String getlastClassification() {

		ChunkBlock lastBlock = chunk.getLastChunkBlock();

		return (lastBlock == null) ? null : lastBlock.getType();

	}

	/**
	 * returns the section lable of chunk
	 * @return
	 * @throws InvalidPopularSpaceValueException
	 */
	public String getSection() throws InvalidPopularSpaceValueException {
		
		ChunkBlock lastBlock = null;
		try {
			lastBlock = parent.getDocument().getLastChunkBlock(chunk);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*String section = (lastBlock == null) ? null : (lastBlock.getType()
				.contains(".")) ? lastBlock.getType().substring(0,
				lastBlock.getType().indexOf(".")) : lastBlock.getType();*/
		String section;
		if(lastBlock==null){ 
			section=null;
		}else if(lastBlock.getType().contains(".")){
			section= lastBlock.getType().substring(0,lastBlock.getType().indexOf("."));
		}else{
			section=lastBlock.getType();
		}
		if (section == null)
			return null;
		else if (isMainSection(section))
			return section;

		ChunkBlock prev = null;
		while (section != null) {

			/**
			 * introducing a special check to see if the call to getLastChunkBlock returns
			 * the same block i.e. lastBlock if so we break the loop and exit with section = lastBlock.getType()
			 */
			prev = lastBlock;
			lastBlock = parent.getDocument().getLastChunkBlock(lastBlock);
			/*if (lastBlock!=null)
			{
				System.out.println(prev.getchunkText());
				System.out.println(lastBlock.getchunkText());
				System.out.println("---------------");
			}
			section = (lastBlock == null) ? null : (lastBlock.getType()
					.contains(".")) ? lastBlock.getType().substring(0,
							lastBlock.getType().indexOf(".")) : lastBlock.getType();*/
			if(lastBlock==null){
				section=null;
			}else if(lastBlock.getType().contains(".")){
				section= lastBlock.getType().substring(0,lastBlock.getType().indexOf("."));
				if(lastBlock.equals(prev)){
					break;
				}
			}else{
				section=lastBlock.getType();
				if(lastBlock.equals(prev)){
					break;
				}
			}
			if (isMainSection(section))
				return section;

		}

		return section;
	}

	private boolean isMainSection(String section) {
		boolean result = !(chunk.TYPE_AFFLIATION.equals(section)
				|| chunk.TYPE_CITATION.equals(section)
				|| chunk.TYPE_FIGURE_LEGEND.equals(section)
				|| chunk.TYPE_FOOTER.equals(section)
				|| chunk.TYPE_HEADER.equals(section)
				|| chunk.TYPE_KEYWORDS.equals(section)
				|| chunk.TYPE_TABLE.equals(section) || chunk.TYPE_UNCLASSIFIED
				.equals(section));

		return result;
	}

	/**
	 * returns true if chunk block has neighbors of specific type within specified distance
	 * @param type
	 * @param nsew
	 * @return
	 */
	
	public boolean hasNeighboursOfType(String type, int nsew) {
		List<ChunkBlock> list = getOverlappingNeighbors(nsew, parent, chunk);
		for (ChunkBlock chunky : list)
			if (chunky.getType().equalsIgnoreCase(type))
				return true;

		return false;
	}

	/**
	 * 
	 */
	private List getOverlappingNeighbors(int nsew, PageBlock parent,
			ChunkBlock chunkBlock) {
		int topX = chunkBlock.getX1();
		int topY = chunkBlock.getY1();
		int width = chunkBlock.getWidth();
		int height = chunkBlock.getHeight();

		if (nsew == NORTH) {
			height = height / 2;
			topY = topY - height;

		} else if (nsew == SOUTH) {
			topY = topY + height;
			height = height / 2;
		} else if (nsew == EAST) {
			topX = topX + width;
			width = width / 2;
		} else if (nsew == WEST) {
			width = width / 2;
			topX = topX - width;
		} else if (nsew == NORTH_SOUTH) {
			topY = topY - height / 2;
			height = height * 2;
		} else if (nsew == EAST_WEST) {
			topX = topX - width / 2;
			width = width * 2;

		}

		SpatialEntity entity = modelFactory.createWordBlock(topX, topY, topX
				+ width, topY + height, 0, null, null, null);
		return parent.intersectsByType(entity, null, ChunkBlock.class);

	}

	/**
	 * returns true if the chunk block contains text that matches the input regex
	 * @param regex
	 * @return
	 */
	public boolean isMatchingRegularExpression(String regex) {
		Pattern pattern = Pattern.compile(regex);
		/*if(chunk.getchunkText().trim().startsWith("PLoS")&&regex.contains("PLoS")){
			System.out.println();
		}*/
		Matcher matcher = pattern.matcher(chunk.getchunkText());
		if (matcher.find())
			return true;

		return false;
	}
    
	/**
	 * returns the page number where the block is located
	 * @return
	 */
	public int getPageNumber() {
		return this.parent.getPageNumber();
	}

	/**
	 * returns true if the chunk is a sinle column centered on the page else returns false
	 * @return
	 */
	public boolean isColumnCentered() {
		int chunkMedian = chunk.getX1() + chunk.getWidth() / 2;
		int pageMedian = parent.getMedian();
		String lrm = chunk.getLeftRightMedLine();
		if (chunk.MIDLINE.equalsIgnoreCase(lrm)) {
			if (Math.abs(pageMedian - chunkMedian) < parent.getDocument()
					.getMostPopularWordHeight() * 2)
				return true;
			return false;
		}
		int pageMedianLeftRight = 0;
		if (chunk.LEFT.equalsIgnoreCase(lrm)) {
			pageMedianLeftRight = parent.getMargin()[0]
			                                         + (pageMedian - parent.getMargin()[0]) / 2;
		} else if (chunk.RIGHT.equalsIgnoreCase(lrm)) {
			pageMedianLeftRight = pageMedian
			+ (parent.getMargin()[2] - pageMedian) / 2;
		}

		if (Math.abs(chunkMedian - pageMedianLeftRight) < parent.getDocument()
				.getMostPopularWordHeight() * 2)
			return true;
		return false;
	}

}
