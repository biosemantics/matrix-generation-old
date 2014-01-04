package edu.arizona.biosemantics.matrixgeneration.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import edu.arizona.biosemantics.matrixgeneration.conversion.datapassing.TaxonCharacterStateTriple;
import edu.arizona.biosemantics.matrixgeneration.sdd.AbstractCharacterDefinition;
import edu.arizona.biosemantics.matrixgeneration.sdd.CatSummaryData;
import edu.arizona.biosemantics.matrixgeneration.sdd.CategoricalCharacter;
import edu.arizona.biosemantics.matrixgeneration.sdd.CharacterLocalStateDef;
import edu.arizona.biosemantics.matrixgeneration.sdd.CodedDescription;
import edu.arizona.biosemantics.matrixgeneration.sdd.CodedDescriptionSet;
import edu.arizona.biosemantics.matrixgeneration.sdd.ModifierDef;
import edu.arizona.biosemantics.matrixgeneration.sdd.ModifierRefWithData;
import edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory;
import edu.arizona.biosemantics.matrixgeneration.sdd.QuantSummaryData;
import edu.arizona.biosemantics.matrixgeneration.sdd.QuantitativeCharacter;
import edu.arizona.biosemantics.matrixgeneration.sdd.Representation;
import edu.arizona.biosemantics.matrixgeneration.sdd.StateData;
import edu.arizona.biosemantics.matrixgeneration.sdd.TaxonNameCore;
import edu.arizona.biosemantics.matrixgeneration.sdd.TaxonNameRef;
import edu.arizona.biosemantics.matrixgeneration.sdd.UnivarSimpleStatMeasureData;
import edu.arizona.biosemantics.matrixgeneration.util.ConversionUtil;

public class CodedDescriptionHandler implements Handler, Observer {
	
	private static final String DESC_REP_PREFIX = "Coded description for ";
	private static final String UBIF_QNAME_URI = "http://rs.tdwg.org/UBIF/2006/";
	private static final String CATEGORICAL_QNAME_LOCAL = "Categorical";
	private static final String QUANTITATIVE_QNAME_LOCAL = "Quantitative";
	private static final String LOW_QNAME_LOCAL = "ObserverEstLower";
	private static final String HIGH_QNAME_LOCAL = "ObserverEstUpper";
	
	private static ObjectFactory sddFactory = new ObjectFactory();
	private CodedDescriptionSet codedDescriptionSet;
	private Map<String, CodedDescription> taxonDescriptions;
	
