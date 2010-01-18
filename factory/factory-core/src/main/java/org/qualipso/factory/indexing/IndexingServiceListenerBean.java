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
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.Depends;

import org.qualipso.factory.binding.BindingService;

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


    private MessageDrivenContext ctx;
    private Queue queue;
    private ConnectionFactory connectionFactory;
    private BindingService binding;
    private IndexingServiceIndexOwner indexOwner;


    
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
	public void setIndexOwner(IndexingServiceIndexOwner indexOwner){
	    this.indexOwner = indexOwner;
	}
	public IndexingServiceIndexOwner getIndexOwner(){
	    if(this.indexOwner==null){
	        this.indexOwner = new IndexingServiceIndexOwnerImp(binding);
	    }
	    return this.indexOwner;
	}
	
    

    

    @Override
    public void onMessage(Message msg) {
        logger.info("onMessage(...) called");
        logger.debug("params : message=" + msg);
        try {
        	String action = msg.getStringProperty("action");
        	String path = msg.getStringProperty("path");
        	try{
                getIndexOwner().execute(action, path);
            }catch(IndexingServiceException e){
                send(action,path);
           	}


        } catch (JMSException ex) {
            logger.error("Unexpected message", ex);
        }

    }
    
    private void send(String action, String path) {
        try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			Message message = session.createMessage();
			message.setStringProperty("action", action);
			message.setStringProperty("path", path);
			message.setIntProperty("counter",IndexingService.NB_RETRY);
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


