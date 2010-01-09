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
package org.qualipso.factory.indexing;

/**
 * An easy to use string holder.
 * @author Benjamin DREUX
 * @author Cynthia FLORENTIN
 * @author Jerome Blanchard (jayblanc@gmail.com)
 * @date 20 May 2009
 * @see IndexableContentI
 */
@SuppressWarnings("serial")

public class IndexableContent{

    private StringBuffer sb;

    /**
     * Class constructor
     * Create a new indexable Content
     */
    public IndexableContent() {
        sb = new StringBuffer();
    }

    /**
	 * Add a new element
	 * @param content a string to add to the content.
	 */
    public void addContentPart(String content){
        sb.append(content);
    }

    /**
	 * Allows to represent the list of keyword's hits on the document.
	 * @return a String that represent the whole content.
	 */
    public String toString() {
        return sb.toString();
    }
}
