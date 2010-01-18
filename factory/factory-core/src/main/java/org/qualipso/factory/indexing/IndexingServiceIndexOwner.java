package org.qualipso.factory.indexing;

import org.qualipso.factory.binding.BindingService;

public interface IndexingServiceIndexOwner {

	public abstract void setBindingService(BindingService binding);

	public abstract BindingService getBindingService();

	public abstract void execute(String action, String path)
			throws IndexingServiceException;

}