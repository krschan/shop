package dao.jaxb;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;

public class JaxbMarshaller {
	
	public void init () {
		try {
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = dateTime.format(myFormatObj);
			
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller();
			System.out.println("marshalling... ");
			ProductList products = createXml();
			marshaller.marshal(products, new File("jaxb/inventory_" + formattedDate + ".xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private ProductList createXml() {
	    ArrayList<Product> products = new ArrayList<>();

	    ProductList productList = new ProductList(products);
	    productList.setTotal(products.size());
	    
	    // print products
	    for (Product p : products) {
	        System.out.println(p);
	    }

	    return productList;
	}

}
