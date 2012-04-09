package edu.isi.bmkeg.pdf.pipeline.analysisEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.jpedal.exception.PdfException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ConfigurationParameterFactory;

import edu.isi.bmkeg.pdf.classification.ruleBased.RuleBasedChunkClassifier;
import edu.isi.bmkeg.pdf.extraction.exceptions.AccessException;
import edu.isi.bmkeg.pdf.extraction.exceptions.EncryptionException;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.Document;
import edu.isi.bmkeg.pdf.model.PageBlock;
import edu.isi.bmkeg.pdf.model.RTree.RTModelFactory;
import edu.isi.bmkeg.pdf.model.ordering.SpatialOrdering;
import edu.isi.bmkeg.pdf.parser.RuleBasedParser;
import edu.isi.bmkeg.pdf.text.SpatialLayoutFeaturesReportGenerator;
import edu.isi.bmkeg.pdf.text.SpatiallyOrderedChunkTextWriter;
import edu.isi.bmkeg.pdf.text.SpatiallyOrderedChunkTypeFilteredTextWriter;
import edu.isi.bmkeg.pdf.xml.OpenAccessXMLWriter;
import edu.isi.bmkeg.pdf.xml.SpatialXMLWriter;
import edu.isi.bmkeg.utils.FileUtils;
import edu.isi.bmkeg.utils.ISI_UIMA_PDFUtils;
import edu.isi.bmkeg.utils.PageImageOutlineRenderer;

public class ParserRuleBasedClassfierAE extends JCasAnnotator_ImplBase {

	public static final String PARAM_MODE = ConfigurationParameterFactory.createConfigurationParameterName(ParserRuleBasedClassfierAE.class,"mode");
	@ConfigurationParameter(mandatory=true, description="This is the mode of operation.")
	private int mode;
	
	public static final String PARAM_RULEFILE = ConfigurationParameterFactory.createConfigurationParameterName(ParserRuleBasedClassfierAE.class,"ruleFile");
	@ConfigurationParameter(mandatory=false, description="This is the rule file used for block classification.")
	private String ruleFile;
	
	public static final String PARAM_BOOLEAN_BLOCK_REPORT = ConfigurationParameterFactory.createConfigurationParameterName(ParserRuleBasedClassfierAE.class,"reportBlocks");
	@ConfigurationParameter(mandatory=true, description="This is the flag used to trigger debug reporting.")
	private Boolean reportBlocks;
	
	public static final String PARAM_BOOLEAN_EXTRACT_UNCLASSIFIED_FLOW_AWARE_TEXT = ConfigurationParameterFactory.createConfigurationParameterName(ParserRuleBasedClassfierAE.class, "extractUnclassified");
	@ConfigurationParameter(mandatory=true, description="this flag is used to decide whether unclassified flow aware output text is required.")
	private boolean extractUnclassified;
	
	public static final String PARAM_OUTPUT_FOLDER = ConfigurationParameterFactory.createConfigurationParameterName(ParserRuleBasedClassfierAE.class,"outputFolder");
	@ConfigurationParameter(mandatory=true, description="This is the location of the output for debug and results.")
	private String outputFolder;
	
	private File outputFolderFileDescriptor;
	
	private RuleBasedParser parser;
	
	
	
