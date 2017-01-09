QuotientFilter
==============

###Java implementation of the approximate membership query set (AMQ)
A quotient filter, introduced by Bender et al. in 2011, is a space-efficient probabilistic data structure used to test whether an element is a member of a set (an approximate member query filter, AMQ). A query will elicit a reply specifying either that the element is definitely not in the set or that the element is probably in the set. The former result is definitive; i.e., the test does not generate false negatives. But with the latter result there is some probability, ε, of the test returning "element is in the set" when in fact the element is not present in the set (i.e., a false positive). There is a tradeoff between ε, the false positive rate, and storage size; increasing the filter's storage size reduces ε. Other AMQ operations include "insert" and "optionally delete". The more elements are added to the set, the larger the probability of false positives.

####Build
cd $QuotientFilter_Dir

`mvn clean package -DskipTests`



More Details here:
http://en.wikipedia.org/wiki/Quotient_filter
