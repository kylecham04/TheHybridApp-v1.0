/**
 * Author - Kyle Chambers
 * File name - RegisterUser
 * Date of last update - 6/29/2024
 * Register file description - The register file class successfully registers a new user account,
 * doing so through the use of swift GUI framework and a JDBC connected to a mySql Data base.
 * It allows for no duplicate names or emails, and the age and weight must be inputted integers.
 * Updates to come - I am going to make the user interface more appealing, and try and
 * fix any bugs that I come across :-)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class UserRegister extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfAge;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton registerButton;
    private JButton cancelButton;
    private JTextField tfWeight;
    private JPanel registerPanel;

    /**
     * UserRegister - Main method
     * Description - Creates the register user prompt and all fields/ buttons necessary to hold
     * the information. It then calls the methods necessary to transfer the data into the mySQL
     * database.
     */
    public UserRegister(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createActionButtons();
    }

    /**
     * createActionButtons
     * Description - createActonButtons method is responsible for creatinga  listener for both the
     * submit and cancel button, which now respond on the mouse pressing on them. It also calls the
     * corresponding methods that store the data in the database, and print all messages.
     */
    private void createActionButtons() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLogIn userLogIn = new UserLogIn(null);
            }
        });
        setVisible(true);
    }

    /**
     * registerUser
     * Description - The registerUser method is responsible for getting the data from the user
     * inputs, checking to make sure it is correct, and then adding it into the mySQL database.
     * It continues to re-prompt until all fields are filled out correctly, and calls the method
     * responsible for actually adding the data after wards.
     */
    private void registerUser() throws SQLException {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String userName = tfUsername.getText().trim();
        String age = tfAge.getText().trim();
        String weight = tfWeight.getText().trim();
        String password = String.valueOf(tfPassword.getPassword()).trim();
        String confirmPassword = String.valueOf(tfConfirmPassword.getPassword()).trim();

        if (!validateInputs(name, email, userName, age, weight, password, confirmPassword)) {
            return;
        }

        try {
            int fixedWeight = Integer.parseInt(weight);
            int fixedAge = Integer.parseInt(age);
            User user = addUserToDatabase(name, email, fixedAge, fixedWeight, userName, password);
            if (user != null) {
                showMessage("Registration successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AddMaxLifts(null, user);
            } else {
                showMessage("Failed to register new user", "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            showMessage("Please enter valid integers for age and weight fields.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * validateInputs
     * Description - Checks all fields ensuring they are properly filled out
     *
     * @param @param   name - String representing the name inputted by the user
     * @param email    - String representing the unique email inputted by the user
     * @param age      - Int representing the age inputted by the user
     * @param weight   - Int representing the weight inputted by the user
     * @param userName - String representing the unique username inputted by the user
     * @param password - String representing the password inputted by the user
     * @return whether the inputs are valid or not
     */
    private boolean validateInputs(String name, String email, String userName, String age,
                                   String weight, String password, String confirmPassword) throws SQLException {
        if (name.isEmpty() || userName.isEmpty() || email.isEmpty() || age.isEmpty() ||
                weight.isEmpty() || password.isEmpty()) {
            showMessage("Please enter all fields", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Confirm password does not match", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (userName.length() > 15) {
            showMessage("Username must be at most 15 characters",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isPasswordValid(password)) {
            showMessage("Password must be 9-15 characters long and include at " +
                            "least one special character and one number", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isValidEmail(email)) {
            showMessage("Please enter a valid email", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (isFieldTaken("userName", userName)) {
            showMessage("Username already taken", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (isFieldTaken("email", email)) {
            showMessage("Email already taken", "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * isPasswordValid
     * Description - Checks to make sure the password has atleast 1 number and 1 special character,
     * as well as it is between 9-15 characters long
     *
     * @param password String representing the user inputted password to check
     * @return whether the password is valid or not
     */
    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{9,15}$";
        return Pattern.matches(passwordPattern, password);
    }

    /**
     * isPasswordEmail
     * Description - Checks to make sure the email is a legit email (ex. kylecham is not, but
     * kylecham04@yahoo.com is)
     *
     * @param email String representing the user inputted email to check
     * @return whether the email is valid or not
     */
    private boolean isValidEmail(String email) {
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(emailPattern, email);
    }

    /**
     * isFieldTaken
     * Description - Checks whether the user inputted email or username has already
     * been taken, running it until it is
     *
     * @param fieldName the string representing the field that needs to be checked, either email
     *                  or username
     * @param value     the value of that corresponding field that needs to be checked
     * @return whether the username or email already exists in the database or not
     */
    private boolean isFieldTaken(String fieldName, String value) throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        boolean fieldTaken = false;

        try (Connection con = connector.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT COUNT(*) " +
                     "FROM registerUsers WHERE " + fieldName + " = ?")){
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                fieldTaken = true;
            }
        }
        return fieldTaken;
    }

    /**
     * addUserToDataBase
     * Description - Does the connecting and actual addition of the user to the database, after
     * everything has already been checked and is clear to be added
     *
     * @param name     - String representing the name inputted by the user
     * @param email    - String representing the unique email inputted by the user
     * @param age      - Int representing the age inputted by the user
     * @param weight   - Int representing the weight inputted by the user
     * @param userName - String representing the unique username inputted by the user
     * @param password - String representing the password inputted by the user
     * @return the user object with all of its registered attributes set
     */
    private User addUserToDatabase(String name, String email, int age, int weight,
                                   String userName, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        User user = null;

        try (Connection con = databaseConnector.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("INSERT into " +
                     "registerUsers (userName, name, email, age, weight, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, age);
            preparedStatement.setInt(5, weight);
            preparedStatement.setString(6, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.age = age;
                user.name = name;
                user.userName = userName;
                user.password = password;
                user.weight = weight;
                user.email = email;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
}