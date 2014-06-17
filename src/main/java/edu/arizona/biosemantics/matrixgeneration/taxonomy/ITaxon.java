/**
 * 
 */
package edu.arizona.biosemantics.matrixgeneration.taxonomy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.annotationSchema.jaxb.Relation;
import edu.arizona.biosemantics.matrixgeneration.annotationSchema.jaxb.Structure;
import edu.arizona.biosemantics.matrixgeneration.states.IState;
import edu.arizona.biosemantics.matrixgeneration.tree.Tree;


/**
 * All level of taxa implement this base interface.
 * @author Alex
 *
 */
public interface ITaxon {

	public String getSourceFile();
	
	/**
	 * Get the name of this taxon.
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the character-state map for this taxon.
	 * @return
	 */
	public Map<Structure, Map<edu.arizona.biosemantics.matrixgeneration.annotationSchema.jaxb.Character, IState>> getCharMap();
	
	/**
	 * @return The structure tree of this taxon.
	 */
	public Tree<Structure> getStructureTree();
	
	/**
	 * @return The rank of this taxon.
	 */
	public TaxonRank getTaxonRank();
	
	/**
	 * Add a relation between structures to the list of relations for this taxon.
	 * @param r The relation to add.
	 */
	public void addRelation(Relation r);
	
	/**
	 * Gets the list of relations between structures for this taxon.
	 * @return
	 */
	public List<Relation> getRelations();
	
	/**
	 * Normalize the names of all the structures, relations and characters
	 * of this taxon.
	 */
	public void normalizeAllNames();  //HashMap sigPluMap   //by Jing Liu   Oct.31, 2013
	
	/**
	 * Returns a map from statement ids to the text contained therein.
	 * @return
	 */
	public Map<String, String> getStatementTextMap();
	
	/**
	 * Adds a statementId->text entry to the map for this taxon.
	 * @param statementId
	 * @param text
	 */
	public void addStatementTextEntry(String statementId, String text);
}
