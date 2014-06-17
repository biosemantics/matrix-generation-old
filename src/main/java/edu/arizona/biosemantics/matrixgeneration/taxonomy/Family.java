package edu.arizona.biosemantics.matrixgeneration.taxonomy;

/**
 * Representation for a Family taxon.
 * @author Alex 
 *
 */
public class Family extends TaxonBase implements ITaxon {

	/**
	 * Create a new Family taxon object.
	 */
	public Family() {
		super(TaxonRank.FAMILY);
	}
	
	/**
	 * Create a new Family taxon object.
	 * @param name The scientific name of the family.
	 */
	public Family(String sourceFile, String name) {
		super(sourceFile, TaxonRank.FAMILY, name);
	}

}
