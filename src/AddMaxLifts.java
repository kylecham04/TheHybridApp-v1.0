/**
 * Author - Kyle Chambers
 * File name - AddMaxLifts
 * Date of last update - 7/12/2024
 * AddMaxLifts file description - Creates the add max lifts landing page and takes the user inputted
 * maxes and updates the table in the database with them, all maxes are set to 0 if not updated.
 * Updates to come - I am going to make it cleaner and add/ remove fields
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddMaxLifts extends JDialog {
    private JPanel addMaxLifts;
    private JTextField tfMaxBench;
    private JTextField tfMaxSquat;
    private JTextField tfMaxDeadLift;
    private JTextField tfMaxPullups;
    private JButton goToHomePageButton;
    private JButton submitButton;
    private JTextField tfMaxBarbellRow;
    private JTextField tfWeight;

    /**
     * AddMaxLifts
     * Description - Creates the add max lifts landing page and calls the methods to create the
     * action listeners and take the user inputted data
     *
     * @param parent - the JFrame used to center the add max landing page
     * @param user   - the user currently running the program
     */
    public AddMaxLifts(JFrame parent, User user) {
        super(parent);
        setTitle("Add Max Lifts");
        setContentPane(addMaxLifts);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createAddMaxLiftsButtons(user);
    }

    /**
     * createAddMaxLiftsButtons
     * Description - Creates the action listeners that either submit the added max lifts to the
     * database, or send the user to the homepage
     *
     * @param user - the user currently running the program
     */
    private void createAddMaxLiftsButtons(User user) {
        goToHomePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInputtedMaxs(user);
            }
        });
        setVisible(true);
    }

    /**
     * addInputtedMaxs
     * Description - Sets all the user's maxes from their inputs and calls the method to add them
     * to the database. Only proceeds if all fields are valid integers.
     *
     * @param user The user currently running the program
     */
    private void addInputtedMaxs(User user) {
        try {
            user.weight = parseInput(tfWeight.getText(), "Weight");
            user.maxBench = parseInput(tfMaxBench.getText(), "Max Bench");
            user.maxSquat = parseInput(tfMaxSquat.getText(), "Max Squat");
            user.maxDeadlift = parseInput(tfMaxDeadLift.getText(), "Max Deadlift");
            user.maxPullups = parseInput(tfMaxPullups.getText(), "Max Pullups");
            user.maxBarbellRow = parseInput(tfMaxBarbellRow.getText(), "Max Barbell Row");

            user.totalWeight = user.maxBench + user.maxSquat + user.maxDeadlift +
                    user.maxPullups + user.maxBarbellRow;

            addMaxLiftsToDB(user);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * parseInput
     * Description - Parses the input string to an integer.
     *
     * @param input     The input string
     * @param fieldName The name of the field for error messages
     * @return The parsed integer
     * @throws NumberFormatException If the input is not a valid integer
     */
    private int parseInput(String input, String fieldName) throws NumberFormatException {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Please enter a valid integer for " + fieldName);
        }
    }

    /**
     * addMaxLiftsToDB
     * Description - Adds all the user's maxes to the database.
     *
     * @param user The user currently running the program
     */
    private void addMaxLiftsToDB(User user) {
        final String url = "jdbc:mysql://localhost:3306/myfitnesstracker";
        final String username = "root";
        final String dataBasePassword = "Thehybridapp12!";

        String updateSql = "UPDATE registerUsers SET maxBench = ?, maxSquat = ?, " +
                "maxDeadlift = ?, maxPullups = ?, maxBarbellRow = ?, totalWeight = ?, " +
                "weight = ? WHERE userName = ?";

        try (Connection con = DriverManager.getConnection(url, username, dataBasePassword);
             PreparedStatement preparedStatement = con.prepareStatement(updateSql)) {

            preparedStatement.setInt(1, user.maxBench);
            preparedStatement.setInt(2, user.maxSquat);
            preparedStatement.setInt(3, user.maxDeadlift);
            preparedStatement.setInt(4, user.maxPullups);
            preparedStatement.setInt(5, user.maxBarbellRow);
            preparedStatement.setInt(6, user.totalWeight);
            preparedStatement.setInt(7, user.weight);
            preparedStatement.setString(8, user.userName);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Max lifts added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new HomePage(null, user);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add lifts.",
                        "Try again", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " +
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}