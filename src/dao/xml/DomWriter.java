package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Product;

public class DomWriter {
	private Document document;

	public DomWriter() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR generating document");
		}
	}

	public boolean generateDocument(ArrayList<Product> inventory) {

		// PARENT NODE
		// root node
		Element products = document.createElement("products");
		products.setAttribute("total", Integer.toString(Product.getTotalProducts()));
		document.appendChild(products);

		for (Product product : inventory) {
			// CHILD NODES
			// child node into root node "products"
			Element item = document.createElement("product");
			item.setAttribute("id", Integer.toString(product.getId()));
			products.appendChild(item);

			// FINAL NODES
			// child name into product with content
			Element name = document.createElement("name");
			name.setTextContent(product.getName());
			item.appendChild(name);

			// child price into product with attribute and content
			Element price = document.createElement("price");
			price.setAttribute("currency", "€");
			price.setTextContent(Double.toString(product.getWholesalerPrice()));
			item.appendChild(price);

			// child stock into product with content
			Element stock = document.createElement("stock");
			stock.setTextContent(Integer.toString(product.getStock()));
			item.appendChild(stock);
		}

		return generateXml();

	}

	private boolean generateXml() {
		try {
			// Create date & time object
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = dateTime.format(myFormatObj);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			Source source = new DOMSource(document);
			File file = new File("xml/inventory_" + formattedDate + ".xml");
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			Result result = new StreamResult(pw);

			transformer.transform(source, result);
			return true;
		} catch (IOException e) {
			System.out.println("Error when creating writter file");
			return false;
		} catch (TransformerException e) {
			System.out.println("Error transforming the document");
			return false;
		}
	}
}
