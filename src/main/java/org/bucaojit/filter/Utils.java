package org.bucaojit.filter;

public class Utils {
	
	public static short getQuotient(Object obj) {
		Integer hashcode = obj.hashCode();
		hashcode = hashcode >> 16;
		hashcode = Math.abs(hashcode);
		return hashcode.shortValue();
	}
	
	public static short getRemainder(Object obj) {
		Integer hashcode = obj.hashCode();
		return hashcode.shortValue();
	}
	
	public static int getIndex(Object obj, int size) {
		return getQuotient(obj)%size;
	}

}
