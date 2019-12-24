import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
import java.text.DecimalFormat;

public class Receipt {
	private ArrayList<Product> productsList = new ArrayList<Product>();
	private double total, taxT;
	
	@SuppressWarnings("resource")
	public Receipt(String inputFileName){
		// This method accepts reads an input text file, Each line is read and differentiated into products	 
		try {

            Scanner input = new Scanner(System.in);
            File file = new File(inputFileName);
            input = new Scanner(file); 
            while (input.hasNextLine()) {
            	//Take the line and divide it into tokens
            	String line = input.nextLine(); 
            	String[] words = line.split(" "); 
            	int qty = Integer.parseInt(words[0]);
            	//Check if the item is imported or in the exempted list
            	boolean isImported = line.contains("imported"); 
            	String[] exemptedItems =  new String[]{"book","chocolate","pills"}; 
            	int exemptedItemIndex = itemPresent(line,exemptedItems); //Find the type
            	String exemptedType = null;
            	
            	if(exemptedItemIndex != -1){
            		//the item is tax exempted            		
            		//the exempted word is contained at exempted item index
                	exemptedType = exemptedItems[exemptedItemIndex];      			
            	}

            	int splitIndex = line.lastIndexOf("at");
            	if(splitIndex == -1){
            		System.out.println("Formatting error");
            	} else {
            		float price = Float.parseFloat((line.substring(splitIndex + 2))); //the price will be the token after the substring "at"
                    String name = line.substring(1, splitIndex); 
                	for(int i = 0;i<qty;++i){
                    	//loop for the total quantity of the item to make that many in the list
                    	Product newProduct = null;
                    	if(isImported){
                        	if(exemptedType != null){
                        		//the product is not imported and is exempt of sales tax
                        		if(exemptedType == "book"){ newProduct = new Product(name,price,ProductType.IMPORTED_BOOK); }
                        		else if(exemptedType == "pills"){ newProduct = new Product(name,price,ProductType.IMPORTED_MEDICAL); } 
                        		else if(exemptedType == "chocolate"){ newProduct = new Product(name,price,ProductType.IMPORTED_FOOD); }
                        	} 
                        	//The product will be imported and sales tax will be added
                        	else { newProduct = new Product(name,price,ProductType.IMPORTED_OTHERS); }                     	
                    	} else {                    	
                        	if(exemptedType != null){
                        		//Product does not get sales tax                        		
                        		if(exemptedType == "book"){
                        			newProduct = new Product(name,price,ProductType.BOOK);
                        		} else if(exemptedType == "pills"){
                        			newProduct = new Product(name,price,ProductType.MEDICAL_PRODUCTS);
                        		} else if(exemptedType == "chocolate"){
                        			newProduct = new Product(name,price,ProductType.FOOD);
                        		}
                        	} else {
                        		//the product is domestic and is sales taxed
                        		newProduct = new Product(name,price,ProductType.OTHERS);
                        	}
                    	}
                    	
                        productsList.add(newProduct); //add the product to our receipt's list
                    }
            	}
            	
            }
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public void Totals(){
		//Calculate totals for each item
		int numItems = productsList.size();
		
		BigDecimal sum = new BigDecimal("0");
		BigDecimal taxSum = new BigDecimal("0");
		
		for(int i = 0;i<numItems;++i){
			
			taxSum = BigDecimal.valueOf(0);
			BigDecimal totalBeforeTax = new BigDecimal(String.valueOf(this.productsList.get(i).getPrice()));
			sum = sum.add(totalBeforeTax);
			if(productsList.get(i).isSalesTaxable()){
				//Charge 10% tax and round to the nearest 0.05			
			    BigDecimal salesTaxPercent = new BigDecimal(".10");
			    BigDecimal salesTax = salesTaxPercent.multiply(totalBeforeTax);			    
			    salesTax = round(salesTax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    taxSum = taxSum.add(salesTax);
			} 
			
			if(productsList.get(i).isImportedTaxable()){
				//Charge 5% tax and round to the nearest 0.05
			    BigDecimal importTaxPercent = new BigDecimal(".05");
			    BigDecimal importTax = importTaxPercent.multiply(totalBeforeTax);
			    importTax = round(importTax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    taxSum = taxSum.add(importTax);			   
			}

			productsList.get(i).setPrice(taxSum.floatValue() + productsList.get(i).getPrice());		
			taxT += taxSum.doubleValue();			
			sum = sum.add(taxSum);
		}
			//save out sales tax, and total
			taxT = roundDecimals(taxT);
			total = sum.doubleValue();
	}
	
	public void setTotal(BigDecimal amount){ total = amount.doubleValue(); }
	
	public double getTotal(){ return total; }
	public void setSalestaxT(BigDecimal amount){ taxT = amount.doubleValue(); }
	public double getSalestaxT(){ return taxT; }
	
	public static int itemPresent(String inputString, String[] items) {
		// This method finds items in the string and returns them if not found then -1 is returned
		int index = -1;		
		for(int i = 0;i<items.length;++i){			
			index = inputString.indexOf(items[i]);
			if(index != -1)
				return i;				
		}
		return -1;		
	}
	
	public static BigDecimal round(BigDecimal val, BigDecimal inc,RoundingMode roundingMode) {
		// This method handles custom rounding to 0.05, and also sets the BigDecimal numbers to use 2 decimals
		if (inc.signum() == 0) {
		// 0 increment does not make much sense, but prevent division by 0
		return val;
		} else {
			BigDecimal divided = val.divide(inc, 0, roundingMode);
			BigDecimal result = divided.multiply(inc);
			result.setScale(2, RoundingMode.UNNECESSARY);
			return result;
		}
	}
	
	public double roundDecimals(double dec) {
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    return Double.valueOf(twoDForm.format(dec));
	}
	
	public void printReceipt(){
		// Print receipt details 
		int numItems = productsList.size();
		for(int i = 0;i < numItems; ++i){
			System.out.println("1" + productsList.get(i).getName() + "at " + productsList.get(i).getPrice());
		}
		System.out.printf("Sales Tax: %.2f\n", taxT);
		System.out.println("Total: " + total);
	}
	
}
