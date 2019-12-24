enum ProductType{ BOOK(true,false), MEDICAL_PRODUCTS(true,false), FOOD(true,false), OTHERS (false , false),
	IMPORTED_BOOK(true,true), IMPORTED_MEDICAL(true,true), IMPORTED_FOOD(true,true), IMPORTED_OTHERS(false,true);
	
	private boolean isExempted, isImported;
	//private boolean isImported;
	
	private ProductType(boolean exempts , boolean imports){
		isExempted = exempts;
		isImported = imports;
	}

	public boolean isImported(){ return isImported; }
	public boolean isExempted(){ return isExempted;	}
}

public class Product {
	private String name;
	private float price;
	private ProductType type;
	
	public Product(String n, float p, ProductType ProductType){
		this.name = n;
		this.setPrice(p);
		this.type = ProductType;		
	}
	
	public String toString(){ return this.name + this.getPrice(); }

	public float getPrice() { return price; }

	public void setPrice(float price) { this.price = price; }
	
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }
	
	public boolean isSalesTaxable() { return !this.type.isExempted(); }

	public boolean isImportedTaxable() { return this.type.isImported(); }

}
