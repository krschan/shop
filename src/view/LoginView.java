package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exception.LimitLoginException;
import model.Employee;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class LoginView extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField userTextField;
	private JTextField passwordTextField;
	private JLabel employeeLabel;
	private JLabel passwordLabel;
	private JButton loginButton;
	private int counter = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
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
	public LoginView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 250, 400, 175);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{105, 45, 96, 85, 0};
		gbl_contentPanel.rowHeights = new int[]{21, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		employeeLabel = new JLabel("Employee");
		GridBagConstraints gbc_employeeLabel = new GridBagConstraints();
		gbc_employeeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_employeeLabel.gridx = 1;
		gbc_employeeLabel.gridy = 2;
		contentPanel.add(employeeLabel, gbc_employeeLabel);
		
		userTextField = new JTextField();
		GridBagConstraints gbc_userTextField = new GridBagConstraints();
		gbc_userTextField.anchor = GridBagConstraints.WEST;
		gbc_userTextField.insets = new Insets(0, 0, 5, 5);
		gbc_userTextField.gridx = 2;
		gbc_userTextField.gridy = 2;
		contentPanel.add(userTextField, gbc_userTextField);
		userTextField.setColumns(10);
		
		passwordLabel = new JLabel("Password");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 3;
		contentPanel.add(passwordLabel, gbc_passwordLabel);
		
		passwordTextField = new JTextField();
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 2;
		gbc_passwordTextField.gridy = 3;
		contentPanel.add(passwordTextField, gbc_passwordTextField);
		passwordTextField.setColumns(10);
		
		loginButton = new JButton("Login");
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.insets = new Insets(0, 0, 5, 5);
		gbc_loginButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginButton.anchor = GridBagConstraints.NORTH;
		gbc_loginButton.gridx = 2;
		gbc_loginButton.gridy = 5;
		contentPanel.add(loginButton, gbc_loginButton);
		
		loginButton.addActionListener(this);
	
	}
	
	public void validLogin() {
		Employee employee = new Employee();
		boolean login = false;
		counter++;
		
		try {
			
			// It shows an error if user made 3 attempts
			if (counter == 3 && !login) {
				throw new LimitLoginException();
			}
			
			while (!login) {
				
				int employeeNumber = Integer.parseInt(userTextField.getText());
				String password = passwordTextField.getText();
				
				login = employee.login(employeeNumber, password);
				
				if (!login) {
					JOptionPane.showMessageDialog(this, "Error. Cuenta no válida." + counter);
					login = true;
				} else {
					dispose();
					ShopView shopview = new ShopView();
					shopview.setVisible(true);
				}
			}
			
		} catch (LimitLoginException limitLogin) {
			JOptionPane.showMessageDialog(LoginView.this, limitLogin.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			validLogin();
		}
		
	}
	
}
