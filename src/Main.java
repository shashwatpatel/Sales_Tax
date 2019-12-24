
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
Receipt receipt1 = new Receipt("Input1.txt");	
		
		receipt1.Totals();
	
		System.out.println("Output 1");
		receipt1.printReceipt();
		System.out.println();

		Receipt receipt2 = new Receipt("Input2.txt");

		receipt2.Totals();
		
		System.out.println("Output 2");
		receipt2.printReceipt();
		System.out.println();
		
		Receipt receipt3 = new Receipt("Input3.txt");
		
		receipt3.Totals();
		
		System.out.println("Output 3");
		receipt3.printReceipt();
		

	}

}
