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
package org.qualipso.factory.indexing.engine;

import java.util.ArrayList;

import javax.ejb.Local;

import org.qualipso.factory.FactoryResourceIdentifier;
import org.qualipso.factory.indexing.IndexableDocument;
import org.qualipso.factory.indexing.IndexingService;
import org.qualipso.factory.indexing.IndexingServiceException;
import org.qualipso.factory.indexing.SearchResult;

@Local
/**
 * <p>Interface which own the logic of indexing.
 * His utilization depends on the text search engine library</p>
 * @author cynthia FLORENTIN 
 * @author philippe SCHMUCKER
 */
public interface IndexEngine {
	/**
	 * <p>
	 * Index an indexable document
	 * </p>
	 * 
	 * @param doc
	 *            is an indexable document
	 * @throws IndexingServiceException
	 * @see {@link IndexingService}{@link #index(IndexableDocument)}
	 */
	public void index(IndexableDocument doc) throws IndexingServiceException;

	/**
	 * <p>
	 * Update the index with an indexable document and an Factory ressource
	 * Identifier
	 * </p>
	 * 
	 * @param path
	 *            path to the indexed resource
	 * @param doc
	 *            an Object of type IndexableDocument
	 * @throws IndexingServiceException
	 * @see {@link IndexingService}
	 *      {@link #reindex(FactoryResourceIdentifier, IndexableDocument)}
	 */
	public void reindex(String path, IndexableDocument doc)
			throws IndexingServiceException;

	/**
	 * <p>
	 * Remove the resource's indexing data from the index, or throws an
	 * IndexingServiceExcpetion if an error occurs.
	 * </p>
	 * 
	 * @param path
	 *            path to the indexed resource
	 * @throws IndexingServiceException
	 * @see {@link IndexingService}{@link #remove(FactoryResourceIdentifier)}
	 */
	public void remove(String path) throws IndexingServiceException;

	/**
	 * <p>
	 * Allows to do a query for the search
	 * </p>
	 * 
	 * @param query
	 *            is a String
	 * @return an arraylist of query's result
	 * @throws IndexingServiceException
	 */
	public ArrayList<SearchResult> search(String query)
			throws IndexingServiceException;

	/**
	 * Erase the index and create a new one.
	 * 
	 * @thows IndexingServiceException if an error occur during new index
	 *        creation.
	 */
	public void reset() throws IndexingServiceException;

	/**
	 * Give a number of indexed resource path.
	 * @param crawlResourceByRound the number of resource to give.
	 * @return
	 * @throws IndexingServiceException 
	 */
	public String[] getNextResources(int crawlResourceByRound) throws IndexingServiceException;

}
