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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.qualipso.factory.FactoryResourceIdentifier;

/**
 * A document is serializble repesentation of a factory resource. 
 * @author Benjamin DREUX
 * @author cynthia FLORENTIN
 * @author Jerome Blanchard (jayblanc@gmail.com)
 * @date 20 May 2009
 */


public class IndexableDocument {
    private FactoryResourceIdentifier resourceFRI;
    private String resourceService;
    private String resourceType;
    private String path;
    private IndexableContent indexableContent;



    /**
     * Set the Factory Resource Identifier wich identfie the resource represented by this document.
     * @param resourceFRI the identifier of the resource
     */
    public void setResourceIdentifier(FactoryResourceIdentifier resourceFRI){
    	this.resourceFRI = resourceFRI;
    }
    
    /**
     * Set the name of the service which create the resource represented by this document.
     * @param resourceService name of the service.
     */
    public void setResourceService(String resourceService) {
        this.resourceService = resourceService;
    }


    /*
     * Set the type of the resource represented by this document.
     * @param resourceType A string wich represent the type of resource.
     */
     public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /*
     * Set the name of the resource represented by this document.
     * @param resourceShortName the name of the resource
     */
    

    /*
     * Set the content of the resource represented by this document.
     * @param indexableContent the content of the resource
     */
    public void setIndexableContent(IndexableContent indexableContent) {
        this.indexableContent = indexableContent;
    }
    
    /**
    * Set the path of the resource represented by this document.
    * @param path the path of the resource
    **/
    public void setResourcePath(String path){
    	this.path = path;

    }

    /**
     * Get the identifier of the resource represented by this document.
     * @return the identifier.
     */
    public FactoryResourceIdentifier getResourceIdentifier() {
        return resourceFRI;

    }

    /**
     * Get the name of the service wich create the resource represented by this document.
     * @return the name of the service.
     */
    public String getResourceService() {
        return resourceService;
    }

    /**
     * Get the type of resource represented by this document.
     * @return the type of the resource.
     */
    public String getResourceType() {
        return resourceType;
    }

    
    

    /**
     * Get the content of the resource represented by this document.
     * @return the content of the resource.
     */
    public IndexableContent getIndexableContent() {
        return indexableContent;
    }

    /**
    * Get the path tio the resource represented by this document.
    * @return the path to the resource, when it was indexed.
    **/
    public String getResourcePath(){
    	return path;
    }

    /**
     * <p> Give a lucene document. 
     * A Document has a list of fields; each field has a name and a textual value.  
     * A field Index specifies whether and how a field should be indexed. 
     * Index the tokens produced by running the field's value through an Analyzer.
     * </p>
     * 
     * @return a Lucene Document is a record in the index.
     */
    public Document getDocument() {
        Document document = new Document();
        document.add(new Field("IDENTIFIER", resourceFRI.serialize(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("SERVICE", resourceService, Field.Store.YES, Field.Index.NO));
        document.add(new Field("TYPE", resourceType, Field.Store.YES, Field.Index.NO));
        document.add(new Field("PATH", path, Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("CONTENT", indexableContent.toString(), Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }
}
