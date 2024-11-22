package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import dao.xml.DomWriter;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao{

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
		ArrayList<Product> inventory = new ArrayList<Product>();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			File file = new File ("./xml/inputInventory.xml");
			dao.xml.SaxReader saxReader = new dao.xml.SaxReader();
			parser.parse(file, saxReader);
			inventory = saxReader.getProducts();
			
		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("ERROR creating the parser");
			return null;
		} catch (IOException e) {
			System.out.println("ERROR file not found");
			return null;
		}
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		//Create a new xml document
	    DomWriter domWriter = new DomWriter();
	    boolean success = domWriter.generateDocument(inventory);
	    return success;
	}

}
