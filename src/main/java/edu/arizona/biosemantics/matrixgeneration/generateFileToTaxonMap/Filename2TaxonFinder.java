package edu.arizona.biosemantics.matrixgeneration.generateFileToTaxonMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import edu.arizona.biosemantics.matrixgeneration.MatrixGeneration;
import edu.arizona.biosemantics.matrixgeneration.taxonomy.TaxonRank;

/**
 * DAO for filename-taxon database.
 *
 * @author Jing Liu
 *
 */
public class Filename2TaxonFinder{

    /**
     * Ordered list of taxon ranks.
     */
    private List<String> taxonRank;
    protected HashMap valuesMap;
    protected static final Logger LOGGER = Logger.getLogger(FileName2TaxonLoader.class);

    /**
     * Create a new FilenameTaxonDao and add ranking list.
     */
    public Filename2TaxonFinder(HashMap valuesMap) {
        taxonRank = Arrays.asList(MatrixGeneration.ranks);
        this.valuesMap = valuesMap; 
    }

    public HashMap getValueMap(){
        return this.valuesMap;
    }
    
    /**
     * Gets a map of taxon level values for a given file.
     *
     * @param filename
     * @return
     */
    public Map<String, String> getTaxonValues(String filename) {
        Map<String, String> result = new HashMap<String, String>();
        result = (Map<String, String>) valuesMap.get(filename);
        return result;
    }

    /**
     * added by Jing Liu Gets a taxon level name list for a given file.
     *
     * @param filename
     * @return
     */
    public List<String> getTaxonRankNameList(String filename) {
        Map<String, String> result = getTaxonValues(filename);
        List<String> ranknamelist = new LinkedList<String>();
        Map<String, String> values = getTaxonValues(filename);
        
		for(String rank : MatrixGeneration.ranks) {
			String rankName = values.get(rank);
			if(!rankName.isEmpty())
				ranknamelist.add(rankName);
		}
        return ranknamelist;
    }

    /**
     * added by Jing Liu Gets a taxon level list for a given file.
     *
     * @param filename
     * @return
     */
    public List<String> getTaxonRankList(String filename) {
        Map<String, String> result = getTaxonValues(filename);
        List<String> ranklist = new LinkedList<String>();
        
        for(String rank : MatrixGeneration.ranks) {
			String rankName = result.get(rank);
			if(!rankName.isEmpty())
				ranklist.add(rank);
		}
        return ranklist;
    }

    /**
     * added by Jing Liu Gets a map of taxon level value for a given file.
     *
     * @param filename
     * @return
     */
    public String getTaxonRank(String filename) {
        String result = "";

        Map<String, String> values = getTaxonValues(filename);
        
        for(String rank : MatrixGeneration.ranks) {
			String rankName = values.get(rank);
			if(!rankName.isEmpty())
				result = rank;
		}
        return result;
    }

