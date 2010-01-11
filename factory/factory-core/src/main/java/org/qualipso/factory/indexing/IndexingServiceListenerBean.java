package org.qualipso.factory.indexing;

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

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.Depends;
import org.qualipso.factory.Factory;
import org.qualipso.factory.FactoryResourceIdentifier;
import org.qualipso.factory.binding.BindingService;
import org.qualipso.factory.indexing.base.IndexBase;
import org.qualipso.factory.indexing.base.IndexBaseFactory;

/**
 * @date 2 dec 2009
 * @author Benjamin Dreux(benjiiiiii@gmail.com)
 */

@MessageDriven(mappedName = "indexingListener", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "topics/factoryIndexing"),
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener") })
@Depends("jboss.mq.destination:service=Topic,name=factoryIndexing")
public class IndexingServiceListenerBean implements MessageListener {

    private static Log logger = LogFactory.getLog(IndexingServiceListenerBean.class);

    // time period between two attempt to get the IndexableDocument
    private static int waitingTime = 100;
    private static int maxAttempt = 3;
    private MessageDrivenContext ctx;
    private BindingService binding;
    private IndexBase index;

    
    @Resource
    public void setMessageDrivenContext(MessageDrivenContext ctx) {
    	this.ctx = ctx;
    }
    
    public MessageDrivenContext getMessageDrivenContext() {
    	return this.ctx;
    }
    
    @EJB
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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(Message msg) {
        logger.info("onMessage(...) called");
        logger.debug("params : message=" + msg);
        try {
        	String path = msg.getStringProperty("path");
        	String action = msg.getStringProperty("action");
            if (action.equals("index"))
                this.addToIndexBase(path);
            if (action.equals("reindex"))
                this.updateInIndexBase(path);
            if (action.equals("remove"))
                this.removeFromIndexBase(path);

        } catch (Exception ex) {
            logger.error("Unexpected message", ex);
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void addToIndexBase(String path) throws IndexingServiceException {
        logger.info("index(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().index(toIndexableDocument(path));


    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void updateInIndexBase(String path) throws IndexingServiceException {
        logger.info("reindex(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().reindex(path, toIndexableDocument(path));
 
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void removeFromIndexBase(String path) throws IndexingServiceException {
        logger.info("remove(...) called");
        logger.debug("params : path=" + path);
        getIndexBase().remove(path);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private IndexableDocument toIndexableDocument(String path) throws IndexingServiceException {
        int nbAttempt = 0;   
        boolean succes = false;
        try {

        	FactoryResourceIdentifier identifier = binding.lookup(path);
        	IndexableService service = Factory.findIndexableService(identifier.getService());
        	IndexableContent content = null;
        	//if the action fails because of inconsitancy between 
        	//reel and index, retry this action tree time
        	while(nbAttempt <= maxAttempt && !succes){
            	try{
                   	content = ((IndexableService)service).getIndexableContent(path);
                    succes = true;
           	     }catch(Exception e){
                    Thread.sleep(waitingTime);
                    nbAttempt++;
                }
               
            }
            if(content == null ){
                throw new IndexingServiceException("unable to acces to resource "+path);
            }
     		
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
/*
    private class RetryIndexTask extends TimerTask{
  
        public RetryIndexTask(HashMap<String,Integer> map){
            this.map = map;
        }
    
        public void run(){
            logger.info("retyIndex(...) called");
            for (Iterator<String> i = map.keySet().iterator() ; i.hasNext() ; ){
                String key = i.next();
                Integer value = map.get(i.next());
                try{
                    addToIndexBase(key);
                }catch(Exception e){
                    map.remove(key);
                    if(value>1){
                        map.put(key,value-1);  
                    }            
                }
            }
        }
    }
*/

}
