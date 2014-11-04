package filters;
// Author: Oliver



// General purpose quotient filter, takes in java Objects as an entry 
// Approximate Membership Query (AMQ)

// import org.apache.hadoop.hbase.util for long to Bytes
import java.io.IOException;
import java.util.ArrayList;


/*	Completed lookup. 
 * 
 *  DONE: Lookup optimization
 *   - if lookup == true, then return index
 *   - if lookup == false, return index to insert
 
 	TODO: Insertion
 	- Review MAY-CONTAIN algorithm from white paper
 	- Similar to lookup, lookup will find where the slot should go
	- Once known that the key is NOT in the filter, we insert the remainder in the current run that keeps things in sorted order. 
	- Shift forward the remainders in slots in the cluster or after the chosen slots, update the metadata.
	From wiki:
 *  Shifting a slot's remainder does not affect the slot's is_occupied bit because it pertains to the slot, not the remainder contained in the slot.
 *  If we insert a remainder at the start of an existing run, the previous remainder is shifted and becomes a continuation slot, so we set its is_continuation bit.
 *  We set the is_shifted bit of any remainder that we shift.
 	
 	For Insertion, if the array gets filled at the end, we wrap around to the beginning
 	
 	DELETION is similar to insertion, but subsequent slots are shifted back
*/
 

public class QuotientFilter {
	private final int DEFAULT_SIZE = 1000;
    // This size is the total capacity of the qf
    private int qfSize;
	protected ArrayList<Slot> set;
    // This size should be the number of values inserted into the quotient filter
	protected int size;
	
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
        this.qfSize = this.set.size();
		// this.size = this.set.size();
	}

    public int getSize() {
        return this.qfSize;
    }
	
	// For Testing use only
	public void setSlot(int index, Slot slot) {
		this.set.set(index, slot);
	}
	
	protected ArrayList<Slot> getSet() {
		return set;
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
	
	public void insert(Object obj) throws Exception{		
		int index = getIndex(obj);
		Slot currentSlot = set.get(index);
        short remainder = QuotientFilter.getRemainder(obj);
		if(!currentSlot.getMetadata().getOccupied()) {
			// Slot is currently empty, free to set		
			currentSlot.setRemainder(QuotientFilter.getRemainder(obj));		 
			// TODO: depends on the current Slot's metadata
			currentSlot.setMetadata(new Metadata());
		}
		else {
			// The slot is occupied, see if we find the value. 
			int foundIndex;
            foundIndex = lookup(index, remainder);
			if(foundIndex != -1) {
				// lookup returned TRUE 
				throw new Exception("Unable to insert, object already exists");
			}
			else {
				// lookup returned index value of where to insert
				// foundIndex holds the index to insert the value
                insertAndShift(remainder, foundIndex);
			}
		}
	}

    public void insertAndShift(short remainder, int index) throws IOException {
        int currentIndex=index;
        boolean hasMore = false;
        do {
            // While there is a value in the index, if the next index is occupied,
            // save the next index and put the value there.  If not occupied then move the current index there
            // and done.
            currentIndex++;
            if (currentIndex > this.qfSize)
                currentIndex = 1;
            if (currentIndex == index)
                throw new IOException("Ran out of open index locations");
        }while(hasMore);
    }

    public void deleteAndShift(int index) throws IOException {
        int currentIndex=index;
        boolean hasMore = false;
        do {
            // similar to insertAndShift, shift until there is an unoccupied slot
            currentIndex++;
            if (currentIndex > this.qfSize)
                currentIndex = 1;
            if (currentIndex == index)
                throw new IOException("Ran out of open index locations");
        }while(hasMore);
    }
	
	public void delete(Object obj) throws Exception {
		int index = getIndex(obj);
		int foundIndex;
		foundIndex = lookup(index, QuotientFilter.getRemainder(obj));
		if(foundIndex != -1) {
			// Found the value
			// shift left the moved slots if they exist, otherwise set index to null
			
			// No slots to move, inserting empty slot
			Slot newSlot = new Slot();
			set.set(foundIndex, newSlot);
		}
	}

    // TODO: revisit foundIndex, changed from returning a boolean to an int
	public int lookup(Object obj) {
		return lookup(getIndex(obj), QuotientFilter.getRemainder(obj));
	}
	
	public int lookup(int index, short remainder) {
		int isOccupiedCount = 0, isContinuationCount = 0;	
		int currentIndex = index;
		Slot currentSlot = set.get(currentIndex);
        int foundIndex = -1;

		// Check if metadata bits are all clear for object's slot
		if(currentSlot.getMetadata().isClear())
			return -1;
		
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
			currentIndex++;
		}
		
		currentIndex--;		
		// currentIndex should now be at the start of the run
		
		// Now we check
		currentSlot = set.get(currentIndex);
		do {
			if (currentSlot.getRemainder() == remainder) {
				if (foundIndex != -1)
					foundIndex = currentIndex;
				return foundIndex;
			}
			else if(currentSlot.getRemainder() > remainder) {
				if (foundIndex != -1l)
					foundIndex = currentIndex;
				return -1;
			}
			currentIndex++;
			currentSlot = set.get(currentIndex);
		} while(currentSlot.getMetadata().getContinuation() && currentIndex < this.size);
		
		// Did not find the remainder in the run, false
		return -1;
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
