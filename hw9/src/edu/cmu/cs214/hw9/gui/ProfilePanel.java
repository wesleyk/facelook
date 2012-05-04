package edu.cmu.cs214.hw9.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import edu.cmu.cs214.hw9.db.Post;
import edu.cmu.cs214.hw9.facelook.FriendController;
import edu.cmu.cs214.hw9.facelook.NewsfeedController;
import edu.cmu.cs214.hw9.facelook.PostController;
import edu.cmu.cs214.hw9.facelook.SubscriptionController;

public class ProfilePanel extends JPanel {

	private FacelookAppGUI container;
	private JTextField textField;
	//emailName is the name of the profile you are viewing, emailUser is the user accessing it
	public ProfilePanel(final String emailName, final String emailUser, FacelookAppGUI a) {
		super();
		container = a;
		this.setBackground(Color.decode("#3b5998"));
		this.setPreferredSize(new Dimension(770,539));
		setLayout(null);
		
		JLabel lblFacelook = new JLabel("Facelook");
		lblFacelook.setFont(new Font("Lucida Fax", Font.PLAIN, 32));
		lblFacelook.setForeground(Color.WHITE);
		lblFacelook.setBounds(12, 13, 199, 32);
		add(lblFacelook);
		
		String name = NewsfeedController.getUserName(emailName);
		//GET THE NAME THAT IS TIED TO THE EMAIL ADDRESS
		
		String username = NewsfeedController.getUserName(emailUser);
		//GET THE USERNAME THAT IS TIED TO THE EMAIL ADDRESS


		JLabel nameLabel = new JLabel(name);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setBounds(173, 17, 199, 32);
		nameLabel.setFont(new Font("Lucida Fax", Font.PLAIN, 21));
		add(nameLabel);
		
		
		JLabel lblLatest = new JLabel("Latest Posts");
		lblLatest.setFont(new Font("Lucida Fax", Font.PLAIN, 20));
		lblLatest.setForeground(Color.WHITE);
		lblLatest.setBounds(12, 87, 166, 40);
		add(lblLatest);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 147, 719, 379);
		add(panel);
		panel.setLayout(new GridLayout(5, 2, 5, 5));
		
		/*
		 * Fill this GridLayout with StatusPost buttons for the status/notifications
		 * It is ok to generate this at the beginning and only refresh when coming back to this page. 
		 * If there are less than 10 then leave the remainder of the grid blank. These don't need to link back to same page.
		 */
		
		//retrieves most recent status posts from database
		//shows status and notifications if friends, otherwise, 
		//just shows notifications
		ArrayList<Post> arr = PostController.showPosts(emailName, emailUser);
		if (arr.size() > 10){
			System.out.println("DIDN'T GET 10 POSTS!");
		}
		
		
		System.out.println("Posts from database (" + arr.size() + "): ");
		ArrayList<StatusPost> stArr = new ArrayList<StatusPost>();
		for (Post p : arr){
			Date d = new Date(p.getDateAdded()*1000);
			StatusPost sp = new StatusPost(p.getEmail(), d, p.getContent());
			System.out.println(p.getContent());
			stArr.add(sp);
			panel.add(sp);
		}
		System.out.println("Done printing posts from database!");
		
		if(!emailUser.equals(emailName)){//Only show these when it is not your own profile
		JButton btnAddFriend = new JButton("Add/Remove Friend");
		btnAddFriend.setBounds(12, 49, 155, 25);
		add(btnAddFriend);
		
		//=====================================//
		btnAddFriend.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FILL IN CODE TO ADD/REMOVE FRIEND
				//modifyfriend either adds or removes a friend/friend request
				//based on the current relationship between the user viewing
				//the page and the current user's profile that you are viewing
				if(FriendController.modifyFriend(emailUser, emailName)){
					
					JOptionPane.showMessageDialog(null, "Added/Removed user as friend.");
				}
				else{
					
					JOptionPane.showMessageDialog(null, "Error: Cannot modify relationship.");
				}
				
			}
			
		});
		//=====================================//

		
		
		JButton btnAddSubscription = new JButton("Add/Remove Subscription");
		btnAddSubscription.setBounds(183, 49, 199, 25);
		add(btnAddSubscription);
		
		
		//=====================================//
		btnAddSubscription.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FILL IN CODE TO SUBSCRIBE TO USER
				//adds/removes subscription
				if(SubscriptionController.modifySubscription(emailUser, emailName)){
					
					JOptionPane.showMessageDialog(null, "Added/removed subscription to " + emailName);
				}
				else{
					
					JOptionPane.showMessageDialog(null, "Error");
				}
				
			}
			
		});
		}
		//=====================================//

		
		JButton btnNewsFeed = new JButton("News Feed");
		btnNewsFeed.setBounds(661, 49, 97, 25);
		add(btnNewsFeed);
		btnNewsFeed.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				container.replace(new NewsFeedPanel(emailUser, container));
			}
			
		});
		
		
		if(emailUser.equals(emailName)){
			textField = new JTextField("What's on your mind?");
			textField.setBounds(12, 60, 300, 32);
			add(textField);
			textField.setColumns(10);
		textField.addFocusListener(new FocusListener() {

	        public void focusGained(FocusEvent e) {
	            if (textField.getText().equals("What's on your mind?")) {
	            	textField.setText("");
	            }
	        }

	        public void focusLost(FocusEvent e) {
	            if ("".equalsIgnoreCase(textField.getText().trim())) {
	            	textField.setText("What's on your mind?");
	            }
	        }});
		
		JButton btnNewButton = new JButton("Post Status");
		btnNewButton.setBounds(312, 60, 97, 32);
		add(btnNewButton);
		
		
		//=====================================//
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO HANDLE POSTING OF STATUS
				String status = textField.getText();
				
				//max post length is 150
				if(status.length() > 150) {
					JOptionPane.showMessageDialog(null, "Error! Post can only be up to 150 characters long.");
				}
				
				//creates a new post on the profile panel, sends info to server
				else {
					long d = System.currentTimeMillis()/1000;
					boolean t = PostController.doPost(emailName, status, 1, d);
					if (t){
						JOptionPane.showMessageDialog(null, "Post Successful!");
						System.out.println("Tried to post: " + status);
					}
					else{
						JOptionPane.showMessageDialog(null, "An Error Occured while trying to post");
					}
				}
			}
			
		});
		//=====================================//

		
		
		JButton btnNewButton1 = new JButton("Post Notification");
		btnNewButton1.setBounds(409, 60, 140, 32);
		add(btnNewButton1);
		
		
		//=====================================//
		btnNewButton1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO HANDLE POSTING OF NOTIFICATION
				//posts new notification to server
				String notif = textField.getText();
				long d = System.currentTimeMillis()/1000;
				boolean t = PostController.doPost(emailName, notif, 0, d);
				if (t){
					JOptionPane.showMessageDialog(null, "Post Successful!");
					System.out.println("Tried to post: " + notif);
				}
				else{
					JOptionPane.showMessageDialog(null, "An Error Occured while trying to post");
				}
			}
		});
		//=====================================//

		
		}
	}
}
