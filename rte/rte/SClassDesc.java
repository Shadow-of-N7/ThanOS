package rte;
import rte.*;
import java.lang.*;

public class SClassDesc
{
	public SClassDesc parent;
	public SIntfMap implementations;
	public SClassDesc nextUnit; //nächste Unit des aktuellen Packages
	public String name; //einfacher Name der Unit
	public SPackage pack; //besitzendes Package, noClassPack deaktiviert*
	public SMthdBlock mthds; //erste Methode der Unit
	public int modifier; //Modifier der Unit, noClassMod deaktiviert*
}
