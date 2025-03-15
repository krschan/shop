package dao;

import java.util.Date;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> usersCollection;
	private MongoCollection<Document> inventoryCollection;

	public DaoImplMongoDB() {
		connect();
	}

	@Override
	public void connect() {
		try {
			String uri = "mongodb+srv://krischxn:qMZROEL6ak5TFuXn@cluster0.cezwmpv.mongodb.net/";

			mongoClient = new MongoClient(new MongoClientURI(uri));
			mongoDatabase = mongoClient.getDatabase("shop");
			usersCollection = mongoDatabase.getCollection("users");
			inventoryCollection = mongoDatabase.getCollection("inventory");

			System.out.println("Connected in MongoDB");
			return;
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int user, String pw) {
		try {
			Document query = new Document("employeeId", user).append("password", pw);
			Document found = usersCollection.find(query).first();

			if (found != null) {
				System.out.println("User found: " + found.toJson());

				int id = found.getInteger("employeeId");
				String password = found.getString("password");

				return new Employee(id, password);
			} else {
				System.out.println("No user was found with those credentials.");
			}
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
			System.out.println("Disconnected from MongoDB");
		}
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();

		try {
			for (Document doc : inventoryCollection.find()) {

				int id = doc.getInteger("id");
				String name = doc.getString("name");
				Document wholesalerPriceDoc = (Document) doc.get("wholesalerPrice");
				double wholesalerValue = wholesalerPriceDoc.getDouble("value");
				String currency = wholesalerPriceDoc.getString("currency");
				boolean available = doc.getBoolean("available");
				int stock = doc.getInteger("stock");

				Amount wholesalerPrice = new Amount(wholesalerValue, currency);
				Amount publicPrice = new Amount(wholesalerValue * 2, currency);

				Product product = new Product(id, name, wholesalerValue, available, stock);
				product.setWholesalerPrice(wholesalerPrice);
				product.setPublicPrice(publicPrice);

				products.add(product);
			}
			System.out.println("Inventory loaded successfully. Products found:  " + products.size());

		} catch (MongoException e) {
			e.printStackTrace();
		}

		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		try {
			MongoCollection<Document> historicalInventory = mongoDatabase.getCollection("historical_inventory");

			ArrayList<Document> documents = new ArrayList<>();

			for (Product product : products) {
				Document doc = new Document("id_product", product.getId()).append("name", product.getName())
						.append("wholesalerPrice",
								new Document("value", product.getWholesalerPrice().getValue()).append("currency",
										product.getWholesalerPrice().getCurrency()))
						.append("available", product.isAvailable()).append("stock", product.getStock())
						.append("created_at", new Date());

				documents.add(doc);
			}

			historicalInventory.insertMany(documents);

			System.out.println("Inventory history written successfully. Total logged products: " + products.size());
			return true;

		} catch (MongoException e) {
			System.err.println("Error writing inventory history: " + e.getMessage());
			return false;
		}
	}

	@Override
	public void updateProduct(Product product) {
		try {
			Document query = new Document("id", product.getId());

			Document updatedDoc = new Document("$set",
					new Document("name", product.getName())
							.append("wholesalerPrice",
									new Document("value", product.getWholesalerPrice().getValue()).append("currency",
											product.getWholesalerPrice().getCurrency()))
							.append("available", product.isAvailable()).append("stock", product.getStock()));

			inventoryCollection.updateOne(query, updatedDoc);
			System.out.println("Product updated successfully: " + product.getName());
		} catch (MongoException e) {
			System.out.println("Error updating product: " + e.getMessage());
		}
	}

	@Override
	public void addProduct(Product product) {
		try {
			Document doc = new Document("id", product.getId()).append("name", product.getName())
					.append("wholesalerPrice",
							new Document("value", product.getWholesalerPrice().getValue()).append("currency",
									product.getWholesalerPrice().getCurrency()))
					.append("available", product.isAvailable()).append("stock", product.getStock());

			inventoryCollection.insertOne(doc);
			System.out.println("Product added successfully: " + product.getName());
		} catch (MongoException e) {
			System.out.println("Error adding product: " + e.getMessage());
		}
	}

	@Override
	public void deleteProduct(Product product) {
		try {
			Document query = new Document("id", product.getId());
			inventoryCollection.deleteOne(query);
			System.out.println("Product deleted successfully: " + product.getName());
		} catch (MongoException e) {
			System.out.println("Error deleting product: " + e.getMessage());
		}
	}

}
