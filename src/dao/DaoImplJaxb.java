package dao;

import java.util.ArrayList;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJaxb implements Dao {

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int user, String pw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {
		ProductList productList = (new JaxbUnMarshaller()).init();

		// Check if the product list is not null.
		if (productList != null) {
			// Return the list of products if available.
			return productList.getProducts();
		} else {
			// Return an empty ArrayList if the list is null.
			return new ArrayList<>();
		}
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		JaxbMarshaller jaxbMarshaller = new JaxbMarshaller();
		ProductList productList = new ProductList();
		
		productList.setProducts(inventory);
		boolean success = jaxbMarshaller.init(productList);
		
		return success;
	}

}
