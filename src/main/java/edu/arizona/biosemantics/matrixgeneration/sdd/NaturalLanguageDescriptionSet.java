//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.17 at 07:34:43 PM EDT 
//


package edu.arizona.biosemantics.matrixgeneration.sdd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaturalLanguageDescriptionSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NaturalLanguageDescriptionSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Set">
 *       &lt;sequence>
 *         &lt;element name="NaturalLanguageDescription" type="{http://rs.tdwg.org/UBIF/2006/}NaturalLanguageDescription" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NaturalLanguageDescriptionSet", propOrder = {
    "naturalLanguageDescription"
})
public class NaturalLanguageDescriptionSet
    extends Set
{

    @XmlElement(name = "NaturalLanguageDescription", required = true)
    protected List<NaturalLanguageDescription> naturalLanguageDescription;

    /**
     * Gets the value of the naturalLanguageDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the naturalLanguageDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNaturalLanguageDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NaturalLanguageDescription }
     * 
     * 
     */
    public List<NaturalLanguageDescription> getNaturalLanguageDescription() {
        if (naturalLanguageDescription == null) {
            naturalLanguageDescription = new ArrayList<NaturalLanguageDescription>();
        }
        return this.naturalLanguageDescription;
    }

}