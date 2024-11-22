package dao.jaxb;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import model.Product;
import model.ProductList;

public class JaxbUnMarshaller {

    // Method to initialize the ProductList from XML
    public ProductList init() {
        try {
            // Create JAXB context for ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("Unmarshalling...");

            // Unmarshal the XML file into a ProductList object
            ProductList products = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml"));
            initializeProducts(products); // Initialize product details
            return products;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null; // Return null if an error occurs
        }
    }

    // Method to initialize product details such as ID and availability
    private void initializeProducts(ProductList products) {
    	// Product ID counter
        int productIdCounter = 1;
        for (Product product : products.getProducts()) {
        	// Set product ID and increment counter
        	product.setId(productIdCounter++);
            
            // Set availability based on stock
            product.setAvailable(product.getStock() > 0);
            
            // Calculate public price
            product.publicPriceCalculate();
        }
    }
}
