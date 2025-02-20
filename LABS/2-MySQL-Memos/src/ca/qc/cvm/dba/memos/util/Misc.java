package ca.qc.cvm.dba.memos.util;

public class Misc {
		
	public static boolean isNumeric(String value) {
		boolean numeric = false;
		
		try {
			int tmp = Integer.parseInt(value);
			numeric = true;
		}
		catch (NumberFormatException e) {}
		
		return numeric;
	}
}