package filters;
// Author: Oliver



// General purpose quotient filter, takes in java Objects as an entry 
// Approximate Membership Query (AMQ)

// import org.apache.hadoop.hbase.util for long to Bytes
import java.util.ArrayList;

public class QuotientFilter {
	private final int DEFAULT_SIZE = 1000;
	private ArrayList<Slot> set;
	private int size;
	
	public QuotientFilter() {
		// All bits set to null
		this.set = new ArrayList<Slot>(DEFAULT_SIZE);
		for(int i = 0; i < DEFAULT_SIZE; i++) 
			this.set.add(new Slot());
		this.size = this.set.size();
	}
	
	public QuotientFilter(int size) {
		this.set = new ArrayList<Slot>(size);
		for(int i = 0; i < size; i++) 
			this.set.add(new Slot());
		this.size = this.set.size();
	}
	
	protected ArrayList<Slot> getSet() {
		return set;
	}
	
	public int getSize() {
		return size;
	}
	
	protected static short getQuotient(Object obj) {
		Integer hashcode = obj.hashCode();
		// Want only the first 16-bits
		hashcode = hashcode >> 16;
		return hashcode.shortValue();
	}
	
	protected static short getRemainder(Object obj) {
		Integer hashcode = obj.hashCode();
		return hashcode.shortValue();
	}
	
	private int getIndex(Object obj) {
		return QuotientFilter.getQuotient(obj)/size;
	}
	
	public void insert(Object obj) {
		int index = getIndex(obj);
		Slot currentSlot = set.get(index);
		currentSlot.setRemainder(QuotientFilter.getRemainder(obj));
		 
		//depends on the current Slot's metadata
		currentSlot.setMetadata(new Metadata());
	}
	
	public Boolean lookup(Object obj) {
		int isOccupiedCount = 0, isContinuationCount = 0;	
		int currentIndex = getIndex(obj);
		Slot currentSlot = set.get(currentIndex);

		// Check if metadata bits are all clear for object's slot
		if(currentSlot.getMetadata().isClear())
			return false;
		
		// Scan left until we reach beginning of the cluster, 
		// or reach beginning of array.  
		// This is when the SHIFTED_BIT is false		
		while (currentIndex > 0) {
			currentSlot = set.get(currentIndex);
			if(currentSlot.getMetadata().getOccupied()) 
				isOccupiedCount++;
			if(!currentSlot.getMetadata().getShifted()) 
				break;
			currentIndex--;
		}		
		// currentIndex is now the start of the cluster
		while(isOccupiedCount > isContinuationCount) {
			currentSlot = set.get(currentIndex);
			if (!currentSlot.getMetadata().getContinuation()) 
				isContinuationCount++;
			currentIndex--;
		}
		
		currentIndex++;		
		// currentIndex should now be at the start of the run
		
		// Now we check
		currentSlot = set.get(currentIndex);
		do {
			if (currentSlot.getRemainder() == QuotientFilter.getRemainder(obj))
				return true;
			currentIndex++;
			currentSlot = set.get(currentIndex);
		} while(currentSlot.getMetadata().getContinuation() && currentIndex < this.size);
		
		// Did not find the remainder in the run, false
		return false;
		/*
		do {			
			currentSlot = set.get(currentIndex);		
		} while (currentIndex > 0 && currentSlot.getMetadata().getOccupied());
		*/		
	}
	
	public static void main(String[] args) {
		QuotientFilter qf = new QuotientFilter(10);
		System.out.println(qf.hashCode());
		
		System.out.println(Integer.toBinaryString(qf.hashCode()));
		System.out.println(Integer.toBinaryString(0xFFFF & QuotientFilter.getQuotient(qf)));
		System.out.println(Integer.toBinaryString(0xFFFF & QuotientFilter.getRemainder(qf)));	
		
	}	
}
