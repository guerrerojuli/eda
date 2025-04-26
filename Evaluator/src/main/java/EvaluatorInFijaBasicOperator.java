/*

import java.util.HashMap;  
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class EvaluatorInFijaBasicOperator {
	
	// opcion 1

	 private static Map<String, Integer> mapping = new HashMap<String, Integer>()
	 {   { put("+", 0); put("-", 1); put("*", 2); put("/", 3); }  
	 };
	 
	private static boolean[][] precedenceMatriz= 
	{   { true,  true,  false, false }, 
		{ true,  true,  false, false }, 
		{ true,  true,  true,  true  }, 
		{ true,  true,  true,  true }, 
	};

	private boolean getPrecedence(String tope, String current)
	{
		Integer topeIndex;
		Integer currentIndex;
	
		if ((topeIndex= mapping.get(tope))== null)
			throw new RuntimeException(String.format("tope operator %s not found", tope));
		
		if ((currentIndex= mapping.get(current)) == null)
			throw new RuntimeException(String.format("current operator %s not found", current));

		return precedenceMatriz[topeIndex][currentIndex];
	}


	// opcion 2: asumo que _ no es parte de ningun operador posible

	private static Map<String, Boolean> precendeceMap= new HashMap<String, Boolean>() 
	{	{
			put("+_+", true); put("+_-", true); put("+_*", false); put("+_/", false);
			put("-_+", true); put("-_-", true); put("-_*", false); put("-_/", false);
			put("*_+", true); put("*_-", true); put("*_*", true); put("*_/", true);
			put("/_+", true); put("/_-", true); put("/_*", true); put("/_/", true);
			put("^_+", true); put("^_-", true); put("^_*", true); put("^_/", true);
		}  };
		
	private final static String extraSymbol= "_";

	private boolean getPrecedence2(String tope, String current)
	{
		Boolean rta= precendeceMap.get(String.format("%s%s%s", tope, extraSymbol, current));
		if (rta == null)
			throw new RuntimeException(String.format("operator %s or %s not found", tope, current));
		
		return rta;
	}


	
	private Scanner scannerLine;
	
	public EvaluatorInFijaBasicOperator()  {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		System.out.print("Introduzca la expresión en notación infija: ");
		String line= input.nextLine();
		input.close();
		
		scannerLine = new Scanner(line).useDelimiter("\\s+");
	}


	
	public Double evaluate()
	{
		String exp = infijaToPostfija();
		scannerLine = new Scanner(exp).useDelimiter("\\s+");

		// bla bla bla
		// lo que tenian de antes...
	}
	
	private String infijaToPostfija()
	{
		String postfija= "";
		Stack<String> theStack= new Stack<String>();
		
		// bla bla bla
		// lo que analizamos hoy
		while( scannerLine.hasNext() )
		{

		}
		System.out.println("Postfija= " + postfija);
		return postfija;
	}		



	
	public static void main(String[] args) {

		EvaluatorInFijaBasicOperator e = new EvaluatorInFijaBasicOperator();
		System.out.println(e.evaluate());


	}
}

 */