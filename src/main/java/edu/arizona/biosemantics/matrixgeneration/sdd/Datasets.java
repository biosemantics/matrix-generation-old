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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TechnicalMetadata" type="{http://rs.tdwg.org/UBIF/2006/}TechnicalMetadata"/>
 *         &lt;element name="Dataset" type="{http://rs.tdwg.org/UBIF/2006/}Dataset" maxOccurs="unbounded"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecificExtension" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "technicalMetadata",
    "dataset",
    "nextVersion"
})
@XmlRootElement(name = "Datasets")
public class Datasets {

    @XmlElement(name = "TechnicalMetadata", required = true)
    protected TechnicalMetadata technicalMetadata;
    @XmlElement(name = "Dataset", required = true)
    protected List<Dataset> dataset;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

    /**
     * Gets the value of the technicalMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link TechnicalMetadata }
     *     
     */
    public TechnicalMetadata getTechnicalMetadata() {
        return technicalMetadata;
    }

    /**
     * Sets the value of the technicalMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link TechnicalMetadata }
     *     
     */
    public void setTechnicalMetadata(TechnicalMetadata value) {
        this.technicalMetadata = value;
    }

    /**
     * Gets the value of the dataset property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataset property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataset().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dataset }
     * 
     * 
     */
    public List<Dataset> getDataset() {
        if (dataset == null) {
            dataset = new ArrayList<Dataset>();
        }
        return this.dataset;
    }

    /**
     * Gets the value of the nextVersion property.
     * 
     * @return
     *     possible object is
     *     {@link VersionExtension }
     *     
     */
    public VersionExtension getNextVersion() {
        return nextVersion;
    }

    /**
     * Sets the value of the nextVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionExtension }
     *     
     */
    public void setNextVersion(VersionExtension value) {
        this.nextVersion = value;
    }

}