	protected Document doc;
	protected RuleBasedChunkClassifier classfier;
	public void initialize(UimaContext uimaContext)
			throws ResourceInitializationException {
		// TODO Auto-generated method stub
		super.initialize(uimaContext);
		parser = new RuleBasedParser(new RTModelFactory());
		mode = (Integer) uimaContext.getConfigParameterValue(PARAM_MODE);
		ruleFile = (String) uimaContext.getConfigParameterValue(PARAM_RULEFILE);
		if(ruleFile!=null){
			System.out.println("Using rulefile "+ruleFile);
		}
		extractUnclassified = (Boolean) uimaContext.getConfigParameterValue(PARAM_BOOLEAN_EXTRACT_UNCLASSIFIED_FLOW_AWARE_TEXT);
		reportBlocks = (Boolean) uimaContext.getConfigParameterValue(PARAM_BOOLEAN_BLOCK_REPORT);
		outputFolder = (String) uimaContext.getConfigParameterValue(PARAM_OUTPUT_FOLDER);
		outputFolderFileDescriptor = new File(outputFolder);
		if(!outputFolderFileDescriptor.exists()){
			System.out.println(outputFolderFileDescriptor.getAbsolutePath()+" does not exist! Creating it!!");
			outputFolderFileDescriptor.mkdir();
		}
		parser.setPath(outputFolderFileDescriptor.getAbsolutePath());
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		try {
			String inputPDFFilePath = ISI_UIMA_PDFUtils.getDocumentSecondaryID(jcas);
			String inputPDFFileName = new File(inputPDFFilePath).getName();
		
			Document doc = null;
			switch (mode) {
			case 1:
				System.out.println("\nRunning block detection on "+inputPDFFilePath);
				try
				{
					doc = getParsedDocument(inputPDFFilePath);
					if(doc==null){
						System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath+" because doc is null");
						break;
					}
				} catch (PdfException e)
				{
					System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath);
					e.printStackTrace();
					break;
				}
				System.out.println("Writing spatial block xml to "+outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName  + "_spatial.xml");
				new SpatialXMLWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatial.xml");
				if(reportBlocks){
					System.out.println("\nRunning block feature reporter on "+inputPDFFilePath);
					new SpatialLayoutFeaturesReportGenerator().write(doc,  outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatialFeatures.dat");
				}
				if(extractUnclassified){
					new SpatiallyOrderedChunkTextWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_unclassifiedFlowAwareText.dat");
				}
				break;

			case 2:
				System.out.println("\nRunning block detection on "+inputPDFFilePath);
				try
				{
					doc = getParsedDocument(inputPDFFilePath);
					if(doc==null){
						System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath+" because doc is null");
						break;
					}
				} catch (PdfException e)
				{
					System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath);
					e.printStackTrace();
					break;
				}
				System.out.println("Writing spatial block xml to "+outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName  + "_spatial.xml");
				new SpatialXMLWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatial.xml");
				System.out.println("Running block classification on "+inputPDFFilePath);
				classifyDocument(doc,inputPDFFilePath);
				System.out.println("Writing block classified XML in OpenAccess format "+outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName  + "_rhetorical.xml");
				new OpenAccessXMLWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_rhetorical.xml");
				if(reportBlocks){
					System.out.println("\nRunning block feature reporter on "+inputPDFFilePath);
					new SpatialLayoutFeaturesReportGenerator().write(doc,  outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatialFeatures.dat");
				}
				if(extractUnclassified){
					new SpatiallyOrderedChunkTextWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_unclassifiedFlowAwareText.dat");
				}
				break;
			case 3:
				System.out.println("\nRunning block detection on "+inputPDFFilePath);
				try
				{
					doc = getParsedDocument(inputPDFFilePath);
					if(doc==null){
						System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath+" because doc is null");
						break;
					}
				} catch (PdfException e)
				{
					System.err.println("Error encountered while performing block detection. Skipping "+inputPDFFilePath);
					e.printStackTrace();
					break;
				}
				System.out.println("Running block classification on "+inputPDFFilePath);
				classifyDocument(doc,inputPDFFilePath);
				System.out.println("Writing block classified text in Spatially ordered but Section type filtered format "+outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName  + "_spatialFiltered.txt");
				new SpatiallyOrderedChunkTypeFilteredTextWriter(true,true).write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatialFiltered.txt");
				System.out.println("Writing block classified XML in OpenAccess format "+outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName  + "_rhetorical.xml");
				new OpenAccessXMLWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_rhetorical.xml");
				if(reportBlocks){
					System.out.println("\nRunning block feature reporter on "+inputPDFFilePath);
					new SpatialLayoutFeaturesReportGenerator().write(doc,  outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_spatialFeatures.dat");
				}
				if(extractUnclassified){
					new SpatiallyOrderedChunkTextWriter().write(doc, outputFolderFileDescriptor.getAbsolutePath()+"/"+inputPDFFileName + "_unclassifiedFlowAwareText.dat");
				}
				break;
			}

			
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Document getParsedDocument(String fileName) throws PdfException,
			AccessException, EncryptionException {
		Document doc=null;
		doc = parser.parse(fileName);
		if(doc.hasjPedalDecodeFailed()){
			return null;
		}
		return doc;
	}

	private void classifyDocument(Document document, String pdfFileAbsoluteFileName) {
		String prefix = "/"+new File(pdfFileAbsoluteFileName).getName();
		classfier = new RuleBasedChunkClassifier(
				ruleFile, new RTModelFactory());
		for (int i = 1; i <= document.getTotalNumberOfPages(); i++) {
			PageBlock page = document.getPage(i);
			List<ChunkBlock> chunkList = page
					.getAllChunkBlocks(SpatialOrdering.COLUMN_AWARE_MIXED_MODE);

			classfier.classify(chunkList);
			
			PageImageOutlineRenderer.createPageImage(page, pdfFileAbsoluteFileName, parser.getPath() +prefix + "_" + page.getPageNumber() + ".png", 1);

		}
		PageImageOutlineRenderer.createReport(parser.getPath() + prefix + "_"
				+ "report.png");
	}

	
	public static AnalysisEngine createAnalysisEngine(TypeSystemDescription typeSystem, int mode, boolean reportBlocks, boolean extractUnclassified, String outputFolderName, String ruleFileName)
	 throws ResourceInitializationException {
		
			AnalysisEngineDescription aed = AnalysisEngineFactory.createPrimitiveDescription(
					ParserRuleBasedClassfierAE.class, typeSystem, 
					// name,      									value
					ParserRuleBasedClassfierAE.PARAM_OUTPUT_FOLDER, outputFolderName, 
					ParserRuleBasedClassfierAE.PARAM_RULEFILE,ruleFileName, 
					ParserRuleBasedClassfierAE.PARAM_MODE,mode,
					ParserRuleBasedClassfierAE.PARAM_BOOLEAN_BLOCK_REPORT, reportBlocks,
					ParserRuleBasedClassfierAE.PARAM_BOOLEAN_EXTRACT_UNCLASSIFIED_FLOW_AWARE_TEXT, extractUnclassified
					);
	 
	 	return AnalysisEngineFactory.createPrimitive(aed);
	 }
	public static AnalysisEngine createAnalysisEngine(TypeSystemDescription typeSystem, int mode, boolean reportBlocks, boolean extractUnclassified, String outputFolderName)
	 throws ResourceInitializationException {
		
			AnalysisEngineDescription aed = AnalysisEngineFactory.createPrimitiveDescription(
					ParserRuleBasedClassfierAE.class, typeSystem, 
					// name,      									value
					ParserRuleBasedClassfierAE.PARAM_OUTPUT_FOLDER, outputFolderName, 
					ParserRuleBasedClassfierAE.PARAM_RULEFILE,null, 
					ParserRuleBasedClassfierAE.PARAM_MODE,mode,
					ParserRuleBasedClassfierAE.PARAM_BOOLEAN_BLOCK_REPORT, reportBlocks,
					ParserRuleBasedClassfierAE.PARAM_BOOLEAN_EXTRACT_UNCLASSIFIED_FLOW_AWARE_TEXT, extractUnclassified
					);
	 
	 	return AnalysisEngineFactory.createPrimitive(aed);
	 }
}
