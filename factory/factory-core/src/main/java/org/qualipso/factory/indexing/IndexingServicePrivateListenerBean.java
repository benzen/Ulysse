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

import org.qualipso.factory.binding.BindingService;
import javax.annotation.Resource;
import org.jboss.ejb3.annotation.Depends;
import javax.ejb.EJB;
import javax.ejb.ActivationConfigProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Queue;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;


/**
 * @date 12 jan 2010
 * @author Benjamin Dreux(benjiiiiii@gmail.com)
 */ 
 
 
@MessageDriven(mappedName = "indexingPrivateListener", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/factoryIndexingPrivate"),
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener") })
@Depends("jboss.mq.destination:service=Queue,name=factoryIndexingPrivate")
public class IndexingServicePrivateListenerBean implements MessageListener {

    private static Log logger = LogFactory.getLog(IndexingServiceListenerBean.class);
    private MessageDrivenContext ctx;
    private Queue queue;
    private ConnectionFactory connectionFactory;
    private BindingService binding;
    private IndexEngineManager indexOwner;



    
    @Resource
    public void setMessageDrivenContext(MessageDrivenContext ctx) {
    	this.ctx = ctx;
    }
    
    public MessageDrivenContext getMessageDrivenContext() {
    	return this.ctx;
    }
    @Resource(mappedName = "ConnectionFactory")
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public ConnectionFactory getConnectionFatory() {
		return this.connectionFactory;
	}

	@Resource(mappedName = "queue/factoryIndexingPrivate")
	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public Queue getQueue() {
		return this.queue;
	}
	@EJB
	public void setBindingService(BindingService binding){
	    this.binding = binding;
	}
	public BindingService getBindingService(){
	    return this.binding;
	}
	public void setIndexOwner(IndexEngineManager indexOwner){
	    this.indexOwner = indexOwner;
	}
	public IndexEngineManager getIndexOwner(){
	    if(this.indexOwner==null){
	        this.indexOwner = new IndexEngineManagerImp(binding);
	    }
	    return this.indexOwner;
	}

    
   @Override
    public void onMessage(Message msg) {
        logger.info("onMessage(...) called");
        logger.debug("params : message=" + msg);
        
        String path = null;
      	String action = null;
        int counter = -1;
        try {
           	path = msg.getStringProperty("path");
        	action = msg.getStringProperty("action");
            counter = msg.getIntProperty("counter");
            
            if( counter > 0){   	
            	if(action.equals("index")){
        			getIndexOwner().index(path);
        		}else if(action.equals("reindex")){
        			getIndexOwner().reindex(path);
        		}else if(action.equals("remove")){
        			getIndexOwner().remove(path);
        		}
            }
            
        }catch(IndexingServiceException e){
            //action fail
            send(action,path,counter-1);
            logger.error(action+ " of "+ path+" rescheduled", e);
        }catch (JMSException ex) {
            logger.error("Unexpected message", ex);
        }

    }
    private void send(String action, String path, int counter){
        try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			Message message = session.createMessage();
			message.setStringProperty("action", action);
			message.setStringProperty("path", path);
			message.setIntProperty("counter",counter);
			connection.start();
			producer.send(message);
			producer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			logger.error("Unable to send message " + action, e);
		}
	}
}

