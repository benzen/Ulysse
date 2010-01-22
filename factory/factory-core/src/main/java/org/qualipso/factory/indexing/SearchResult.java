/*
 *
 * Qualipso Factory
 * Copyright (C) 2006-2010 INRIA
 * http://www.inria.fr - molli@loria.fr
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of LGPL. See licenses details in LGPL.txt
 *
 * Initial authors :
 *
 * Jérôme Blanchard / INRIA
 * Pascal Molli / Nancy Université
 * Gérald Oster / Nancy Université
 * Christophe Bouthier / INRIA
 * 
 */
package org.qualipso.factory.indexing;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.qualipso.factory.FactoryNamingConvention;
import org.qualipso.factory.FactoryResourceIdentifier;


/**
 * <p>
 * Class which allows to get back the answer of a request for a research
 * with certain property as the type of document, its path, a description on
 * document, etc....
 * </p>
 * 
 * @author Benjamin DREUX
 * @author Cynthia FLORENTIN
 * @author Jerome Blanchard (jayblanc@gmail.com)
 * @date 20 May 2009
 */


// TODO move this class to the factory level (FactorySearchResult)
@XmlType(name = "search-result", namespace = FactoryNamingConvention.SEARCH_NAMESPACE, propOrder = { "path", "score", "explain", "name", "type", "identifier" })
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String path;
    private float score;
    private String explain;
    private String name;
    private String type;
    private FactoryResourceIdentifier resourceFRI;

    /**
     * Get the path to resource.
     * @return the  path to resource when it was indexed.
     */
    @XmlAttribute(name = "identifier", required = true)
    public String getPath() {
        return path;
    }

    /**
     * Set the path to the resource.
     *@param path the path to the resource
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the score(correspond to the matching to a given to a search query)
     * @return the score of the search result.
     */
    @XmlAttribute(name = "score", required = true)
    public float getScore() {
        return score;
    }

    /**
     * Set the score(correspond to the matching to a given to a search query)
     * @param score the score of the result.
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Get a text to explain the content of a resource described by the searchResult.
     * @return the explanation.
     */
    @XmlAttribute(name = "explain", required = true)
    public String getExplain() {
        return explain;
    }

    /**
     * Set a text to explain the content of the resource.
     */
    public void setExplain(String explain) {
        this.explain = explain;
    }

    /**
     * Get the name of the resource presented by the result.
     * @return the name of the resource
     */
    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Set a name of document
     * </p>
     * 
     * @param name, the string which is the name of document
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the resource described by this result.
     * @return the type of the resource
     */
    @XmlAttribute(name = "type", required = true)
    public String getType() {
        return type;
    }

    /**
     * Set the type of the resource represented by this search result.
     * @param type the type of the resource.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the identifier of the resource.
     * @return the identfier of the resource.
     */
    @XmlTransient
    public FactoryResourceIdentifier getFactoryResourceIdentifier() {
        return resourceFRI;
    }

    /**
     * Set the identfier of the resource described by this result.
     * @param resourceIdentfier the identifier of the resource.
     */
    public void setFactoryResourceIdentifier(FactoryResourceIdentifier resourceIdentifier) {
        this.resourceFRI = resourceIdentifier;
    }

    /**
     * Set the identfier of the resource described by this result.
     * @param resourceIdentfier the identifier of the resource as a string.
     */
    public void setFactoryResourceIdentifier(String resourceIdentifier) {
        this.resourceFRI = FactoryResourceIdentifier.deserialize(resourceIdentifier);
    }

}
