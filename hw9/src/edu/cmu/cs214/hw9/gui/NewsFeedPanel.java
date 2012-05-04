package edu.cmu.cs214.hw9.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTextField;

import edu.cmu.cs214.hw9.db.Post;
import edu.cmu.cs214.hw9.facelook.NewsfeedController;
import edu.cmu.cs214.hw9.facelook.PostController;

public class NewsFeedPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private FacelookAppGUI container;
	
	//private emailName field for use in creating new GUI panels
	private String emailName; 
	
	public NewsFeedPanel(String email, FacelookAppGUI a) {
		super();
		container = a;
		this.setBackground(Color.decode("#3b5998"));
		this.setPreferredSize(new Dimension(770,539));
		setLayout(null);
		
		emailName = email;
		//gets the name that is tied to the email address through NewsFeedController
		String name = NewsfeedController.getUserName(email);
		
		
		JLabel lblFacelook = new JLabel("Facelook");
		lblFacelook.setFont(new Font("Lucida Fax", Font.PLAIN, 32));
		lblFacelook.setForeground(Color.WHITE);
		lblFacelook.setBounds(12, 13, 199, 32);
		add(lblFacelook);
		
		JButton profile = new JButton(name);
		profile.setBounds(12, 47, 150, 25);
		profile.setFont(new Font("Lucida Fax", Font.PLAIN, 12));
		add(profile);
		
		//==================================================/
		profile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FILL IN CODE TO MOVE TO USER'S OWN PROFILE
				container.replace(new ProfilePanel(emailName, emailName, container));
				
			}
			
		});
		//==================================================/
		
		
		JButton btnFriends = new JButton("Friends");
		btnFriends.setBounds(172, 47, 97, 25);
		btnFriends.setFont(new Font("Lucida Fax", Font.PLAIN, 12));
		add(btnFriends);
		
		
		//==================================================/
		btnFriends.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FILL IN CODE TO MOVE TO USER'S FRIENDS LIST
				container.replace(new FriendListPanel(emailName, container));
				
			}
			
		});
		//==================================================/
		
		
		
		JButton btnSubscriptions = new JButton("Subscriptions");
		btnSubscriptions.setFont(new Font("Lucida Fax", Font.PLAIN, 10));
		btnSubscriptions.setBounds(279, 48, 97, 25);
		add(btnSubscriptions);
		
		
		
		//==================================================/
		btnSubscriptions.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FILL IN CODE TO MOVE TO USER'S SUBSCRIPTION LIST
				container.replace(new SubscriptionListPanel(emailName, container));
				
			}
			
		});
		//==================================================/
		
		
		
		JLabel lblNewsFeed = new JLabel("News Feed");
		lblNewsFeed.setFont(new Font("Lucida Fax", Font.PLAIN, 20));
		lblNewsFeed.setForeground(Color.WHITE);
		lblNewsFeed.setBounds(12, 87, 166, 40);
		add(lblNewsFeed);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 147, 719, 379);
		add(panel);
		panel.setLayout(new GridLayout(5, 2, 5, 5));
		
		
		/*
		 * Fill this GridLayout with StatusPost buttons for the status/notifications
		 * It is ok to generate this at the beginning and only refresh when coming back to this page. 
		 * If there are less than 10 then leave the remainder of the grid blank.
		 */
		
		//retrieves most recent 10 statuses/notifications
		ArrayList<Post> arr = PostController.showNewsFeedPosts(email);
		
		//should always return 10 or less posts, otherwise an error has occured
		if (arr.size() > 10){
			System.out.println("DIDN'T GET 10 POSTS!");
		}
		
		System.out.println("Posts from database (" + arr.size() + "): ");
		
		//creates an arraylist of status posts to put on the newsfeed
		ArrayList<StatusPost> stArr = new ArrayList<StatusPost>();
		for (Post p : arr){
			Date d = new Date(p.getDateAdded()*1000);
			StatusPost sp = new StatusPost(p.getEmail(), d, p.getContent());
			System.out.println(p.getContent());
			stArr.add(sp);
			panel.add(sp);
		}
		System.out.println("Done printing posts from database!");
		
		
		
		
		
		final JTextField txtExample = new JTextField();
		txtExample.setText("example@example.com");
		txtExample.setBounds(432, 13, 166, 22);
		add(txtExample);
		txtExample.setColumns(10);
		txtExample.addFocusListener(new FocusListener() {

	        public void focusGained(FocusEvent e) {
	            if (txtExample.getText().equals("example@example.com")) {
	            	txtExample.setText("");
	            }
	        }

	        public void focusLost(FocusEvent e) {
	            if ("".equalsIgnoreCase(txtExample.getText().trim())) {
	            	txtExample.setText("example@example.com");
	            }
	        }
				
		});
		
		JButton btnFindUser = new JButton("Find User");
		btnFindUser.setBounds(598, 13, 97, 22);
		add(btnFindUser);
		
		
		//==================================================/
		btnFindUser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO GO TO THE USER'S PROFILE THAT IS SEARCHED FOR IN txtExample
				// JTextField
				
				String searchEmail = txtExample.getText();
				
				//user does not exist
				if(NewsfeedController.getUserName(searchEmail).equals("")) 
					JOptionPane.showMessageDialog(null, "User with that email does not exist");
				else
					container.replace(new ProfilePanel(searchEmail, emailName, container));
				
				
			}
			
		});
		//==================================================/

	}
}
