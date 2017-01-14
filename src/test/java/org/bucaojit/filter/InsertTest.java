package org.bucaojit.filter;

import junit.framework.Assert;
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
			
			System.out.println("Checking for 'value'");
			int output = qf.lookup(new String("value"));
			System.out.println("The value output: " + output);
			Assert.assertEquals(58, output);
			
			int outputSecond = qf.lookup(new Integer(23));
			System.out.println("The 23 output: " + outputSecond);
			//Assert.assertEquals(23, outputSecond);
			
			int outputThird = qf.lookup(new Integer(444));
			System.out.println("The 4444 output: " + outputThird);
			//Assert.assertEquals(1, outputThird);
			
			int outputFourth = qf.lookup(new String("Not Exists"));
			System.out.println("The 'Not Exists' output: " + outputFourth);
			
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
