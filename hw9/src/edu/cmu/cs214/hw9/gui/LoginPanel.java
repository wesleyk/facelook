package edu.cmu.cs214.hw9.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.cmu.cs214.hw9.facelook.LoginController;

import net.miginfocom.swing.MigLayout;

public class LoginPanel extends JPanel {
	private FacelookInitGUI container;
	public LoginPanel(FacelookInitGUI j){
		super();
		container = j;
		this.setBackground(Color.decode("#3b5998"));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new MigLayout("", "[grow]", "[][][][][]"));
		
		JLabel lblFacelook = new JLabel("Facelook");
		lblFacelook.setForeground(Color.WHITE);
		lblFacelook.setFont(new Font("Lucida Fax", Font.BOLD, 36));
		this.add(lblFacelook, "cell 0 0");
		
		final JTextField txtUsername = new JTextField();
		txtUsername.setText("Email Address");
		txtUsername.setFont(new Font("Lucida Fax", Font.PLAIN, 13));
		txtUsername.setBackground(Color.decode("#3b5998"));
		txtUsername.setForeground(Color.WHITE);
		this.add(txtUsername, "cell 0 2,growx");
		txtUsername.setColumns(10);
		txtUsername.addFocusListener(new FocusListener() {

	        public void focusGained(FocusEvent e) {
	            if (txtUsername.getText().equals("Email Address")) {
	            	txtUsername.setText("");
	            }
	        }

	        public void focusLost(FocusEvent e) {
	            if ("".equalsIgnoreCase(txtUsername.getText().trim())) {
	            	txtUsername.setText("Email Address");
	            }
	        }});
		
		final JPasswordField txtPassword = new JPasswordField();
		txtPassword.setText("Password");
		txtPassword.setEchoChar((char)0);
		txtPassword.setFont(new Font("Lucida Fax", Font.PLAIN, 13));
		txtPassword.setBackground(Color.decode("#3b5998"));
		txtPassword.setForeground(Color.WHITE);
		this.add(txtPassword, "cell 0 3,growx");
		txtPassword.setColumns(10);
		txtPassword.addFocusListener(new FocusListener() {

	        public void focusGained(FocusEvent e) {
	            txtPassword.setEchoChar('*');
	            if (txtPassword.getText().equals("Password")) {
	            	txtPassword.setText("");
	            }
	        }

	        public void focusLost(FocusEvent e) {
	            if ("".equalsIgnoreCase(txtPassword.getText().trim())) {
	            	txtPassword.setEchoChar((char)0);
	            	txtPassword.setText("Password");
	            }
	        }});
		
		
		//=====================================//
		JButton btnLogin = new JButton("Login");
		this.add(btnLogin, "flowx,cell 0 4");
		btnLogin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO FILL THIS IN WITH CODE TO DEAL WITH LOGIN
				// UPON SUCCESSFUL LOGIN CREATE A FACELOOKAPPGUI JFRAME
				String email = txtUsername.getText();
				String password = txtPassword.getText();
				
				//calls logincontroller method from controller,
				//determines if login was successful or not.
				//launches a new GUI if login was successful, otherwise displays error
				boolean login = LoginController.login(email, password);
				if(login) {
					FacelookAppGUI fl = new FacelookAppGUI(email);
				}
				
				else {
					JOptionPane.showMessageDialog(null, "Login Failed. Try Again, or Register!");
				}
				
			}
			
		});
		//=====================================//
		
		
		JButton btnRegister = new JButton("Register");
		btnRegister.requestFocusInWindow();
		this.add(btnRegister, "cell 0 4");
		//Swaps to the registration panel
		btnRegister.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				container.replace(new RegisterPanel(container));
			}});
	}
}
