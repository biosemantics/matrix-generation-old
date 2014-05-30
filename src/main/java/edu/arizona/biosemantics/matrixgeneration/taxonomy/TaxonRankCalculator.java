/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.arizona.biosemantics.matrixgeneration.taxonomy;

/**
 *
 * @author jingliu5
 */
public class TaxonRankCalculator {
     public TaxonRank getParentRank(TaxonRank rank){
         return getRankFromOrder(getRankOrder(rank)+1);
     }
     public TaxonRank getChildRank(TaxonRank rank){
        return getRankFromOrder(getRankOrder(rank)-1);
     }

    public int getRankOrder(TaxonRank rank) {     
    	for(int i=0; i<TaxonRank.values().length; i++) {
    		if(TaxonRank.values()[i].equals(rank)) {
    			return TaxonRank.values().length - i;
    		}
    	}
    	return -1;
    }
    
   public TaxonRank getRankFromOrder(int order) {     
	   return TaxonRank.values()[TaxonRank.values().length - order];   	
    }
}
