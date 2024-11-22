package dao.jaxb;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import model.ProductList;

public class JaxbMarshaller {

    // Method to marshal the ProductList to XML
    public boolean init(ProductList inventory) {
        try {
        	// Get the current date in the required format
            String formattedDate = getCurrentDate();

            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Marshaller marshaller = context.createMarshaller();
            System.out.println("Marshalling... ");

            // Update inventory with total products and marshal it to XML
            inventory = createXml(inventory);
            marshaller.marshal(inventory, new File("jaxb/inventory_" + formattedDate + ".xml"));

            // Return true if marshaling is successful
            return true;
        } catch (JAXBException e) {
        	// Return false if an error occurs
            e.printStackTrace();
            return false;
        }
    }

    // Method to create XML by setting the total number of products
    private ProductList createXml(ProductList inventory) {
    	// Set total products in the inventory
    	inventory.setTotal(inventory.getProducts().size());
        return inventory;
    }

    // Method to get the current date in "yyyy-MM-dd" format
    private String getCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(myFormatObj);
    }
}
