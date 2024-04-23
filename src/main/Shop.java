package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Shop {
	private Amount cash = new Amount(100.00);
	private ArrayList<Product> inventory = new ArrayList<>();
	private int numberProducts;
	private ArrayList<Sale> sales = new ArrayList<>();
	private int customerSale;

	final static double TAX_RATE = 1.04;

	public static void main(String[] args) {
		Shop shop = new Shop();
		
		// Load inventory
		shop.loadInventory();
		
		// Login employee
		shop.initSession();

		Scanner sc = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver venta total");
			System.out.println("9) Eliminar producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = sc.nextInt();

			switch (opcion) {
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
		Scanner sc = new Scanner(System.in);
		Employee employee = new Employee();
		boolean login = false;
		
		while (!login) {
			System.out.print("Introduzca numero de empleado: ");
			int employeeNumber = sc.nextInt();
			System.out.print("Introduzca contraseña: ");
			String password = sc.next();
			
			login = employee.login(employeeNumber, password);
			
			if (!login) {
				System.out.println("Los datos son incorrectos. Reintentalo.");
			} else {
				System.out.println("Los datos introducidos son correctos!");
			}
		}
	
	}

	// Load initial inventory to shop
	public void loadInventory() {
		try {
			File file = new File("./files/inputInventory.txt");
			
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String initialInventory = bufferedReader.readLine();
			
			while(initialInventory != null) {
				
				String[] line = initialInventory.split(";");
				
				// Product name
				String split0 = line[0];
				String[] productSplit = split0.split(":");
				String name = productSplit[1];
				
				// Wholesaler price
				String split1 = line[1];
				String[] wholesalerSplit = split1.split(":");
				float wholesalerPrice = Float.parseFloat(wholesalerSplit[1]);
				
				// Public price
				float publicPrice = wholesalerPrice * 2;
				
				// Stock
				String split2 = line[2];
				String[] stockSplit = split2.split(":");
				int stock = Integer.parseInt(stockSplit[1]);
				
				addProduct(new Product(name, wholesalerPrice, publicPrice, true, stock));
				
				initialInventory = bufferedReader.readLine();
				
			}
			bufferedReader.close();
			
		} catch (java.io.IOException e){
			System.out.println("Ha habido un problema con el fichero.");
		}
	}

	// (CASE 1) show current total cash
	public void showCash() {
		System.out.println("Dinero actual: " + cash);
	}

	// (CASE 2) add a new product to inventory getting data from console
	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos");
			return;
		}
		Scanner sc = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = sc.nextLine();
		System.out.print("Precio mayorista: ");
		double wholesalerPrice = sc.nextDouble();
		System.out.print("Stock: ");
		int stock = sc.nextInt();

		// Public Price
		double publicPrice = wholesalerPrice * 2;

		addProduct(new Product(name, wholesalerPrice, publicPrice, true, stock));

	}

	// (CASE 3) add stock for a specific product
	public void addStock() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = sc.next();
		Product product = findProduct(name);

		if (product != null) {
			// ask for stock
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = sc.nextInt();
			// update stock product
			product.setStock(stock + product.getStock());
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	// (CASE 4) set a product as expired
	public void setExpired() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = sc.next();

		Product product = findProduct(name);

		if (product != null) {
			double newPublicPrice = 0.6 * product.getPublicPrice(); // 40% of price goes down because the expiration is
																	// soon
			product.setPublicPrice(newPublicPrice);
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getPublicPrice());

		}

	}

	// (CASE 5) show all inventory
	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");

		for (Product product : inventory) {
			if (product != null) {
				System.out.println(product.getName() + ", " + product.getWholesalerPrice() + ", Precio público: "
						+ product.getPublicPrice() + ", " + product.isAvailable() + ", " + product.getStock());
			}

		}

	}

	// (CASE 6) make a sale of products to a client
	public void sale() {
		
		// create date & time object
		LocalDateTime dateTime = LocalDateTime.now();
		
		// temporalArray to save products
		ArrayList<Product> products = new ArrayList<>();

		// ask for client name	
		Scanner sc = new Scanner(System.in);
		System.out.println("Realizar venta, escribir nombre cliente");
		String clientName = sc.nextLine();
		
		// create object client
		Client clientBill = new Client(clientName);

		// sale product until input name is not 0
		double total = 0.0;
		String name = "";
		while (!name.equals("0")) {
			System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}
			Product product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				products.add(product);

				productAvailable = true;
				total += product.getPublicPrice();
				product.setStock(product.getStock() - 1);
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				System.out.println("Producto añadido con éxito");
			}

			if (!productAvailable) {
				System.out.println("Producto no encontrado o sin stock");
			}
		}

		// show cost total
		total = total * TAX_RATE;
		Amount totalAmount = new Amount(total);
		double totalCash = cash.getValue();
		cash.setValue(total + totalCash);
		System.out.println("Venta realizada con éxito, total: " + totalAmount);

		// add Sales to log sales
		sales.add(new Sale(clientName, products, total, dateTime));
		
		clientBill.pay(totalAmount);
		
	}

	// (CASE 7) show all sales
	public void showSales() {
		
		boolean salesItems = false;

		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale.toString());
				salesItems = true;
			}
		}
		
		if (!salesItems) {
			System.out.println("No hay ninguna venta.");
		} else {
			Scanner sc = new Scanner (System.in);
			
			System.out.println("Quieres escribir todas las ventas en un fichero?");
			String choice = sc.next();
			
			
			switch (choice) {
			
			case "si":
				
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
                        writer.write(saleNumber + ";" + sale.getClient() + ";" +
                                "Date=" + sale.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ";\n");

                        StringBuilder products = new StringBuilder();
                        for (Product product : sale.getProducts()) {
                            products.append(product.getName()).append(",").append(product.getPublicPrice()).append("€;");
                        }
                        writer.write(saleNumber + ";Products=" + products + "\n");

                        writer.write(saleNumber + ";Amount=" + sale.getAmount() + ";\n");

                        saleNumber++;
                    }
					
					System.out.println("Las ventas han sido creadas en un fichero.");
					writer.close();
					break;
					
				} catch (java.io.IOException e) {
					System.out.println("Ha habido un problema con el fichero.");
				}
			
			case "no":
				System.out.println("Las ventas no han sido creadas en un fichero.");
				break;
				
			default:
				System.out.println("Respuesta incorrecta.");
				break;
			}
		}	
	}

	// (CASE 8) Total sales
	public void showTotalSale() {
		double totalSale = 0;
		for (Sale sale : sales) {
			if (sale != null) {
				totalSale += sale.getAmount();
			}
		}
		System.out.println("La venta total es: " + totalSale);
	}

	// (CASE 9) delete product
	public void deleteProduct() {
		Scanner sc = new Scanner(System.in);

		System.out.println("¿Qué producto quieres eliminar?");
		String name = sc.next();
		Product product = findProduct(name);

		if (product != null) {
			inventory.remove(product);
			System.out.println("Producto eliminado: " + name);

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}

	}

	// add sales
	public void addSales(Sale customer) {
		if (isSalesFull()) {
			System.out.println("No se pueden añadir más ventas, se ha alcanzado el máximo de " + sales.size());
			return;
		}
		for (int i = 0; i < sales.size(); i++) {
			sales.add(customer);
		}
	}

	// check if inventory is full or not
	public boolean isSalesFull() {
		if (customerSale == 10) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
			return;
		}

		inventory.add(product);
	}

	// check if inventory is full or not
	public boolean isInventoryFull() {
		if (numberProducts == 10) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}

		return null;

	}

	// Exit program
	public void exit() {
		System.out.println("Hasta luego usuario...");
	}

}