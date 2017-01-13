package org.bucaojit.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class InsertTest extends TestCase{
	
	public InsertTest( String testName )
    {
        super( testName );
    }
	
    public static Test suite()
    {
        return new TestSuite( InsertTest.class );
    }
	
	public  void testApp() {
		QuotientFilter qf = new QuotientFilter(75);
		try {
			qf.insert(new String("value"));
			qf.printQF();
			qf.insert(new String("second value"));
			qf.printQF();
			qf.insert(new Long(343443));
			qf.printQF();
			qf.insert(new Integer(444));
			qf.printQF();
			qf.insert(new Integer(23));
			qf.printQF();
			qf.insert(new String("343443"));
			qf.printQF();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String str[]) {
		InsertTest it = new InsertTest("insert test");
		it.testApp();
	}
}
