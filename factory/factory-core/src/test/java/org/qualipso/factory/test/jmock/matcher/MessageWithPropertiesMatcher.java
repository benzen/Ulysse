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

package org.qualipso.factory.test.jmock.matcher;

import javax.jms.Message;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.qualipso.factory.notification.Event;

/**
 * @date 13 jan 2010
 * @author Benjamin Dreux (benjiiiiii@gmail.com)
 */

public class MessageWithPropertiesMatcher extends TypeSafeMatcher<Message> {
    private String action;
    private String path;
    private int counter;

    public MessageWithPropertiesMatcher(String action, String path) {
        this.action = action;
        this.path = path;
        this.counter = 0;
    }
    public MessageWithPropertiesMatcher(String action, String path, int counter) {
        this.action = action;
        this.path = path;
        this.counter = counter;
    }

    public boolean matchesSafely(Message msg) {
    	if(counter==0){
    		try{
    			return msg.propertyExists("action") &&
         	       msg.propertyExists("path")&&
         	       msg.getStringProperty("action").equals(action)&&
        		   msg.getStringProperty("path").equals(path);
    		}catch(Exception e){
        		return false;
    		}
    	}
    	else{
    		try{
    			return msg.propertyExists("action")
    			&&     msg.propertyExists("path")
    			&&     msg.propertyExists("counter")
    			&&     msg.getStringProperty("action").equals(action)
    			&&     msg.getStringProperty("path").equals(path)
    			&&     msg.getIntProperty("counter")==counter;
    		}catch(Exception e){
        		return false;
    		}
    	}
    }

    public void describeTo(Description description) {
    	if( counter != 0){
    		description.appendText("an message with properties ").appendValue(action).appendValue(" and ").appendValue(path).appendValue(" and ").appendValue(counter);
    	}
    	else{
    		description.appendText("an message with properties ").appendValue(action).appendValue(" and ").appendValue(path);
    	}
    }

    @Factory
    public static Matcher<Message> aMessageWithProperties(String action, String path) {
        return new MessageWithPropertiesMatcher(action, path);
    }
    @Factory
    public static Matcher<Message> aMessageWithProperties(String action, String path, int counter) {
        return new MessageWithPropertiesMatcher(action, path, counter);
    }
}
