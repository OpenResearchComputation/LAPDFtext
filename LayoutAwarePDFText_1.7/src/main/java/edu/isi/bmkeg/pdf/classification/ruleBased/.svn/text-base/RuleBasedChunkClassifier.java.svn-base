package edu.isi.bmkeg.pdf.classification.ruleBased;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DecisionTableFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

import edu.isi.bmkeg.pdf.classification.Classifier;
import edu.isi.bmkeg.pdf.features.ChunkFeatures;
import edu.isi.bmkeg.pdf.model.ChunkBlock;
import edu.isi.bmkeg.pdf.model.factory.AbstractModelFactory;
/**
 * Rule based classification of blocks using drools. 
 * @author cartic
 *
 */
public class RuleBasedChunkClassifier implements Classifier<ChunkBlock> {
	private StatelessKnowledgeSession kSession;
	private AbstractModelFactory modelFactory;
	private KnowledgeBase kbase;
	private void reportCompiledRules(String droolsFileName, DecisionTableConfiguration dtableconfiguration){
		try
		{
			String rules = DecisionTableFactory.loadFromInputStream(ResourceFactory.newFileResource(droolsFileName).getInputStream(), dtableconfiguration);
			System.out.println(rules);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public RuleBasedChunkClassifier(String droolsFileName,
			AbstractModelFactory modelFactory) {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		.newKnowledgeBuilder();
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
		if(droolsFileName.endsWith(".csv")) {
			DecisionTableConfiguration dtableconfiguration =
				KnowledgeBuilderFactory.newDecisionTableConfiguration();
			dtableconfiguration.setInputType( DecisionTableInputType.CSV );
			Resource xlsRes = ResourceFactory.newFileResource( droolsFileName );
			kbuilder.add( xlsRes, ResourceType.DTABLE, dtableconfiguration);
			reportCompiledRules(droolsFileName,dtableconfiguration);
		}else if(droolsFileName.endsWith(".xls")) {
			DecisionTableConfiguration dtableconfiguration =
				KnowledgeBuilderFactory.newDecisionTableConfiguration();
			dtableconfiguration.setInputType( DecisionTableInputType.XLS );
			Resource xlsRes = ResourceFactory.newFileResource( droolsFileName );
			kbuilder.add( xlsRes, ResourceType.DTABLE, dtableconfiguration);
			reportCompiledRules(droolsFileName,dtableconfiguration);
		}else if( droolsFileName.endsWith(".drl")) {
			kbuilder.add(ResourceFactory.newFileResource(droolsFileName),
					ResourceType.DRL);
		}
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors());
			return;
		}
		ArrayList<KnowledgePackage> kpkgs = new ArrayList<KnowledgePackage>(
				kbuilder.getKnowledgePackages());
		kbase.addKnowledgePackages(kpkgs);
		this.modelFactory = modelFactory;
	}

	@Override
	public void classify(List<ChunkBlock> blockList) {
		this.kSession = kbase.newStatelessKnowledgeSession();
		for (ChunkBlock chunk : blockList) {
			kSession.setGlobal("chunk", chunk);
			kSession.execute(new ChunkFeatures(chunk, modelFactory));
		}
		this.kSession = null;
	}

}
