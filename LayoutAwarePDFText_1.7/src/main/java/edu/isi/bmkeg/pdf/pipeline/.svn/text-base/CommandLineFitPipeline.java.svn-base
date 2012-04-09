package edu.isi.bmkeg.pdf.pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.uimafit.factory.CollectionReaderFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import edu.isi.bmkeg.pdf.pipeline.analysisEngine.ParserRuleBasedClassfierAE;
import edu.isi.bmkeg.pdf.pipeline.collectionReader.DirectoryCollectionReader;
import edu.isi.bmkeg.pdf.scripts.LogTarget;
import edu.isi.bmkeg.utils.PipelineLauncher;


public class CommandLineFitPipeline
{
	private List<AnalysisEngine> aeList = new ArrayList<AnalysisEngine>();
	private CollectionReader documentCollectionReader;
	private static String PDF_SUFFIX = ".pdf";
	private String modeNumber;
	/**
	 * Start time of the processing - used to compute elapsed time.
	 */
	private long mStartTime;

	public CommandLineFitPipeline(String inputDocumentsLocation, String ruleFileLocation, boolean reportBlocks, boolean extractUnclassified, String outputDocumentsLocation, LogTarget log) throws ResourceInitializationException
	{
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription("desc.typeSystem.LAPDFTextTypeSystemDescriptor");
		System.out.println("Loaded the type system...");
		
		documentCollectionReader = CollectionReaderFactory.createCollectionReader(DirectoryCollectionReader.class, 
				DirectoryCollectionReader.PARAM_DIRECTORY_PATH, inputDocumentsLocation,
				DirectoryCollectionReader.PARAM_FILE_SUFFIX, PDF_SUFFIX,
				DirectoryCollectionReader.PARAM_RECURSE_INTO_DIRECTORY_STRUCTURE, true,
				DirectoryCollectionReader.PARAM_ITEMS_TO_SKIP, -1,
				DirectoryCollectionReader.PARAM_END_INDEX, -1);
		AnalysisEngine pdfParserClassifier = null;
		if(reportBlocks){
			pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 2, reportBlocks, extractUnclassified, outputDocumentsLocation, ruleFileLocation);
		}else{
			pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 3, reportBlocks, extractUnclassified, outputDocumentsLocation, ruleFileLocation);
		}
		
		aeList.add(pdfParserClassifier);
	}

	public CommandLineFitPipeline(String inputDocumentsLocation, String ruleFileLocation, boolean reportBlocks, boolean extractUnclassified, String outputDocumentsLocation, LogTarget log, int itemsToSkip, int endIndex) throws ResourceInitializationException
	{
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription("desc.typeSystem.LAPDFTextTypeSystemDescriptor");
		System.out.println("Loaded the type system...");
		
		documentCollectionReader = CollectionReaderFactory.createCollectionReader(DirectoryCollectionReader.class, 
				DirectoryCollectionReader.PARAM_DIRECTORY_PATH, inputDocumentsLocation,
				DirectoryCollectionReader.PARAM_FILE_SUFFIX, PDF_SUFFIX,
				DirectoryCollectionReader.PARAM_RECURSE_INTO_DIRECTORY_STRUCTURE, true,
				DirectoryCollectionReader.PARAM_ITEMS_TO_SKIP, itemsToSkip,
				DirectoryCollectionReader.PARAM_END_INDEX, endIndex);
		AnalysisEngine pdfParserClassifier = null;
		if(reportBlocks){
			pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 2, reportBlocks, extractUnclassified, outputDocumentsLocation, ruleFileLocation);
		}else{
			pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 3, reportBlocks, extractUnclassified, outputDocumentsLocation, ruleFileLocation);
		}
		
		aeList.add(pdfParserClassifier);
	}

	
	public CommandLineFitPipeline(String inputDocumentsLocation, boolean reportBlocks, boolean extractUnclassified, String outputDocumentsLocation, LogTarget log) throws ResourceInitializationException
	{
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription(
				"desc.typeSystem.LAPDFTextTypeSystemDescriptor");
		System.out.println("Loaded the type system...");
		
		documentCollectionReader = CollectionReaderFactory.createCollectionReader(DirectoryCollectionReader.class, 
				DirectoryCollectionReader.PARAM_DIRECTORY_PATH, inputDocumentsLocation,
				DirectoryCollectionReader.PARAM_FILE_SUFFIX, PDF_SUFFIX,
				DirectoryCollectionReader.PARAM_RECURSE_INTO_DIRECTORY_STRUCTURE, true,
				DirectoryCollectionReader.PARAM_ITEMS_TO_SKIP, -1,
				DirectoryCollectionReader.PARAM_END_INDEX, -1);
		
		AnalysisEngine pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 1, reportBlocks, extractUnclassified, outputDocumentsLocation);

		aeList.add(pdfParserClassifier);
	}
	
	public CommandLineFitPipeline(String inputDocumentsLocation, boolean reportBlocks, boolean extractUnclassified, String outputDocumentsLocation, LogTarget log, int itemsToSkip, int endIndex) throws ResourceInitializationException
	{
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription(
				"desc.typeSystem.LAPDFTextTypeSystemDescriptor");
		System.out.println("Loaded the type system...");
		
		documentCollectionReader = CollectionReaderFactory.createCollectionReader(DirectoryCollectionReader.class, 
				DirectoryCollectionReader.PARAM_DIRECTORY_PATH, inputDocumentsLocation,
				DirectoryCollectionReader.PARAM_FILE_SUFFIX, PDF_SUFFIX,
				DirectoryCollectionReader.PARAM_RECURSE_INTO_DIRECTORY_STRUCTURE, true,
				DirectoryCollectionReader.PARAM_ITEMS_TO_SKIP, itemsToSkip,
				DirectoryCollectionReader.PARAM_END_INDEX, endIndex);
		
		AnalysisEngine pdfParserClassifier = ParserRuleBasedClassfierAE.createAnalysisEngine(typeSystem, 1, reportBlocks, extractUnclassified, outputDocumentsLocation);

		aeList.add(pdfParserClassifier);
	}


	public void run()
	{
		System.out.println("Running Pipeline...");
		try
		{
			AnalysisEngine[] aeArray = aeList.toArray(new AnalysisEngine[0]);
			PipelineLauncher.runPipeline(documentCollectionReader, aeArray);
			//SimplePipeline.runPipeline(articleCollectionReader, aeArray);
		} catch (ResourceInitializationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UIMAException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
