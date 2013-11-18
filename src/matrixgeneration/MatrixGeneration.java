/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixgeneration;

import generateFileToTaxonMap.FileName2TaxonLoader;
import generateFileToTaxonMap.FileName2TaxonLoaderFNA;
import generateFileToTaxonMap.Filename2TaxonFinder;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import IO.TxtFileIO;
import taxonomy.ITaxon;
import taxonomy.TaxonHierarchy;
import taxonomy.TaxonRank;
import conversion.DescriptionParser;
import conversion.TaxonCharacterMatrix;
import processListen.ProcessSubject;
import taxonomy.TaxonRankCalculator;

/**
 *
 * @author jingliu5
 */
public class MatrixGeneration extends ProcessSubject {

    private Filename2TaxonFinder filenameTaxon;
   // private HashMap sigPluMap;    //by Jing Liu Oct. 31, 2013
    private String inputPath;
    private boolean isPeudoroot = true;
    private String topName;
    private TaxonRank topRank;
    private File outMatrixFile;

    public MatrixGeneration(String inputPath, HashMap valueMap, File outMatrixFile) throws IOException {  // String singularPluralFileName, // Oct.31, 2013, by Jing Liu
        //     FileName2TaxonLoader filenameLoader = new FileName2TaxonLoaderFNA(inputPath);
        filenameTaxon = new Filename2TaxonFinder(valueMap);
        TxtFileIO txtio = new TxtFileIO();
  //    sigPluMap = txtio.importSingularPlural(singularPluralFileName);   //by Jing Liu Oct. 31, 2013
        this.outMatrixFile = outMatrixFile;
        this.inputPath = inputPath;
    }

    public void setIsPeudoroot(boolean isPeudoroot) {
        this.isPeudoroot = isPeudoroot;
    }

    public void setRankName(String name) {
        topName = name;
    }

    public void setRank(TaxonRank rank) {
        topRank = rank;
    }

    @Override
    protected Object doInBackground() throws Exception {
        setCurrentMessage("Start Building Hierarchy ...");
        TaxonHierarchy th;
        if (isPeudoroot) {
            th = makeHierarchyMultipleLevelsLoop_peudoRoot();
        } else {
            th = makeHierarchyMultipleLevelsLoop();
        }
        setCurrentMessage("End Building Hierarchy.");
        setCurrentMessage("Start Saving Matrix to File ...");
        TaxonCharacterMatrix matrix = new TaxonCharacterMatrix(th);
        matrix.generateMatrixFile(outMatrixFile);
        setCurrentPercentage(100);
        setCurrentMessage("End Saving Matrix to File. \r\nDone!");
        return th;
    }

    public TaxonHierarchy makeHierarchyMultipleLevelsLoop_peudoRoot() throws Exception {
        System.out.println("Making hierarchy for : PeudoRoot ");
        String rootName = "PseudoROOT";
        String existTopRank = filenameTaxon.getHigestRank();
        String existBottomRank = filenameTaxon.getLowestRank();
        TaxonRankCalculator trc = new TaxonRankCalculator();
        TaxonRank topRank = trc.getParentRank(TaxonRank.valueOf(existTopRank.toUpperCase()));        
   //     setCurrentMessage("pesudoroot1");
        DescriptionParser rootParser = new DescriptionParser(rootName, topRank, filenameTaxon, inputPath);  // sigPluMap,  //by Jing Liu Oct. 31, 2013
        ITaxon rootTaxon = rootParser.parsePeudoTaxon();
        TaxonHierarchy h = new TaxonHierarchy(rootTaxon);
        List<String> nonEmptyRankList = filenameTaxon.getNonEmptyRankList();
 //       setCurrentMessage("pesudoroot2");
        makeHierarchyMultipleLevelsLoop1(h, nonEmptyRankList);
        return h;
    }

    public TaxonHierarchy makeHierarchyMultipleLevelsLoop() throws Exception {
        System.out.println("Making hierarchy for : " + topRank.name() + " " + topName);
        DescriptionParser parser = new DescriptionParser(topName, topRank, filenameTaxon, inputPath);  // sigPluMap,  //by Jing Liu Oct. 31, 2013
        ITaxon taxon = parser.parseTaxon();
        String rankTop = filenameTaxon.getFilenameForDescription(topRank, topName);
        List<TaxonRank> ranklist = new LinkedList<TaxonRank>();
        ranklist.add(topRank);
        List<String> ranknamelist = new LinkedList<String>();
        ranknamelist.add(topName);
        TaxonHierarchy h = new TaxonHierarchy(taxon);

        List<String> nonEmptyRankList = filenameTaxon.getNonEmptyRankList();
        nonEmptyRankList.remove(0);

        makeHierarchyMultipleLevelsLoop1(h, nonEmptyRankList);
        return h;
    }

