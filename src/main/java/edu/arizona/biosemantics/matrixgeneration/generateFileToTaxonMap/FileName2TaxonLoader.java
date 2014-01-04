/**
 *
 */
package edu.arizona.biosemantics.matrixgeneration.generateFileToTaxonMap;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import edu.arizona.biosemantics.matrixgeneration.processListen.ProcessSubject;

public abstract class FileName2TaxonLoader extends ProcessSubject{

    protected String inputfilepath;
    protected static final Logger LOGGER = Logger.getLogger(FileName2TaxonLoader.class);
    protected HashMap valuesMap = new HashMap();
    protected Hashtable<String, String> values = new Hashtable<String, String>();

    public FileName2TaxonLoader(String inputfilepath) throws Exception {
        resetValues();
        this.inputfilepath = inputfilepath;     
     //   loadValueMap();
    }
    
    public void loadValueMap() throws Exception{
        setCurrentMessage("Start Loading Files...");
        populateFilename2TaxonTable_AlphebeticNames();
        setCurrentMessage("End Loading.");
    }
   
    @Override
    protected Object doInBackground() throws Exception {
        loadValueMap();
        return this;
    }

    protected void resetValues() {
        values.put("filename", "");
        values.put("hasdescription", "");
        values.put("domain", "");
        values.put("kingdom", "");
        values.put("phylum", "");
        values.put("subphylum", "");
        values.put("superdivision", "");
        values.put("division", "");
        values.put("subdivision", "");
        values.put("superclass", "");
        values.put("class", "");
        values.put("subclass", "");
        values.put("superorder", "");
        values.put("order", "");
        values.put("suborder", "");
        values.put("superfamily", "");
        values.put("family", "");
        values.put("subfamily", "");
        values.put("tribe", "");
        values.put("subtribe", "");
        values.put("genus", "");
        values.put("subgenus", "");
        values.put("section", "");
        values.put("subsection", "");
        values.put("species", "");
        values.put("subspecies", "");
        values.put("variety", "");
    }

    protected void populateFilename2TaxonTable() {
        File[] xmls = (new File(this.inputfilepath)).listFiles();
        int[] filenames = new int[xmls.length];
        //from 1.xml 10.xml, 100.xml ...

        int i = 0;
        int j = 0;
        HashMap<Integer, String> fnMap = new HashMap();
        for (File xml : xmls) {
            String name = xml.getName().replace(".xml", "");
            if (name.indexOf("_") > 0) {
                j++;
                filenames[i] = Integer.parseInt(name.substring(0, name.length() - 2)) + j;
                fnMap.put(filenames[i], name);
                i++;
                continue;
            }
            filenames[i] = Integer.parseInt(name) + j;
            fnMap.put(filenames[i], name);
            i++;
        }
        //to 1, 2, 3, 4
        Arrays.sort(filenames);
        //int size = xmls.length;
        //must be in the original order in the original volume.
        for (i = 0; i <= filenames.length - 1; i++) {
            setCurrentPercentage(Math.round(i*100/filenames.length));
     //       setCurrentMessage(filenames[i] + ".xml");
            System.out.println(filenames[i] + ".xml");
            populateFilename2TaxonTableUsing(new File(this.inputfilepath, fnMap.get(filenames[i]) + ".xml"));
        }
    }

    //added by Jing Liu
    // used when the file names are not integer
    protected void populateFilename2TaxonTable_AlphebeticNames() {
        File[] xmls = (new File(this.inputfilepath)).listFiles();
        int[] filenames = new int[xmls.length];

        int i = 0;
        int j = 0;
        for (File xml : xmls) {
            i++;
            setCurrentPercentage(Math.round(i*100/xmls.length));
            setCurrentMessage(xml.getName());
            System.out.println(xml.getName());
            populateFilename2TaxonTableUsing(new File(this.inputfilepath, xml.getName()));
        }

    }

    protected abstract void populateFilename2TaxonTableUsing(File xml);

    public void addToList() {
        valuesMap.put(values.get("filename"), values);
        values = new Hashtable<String, String>();

    }

    public HashMap getValuesMap() {
        return valuesMap;
    }
}
