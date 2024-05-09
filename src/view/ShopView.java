package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class ShopView extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton showCashButton;
	private JButton addProductButton;
	private JButton addStockButton;
	private JButton deleteProductButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShopView frame = new ShopView();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShopView() {
		
		menuUI();
		
	}
	
	public void menuUI() {
		Shop shop = new Shop();
		// Load inventory
		shop.loadInventory();
		
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 0, 0, 0));
		
		JLabel optionLabel = new JLabel("seleccione una opción:");
		contentPane.add(optionLabel);
		
		showCashButton = new JButton("mostrar caja");
		contentPane.add(showCashButton);
		
		addProductButton = new JButton("añadir producto");
		contentPane.add(addProductButton);
		
		addStockButton = new JButton("añadir stock");
		contentPane.add(addStockButton);
		
		deleteProductButton = new JButton("eliminar producto");
		contentPane.add(deleteProductButton);
		
		showCashButton.addActionListener(this);
		addProductButton.addActionListener(this);
		addStockButton.addActionListener(this);
		deleteProductButton.addActionListener(this);
		
		// detect when user uses a key on the frame.
		this.addKeyListener(this);
	}
	
	public void openCashView() {
		CashView cashView = new CashView();
		cashView.setVisible(true);
	
	}
	
	public void openProductView(int option) {
		Shop shop = new Shop();
		ProductView productView = new ProductView(option, shop);
		productView.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		int key = e.getKeyCode();
		
		switch (key) {
		
		case KeyEvent.VK_1:
			openCashView();
			break;
		case KeyEvent.VK_2:
			openProductView(2);
			break;
		case KeyEvent.VK_3:
			openProductView(3);
			break;
		case KeyEvent.VK_9:
			openProductView(9);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent interactionButtons) {
		if (interactionButtons.getSource() == showCashButton) {
			openCashView();
		} else if (interactionButtons.getSource() == addProductButton) {
			openProductView(2);
		} else if (interactionButtons.getSource() == addStockButton) {
			openProductView(3);
		} else if (interactionButtons.getSource() == deleteProductButton) {
			openProductView(9);
		}
		
	}

}
