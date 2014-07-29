package filters;
// Author: Oliver
// import org.apache.hadoop.hbase.util for long to Bytes
import java.util.BitSet;

public class QuotientFilter {
	private BitSet[] bitset;
	
	public QuotientFilter() {
		// All bits set to null
		bitset = new BitSet[1000];
	}
	
	public BitSet[] bitSet() {
		return bitset;
	}
	
	public static void main(String[] args) {
		QuotientFilter qf = new QuotientFilter();
		System.out.println(qf.hashCode());
		BitSet[] mybitset = qf.bitSet();
		//BitSet bits = 
		//mybitset[1] = 
	}
	public class SlotEntry {
		// metadata works well as bitset, quotient is 16-bits. 
		public Byte quotient;
		public BitSet metadata;
		public SlotEntry(Byte quotient) {
			this.quotient = quotient;
			//this.metadata = 
		}
	}
}
