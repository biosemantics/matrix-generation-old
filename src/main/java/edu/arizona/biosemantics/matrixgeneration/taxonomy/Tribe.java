package edu.arizona.biosemantics.matrixgeneration.taxonomy;

/**
 * @author alex
 *
 */
public class Tribe extends TaxonBase {

	/**
	 * Create a new Tribe taxon object.
	 */
	public Tribe() {
		super(TaxonRank.TRIBE);
	}

	/**
	 * Creates a tribe with a given name.
	 * @param name
	 */
	public Tribe(String sourceFile, String name) {
		super(sourceFile, TaxonRank.TRIBE, name);
	}

}
