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
import org.qualipso.factory.Factory;
import org.qualipso.factory.FactoryResourceIdentifier;
import org.qualipso.factory.indexing.engine.IndexEngine;
import org.qualipso.factory.indexing.engine.IndexEngineFactory;

/**
 * @date 12 jan 2010
 * @author Benjamin Dreux(benjiiiiii@gmail.com)
 */

public class IndexEngineManagerImp implements IndexEngineManager{

    private static Log logger = LogFactory.getLog(IndexingServiceListenerBean.class);
    private BindingService binding;
    private IndexEngine index;
    
    public IndexEngineManagerImp(BindingService binding){
       setBindingService(binding);
    }

    /* (non-Javadoc)
	 * @see org.qualipso.factory.indexing.IndexEngineManager#setBindingService(org.qualipso.factory.binding.BindingService)
	 */
    public void setBindingService(BindingService binding) {
        this.binding = binding;
    }

    /* (non-Javadoc)
	 * @see org.qualipso.factory.indexing.IndexEngineManager#getBindingService()
	 */
    public BindingService getBindingService() {
        return binding;
    }
    
    private IndexEngine getIndexEngine() {
    	if ( index == null ) {
    		try {
    			index = IndexEngineFactory.getIndexEngine();
    		} catch ( Exception e ) {
    			logger.error("unable to get index base", e);
    		}
    	}
    	return index;
    }
    


    public void index(String path) throws IndexingServiceException {
        logger.info("index(...) called");
        logger.debug("params : path=" + path);
        getIndexEngine().index(toIndexableDocument(path));


    }


    public void reindex(String path) throws IndexingServiceException {
        logger.info("reindex(...) called");
        logger.debug("params : path=" + path);
        getIndexEngine().reindex(path, toIndexableDocument(path));
 
    }

    public void remove(String path) throws IndexingServiceException {
        logger.info("remove(...) called");
        logger.debug("params : path=" + path);
        getIndexEngine().remove(path);
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
