/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bucaojit.filter;

// Author: Oliver

// General purpose quotient filter, takes in java Objects as an entry 
// Approximate Membership Query (AMQ)

import java.io.IOException;
import java.util.ArrayList;

public class QuotientFilter {
	private final int DEFAULT_SIZE = 1000;
    private int qfSize;
	protected ArrayList<Slot> set;
	protected int size;
	
	public QuotientFilter() {
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
	}

    public int getSize() {
        return this.qfSize;
    }
	
	public void setSlot(int index, Slot slot) {
		this.set.set(index, slot);
	}
	
	protected ArrayList<Slot> getSet() {
		return set;
	}
	
	protected static short getQuotient(Object obj) {
		Integer hashcode = obj.hashCode();
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
			currentSlot.setRemainder(QuotientFilter.getRemainder(obj));		 
			// TODO: depends on the current Slot's metadata
            Metadata md = new Metadata();
            md.setOccupied();
			currentSlot.setMetadata(md);
		}
		else { 
			int foundIndex;
            foundIndex = lookup(index, remainder);
			if(foundIndex != -1) { 
				throw new Exception("Unable to insert, object already exists");
			}
			else {
                insertAndShift(remainder, foundIndex);
			}
		}
	}

    public void insertAndShift(short remainder, int index) throws IOException {
        Metadata md = new Metadata();
        md.setOccupied();
        Slot newSlot = new Slot(remainder, md);
        set.add(index, newSlot);

        /*
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
        */
    }

    public void deleteAndShift(int index) throws IOException {
        set.remove(index);

        /*
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
        */

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
