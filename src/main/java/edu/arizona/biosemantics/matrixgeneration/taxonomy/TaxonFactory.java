package edu.arizona.biosemantics.matrixgeneration.taxonomy;

/**
 * Static factory for producing Taxon objects.
 * @author alex
 *
 */
public class TaxonFactory {
	
	public static TaxonBase getTaxonObject(String sourceFile, TaxonRank rank, String name) {

		switch (rank) {
		case FAMILY:
			return new Family(sourceFile, name);
		case GENUS:
			return new Genus(sourceFile, name);
		case SPECIES:
			return new Species(sourceFile, name);
		case SUBGENUS:
			return new Subgenus(sourceFile, name);
		case SUBSPECIES:
			return new Subspecies(sourceFile, name);
		case SUBTRIBE:
			return new Subtribe(sourceFile, name);
		case TRIBE:
			return new Tribe(sourceFile, name);
		default:
			return new TaxonBase(sourceFile, rank, name);
		}
	}

}