    private void makeHierarchyMultipleLevelsLoop1(TaxonHierarchy h, List<String> nonEmptyRankList) throws Exception {
        String lowestRank = filenameTaxon.getLowestRank();
        int i = 0;
        HashMap valuemap = filenameTaxon.getValueMap();
        setCurrentPercentage(0);

        for (String r : nonEmptyRankList) {
            List<String> filenames = filenameTaxon.getFilenamesForGivenRank(r);
            for (String file : filenames) {
                List<String> ranklist = null, namelist = null;
                namelist = filenameTaxon.getTaxonRankNameList(file);
                ranklist = filenameTaxon.getTaxonRankList(file);
                TaxonRank botRank = TaxonRank.valueOf(r.toUpperCase());
                String bottomName = filenameTaxon.getTaxonValues(file).get(r);
                DescriptionParser bottomParser;
                if (r.equals("species")) {
                    String genusName = filenameTaxon.getTaxonValues(file).get("genus");
                    bottomName = genusName + "_" + bottomName;
                }
                System.out.println("Taxon name: " + bottomName);
                System.out.println("File name: " + file);
                setCurrentMessage("Taxon name: " + bottomName + "      File name: " + file);
                bottomParser = new DescriptionParser(bottomName, botRank,
                        filenameTaxon, inputPath);   // sigPluMap,  //by Jing Liu Oct. 31, 2013
                ITaxon bottomTaxon = bottomParser.parseTaxon(file);
                List<TaxonRank> rl = new LinkedList<TaxonRank>();
                for (String rs : ranklist) {
                    rl.add(TaxonRank.valueOf(rs.toUpperCase()));
                }
                rl.remove(rl.size() - 1);
                namelist.remove(namelist.size() - 1);
                h.addSubTaxon(rl, namelist, bottomTaxon);
                i++;
                setCurrentPercentage(Math.round(i * 95 / valuemap.size()));
            }
        }
        setCurrentPercentage(95);
    }
    
    
    public static void main(String[] args) throws Exception {
    	String input = args[0];
    	String output = args[1];
    	
    	//String input = "input";
    	//String output = "output";
    	
    	FileName2TaxonLoader filename2Taxonloader = new FileName2TaxonLoaderFNA(input);
    	Filename2TaxonFinder filenameTaxon = new Filename2TaxonFinder(filename2Taxonloader.getValuesMap());
    	filename2Taxonloader.loadValueMap();
    	
    	MatrixGeneration mg = new MatrixGeneration(input, filenameTaxon.getValueMap(), new File(output));
    	mg.setIsPeudoroot(true);
    	
        TaxonHierarchy th;
        th = mg.makeHierarchyMultipleLevelsLoop_peudoRoot();
        TaxonCharacterMatrix matrix = new TaxonCharacterMatrix(th);
        matrix.generateMatrixFile(new File(output));
    }
    
    /**
     * @param args the command line arguments
     */
    /*  public static void main(String[] args) {
     String singularPluralFileName = "C:\\Users\\jingliu5\\UFLwork\\Data\\Matrix Conversion\\test_singularplural.txt";
     String inputPath = "C:\\Users\\jingliu5\\UFLwork\\Data\\Charaparser\\FoC\\FoCV10\\target\\finaltest\\";
     String outdirPath = "C:\\Users\\jingliu5\\UFLwork\\Data\\SDD\\Spongesmatrices\\";
     String matricesFileName = "v10matrices.txt";
     try {
     MatrixGeneration mg = new MatrixGeneration(inputPath, singularPluralFileName);
     TaxonHierarchy th = mg.makeHierarchyMultipleLevelsLoop("fabaceae (leguminosae)", TaxonRank.FAMILY);
     TaxonCharacterMatrix matrix = new TaxonCharacterMatrix(th);

     //output matrix file
     File outputdir = new File(outdirPath);
     if (!outputdir.exists()) {
     outputdir.mkdir();
     }
     matrix.generateMatrixFile(new File(outputdir, matricesFileName));
     } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     }
     }*/
}
