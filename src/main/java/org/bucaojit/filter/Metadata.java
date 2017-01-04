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

public interface Metadata<T> {
	
	public T getMetadataSet();
	
	public void setMetadata(T metadata);
	
	public Boolean getOccupied();
	
	public Boolean getShifted();
	
	public Boolean getContinuation();
	
	public void setOccupied() ;
	
	public void setContinuation() ;
	
	public void setShifted();
	
	public void clearOccupied();
	
	public void clearContinuation() ;
	
	public void clearShifted();
	
	public Boolean isClear();
}
