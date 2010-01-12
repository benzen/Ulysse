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
 *
 */
package org.qualipso.factory.indexing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qualipso.factory.binding.BindingService;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Queue;
import org.qualipso.factory.Factory;
import org.qualipso.factory.FactoryResourceIdentifier;
import org.qualipso.factory.indexing.base.IndexBaseFactory;
import org.qualipso.factory.indexing.base.IndexBase;

/**
 * @date 12 jan 2010
 * @author Benjamin Dreux(benjiiiiii@gmail.com)
 */

public class IndexOwner{

    private static Log logger = LogFactory.getLog(IndexingServiceListenerBean.class);
    private BindingService binding;
    private IndexBase index;
    
    public IndexOwner(BindingService binding){
       setBindingService(binding);
    }

    public void setBindingService(BindingService binding) {
        this.binding = binding;
    }

    public BindingService getBindingService() {
        return binding;
    }
    
    private IndexBase getIndexBase() {
    	if ( index == null ) {
    		try {
    			index = IndexBaseFactory.getIndexBase();
    		} catch ( Exception e ) {
    			logger.error("unable to get index base", e);
    		}
    	}
    	return index;
    }
    
    public void execute(String action,String path) throws IndexingServiceException{
        if(action.equals("index"))
            addToIndexBase(path);
        if(action.equals("reindex"))
            updateInIndexBase(path);
        if(action.equals("remove"))
            removeFromIndexBase(path);
    }

    private void addToIndexBase(String path) throws IndexingServiceException {
        logger.info("index(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().index(toIndexableDocument(path));


    }


    private void updateInIndexBase(String path) throws IndexingServiceException {
        logger.info("reindex(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().reindex(path, toIndexableDocument(path));
 
    }

    private void removeFromIndexBase(String path) throws IndexingServiceException {
        logger.info("remove(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().remove(path);
    }


    private IndexableDocument toIndexableDocument(String path) throws IndexingServiceException {
        try {

        	FactoryResourceIdentifier identifier = binding.lookup(path);
        	IndexableService service = Factory.findIndexableService(identifier.getService());
        	
        	IndexableContent content = ((IndexableService)service).getIndexableContent(path);
        	
        	IndexableDocument document = new IndexableDocument();
        	document.setResourceIdentifier(identifier);
            document.setResourcePath(path);
            document.setResourceService(identifier.getService());
            document.setResourceType(identifier.getType());
            document.setIndexableContent(content);
            	
            return document;
        } catch (Exception e) {
            logger.error("unable index resource " + path, e);
            throw new IndexingServiceException("unable index resource " + e);
        }

        
    }
}
