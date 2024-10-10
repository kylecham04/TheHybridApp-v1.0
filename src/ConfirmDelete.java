/**
 * Author - Kyle Chambers
 * File name - ConfirmDelete
 * Date of last update - 6/29/2024
 * ConfirmDelete file description - Creates the confirm delete page and all the corresponding
 * functions
 * Updates to come - I am going to add an email sender confirming the deletion
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConfirmDelete extends JDialog {
    private JPanel confirmDelete;
    private JTextField tfUserName;
    private JTextField tfPassword;
    private JButton confirmDeletionButton;
    private JButton cancelButton;

    /**
     * ConfirmDelete main method
     * Description - creates the confirm delete page and all of its features used to delete a user
     *
     * @param parent - JFrame used to center the page
     * @param user   - the user running the program
     */
    public ConfirmDelete(JFrame parent, User user) {
        super(parent);
        setTitle("Home Page");
        setContentPane(confirmDelete);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createConfirmDeleteButtons(user);
    }

    /**
     * createConfirmDeleteButtons
     * Description - Creates the buttons to submit the user to delete or cancel the deletion
     *
     * @param user - the user who is running the program
     */
    private void createConfirmDeleteButtons(User user) {
        confirmDeletionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFields();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Settings settings = new Settings(null, user);
            }
        });
        setVisible(true);
    }

    /**
     * getFields
     * Description - Gets the username and password of the user who is deleting there account and
     * ensures all data is properly entered and user exists.
     */
    private void getFields() {
        String userName = tfUserName.getText();
        String password = tfPassword.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int rows = deleteAccount(userName, password);
        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Deletion successful!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            UserLogIn userLogIn = new UserLogIn(null);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username " +
                            "or password.",
                    "Try again", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * deleteAccount
     * Description - Searches the database for the username and password of the user and deletes
     * the account from the database, logging the user out and sending them to the front page
     *
     * @param userName - username entered by the user
     * @param password password entered byt he user
     * @return the number of rows affected in the database table
     */
    private static int deleteAccount(String userName, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        int rows = 0;
        try {
            Connection con = databaseConnector.getConnection();
            String query = "DELETE FROM registerUsers WHERE userName=? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User found in the database.");
            } else {
                System.out.println("No user found with the provided credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
}
