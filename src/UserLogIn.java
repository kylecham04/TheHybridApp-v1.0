/**
 * Author - Kyle Chambers
 * File name - UserLogIn
 * Date of last update - 6/29/2024
 * UserLogIn file description - The user log in file takes a user inputted username and password and
 * searches the database table for corresponding values, if found it logs the user in and sets
 * its attributes, if not it throws an error message on screen
 * Updates to come - I am going to make the user interface more appealing, and try and
 * fix any bugs that I come across :-)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserLogIn extends JDialog {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton logInButton;
    private JButton signUpButton;
    private JPanel userLogInPanel;
    public UserRegister userRegister;
    public User user;


    /**
     * UserLogIn main method
     * Description - Prompts the user to enter there username and password, searching the database
     * to see if it is valid. Can also choose to create a new account if you are new to the app.
     *
     * @param parent - JFrame used in order to center the dialogue page
     */
    public UserLogIn(JFrame parent) {
        super(parent);
        setTitle("Log In");
        setContentPane(userLogInPanel);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createButtons();
        setVisible(true);
    }

    /**
     * createButtons
     * Description - Creates the buttons used to submit the inputted username and password, as well
     * as create a new user. Also calls the methods that actually validate the data inputted.
     */
    private void createButtons() {
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogIn();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                userRegister = new UserRegister(null);
            }
        });
    }

    /**
     * handleLogIn
     * Description - Gets both the username and password and checks if it is valid or not,
     * showing the corresponding message on the screen
     */
    private void handleLogIn() {
        String userName = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();

        if (userName.isEmpty() || password.isEmpty()) {
            showMessage("Please enter all fields", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = logIn(userName, password);
        if (user != null) {
            showMessage("Login successful!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new HomePage(null, user);
        } else {
            showMessage("Invalid Username or password.", "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * logIn
     * Description - accesses the database and sets all the users attributes if the userName and
     * password or valid, logging the user in
     *
     * @param userName - user inputted string used for checking / logging in
     * @param password - user inputted string used for checking / logging in
     * @return the user object with the usernames corresponding attributes
     */
    private User logIn(String userName, String password) {
        User user = null;
        DatabaseConnector databaseConnector = new DatabaseConnector();

        try (Connection con = databaseConnector.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT * FROM registerUsers WHERE userName = ? AND password = ?")){
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.userName = resultSet.getString("userName");
                user.password = resultSet.getString("password");
                user.email = resultSet.getString("email");
                user.age = resultSet.getInt("age");
                user.weight = resultSet.getInt("weight");
                user.maxBench = resultSet.getInt("maxBench");
                user.maxSquat = resultSet.getInt("maxSquat");
                user.maxDeadlift = resultSet.getInt("maxDeadlift");
                user.maxPullups = resultSet.getInt("maxPullups");
                user.totalWeight = resultSet.getInt("totalWeight");
            }
        } catch (SQLException e) {
            System.out.println("Database connection or query execution failed.");
            e.printStackTrace();
        }

        return user;
    }

    /**
     * showMessage
     * Description: Creates and shows the boxes of information after the user submits, or
     * inputs an invalid register form
     *
     * @param message     the message shown to the user after an invalid input or the user submits
     * @param title       the type of message shown
     * @param messageType either an information message or error message, depending on what the
     *                    user inputs
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * Main
     * Description - Creates the userLogIn object that starts the whole app
     */
    public static void main(String[] args) {
        UserLogIn userLogIn = new UserLogIn(null);
        User user = userLogIn.user;
    }
}