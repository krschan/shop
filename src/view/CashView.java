package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;

public class CashView extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton cancelButton;
	private JButton okButton;
	private JLabel cashLabel;
	private Shop shop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CashView(Shop shop) {
		this.shop = shop;

		setBounds(740, 200, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		cashLabel = new JLabel();
		contentPanel.add(cashLabel);

		cashAmount();
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPanel.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);
			}
			{
				cancelButton = new JButton("Volver");
				cancelButton.setActionCommand("Volver");
				buttonPanel.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
	}

	public void cashAmount() {
		String cashAmount = shop.recoverCash();
		cashLabel.setText("Dinero en caja: " + cashAmount);
	}

	@Override
	public void actionPerformed(ActionEvent interactionButtons) {
		if (interactionButtons.getSource() == okButton || interactionButtons.getSource() == cancelButton) {
			dispose();

		}

	}

}
