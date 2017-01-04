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

import java.util.BitSet;

/*
is_occupied
  is_continuation
    is_shifted
0 0 0 : Empty Slot
0 0 1 : Slot is holding start of run that has been shifted from its canonical slot.
0 1 0 : not used.
0 1 1 : Slot is holding continuation of run that has been shifted from its canonical slot.
1 0 0 : Slot is holding start of run that is in its canonical slot.
1 0 1 : Slot is holding start of run that has been shifted from its canonical slot.
        Also the run for which this is the canonical slot exists but is shifted right.
1 1 0 : not used.
1 1 1 : Slot is holding continuation of run that has been shifted from its canonical slot.
        Also the run for which this is the canonical slot exists but is shifted right.
 */

public class Metadata {
	private final int OCCUPIED_BIT = 0;
	private final int CONTINUATION_BIT = 1;
	private final int SHIFTED_BIT = 2;
	
	private BitSet metadata;
	
	public Metadata() {
		this.metadata = new BitSet(3);	
		this.metadata.clear();
	}
	
	public Metadata(BitSet metadata) {
		this.metadata = new BitSet(3);
		this.metadata.and(metadata);
	}
	
	public Metadata(Metadata metadata) {
		this.metadata = new BitSet(3);
		this.metadata.and(metadata.getMetadataSet());
	}
	
	protected BitSet getMetadataSet() {
		return this.metadata;
	}
	
	public void setMetadata(BitSet metadata) {
		this.metadata.and(metadata);
	}
	
	public Boolean getOccupied() {
		return metadata.get(OCCUPIED_BIT);
	}
	
	public Boolean getShifted() {
		return metadata.get(SHIFTED_BIT);
	}
	
	public Boolean getContinuation() {
		return metadata.get(CONTINUATION_BIT);
	}
	
	public void setOccupied() {
		metadata.set(OCCUPIED_BIT);
	}
	
	public void setContinuation() {
		metadata.set(CONTINUATION_BIT);
	}
	
	public void setShifted() {
		metadata.set(SHIFTED_BIT);
	}
	
	public void clearOccupied() {
		metadata.clear(OCCUPIED_BIT);
	}
	
	public void clearContinuation() {
		metadata.clear(CONTINUATION_BIT);
	}
	
	public void clearShifted() {
		metadata.clear(SHIFTED_BIT);
	}
	
	public Boolean isClear() {
		//if (this.metadata.and(set))
		if(this.metadata.isEmpty()) 
			return true;
		return false;
	}
}
