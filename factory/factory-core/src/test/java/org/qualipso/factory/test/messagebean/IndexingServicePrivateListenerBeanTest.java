package org.qualipso.factory.test.messagebean;

import com.bm.testsuite.BaseSessionBeanFixture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;

import javax.ejb.MessageDrivenContext;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.qualipso.factory.indexing.IndexingServiceListenerBean;
import org.qualipso.factory.indexing.IndexingServiceIndexOwner;
import org.qualipso.factory.indexing.IndexingServiceException;
import org.qualipso.factory.test.mock.MockJMSMessage;
import org.qualipso.factory.binding.BindingService;

import static org.qualipso.factory.test.jmock.matcher.MessageWithPropertiesMatcher.aMessageWithProperties;


public class IndexingServicePrivateListenerBeanTest extends BaseSessionBeanFixture<IndexingServiceListenerBean> {

	private static Log logger = LogFactory.getLog(IndexingServicePrivateListenerBeanTest.class);


	private static final Class[] usedBeans = {};

	public IndexingServicePrivateListenerBeanTest() {
	    super(IndexingServiceListenerBean.class, usedBeans);
    }
    
	private Mockery mockery;
    private MessageDrivenContext ctx;
    private Queue queue;
    private ConnectionFactory connectionFactory;
    private BindingService binding;
    private IndexingServiceIndexOwner indexOwner;


   	@BeforeClass
	public void setUp() throws Exception {
		super.setUp();
		logger.debug("injecting mock partners session beans");
		mockery = new Mockery();
		binding = mockery.mock(BindingService.class);
		ctx = mockery.mock(MessageDrivenContext.class);
		queue = mockery.mock(Queue.class);
		connectionFactory = mockery.mock(ConnectionFactory.class);
		indexOwner = mockery.mock(IndexingServiceIndexOwner.class);
		
		
		getBeanToTest().setBindingService(binding);
		getBeanToTest().setMessageDrivenContext(ctx);
		getBeanToTest().setQueue(queue);
		getBeanToTest().setConnectionFactory(connectionFactory);
		getBeanToTest().setIndexOwner(indexOwner);

	}
	@Test
	public void sendToPrivateQueue(){
	    logger.debug("test failed indexation");
        final Sequence sequence1 = mockery.sequence("sequence1");

        try {
            mockery.checking(new Expectations() {
            {
                //what if indexing process fail
                oneOf(indexOwner).execute(with(equal(new String("index"))),with(equal(new String("/fake/path"))));
                will(throwException(new IndexingServiceException("")));
                inSequence(sequence1);
                
                //the action is rethrow to the private queue
                 oneOf(connectionFactory).createConnection().createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE).createProducer(queue).send(with(aMessageWithProperties("index", "/fake/path", 2)));
                 inSequence(sequence1);
                 
                 //what if indexing process fail
                 oneOf(indexOwner).execute(with(equal(new String("index"))),with(equal(new String("/fake/path"))));
                 will(throwException(new IndexingServiceException("")));
                 inSequence(sequence1);
                 
                 //the action is rethrow to the private queue
                  oneOf(connectionFactory).createConnection().createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE).createProducer(queue).send(with(aMessageWithProperties("index", "/fake/path", 1)));
                  inSequence(sequence1);
                  
                  //what if indexing process fail
                  oneOf(indexOwner).execute(with(equal(new String("index"))),with(equal(new String("/fake/path"))));
                  will(throwException(new IndexingServiceException("")));
                  inSequence(sequence1);
                  
                  //the action is rethrow to the private queue
                   oneOf(connectionFactory).createConnection().createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE).createProducer(queue).send(with(aMessageWithProperties("index", "/fake/path", 0)));
                   inSequence(sequence1);

	          }
              });
            
            MockJMSMessage msg = new MockJMSMessage();
            msg.setStringProperty("path", "/fake/path");
            msg.setStringProperty("action", "index");
            msg.setIntProperty("counter", 3);
            getBeanToTest().onMessage(msg);
            
            
            mockery.assertIsSatisfied();
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
	
	
}
