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
	public char[] toCharArray() {
		return value;
	}


	public static String concat(String s1, String s2) {
		int finalLength = s1.length() + s2.length();
		char[] newChars = new char[finalLength];
		// Copy all chars of s1
		for(int i = 0; i < s1.length(); i++) {
			newChars[i] = s1.charAt(i);
		}
		// Copy all chars of s2
		for(int i = 0; i < s2.length(); i++) {
			newChars[i + s1.length()] = s2.charAt(i);
		}
		return new String(newChars);
	}


	public static String concat(String s1, char c) {
		int finalLength = s1.length() + 1;
		char[] newChars = new char[finalLength];
		for(int i = 0; i < s1.length(); i++) {
			newChars[i] = s1.charAt(i);
		}
		newChars[finalLength - 1] = c;
		return new String(newChars);
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