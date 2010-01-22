package org.qualipso.factory.indexing;

import org.qualipso.factory.binding.BindingService;

public interface IndexEngineManager  {

	public void setBindingService(BindingService binding);

	public BindingService getBindingService();
	
	public void index(String path) throws IndexingServiceException;
	public void reindex(String path) throws IndexingServiceException;
	public void remove(String path) throws IndexingServiceException;
	
	
}