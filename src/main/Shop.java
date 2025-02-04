package main;

import model.Product;
import model.Sale;
import view.LoginView;
import model.Amount;
import model.Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import dao.DaoImplHibernate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Shop {
	private Amount cash = new Amount(100.00);
	private ArrayList<Product> inventory = new ArrayList<>();
	private int numberProducts;
	private ArrayList<Sale> sales = new ArrayList<>();
	private int customerSale;

	final static double TAX_RATE = 1.04;

	// Connection using Hibernate.
	private Dao dao = new DaoImplHibernate();

	// Connection using JDBC.
	// private Dao dao = new DaoImplJDBC();

	// Connection using Jaxb.
	// private Dao dao = new DaoImplJaxb();

	// Connection using File.
	// private Dao dao = new DaoImplFile();

	// Connection using Xml.
	// private Dao dao = new DaoImplXml();

	public static void main(String[] args) {
		Shop shop = new Shop();

		// Load inventory
		// The method is located in DaoImplFile.

		// shop.loadInventory();
		// Commented out to test if inventory was loaded correctly.

		// Login employee
		shop.initSession();

		Scanner sc = new Scanner(System.in);
		int option = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Main Menu miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Check cash");
			System.out.println("2) Add product");
			System.out.println("3) Add stock");
			System.out.println("4) Mark product as near expiration");
			System.out.println("5) View inventory");
			System.out.println("6) Sale");
			System.out.println("7) View sales");
			System.out.println("8) View total sales");
			System.out.println("9) Delete product");
			System.out.println("10) Exit program");
			System.out.print("Select an option: ");
			option = sc.nextInt();

			switch (option) {
			case 1:
				shop.showCash();
				break;
			case 2:
				shop.addProduct();
				break;
			case 3:
				shop.addStock();
				break;
			case 4:
				shop.setExpired();
				break;
			case 5:
				shop.showInventory();
				break;
			case 6:
				shop.sale();
				break;
			case 7:
				shop.showSales();
				break;
			case 8:
				shop.showTotalSale();
				break;
			case 9:
				shop.deleteProduct();
				break;
			case 10:
				shop.exit();
				exit = true;
				break;
			}

		} while (!exit);
	}

	public void initSession() {
		try {
			LoginView loginView = new LoginView();
			loginView.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Load initial inventory into the shop
	public void loadInventory() {
		// Implement file reading for inventory instead of hardcoding.
		this.readInventory();
	}

	public void readInventory() {
		inventory = dao.getInventory();
	}

	// Write/Export inventory.
	public boolean writeInventory() {
		return dao.writeInventory(inventory);
	}

	// (CASE 1) Display current total cash
	public void showCash() {
		System.out.println("Current cash: " + cash);
	}

	// (CASE 2) Add a new product to inventory by collecting data from console
	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("Cannot add more products.");
			return;
		}
		Scanner sc = new Scanner(System.in);
		System.out.print("Name: ");
		String name = sc.nextLine();
		System.out.print("Wholesaler Price: ");
		double wholesalerPrice = sc.nextDouble();
		System.out.print("Stock: ");
		int stock = sc.nextInt();

		// Calculate Public Price
		double publicPrice = wholesalerPrice * 2;

		addProduct(new Product(true, name, wholesalerPrice, stock));
	}

	// (CASE 3) Add stock for a specific product
	public void addStock() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Select a product name: ");
		String name = sc.next();
		Product product = findProduct(name);

		if (product != null) {
			// Prompt for stock quantity
			System.out.print("Select the quantity to add: ");
			int stock = sc.nextInt();
			// Update product stock
			product.setStock(stock + product.getStock());

			System.out.println("The stock for product " + name + " has been updated to " + product.getStock());
		} else {
			System.out.println("No product found with the name " + name);
		}
	}

	// Method used to add stock in the database
	public void updateProduct(Product product) {
		dao.updateProduct(product);
	}

	// (CASE 4) Mark a product as expired
	public void setExpired() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Select a product name: ");
		String name = sc.next();

		Product product = findProduct(name);

		if (product != null) {
			double publicPriceExpired = 0.6 * product.getPublicPrice().getValue(); // Reduce price by 40% due to
																					// impending expiration
			product.setPublicPrice(new Amount(publicPriceExpired));
			System.out.println(
					"The public price for product " + name + " has been updated to " + product.getPublicPrice());
		}
	}

	// (CASE 5) Display all inventory
	public void showInventory() {
		System.out.println("Current store inventory:");

		for (Product product : inventory) {
			if (product != null) {
				System.out.println(product.getName() + ", Wholesaler Price: " + product.getWholesalerPrice()
						+ ", Public Price: " + product.getPublicPrice() + ", Available: " + product.isAvailable()
						+ ", Stock: " + product.getStock());
			}
		}
	}

	// (CASE 6) Process a sale of products to a client
	public void sale() {
		// Create date & time object
		LocalDateTime dateTime = LocalDateTime.now();

		// Temporary array to hold products
		ArrayList<Product> products = new ArrayList<>();

		// Prompt for client name
		Scanner sc = new Scanner(System.in);
		System.out.println("Process sale, enter client name:");
		String clientName = sc.nextLine();

		// Create client object
		Client clientBill = new Client(clientName);

		// Continue to add products until the user enters "0"
		double total = 0.0;
		String name = "";
		while (!name.equals("0")) {
			System.out.println("Enter the product name, type 0 to finish:");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}
			Product product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				products.add(product);
				productAvailable = true;
				total += product.getPublicPrice().getValue();
				product.setStock(product.getStock() - 1);
				// If no more stock, mark as unavailable for sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				System.out.println("Product added successfully.");
			}

			if (!productAvailable) {
				System.out.println("Product not found or out of stock.");
			}
		}

		// Calculate total cost with tax
		total = total * TAX_RATE;
		Amount totalAmount = new Amount(total);
		double totalCash = cash.getValue();
		cash.setValue(total + totalCash);
		System.out.println("Sale completed successfully, total: " + totalAmount);

		// Log the sale
		sales.add(new Sale(clientName, products, total, dateTime));

		clientBill.pay(totalAmount);
	}

	// (CASE 7) Display all sales
	public void showSales() {
		boolean salesItems = false;

		System.out.println("Sales list:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale.toString());
				salesItems = true;
			}
		}

		if (!salesItems) {
			System.out.println("No sales recorded.");
		} else {
			this.writeSales();
		}
	}

	public void writeSales() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Do you want to write all sales to a file?");
		String choice = sc.next();

		switch (choice) {
		case "yes":
			try {
				// Create date & time object
				LocalDateTime dateTime = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String formattedDate = dateTime.format(myFormatObj);

				// Export data to a file
				File file = new File("files/sales_" + formattedDate + ".txt");
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file, true);
				BufferedWriter writer = new BufferedWriter(fileWriter);

				int saleNumber = 1;

				for (Sale sale : sales) {
					writer.write(saleNumber + ";" + sale.getClient() + ";" + "Date="
							+ sale.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ";\n");

					StringBuilder products = new StringBuilder();
					for (Product product : sale.getProducts()) {
						products.append(product.getName()).append(",").append(product.getPublicPrice()).append("€;");
					}
					writer.write(saleNumber + ";Products=" + products + "\n");

					writer.write(saleNumber + ";Amount=" + sale.getAmount() + ";\n");

					saleNumber++;
				}

				System.out.println("Sales have been written to a file.");
				writer.close();
			} catch (java.io.IOException e) {
				System.out.println("There was a problem with the file.");
			}
			break;

		case "no":
			System.out.println("Sales have not been written to a file.");
			break;

		default:
			System.out.println("Invalid response.");
			break;
		}
	}

	// (CASE 8) Calculate total sales
	public void showTotalSale() {
		double totalSale = 0;
		for (Sale sale : sales) {
			if (sale != null) {
				totalSale += sale.getAmount();
			}
		}
		System.out.println("Total sales amount: " + totalSale);
	}

	// (CASE 9) Delete a product from inventory
	public void deleteProduct() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Which product do you want to delete?");
		String name = sc.next();
		Product product = findProduct(name);

		if (product != null) {
			inventory.remove(product);
			System.out.println("Product deleted: " + name);
		} else {
			System.out.println("No product found with the name " + name);
		}
	}

	// Method used to delete product in the database
	public void deleteProduct(Product product) {
		dao.deleteProduct(product);
	}

	// Add a sale to the log
	public void addSales(Sale customer) {
		if (isSalesFull()) {
			System.out.println("Cannot add more sales, maximum reached: " + sales.size());
			return;
		}
		for (int i = 0; i < sales.size(); i++) {
			sales.add(customer);
		}
	}

	// Check if sales limit is reached
	public boolean isSalesFull() {
		return customerSale == 10;
	}

	/**
	 * Add a product to inventory
	 * 
	 * @param product The product to be added
	 */
	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("Cannot add more products, maximum reached: " + inventory.size());
			return;
		}

		// Add in the array.
		inventory.add(product);

		// Add in the database.
		dao.addProduct(product);
	}

	// Check if inventory limit is reached
	public boolean isInventoryFull() {
		return numberProducts == 10;
	}

	/**
	 * Find a product by name
	 * 
	 * @param name The name of the product to search for
	 * @return The found product or null if not found
	 */
	public Product findProduct(String name) {
		for (Product product : inventory) {
			if (product != null && product.getName().equalsIgnoreCase(name)) {
				return product;
			}
		}
		return null;
	}

	// Exit program
	public void exit() {
		System.out.println("Hasta luego usuario...");
	}

	// Recover cash amount
	public String recoverCash() {
		return cash.toString();
	}

	// Save inventory
	public ArrayList<Product> getInventory() {
		return inventory;
	}
}
