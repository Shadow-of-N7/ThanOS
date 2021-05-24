package rte;
import java.lang.*;

/**
 * Class with additional instance variables
 */
public class SMthdBlock
{
    // Simple name, fully qualified parameter types
    public String namePar;
    // Next method of the current class
    public SMthdBlock nextMthd;
    // Modifier of the method
    public int modifier;
    // Optional line mapping to code bytes
    public int[] lineInCodeOffset;
    // Probably the containing class
    public SClassDesc owner;
}
