/**
 *
 */
package edu.arizona.biosemantics.matrixgeneration.generateFileToTaxonMap;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import edu.arizona.biosemantics.matrixgeneration.MatrixGeneration;

/**
 * @author Hong Updates The input is a FNA volume marked for JSTOR
 */
public class FileName2TaxonLoaderFNA extends FileName2TaxonLoader {
		
    static XPath descriptionpath;
	static XPath descriptionpath2;
	
    static String authorstr = "";
    static String datestr = "";
    static XPath authorpath;
    static XPath datepath;
        
    static {
        try {
        	authorpath = XPath.newInstance("//meta/source/author");
        	datepath = XPath.newInstance("//meta/source/date");
            descriptionpath = XPath.newInstance("//description[@type='morphology']");
            descriptionpath2 = XPath.newInstance("//description[@type='morphology']/statement/text");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LOGGER.error(sw.toString());
        }
    }

    /**
     *
     */
    public FileName2TaxonLoaderFNA(String inputfilepath) throws Exception {
        super(inputfilepath);
    }

    private int number = 0;
    private int number2 = 0;
    
    @Override
    /**
     * use XPath to extract values from XML file need filename, hasdescription,
     * family, etc.
     */
    protected void populateFilename2TaxonTableUsing(File xml) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(xml);
            Element root = doc.getRootElement();
            if (descriptionpath.selectNodes(root).size() > 0) {
                values.put("hasdescription", "1");
                if(descriptionpath.selectNodes(root).size() > 1)
                	System.out.println("has " + descriptionpath.selectNodes(root).size() + " nodes");
                
                System.out.println("size: " + descriptionpath.selectNodes(root).size());
                
                List<Element> nodes= descriptionpath2.selectNodes(root);
                List<Element> descriptions =  descriptionpath.selectNodes(root);
                if(descriptions.size() > 1) {
                	System.out.println("has more than 1");
                	for(int i=1; i<descriptions.size(); i++) {
                		if(descriptions.get(i).getChildren("statement").isEmpty()) {
                			number++;
                		}
                	}
                }
                
                boolean isEmpty = true;
                if(descriptions.size() >= 1) {
                	for(int i=0; i<descriptions.size(); i++) 
                		isEmpty &= descriptions.get(i).getChildren("statement").isEmpty();
                		//if(descriptions.get(i).getChildren("statement").isEmpty())
	                	//	number2++;
                }
                if(isEmpty)
                	number2++;
                System.out.println("Number: " + number);
                System.out.println("Number2: " + number2);

                //number += descriptionpath.selectNodes(root).size();
            
            } else {
                values.put("hasdescription", "0");
                System.out.println("didnt have a description");
            }
            values.put("filename", xml.getName());

            Element author = (Element) authorpath.selectSingleNode(root);
            if(author != null) {
            	authorstr = author.getTextNormalize();
            	values.put("author", authorstr);
            }
            
            Element date = (Element) datepath.selectSingleNode(root);
            if(date != null) {
            	datestr = date.getTextNormalize();
            	values.put("date", datestr);
            }
            
            for(String rank : MatrixGeneration.ranks) {            
            	XPath rankXPath = getRankXPath(rank);
            	Element rankElement = (Element) rankXPath.selectSingleNode(root);
            	if(rankElement != null) {
            		String rankName = rankElement.getTextNormalize().toLowerCase();
            		String rankAuthority = rankElement.getAttributeValue("authority");
            		String rankDate = rankElement.getAttributeValue("date");
            		values.put(rank, rankName);
            		if(rankAuthority != null)
            			values.put(rank + "RankAuthority", rankAuthority.trim().toLowerCase());
            		if(rankDate != null)
            			values.put(rank + "RankDate", rankDate.trim().toLowerCase());
            	}
            }
            
            addToList();
            resetValues();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            e.printStackTrace();
            //	LOGGER.error(ApplicationUtilities.getProperty("CharaParser.version")+System.getProperty("line.separator")+sw.toString());
        }
    }

	private XPath getRankXPath(String rank) throws JDOMException {
		return XPath.newInstance("//taxon_identification[@status='ACCEPTED']/taxon_name[@rank='" + rank + "']");
	}

    /**
     * @param args
     */
 /*   public static void main(String[] args) {
        String filepath = "C:\\Users\\jingliu5\\UFLwork\\Charaparser\\FoC\\FoCV10\\target\\final\\";
        FileName2TaxonLoaderFNA fntf = new FileName2TaxonLoaderFNA(filepath);
        fntf.populateFilename2TaxonTable();
    }*/
}
