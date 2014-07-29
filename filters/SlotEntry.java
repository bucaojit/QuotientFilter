package filters;
// Author: Oliver

public class SlotEntry {
	private short remainder;
	private Metadata metadata;
	
	public SlotEntry(short remainder) {
		this.remainder = remainder;	
		this.metadata = new Metadata();
	}
	
	public SlotEntry(short remainder, Metadata metadata) {
		this.remainder = remainder;
		this.metadata = metadata;
	}
	
	public short getRemainder() {
		return remainder;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
}
