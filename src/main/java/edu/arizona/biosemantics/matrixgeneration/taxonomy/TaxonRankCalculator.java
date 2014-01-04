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
        if (rank.equals(TaxonRank.VARIETY))       
            return 0;
        else if (rank.equals(TaxonRank.SUBSPECIES)) {
            return 1;
        } else if (rank.equals(TaxonRank.SPECIES)) {
            return 2;
        } else if (rank.equals(TaxonRank.SUBSECTION)) {
            return 3;
        } else if (rank.equals(TaxonRank.SECTION)) {
            return 4;
        } else if (rank.equals(TaxonRank.SUBGENUS)) {
            return 5;
        } else if (rank.equals(TaxonRank.GENUS)) {
            return 6;
        } else if (rank.equals(TaxonRank.SUBTRIBE)) {
            return 7;
        } else if (rank.equals(TaxonRank.TRIBE)) {
            return 8;
        } else if (rank.equals(TaxonRank.SUBFAMILY)) {
            return 9;
        } else if (rank.equals(TaxonRank.FAMILY)) {
            return 10;
        }else if (rank.equals(TaxonRank.SUPERFAMILY)) {
            return 11;
        }else if (rank.equals(TaxonRank.SUBORDER)) {
            return 12;
        }else if (rank.equals(TaxonRank.ORDER)) {
            return 13;
        }else if (rank.equals(TaxonRank.SUPERORDER)) {
            return 14;
        }else if (rank.equals(TaxonRank.SUBCLASS)) {
            return 15;
        }else if (rank.equals(TaxonRank.CLASS)) {
            return 16;
        }else if (rank.equals(TaxonRank.SUPERCLASS)) {
            return 17;
        }else if (rank.equals(TaxonRank.SUBDIVSION)) {
            return 18;
        }else if (rank.equals(TaxonRank.DIVISION)) {
            return 19;
        }else if (rank.equals(TaxonRank.SUPERDIVISION)) {
            return 20;
        }else if (rank.equals(TaxonRank.SUBPHYLUM)) {
            return 21;
        }else if (rank.equals(TaxonRank.PHYLUM)) {
            return 22;
        }else if (rank.equals(TaxonRank.KINGDOM)) {
            return 23;
        }else if (rank.equals(TaxonRank.DOMAIN)) {
            return 24;
        }else {
            return -1;
        }      	
    }
    
   public TaxonRank getRankFromOrder(int order) {        
        if (order==0)       
            return TaxonRank.VARIETY;
        else if (order==1) {
            return TaxonRank.SUBSPECIES;
        } else if (order==2) {
            return TaxonRank.SPECIES;
        } else if (order==3) {
            return TaxonRank.SUBSECTION;
        } else if (order==4) {
            return TaxonRank.SECTION;
        } else if (order==5) {
            return TaxonRank.SUBGENUS;
        } else if (order==6) {
            return TaxonRank.GENUS;
        } else if (order==7) {
            return TaxonRank.SUBTRIBE;
        } else if (order==8) {
            return TaxonRank.TRIBE;
        } else if (order==9) {
            return TaxonRank.SUBFAMILY;
        } else if (order==10) {
            return TaxonRank.FAMILY;
        }else if (order==11) {
            return TaxonRank.SUPERFAMILY;
        }else if (order==12) {
            return TaxonRank.SUBORDER;
        }else if (order==13) {
            return TaxonRank.ORDER;
        }else if (order==14) {
            return TaxonRank.SUPERORDER;
        }else if (order==15) {
            return TaxonRank.SUBCLASS;
        }else if (order==16) {
            return TaxonRank.CLASS;
        }else if (order==17) {
            return TaxonRank.SUPERCLASS;
        }else if (order==18) {
            return TaxonRank.SUBDIVSION;
        }else if (order==19) {
            return TaxonRank.DIVISION;
        }else if (order==20) {
            return TaxonRank.SUPERDIVISION;
        }else if (order==21) {
            return TaxonRank.SUBPHYLUM;
        }else if (order==22) {
            return TaxonRank.PHYLUM;
        }else if (order==23) {
            return TaxonRank.KINGDOM;
        }else if (order==24) {
            return TaxonRank.DOMAIN;
        }else {
            return null;
        }      	
    }
}
