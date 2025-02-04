package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {

    public DaoImplHibernate() {
        connect();
    }

    private Session session;
    private Transaction tx;
    private Connection connection;

    @Override
    public void connect() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            org.hibernate.SessionFactory sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee getEmployee(int user, String pw) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            tx = session.beginTransaction();

            // We create a manual query. Remember that "*" does not exist.
            Query<Product> q = session.createQuery("select p from Product p", Product.class);

            // We get a List of Products.
            List<Product> productsList = q.list();

            // We add these products to our ArrayList.
            products.addAll(productsList);

            for (Product p : products) {
                System.out.println(p);
            }

            tx.commit();
            System.out.println("Get inventory successfully.");

        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback(); // Roll back if any exception occurs.
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        tx = null;
        try {
            tx = session.beginTransaction();

            // For each product in the inventory, we create a history record.
            for (Product product : inventory) {
                ProductHistory history = new ProductHistory();

                // We copy the product data to the history.
                history.setName(product.getName());
                history.setAvailable(product.isAvailable());
                history.setPrice(product.getPrice());
                history.setStock(product.getStock());

                // We establish the relationship with the product.
                history.setProduct(product);
                product.addHistory(history);

                // We save the history record.
                session.save(history);
            }

            tx.commit();
            System.out.println("Inventory history saved successfully.");
            return true;

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateProduct(Product product) {
        tx = null;
        try {
            tx = session.beginTransaction();

            Product existingProduct = session.get(Product.class, product.getId());

            if (existingProduct != null) {
                existingProduct.setStock(product.getStock());

                session.update(existingProduct);
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }

            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void addProduct(Product product) {
        tx = null;
        try {
            tx = session.beginTransaction();

            session.save(product);

            tx.commit();
            System.out.println("Product added successfully.");

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        tx = null;
        try {
            tx = session.beginTransaction();
            Product existingProduct = session.get(Product.class, product.getId());

            if (existingProduct != null) {
                session.delete(existingProduct);
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found.");
            }

            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
