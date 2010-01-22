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
package org.qualipso.factory.indexing.schuler;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;


import org.qualipso.factory.binding.BindingService;
import org.qualipso.factory.indexing.IndexingService;
import org.qualipso.factory.indexing.IndexEngineManager;
import org.qualipso.factory.indexing.IndexEngineManagerImp;

/**
 * @author Benjamin Dreux (benjiiiiii@gmail.com)
 * @date 22 jan 2010 
 */
@Stateless
public class IndexSchulder implements  TimedObject {

	private TimerService timerService;
	private BindingService binding;

	public BindingService getBinding() {
		return binding;
	}
	@EJB
	public void setBinding(BindingService binding) {
		this.binding = binding;
	}
	public TimerService getTimerService() {
		return timerService;
	}
	@Resource
	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
	

	@Override	
	public  void  ejbTimeout(Timer timer){
		String path = (String) timer.getInfo();
		int failure = 0;
		IndexEngineManager imanager = new IndexEngineManagerImp(binding);
		try{
			imanager.remove(path);
			while(failure <= IndexingService.NB_RETRY){
				try{
					imanager.index(path);
				}catch(Exception e){
					failure++;	
				}
			}
			
		}catch(Exception e){}
		
	}


	public void create(Serializable resource) {
		  timerService.createTimer(IndexingService.CRAWL_START, IndexingService.CRAWL_INDEX_DURATION, resource);  
	}

}
