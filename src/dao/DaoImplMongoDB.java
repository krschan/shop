package dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
	
	MongoCollection<Document> usersCollection;
	MongoCollection<Document> inventoryCollection;
	ObjectId id;

	@Override
	public void connect() {
		try {
			String uri = "mongodb+srv://krischxn:qMZROEL6ak5TFuXn@cluster0.cezwmpv.mongodb.net/";
			MongoClientURI mongoClientURI = new MongoClientURI(uri);
			MongoClient mongoClient = new MongoClient(mongoClientURI);
			
			MongoDatabase mongoDatabase = mongoClient.getDatabase("shop");
			usersCollection = mongoDatabase.getCollection("users");	
			return;
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int user, String pw) {
		Employee employee = null;
	    try {
	        // Buscar el documento en la colección de usuarios con el ID y la contraseña proporcionados
	        Document query = new Document("user", user).append("password", pw);
	        Document found = usersCollection.find(query).first();

	        if (found != null) {
	            // Extraer los datos del documento y crear un objeto Employee
	            int id = found.getInteger("user");
	            String name = found.getString("name");
	            String password = found.getString("password");

	            return new Employee(name, password);
	        }
	    } catch (MongoException e) {
	        e.printStackTrace();
	    }
	    return null; // Retorna null si no encuentra un usuario o hay un error
	}


	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		
		try {
			
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

}
