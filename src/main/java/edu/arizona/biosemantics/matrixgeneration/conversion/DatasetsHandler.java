package edu.arizona.biosemantics.matrixgeneration.conversion;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import edu.arizona.biosemantics.matrixgeneration.sdd.Datasets;
import edu.arizona.biosemantics.matrixgeneration.sdd.DocumentGenerator;
import edu.arizona.biosemantics.matrixgeneration.sdd.TechnicalMetadata;
import edu.arizona.biosemantics.matrixgeneration.util.XMLGregorianCalendarConverter;



public class DatasetsHandler implements Handler {

	private edu.arizona.biosemantics.matrixgeneration.sdd.Datasets datasets;
	private edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory sddFactory;
	
	/**
	 * All this needs to do is generate the technical metadata.
	 */
	public DatasetsHandler() {
		this.sddFactory = new edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory();
		this.datasets = sddFactory.createDatasets();
	}

	/**
	 * Adds the metadata section to the SDD Datasets element.
	 */
	@Override
	public void handle() {
		addMetadata();		
	}
	
	/**
	 * Adds metadata to the root Datasets object.  Throws on the current time 
	 * as the "created" field and a Generator named "SDD conversion tool."
	 * @param datasets
	 */
	protected void addMetadata() {
		TechnicalMetadata metadata = sddFactory.createTechnicalMetadata();
		XMLGregorianCalendar xgcNow = XMLGregorianCalendarConverter.
				asXMLGregorianCalendar(new Date(System.currentTimeMillis()));
		metadata.setCreated(xgcNow);
		DocumentGenerator gen = sddFactory.createDocumentGenerator();
		gen.setName("SDD conversion tool.");
		gen.setVersion("0.2");
		metadata.setGenerator(gen);
		this.datasets.setTechnicalMetadata(metadata);
	}
	
	public edu.arizona.biosemantics.matrixgeneration.sdd.Datasets getDatasets() {
		return this.datasets;
	}
}
