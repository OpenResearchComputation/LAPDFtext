package edu.isi.bmkeg.pdf.text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import edu.isi.bmkeg.pdf.features.ChunkFeatures;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.Document;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.WordBlock;
import edu.isi.bmkeg.pdf.model.RTree.RTModelFactory;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.pdf.model.spatial.SpatialEntity;

public class SpatialLayoutFeaturesReportGenerator implements TextWriter
{
	private StringBuilder sb;
	public SpatialLayoutFeaturesReportGenerator() throws IOException
	{
		sb = new StringBuilder();
	}

	private StringBuilder writeFeatures(StringBuilder sb, ChunkBlock chunk, PageBlock page){
		List<SpatialEntity> words = page.containsByType(chunk,
				SpatialOrdering.MIXED_MODE, WordBlock.class);
		WordBlock word;
		if(chunk.getNumberOfLine()==1||words.size()==1){//possibly a section heading line
			sb.append("\n-POSSIBLE SECTION HEADING-\n");
			sb.append("\nChunk text: "+chunk.getchunkText());
		}
		sb.append("\nMost popular font "+chunk.getMostPopularWordFont());
		sb.append("\nMost popular font size "+chunk.getMostPopularWordStyle());
		sb.append("\nMost popular word height "+chunk.getMostPopularWordHeight());
		sb.append("\nNumber of Lines "+chunk.getNumberOfLine());
		sb.append("\nAlignment "+chunk.getLeftRightMedLine());

		return sb;
	}

	@Override
	public void write(Document doc, String outputFilename) throws IOException,FileNotFoundException
	{
		PageBlock page;
		List<ChunkBlock> chunks;
		int totalNumberOfPages = doc.getTotalNumberOfPages();
		for (int i = 1; i <= totalNumberOfPages; i++) {
			sb.append("\n\n--------------------------------------------------------------------------");
			sb.append("--------------------PAGE: "+i+"------------------------\n\n");
			page = doc.getPage(i);
			chunks = page.getAllChunkBlocks(SpatialOrdering.COLUMN_AWARE_MIXED_MODE);
			sb.append("\nNumber of Blocks="+chunks.size());
			int chunkCounter = 1;
			for(ChunkBlock chunk : chunks){
				sb.append("\n--------------------TEXT BLOCK:"+chunkCounter+"------------------------");
				sb = writeFeatures(sb,chunk,page);
				chunkCounter++;
			}
		}
		ReadWriteTextFileWithEncoding.write(outputFilename, TextWriter.UTF_8, sb.toString());
	}
}
