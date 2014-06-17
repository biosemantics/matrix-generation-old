package edu.arizona.biosemantics.matrixgeneration.taxonomy;

/**
 * Subspecies class.
 * @author alex
 *
 */
public class Subspecies extends TaxonBase {
	
	/**
	 * Create a new, nameless subspecies.
	 */
	public Subspecies() {
		super(TaxonRank.SUBSPECIES);
	}
	
	/**
	 * Create a new subspecies with a given name.
	 * @param name
	 */
	public Subspecies(String sourceFile, String name) {
		super(sourceFile,TaxonRank.SUBSPECIES, name);
	}

}
