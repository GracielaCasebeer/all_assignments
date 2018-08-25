package edu.jhu.bdpuh;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Hdfs CLient.
 */
public class HdfsClientTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HdfsClientTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( HdfsClientTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testClient()
    {
        assertTrue( true );
    }
}
