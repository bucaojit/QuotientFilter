package filters;
// Author: Oliver

// General purpose quotient filter, takes in java Objects as an entry 
// Approximate Membership Query (AMQ)

// import org.apache.hadoop.hbase.util for long to Bytes
import java.util.BitSet;

public class QuotientFilter {
	private final int DEFAULT_SIZE = 1000;
	private BitSet[] bitset;
	private int size;
	
	public QuotientFilter() {
		// All bits set to null
		this.bitset = new BitSet[DEFAULT_SIZE];
		this.size = DEFAULT_SIZE;
	}
	
	public QuotientFilter(int size) {
		this.bitset = new BitSet[size];
		this.size = size;
	}
	
	public BitSet[] bitSet() {
		return bitset;
	}
	
	public int getSize() {
		return size;
	}
	
	private static short getQuotient(Object obj) {
		Integer hashcode = obj.hashCode();
		// Want only the first 16-bits
		hashcode = hashcode >> 16;
		return hashcode.shortValue();
	}
	
	private static short getRemainder(Object obj) {
		Integer hashcode = obj.hashCode();
		return hashcode.shortValue();
	}
	
	public void insert(Object obj) {
		
	}
	
	public void lookup(Object obj) {
		
	}
	
	public static void main(String[] args) {
		QuotientFilter qf = new QuotientFilter(10);
		System.out.println(qf.hashCode());
		//Object obj = new Object();
		System.out.println(Integer.toBinaryString(qf.hashCode()));
		System.out.println(Integer.toBinaryString(0xFFFF & QuotientFilter.getQuotient(qf)));
		System.out.println(Integer.toBinaryString(0xFFFF & QuotientFilter.getRemainder(qf)));	
		
	}	
}
