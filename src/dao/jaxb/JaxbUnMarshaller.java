package dao.jaxb;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Product;
import model.ProductList;

public class JaxbUnMarshaller {

	public ProductList init() {
		// Read from XML to java object.
		ProductList products = null;
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			System.out.println("Unmarshalling...");
			products = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml"));

			// Calculate the public price.
			for (Product product : products.getProducts()) {
				product.publicPriceCalculate();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
		return products;
	}

}
