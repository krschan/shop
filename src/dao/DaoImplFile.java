package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import main.Shop;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao {

	@Override
	public void connect() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int user, String pw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {
		
		ArrayList<Product> inventory = new ArrayList<Product>();
		
		try {
			
			File file = new File("./files/inputInventory.txt");

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String initialInventory = bufferedReader.readLine();

			while (initialInventory != null) {

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

				inventory.add(new Product(name, wholesalerPrice, publicPrice, true, stock));

				initialInventory = bufferedReader.readLine();

			}
			bufferedReader.close();
			return inventory;

		} catch (java.io.IOException e) {
			System.out.println("Ha habido un problema con el fichero.");
			return null;
		}
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		Scanner sc = new Scanner(System.in);

			try {
				// Create date & time object
				LocalDateTime dateTime = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-mm-dd");
				String formattedDate = dateTime.format(myFormatObj);

				// Export data to a file
				File file = new File("files/inventory_" + formattedDate + ".txt");
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file, true);
				BufferedWriter writer = new BufferedWriter(fileWriter);

				int saleNumber = 1;

				for (Product product : inventory) {
					writer.write(
							saleNumber + ";Product:" + product.getName() + ";" + "Stock:" + product.getStock() + ";\n");

					saleNumber++;
				}

				System.out.println("Las ventas han sido creadas en un fichero.");
				writer.close();
				return true;

			} catch (java.io.IOException e) {
				System.out.println("Ha habido un problema con el fichero.");
				return false;
			}

	}

}
