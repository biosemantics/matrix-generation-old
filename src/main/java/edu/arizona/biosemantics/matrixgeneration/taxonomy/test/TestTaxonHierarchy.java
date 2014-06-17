package edu.arizona.biosemantics.matrixgeneration.taxonomy.test;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.arizona.biosemantics.matrixgeneration.taxonomy.Family;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.Genus;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.ITaxon;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.Species;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.SubTaxonException;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.TaxonHierarchy;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.TaxonRank;


/**
 * @author Alex
 *
 */
public class TestTaxonHierarchy extends TestCase{

	private TaxonHierarchy th;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		th = new TaxonHierarchy();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		th = null;
	}
	
	@Test
	public final void testAddOneTaxon() {
		Family family = new Family("filename", "testFamily");
		try {
			th.addSubTaxon(family);
		} catch (SubTaxonException e) {
			e.printStackTrace();
		}
		assertTrue(th.getHierarchy().getRoot().getElement().equals(family));
//		System.out.println(th);
	}
	
	@Test
	public final void testAddGenusUnderFamily() {
		ITaxon family = new Family("filename","testFamily");
		try {
			th.addSubTaxon(family);
		} catch (SubTaxonException e) {
			e.printStackTrace();
		}
		assertTrue(th.getHierarchy().getRoot().getElement().equals(family));
		ITaxon genus = new Genus("filename","testGenus");
		try {
			th.addSubTaxon(genus);
		} catch (SubTaxonException e) {
			e.printStackTrace();
		}
		ITaxon t = th.getHierarchy().contains(genus).getElement();
		assertTrue(t.equals(genus));
		
	}
	
	@Test
	public final void testAddSpeciesUnderGenusWithFamily() {
		ITaxon family = new Family("filename","testFamily");
		try {
			th.addSubTaxon(family);
		} catch (SubTaxonException e2) {
			e2.printStackTrace();
		}
		assertTrue(th.getHierarchy().getRoot().getElement().equals(family));
		ITaxon genus = new Genus("filename","testGenus");
		try {
			th.addSubTaxon(genus);
		} catch (SubTaxonException e1) {
			e1.printStackTrace();
		}
		ITaxon t = th.getHierarchy().contains(genus).getElement();
		assertTrue(t.equals(genus));
		ITaxon species = new Species("filename","testSpecies");
		try {
			th.addSubTaxon(genus.getName(), TaxonRank.GENUS, species);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(th);
	}

}
