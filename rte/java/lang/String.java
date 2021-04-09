package java.lang;
import rte.*;
import java.lang.*;

public class String
{
	private char[] value;
	private int count;


	public String() {}


	public String(char[] chars) {
		count = chars.length;
		value = chars;
	}


	public boolean equals(String s) {
		if(count != s.count) {
			return false;
		}
		for(int i = 0; i < count; i++) {
			if(value[i] != s.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	@SJC.Inline
	public int length()
	{
		return count;
	}

	@SJC.Inline
	public char charAt(int i)
	{
		return value[i];
	}
}