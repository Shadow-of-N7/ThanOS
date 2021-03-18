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