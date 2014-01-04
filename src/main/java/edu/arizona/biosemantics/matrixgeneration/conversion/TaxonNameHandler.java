package edu.arizona.biosemantics.matrixgeneration.conversion;

import java.util.Observable;
import java.util.Observer;

import edu.arizona.biosemantics.matrixgeneration.sdd.Dataset;
import edu.arizona.biosemantics.matrixgeneration.sdd.LabelText;
import edu.arizona.biosemantics.matrixgeneration.sdd.Representation;
import edu.arizona.biosemantics.matrixgeneration.sdd.TaxonNameCore;
import edu.arizona.biosemantics.matrixgeneration.sdd.TaxonomicRank;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.ITaxon;
import edu.arizona.biosemantics.matrixgeneration.tree.TreeNode;


/**
 * This observer takes care of adding the TaxonName set to an SDD dataset.
 * It's also Observable, as a TaxonHierarchyObserver can subscribe to it.
 * @author alex
 *
 */
public class TaxonNameHandler extends Observable implements Observer, Handler {

	private edu.arizona.biosemantics.matrixgeneration.sdd.Dataset dataset;
	private edu.arizona.biosemantics.matrixgeneration.sdd.TaxonNameSet taxonNameSet;
	private edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory sddFactory;
	
	/**
	 * Observes a DatasetHandler.
	 * @param datasetHandler
	 */
	public TaxonNameHandler(edu.arizona.biosemantics.matrixgeneration.sdd.Dataset dataset) {
		this.sddFactory = new edu.arizona.biosemantics.matrixgeneration.sdd.ObjectFactory();
		this.taxonNameSet = sddFactory.createTaxonNameSet();
		this.dataset = dataset;
	}
	
	/**
	 * Called when a new Taxon Name has been added.  TaxonHierarchyObserver
	 * is subscribed to this, as well as the CTHandler.
	 */
	public void publish(Object arg) {
		this.setChanged();
		this.notifyObservers(arg);
	}
	
	/**
	 * Adds a TaxonName to the TaxonNameSet whenever the observable
	 * datasetHandler changes.
	 */
	@Override
	public void update(Observable obs, Object arg) {
		if(obs instanceof DatasetHandler) {
			DatasetHandler handler = (DatasetHandler)obs;
			if(arg instanceof TreeNode<?>){
				TreeNode<ITaxon> treeNode = (TreeNode<ITaxon>)arg;
				addTaxonNameToSet(treeNode);
				handler.getDataset().setTaxonNames(taxonNameSet);
				publish(treeNode);
			}
			else if(taxonNameSet.getTaxonName().size() == 
						handler.getTaxa().size()){	
				//we have all of the taxon names, 
				//stop listening to the DatasetHandler
				handler.deleteObserver(this);
			}
		}
	}
	
	/**
	 * Add a taxon name and rank to the TaxonNameSet.
	 * @param taxon
	 */
	protected void addTaxonNameToSet(TreeNode<ITaxon> node) {
		ITaxon taxon = node.getElement();
		TaxonNameCore taxonNameCore = sddFactory.createTaxonNameCore();
		taxonNameCore.setId(taxon.getName());
		TaxonomicRank taxonomicRank = sddFactory.createTaxonomicRank();
		String rank = taxon.getTaxonRank().toString().toLowerCase();
		taxonomicRank.setLiteral(rank);
		Representation rep = sddFactory.createRepresentation();
		LabelText labelText = sddFactory.createLabelText();
		labelText.setValue(rank.concat(" ").concat(taxon.getName()));
		rep.getRepresentationGroup().add(labelText);
		taxonNameCore.setRepresentation(rep);
		taxonNameCore.setRank(taxonomicRank);
		this.taxonNameSet.getTaxonName().add(taxonNameCore);
		publish(taxonNameCore);
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub	
	}

	/**
	 * @return the SDD dataset.
	 */
	public Dataset getDataset() {
		return this.dataset;
	}

}