    /**
     * Get a list of filenames that match the name of a given taxon.
     *
     * @param taxon The taxon level at which to match.
     * @param name The name for that taxon.
     * @return A list of all file names matching that taxon name.
     */
    public List<String> getFileListByTaxonName(String taxon, String name) {
        List<String> result = new ArrayList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            if (values.get(taxon).equals(name)) {
                result.add(filename);
            }
        }
        return result;
    }

    /**
     * Get the filename for the family description of a taxon.
     *
     * @param familyName
     * @return
     */
    public String getFilenameOfFamilyDescription(String familyName) {
        String result = "";
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            if (values.get("family").equals(familyName)
                    && values.get("subfamily").equals("")
                    && values.get("tribe").equals("")
                    && values.get("subtribe").equals("")
                    && values.get("genus").equals("")
                    && values.get("subgenus").equals("")
                    && values.get("section").equals("")
                    && values.get("subsection").equals("")
                    && values.get("species").equals("")
                    && values.get("subspecies").equals("")
                    && values.get("variety").equals("")) {
                result = filename;
            }
        }
        return result;
    }

    /**
     * Gets the filename for a taxon at a given rank with a given name, such
     * that the file is the lowest level description of the taxon at that level.
     *
     * @param rank
     * @param name
     * @return
     */
    public String getFilenameForDescription(TaxonRank rank, String name) {
        String tRank = rank.toString().toLowerCase();
        int indexOfRank = taxonRank.indexOf(tRank);
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            int i = 0;
            if (values.get(tRank).equals(name)) {
                for (i = indexOfRank + 1; i < taxonRank.size(); i++) {
                    if (!values.get(taxonRank.get(i)).equals("")) {
                        break;
                    }
                }
            }
            if (i == taxonRank.size()) {
                return filename;
            }
        }
        return filename;
    }

    /**
     * Get filenames for descriptions within a taxonomical range.
     *
     * @param topLevel
     * @param topName
     * @param bottomLevel
     * @return
     */
    public List<String> getFilenamesForManyDescriptions(TaxonRank topLevel,
            String topName, TaxonRank bottomLevel) {
        List<String> result = new ArrayList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;

        String topRank = topLevel.toString().toLowerCase();
        String bottomRank = bottomLevel.toString().toLowerCase();
        int stopIndex = taxonRank.indexOf(bottomRank) + 1; // highest rank index
        // at which value is
        // empty
        String whereClause = topRank + " = \'" + topName + "\' ";
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            if (values.get(topRank).equals(topName)) {
                int i;
                for (i = stopIndex; i < taxonRank.size(); i++) {
                    if (!taxonRank.get(i).equals("")) {
                        break;
                    }
                }
                if (i == taxonRank.size()) {
                    result.add(filename);
                }
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * added by Jing Liu Get filenames for descriptions within a taxonomical
     * range.
     *
     * @param topLevel
     * @param topName
     * @param bottomLevel
     * @return
     */
    public List<String> getFilenamesForManyDescriptionsByOrder(
            TaxonRank topLevel, String topName, TaxonRank bottomLevel) {
        List<String> filenames = new LinkedList<String>();
        String topRank = topLevel.toString().toLowerCase();
        String bottomRank = bottomLevel.toString().toLowerCase();
        int stopIndex = taxonRank.indexOf(bottomRank) + 1; // highest rank index
        // at which value is
        // empty
        int startIndex = taxonRank.indexOf(topRank) + 1;
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = taxonRank.size();
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            if (values.get(topRank).equals(topName)) {
                int j;
                for (j = stopIndex; j < taxonRank.size(); j++) {
                    String tRank = taxonRank.get(j);
                    if (!values.get(tRank).equals("")) {
                        break;
                    }
                }
                if (j == taxonRank.size()) {
                    filenames.add(filename);
                }
            }
        }

        int[] ranks = new int[filenames.size()];
        for (int i = 0; i < filenames.size(); i++) {
            Map<String, String> values = getTaxonValues(filenames.get(i));
            for (int j = stopIndex; j >= startIndex; j--) {
                String tRank = taxonRank.get(j);
                if (!values.get(tRank).equals("")) {
                    ranks[i] = j;
                }
            }
        }


        List<String> orderedfilenames = new LinkedList<String>();
        for (int j = stopIndex; j >= startIndex; j--) {
            for (int i = 0; i < filenames.size(); i++) {
                if (ranks[i] == j) {
                    orderedfilenames.add(filenames.get(i));
                }
            }
        }
        System.out.println(orderedfilenames);
        return orderedfilenames;
    }

    /**
     * added by Jing Liu Get filenames for descriptions in the next rank.
     *
     * @param topLevel
     * @param topName
     * @param bottomLevel
     * @return
     */
    public List<String> getFilenamesForManyDescriptionsNextOrder(
            List<TaxonRank> topLevel, List<String> topName) {
        List<String> filenames = new LinkedList<String>();
        String bottomRank = getNextRank(topLevel, topName).toString()
                .toLowerCase();
        if (!bottomRank.isEmpty()) {
            Set<String> keyset = valuesMap.keySet();
            Iterator it = keyset.iterator();
            String filename = null;
            int index = taxonRank.size();
            while (it.hasNext()) {
                filename = (String) it.next();
                Map<String, String> values = getTaxonValues(filename);
                int i = 0;

                for (String name : topName) {
                    if (!values.get(topLevel.get(i).name().toLowerCase())
                            .equals(name)) {
                        break;
                    } else {
                        i++;
                    }
                }

                if (i == topName.size() && !values.get(bottomRank).equals("")) {
                    int stopIndex = taxonRank.indexOf(bottomRank) + 1; // highest rank
                    // index at
                    // which value
                    // is empty
                    int j;
                    for (j = stopIndex; j < taxonRank.size(); j++) {
                        String tRank = taxonRank.get(j);
                        if (!values.get(tRank).equals("")) {
                            break;
                        }
                    }
                    if (j == taxonRank.size()) {
                        filenames.add(filename);
                    }
                }

            }
        }
        System.out.println(filenames);
        return filenames;
    }

    /**
     * added by Jing Liu Get filenames a given rank.
     *
     * @param rank
     * @return
     */
    public List<String> getFilenamesForGivenRank(String rank) {
        List<String> filenames = new LinkedList<String>();
        int stopIndex = taxonRank.indexOf(rank) + 1; // highest rank index at
        // which value is empty
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = taxonRank.size();
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            if (!values.get(rank).equals("")) {
                int j;
                for (j = stopIndex; j < taxonRank.size(); j++) {
                    String tRank = taxonRank.get(j);
                    if (!values.get(tRank).equals("")) {
                        break;
                    }
                }
                if (j == taxonRank.size()) {
                    filenames.add(filename);
                }
            }
        }
        System.out.println(filenames);
        return filenames;
    }

    /**
     * added by Jing Liu Get filenames for descriptions in the next rank.
     *
     * @param topLevel
     * @param topName
     * @param bottomLevel
     * @return
     */
    public List<String> getRankNamesForNextOrder(List<TaxonRank> topLevel,
            List<String> topName) {
        List<String> ranknames = new LinkedList<String>();
        // String curRank =
        // topLevel.get(topLevel.size()-1).name().toLowerCase();
        String bottomRank = getNextRank(topLevel, topName).toString()
                .toLowerCase();

        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = taxonRank.size();
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            int i = 0;
            for (String name : topName) {
                if (!values.get(topLevel.get(i).name().toLowerCase()).equals(name)) {
                    break;
                } else {
                    i++;
                }
            }
            if (i == topName.size()) {
                if (!values.get(bottomRank).equals("")) {
                    if (!ranknames.contains(values.get(bottomRank))) {
                        ranknames.add(values.get(bottomRank));
                    }
                }
            }
        }
        System.out.println(ranknames);
        return ranknames;
    }

    // added byJing Liu
    /**
     * Gets the the highest rank that is not empty in the current table.
     *
     * @return
     */
    public String getHigestRank() {
        List<String> ranklist = new LinkedList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = taxonRank.size();
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            for (int i = 0; i < taxonRank.size(); i++) {
                String tRank = taxonRank.get(i);
                if (!values.get(tRank).equals("")) {
                    if (index > i) {
                        index = i;
                        break;
                    }
                }

            }
        }
        String rank = taxonRank.get(index);
        return rank;
    }

    /**
     * Gets the the lowest rank that is not empty in the current table.
     *
     * @return
     */
    public String getLowestRank() {
        List<String> ranklist = new LinkedList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = 0;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            for (int i = taxonRank.size() - 1; i >= 0; i--) {
                String tRank = taxonRank.get(i);
                if (!values.get(tRank).equals("")) {
                    if (index < i) {
                        index = i;
                        break;
                    }
                }

            }
        }
        String rank = taxonRank.get(index);
        return rank;
    }

    /**
     * added by Jing Liu Gets the the rank list that is not empty in the current
     * table.
     *
     * @return
     */
    public List<String> getNonEmptyRankList() {
        List<String> ranklist = new LinkedList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            String lowestRank = "";
            for (int i = 0; i < taxonRank.size(); i++) {
                String tRank = taxonRank.get(i);
                if (!values.get(tRank).equals("")) {
                    lowestRank = tRank;
                }
            }
            if (lowestRank != "" && !ranklist.contains(lowestRank)) {
                ranklist.add(lowestRank);
            }
        }
        List<String> ordered_ranklist = new LinkedList<String>();
        if (ranklist.size() > 0) {
            for (int i = 0; i < taxonRank.size(); i++) {
                if (ranklist.contains(taxonRank.get(i))) {
                    ordered_ranklist.add(taxonRank.get(i));
                }
            }
        }
        return ordered_ranklist;

    }

    /**
     * By Jing Liu Whether we have reached the the lowest rank of currrent
     * branch.
     *
     * @return
     */
    public boolean reachedLowestRank(List<TaxonRank> topLevel,
            List<String> topName) {
        String bottomRank = getNextRank(topLevel, topName).toString()
                .toLowerCase();
        return bottomRank.isEmpty();
    }

    /**
     * Gets the the next rank of the current rank that is not empty in the
     * current table.
     *
     * @return
     */
    public String getNextRank(List<TaxonRank> levels, List<String> names) {
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        int index = taxonRank.size();
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            int i = 0;
            for (String name : names) {
                if (!values.get(levels.get(i).name().toLowerCase()).equals(name)) {
                    break;
                } else {
                    i++;
                }
            }
            if (i == names.size()) {
                String currRank = levels.get(i - 1).toString().toLowerCase();
                int currIndex = taxonRank.indexOf(currRank) + 1;
                for (int j = currIndex; j < taxonRank.size(); j++) {
                    String tRank = taxonRank.get(j);
                    if (!values.get(tRank).equals("")) {
                        if (j < index) {
                            index = j;
                            break;
                        } else {
                            break;
                        }

                    }
                }
            }
        }

        it = keyset.iterator();
        String nextRank = taxonRank.get(index);
        return nextRank;
    }

    /**
     * Gets the the lowest rank that is not empty in the current table.
     *
     * @return true if rankA is higher than rankB; false if rank A is equal or
     * lower than rankB.
     *
     */
    public boolean compareRanks(String rankA, String rankB) {
        int a = 0, b = 0;
        for (int i = 0; i < taxonRank.size(); i++) {
            if (taxonRank.get(i).equals(rankA)) {
                a = i;
            }
            if (taxonRank.get(i).equals(rankB)) {
                b = i;
            }
        }
        return a < b;
    }

    /**
     * Gets the the lowest rank that is not empty in the current table.
     *
     * @return true if rankA is equals as rankB; false otherwise.
     *
     */
    public boolean equalRanks(String rankA, String rankB) {
        int a = 0, b = 0;
        for (int i = 0; i < taxonRank.size(); i++) {
            if (taxonRank.get(i).equals(rankA)) {
                a = i;
            }
            if (taxonRank.get(i).equals(rankB)) {
                b = i;
            }
        }
        return a == b;
    }

    /**
     * Get filenames for descriptions within a taxonomical range.
     *
     * @param topLevel
     * @param topName
     * @param bottomLevel
     * @return
     */
    public List<String> getRankNamesForGivenRank(TaxonRank topLevel) {
        List<String> ranknames = new LinkedList<String>();
        String topRank = topLevel.toString().toLowerCase();

        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            Map<String, String> values = getTaxonValues(filename);
            int i = 0;
            if (!values.get(topRank).equals("")) {
                String lowestRank = "";
                for (int j = 0; j < taxonRank.size(); j++) {
                    if (values.get(taxonRank.get(j)) != "") {
                        lowestRank = taxonRank.get(j);
                    }
                }
                if (lowestRank.equals(topRank)) {
                    if (!ranknames.contains(values.get(topRank))) {
                        ranknames.add(values.get(topRank));
                    }
                }
            }
        }
        System.out.println(ranknames);
        return ranknames;
    }

    // added by Jing Liu
    public List<String> getAllRankNames() {
        List<String> res = new LinkedList<String>();
        Set<String> keyset = valuesMap.keySet();
        Iterator it = keyset.iterator();
        String filename = null;
        while (it.hasNext()) {
            filename = (String) it.next();
            List<String> ranknames = getTaxonRankNameList(filename);
            // List<String> ranklists=getTaxonRankList(filename);
            String rankname = ranknames.get((ranknames.size() - 1));
            // String ranklist = ranklists.get((ranklists.size()-1));
            res.add(rankname.toLowerCase());
        }
        return res;
    }
}
