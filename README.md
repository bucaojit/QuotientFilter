QuotientFilter
==============

###Java implementation of the approximate membership query set (AMQ)
A quotient filter, introduced by Bender et al. in 2011, is a space-efficient probabilistic data structure used to test whether an element is a member of a set (an approximate member query filter, AMQ). A query will elicit a reply specifying either that the element is definitely not in the set or that the element is probably in the set. The former result is definitive; i.e., the test does not generate false negatives. But with the latter result there is some probability, ε, of the test returning "element is in the set" when in fact the element is not present in the set (i.e., a false positive). There is a tradeoff between ε, the false positive rate, and storage size; increasing the filter's storage size reduces ε. Other AMQ operations include "insert" and "optionally delete". The more elements are added to the set, the larger the probability of false positives.

####Build
cd $QuotientFilter_Dir

`mvn clean package -DskipTests`

####Example:
See `src/test/java/org/bucaojit/filter/InsertTest.java` for a working example:
```java
		QuotientFilter qf = new QuotientFilter(75);
		try {
			qf.insert(new String("value"));
			qf.insert(new String("second value"));
			qf.insert(new Long(343443));
			qf.insert(new Integer(444));
			qf.insert(new Integer(23));
			qf.insert(new String("343443"));
			qf.printQF();
			
			int output = qf.lookup(new String("value"));
			System.out.println("The value output: " + output);
			Assert.assertEquals(58, output);
			
			int outputSecond = qf.lookup(new Integer(23));
			System.out.println("The 23 output: " + outputSecond);
			Assert.assertEquals(0, outputSecond);
			
			int outputThird = qf.lookup(new Integer(444));
			System.out.println("The 4444 output: " + outputThird);
			Assert.assertEquals(1, outputThird);
			
			int outputFourth = qf.lookup(new String("Not Exists"));
			System.out.println("The 'Not Exists' output: " + outputFourth);			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
```
Resulting output:
```
100:23 111:444 <empty><empty><empty>100:15763 <empty><empty><empty><empty>100:-6527 <empty><empty><empty><empty>
<empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty>
<empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty>
<empty><empty><empty><empty><empty><empty><empty><empty><empty><empty>100:-15675 <empty><empty>100:-28303 <empty>
<empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty><empty>


The value output: 58
The 23 output: 0
The 4444 output: 1
The 'Not Exists' output: -1
```

More Details here:
http://en.wikipedia.org/wiki/Quotient_filter