	public CodedDescriptionHandler() {
		this.codedDescriptionSet = sddFactory.createCodedDescriptionSet();
		this.taxonDescriptions = new HashMap<String, CodedDescription>();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof TaxonNameHandler &&
				arg instanceof TaxonNameCore) {
			TaxonNameHandler handler = (TaxonNameHandler) o;
			TaxonNameCore taxonNameCore = (TaxonNameCore) arg;
			CodedDescription description = sddFactory.createCodedDescription();
			Representation descRep = 
					ConversionUtil.makeRep(DESC_REP_PREFIX+taxonNameCore.getId());
			description.setRepresentation(descRep);
			//indicate the taxon scope.
			TaxonNameRef taxonNameRef = sddFactory.createTaxonNameRef();
			taxonNameRef.setRef(taxonNameCore.getId());
			description.setScope(sddFactory.createDescriptionScopeSet());
			description.getScope().getTaxonName().add(taxonNameRef);
			//initialize summary data set
			description.setSummaryData(sddFactory.createSummaryDataSet());
			codedDescriptionSet.getCodedDescription().add(description);
			handler.getDataset().setCodedDescriptions(codedDescriptionSet);
			this.taxonDescriptions.put(taxonNameCore.getId(), description);
		}
	}
	
	/**
	 * Adds the summary data for each taxon to the CodedDescriptionSet
	 * in the Dataset.
	 * @param matrix
	 * @param matrixModifiers
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addSummaryDataToCodedDescriptions(
			Map<String, Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>>> matrix,
			Map<TaxonCharacterStateTriple, ModifierDef> matrixModifiers){
		//loop over each taxon in the matrix
		for(String taxonName : matrix.keySet()) {
			//create JAXBElement set to hold final data elements
			Set<JAXBElement> dataElementSet = 
					new TreeSet<JAXBElement>(new SummaryDataComparator<JAXBElement>());
			Map<AbstractCharacterDefinition, Set<CharacterLocalStateDef>> charStateMap =
					matrix.get(taxonName);
			//and loop over each character
			for(AbstractCharacterDefinition character : charStateMap.keySet()) {
				//could be either categorical...
				if(character instanceof CategoricalCharacter) {
					CatSummaryData summaryData = sddFactory.createCatSummaryData();
					summaryData.setRef(character.getId());
					//and loop over states
					for(CharacterLocalStateDef state : charStateMap.get(character)) {
						TaxonCharacterStateTriple triple = 
								new TaxonCharacterStateTriple(taxonName, character, state);
						//check for null states...(?)
						if(state != null) {
							StateData stateData = sddFactory.createStateData();
							stateData.setRef(state.getId());
							//add in any modifiers
							ModifierDef modDef = matrixModifiers.get(triple);
							if(modDef != null) {
								ModifierRefWithData modRef = 
										sddFactory.createModifierRefWithData();
								modRef.setRef(modDef.getId());
								stateData.getModifier().add(modRef);
							}
							summaryData.getState().add(stateData);
						}
					}
					//create final data element (JAXBElement) for each character
					JAXBElement<CatSummaryData> dataElement =
							new JAXBElement<CatSummaryData>(
									new QName(UBIF_QNAME_URI, CATEGORICAL_QNAME_LOCAL),
									CatSummaryData.class,
									CatSummaryData.class,
									(CatSummaryData) summaryData);
					dataElementSet.add(dataElement);
				}
				//...or quantitative
				if(character instanceof QuantitativeCharacter) {
					QuantSummaryData summaryData = sddFactory.createQuantSummaryData();
					summaryData.setRef(character.getId());
					UnivarSimpleStatMeasureData low = sddFactory.createUnivarSimpleStatMeasureData();
					low.setType(new QName("http://rs.tdwg.org/UBIF/2006/", LOW_QNAME_LOCAL));
					low.setValue(((edu.arizona.biosemantics.matrixgeneration.sdd.QuantitativeCharacter)character).getMappings().getMapping().get(0).getFrom().getLower());
					summaryData.getMeasureOrPMeasure().add(low);
					UnivarSimpleStatMeasureData high = sddFactory.createUnivarSimpleStatMeasureData();
					high.setType(new QName("http://rs.tdwg.org/UBIF/2006/", HIGH_QNAME_LOCAL));
					high.setValue(((edu.arizona.biosemantics.matrixgeneration.sdd.QuantitativeCharacter)character).getMappings().getMapping().get(0).getFrom().getUpper());
					summaryData.getMeasureOrPMeasure().add(high);
					for(CharacterLocalStateDef state : charStateMap.get(character)) {
						TaxonCharacterStateTriple triple = 
								new TaxonCharacterStateTriple(taxonName, character, state);
						//again, check for null states (?)
						if(state != null) {
							//add in any modifiers
							ModifierDef modDef = matrixModifiers.get(triple);
							if(modDef != null) {
								ModifierRefWithData modRef = sddFactory.createModifierRefWithData();
								modRef.setRef(modDef.getId());
								summaryData.getModifier().add(modRef);
							}
						}
					}
					//create final data element (JAXBElement) for each character
					JAXBElement<QuantSummaryData> dataElement =
							new JAXBElement<QuantSummaryData>(
									new QName(UBIF_QNAME_URI, QUANTITATIVE_QNAME_LOCAL),
									QuantSummaryData.class,
									QuantSummaryData.class,
									(QuantSummaryData) summaryData);
					dataElementSet.add(dataElement);
				}
			}
			CodedDescription description = taxonDescriptions.get(taxonName);
			for(JAXBElement element : dataElementSet)
				description.getSummaryData().getCategoricalOrQuantitativeOrSequence().add(element);
		}	//end taxon name
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub

	}

}
