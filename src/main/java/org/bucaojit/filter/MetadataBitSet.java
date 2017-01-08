package org.bucaojit.filter;

import java.util.BitSet;

public class MetadataBitSet implements Metadata{
	private final int OCCUPIED_BIT = 0;
	private final int CONTINUATION_BIT = 1;
	private final int SHIFTED_BIT = 2;
	
	private BitSet metadata;
	
	public MetadataBitSet() {
		this.metadata = new BitSet(3);	
		this.metadata.clear();
	}
	
	public MetadataBitSet(BitSet metadata) {
		this.metadata = new BitSet(3);
		this.metadata.and(metadata);
	}
	
	public MetadataBitSet(MetadataBitSet metadata) {
		this.metadata = new BitSet(3);
		this.metadata.and(metadata.getMetadataSet());
	}
	
	public BitSet getMetadataSet() {
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
