package org.qualipso.factory.indexing.base;

/**
 * <p>A class which makes the logic of the indexation and the search.
 * It is the only object which will have a coupling with Luccent.
 * Class implements IndexI</p>
 * @author Benjamin Dreux(benjiiiiii@gmail.com)
 * @author Cynthia FLORENTIN
 * @date 28 oct 2009
 * */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.qualipso.factory.indexing.IndexableDocument;
import org.qualipso.factory.indexing.IndexingServiceException;
import org.qualipso.factory.indexing.QualipsoAnalyzer;
import org.qualipso.factory.indexing.SearchResult;

public class LuceneIndexBase implements IndexBase {
    private static LuceneIndexBase instance;
    private static String indexFolderName = "data/index/";
    private File indexDir;
    private Analyzer analyzer;
    private IndexWriter writer;
    private static Log logger = LogFactory.getLog(LuceneIndexBase.class);

    private LuceneIndexBase() throws Exception {

        indexDir = new File(indexFolderName);
        analyzer = new QualipsoAnalyzer();

        synchronized (this) {
            if (!indexDir.exists()) {
                writer = new IndexWriter(FSDirectory.open(indexDir), analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
                writer.close();
            }
        }

    }

    public static synchronized LuceneIndexBase getInstance() throws IndexingServiceException {
        try {
            if (instance == null)
                instance = new LuceneIndexBase();
            return instance;
        } catch (Exception e) {
            logger.error("can't get instance of index ", e);
            throw new IndexingServiceException("Can't get instance of index");
        }
    }

    @Override
    public void index(IndexableDocument doc) throws IndexingServiceException {
        logger.info("index(...) called");
        logger.debug("params : IndexableDocument=" + doc);
        try {
            synchronized (this) {
                try {
                    writer = new IndexWriter(FSDirectory.open(indexDir), analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
                    writer.addDocument(doc.getDocument());
                    writer.optimize();
                } finally {
                    writer.close();
                }
            }
        } catch (IOException e) {
            logger.error("unable to index document " + doc, e);
            throw new IndexingServiceException("Can't index a document", e);
        }

    }

    @Override
    public void reindex(String path, IndexableDocument doc) throws IndexingServiceException {
        logger.info("reindex(...) called");
        logger.debug("params :path=" + path + " doc=" + doc);
        remove(path);
        index(doc);

    }

    @Override
    public void remove(String path) throws IndexingServiceException {
        logger.info("remove(...) called");
        logger.debug("params :path=" + path);
        try {
            synchronized (this) {
                Term term = new Term("PATH", path);
                IndexReader reader = IndexReader.open(FSDirectory.open(indexDir), false);
                reader.deleteDocuments(term);
                reader.close();
            }
        } catch (IOException e) {
            logger.error("unable to remove resource " + path + " from index", e);
            throw new IndexingServiceException("Can't remove resource" + path + " from index", e);
        }

    }

    @Override
    public ArrayList<SearchResult> search(String queryString) throws IndexingServiceException {
        try {
            synchronized (this) {
                QueryParser queryParser = new QueryParser("CONTENT", analyzer);
                Query query = queryParser.parse(queryString);

                IndexReader reader = IndexReader.open(FSDirectory.open(indexDir), true);
                IndexSearcher searcher = new IndexSearcher(reader);

                Hits hits = searcher.search(query);

                ArrayList<SearchResult> listRs = new ArrayList<SearchResult>(hits.length());
                SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<highlighted>", "</highlighted>");
                QueryScorer scorer = new QueryScorer(query);
                Highlighter highlighter = new Highlighter(formatter, scorer);

                for (int i = 0; i < hits.length(); i++) {
                    String fri = hits.doc(i).get("IDENTIFIER");
                    float score = hits.score(i);
                    String higlighteText = highlighter.getBestFragment(analyzer, "CONTENT", hits.doc(i).get("CONTENT"));
                    String name = hits.doc(i).get("NAME");
                    String type = hits.doc(i).get("SERVICE") + "/" + hits.doc(i).get("TYPE");
                    String path = hits.doc(i).get("PATH");
                    SearchResult sr = new SearchResult();
                    sr.setScore(score);
                    sr.setName(name);
                    sr.setFactoryResourceIdentifier(fri);
                    sr.setType(type);
                    sr.setPath(path);
                    sr.setExplain(higlighteText);

                    listRs.add(sr);

                }
                searcher.close();
                reader.close();
                return listRs;
            }

        } catch (Exception e) {
            logger.error("unable search in index using " + queryString, e);
            throw new IndexingServiceException("Can't search in index using '" + queryString + "'\n", e);
        }

    }
    
    @Override
    public void reset() throws IndexingServiceException{
        try{
            synchronized{
                writer = new IndexWriter(FSDirectory.open(indexDir), analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
                writer.deleteAll();
            }
        }catch(Exception e){
            logger.error("unable to reset the index",e);
            throw new IndexingServiceException("unable to reset the index");
        }
        finally{
            writer.close();
        }
                
    }

}
