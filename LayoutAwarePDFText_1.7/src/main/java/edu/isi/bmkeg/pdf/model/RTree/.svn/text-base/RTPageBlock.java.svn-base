package edu.isi.bmkeg.pdf.model.RTree;

import java.util.List;

import edu.isi.bmkeg.pdf.model.Block;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.Document;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.pdf.model.spatial.SpatialEntity;

public class RTPageBlock extends RTSpatialRepresentation implements PageBlock {

	
	private int pageNumber;
	private int boxHeight;
	private int boxWidth;
	private Document document;

	

	public RTPageBlock(int pageNumber,int pageBoxWidth,int pageBoxHeight,Document document) {
		super();
		
		this.pageNumber = pageNumber;
		this.boxHeight=pageBoxHeight;
		this.boxWidth=pageBoxWidth;
		this.document=document;

	}

	
	
	
	public int getHeight() {
		
		return this.getX2()-this.getX1();
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return this.getY2()-this.getY1();
	}

	public int getX1() {

		
		return (int) this.getMargin()[0];
		
	}

	public int getX2() {
		return (int) this.getMargin()[2];
	}

	public int getY1() {
		return (int) this.getMargin()[1];
	}

	public int getY2() {
		return (int) this.getMargin()[3];
	}

	public int getPageNumber() {
		// TODO Auto-generated method stub
		return pageNumber;
	}

	

	

	@Override
	public String getLeftRightMedLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFlush(String condition, int value) {
		
		return false;
	}

	@Override
	public Block getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContainer(Block block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPageBoxHeight() {
		
		return boxHeight;
	}

	@Override
	public int getPageBoxWidth() {
		
		return boxWidth;
	}

	@Override
	public String getType() {
		return Block.TYPE_PAGE;
	}

	@Override
	
	public void setType(String type) {
		
		
	}

	@Override
	public Document getDocument() {
		
		return document;
	}

	@Override
	public int initialize(List<WordBlock> list, int startId) {
		for(WordBlock block:list){
			block.setContainer(this);
			super.add(block, startId++);
		}
		
		return startId;
	}




	
	

}
