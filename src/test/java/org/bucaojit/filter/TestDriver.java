//package filters.tests;
package org.bucaojit.filter;
// Author: Oliver

import org.bucaojit.filter.Metadata;
import org.bucaojit.filter.QuotientFilter;
import org.bucaojit.filter.Slot;

// Test for insert QuotientFilter
public class TestDriver {
	
	public static void main(String[] args) {
		QuotientFilter qf = new QuotientFilter(10);
		// Setting up quotient filter to look like State 2 in
		// http://en.wikipedia.org/wiki/Quotient_filter
		Metadata index1 = new Metadata();
		Metadata index2 = new Metadata();
		Metadata index3 = new Metadata();
		Metadata index4 = new Metadata();
		Metadata index7 = new Metadata();
		
		index1.setOccupied();
		
		index2.setContinuation();
		index2.setOccupied();
		index2.setShifted();
		
		index3.setShifted();
		
		index4.setOccupied();
		
		index7.setOccupied();
				
		// For testing set the remainders to different values
		Slot slot1 = new Slot((short)1, index1);
		Slot slot2 = new Slot((short)2, index2);
		Slot slot3 = new Slot((short)3, index3);
		Slot slot4 = new Slot((short)4, index4);
		Slot slot7 = new Slot((short)7, index7);
		
		qf.setSlot(1, slot1);
		qf.setSlot(2, slot2);
		qf.setSlot(3, slot3);
		qf.setSlot(4, slot4);
		qf.setSlot(7, slot7);
	}
}
