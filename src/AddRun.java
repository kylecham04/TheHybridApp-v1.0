/**
 * Author - Kyle Chambers
 * File name - AddRun
 * Date of last update - 6/30/2024
 * AddRun file description - Creates the add run page and all of its input fields, as well as inserts
 * all the users data into a mySQL table
 * Updates to come - I am going to make it cleaner and update the fields to have a better user
 * interface, as well as calculate the calories burned from the run
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

public class AddRun extends JDialog {
    private JTextField tfDistanceRan;
    private JTextField tfTimeRan;
    private JButton backToChooseRunButton;
    private JButton submitRunButton;
    private JTextField tfDateField;
    private JPanel enterRunData;

    /**
     * AddRun main method
     * Description - Creates the add run window where the user can enter a log of a run on any day
     * , and it is saved into a mySQL database
     *
     * @param parent JFrame used to center the add run window
     * @param user   - the user who is currently running the program
     */
    public AddRun(JFrame parent, User user) {
        super(parent);
        setTitle("Add Run");
        setContentPane(enterRunData);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createAddRunButtons(user);
    }

    /**
     * createAddRunButtons
     * Description - Creates the action listeners for the submit run and cancel buttons, which
     * either bring you back to the choose run or lift page or transfer the data to the database
     * if it is valid
     *
     * @param user - the user who is running the program
     */
    private void createAddRunButtons(User user) {
        backToChooseRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChooseRunOrLiftPage chooseRunOrLiftPage = new ChooseRunOrLiftPage(null, user);
            }
        });

        submitRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    addRunData(user);
                }
            }
        });

        setVisible(true);
    }

    /**
     * validateInput
     * Description - Validates the user input for date, time, and distance.
     *
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInput() {
        if (tfDateField.getText().trim().isEmpty() ||
                tfTimeRan.getText().trim().isEmpty() ||
                tfDistanceRan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(tfTimeRan.getText().trim());
            Double.parseDouble(tfDistanceRan.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for time and distance",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * addRunData
     * Description - Converts the users data into the correct form to be added to the database
     *
     * @param user The user who is currently running the program
     */
    private void addRunData(User user) {
        user.todaysDate = tfDateField.getText().trim();
        user.timeWorkout = Double.parseDouble(tfTimeRan.getText().trim());
        user.milesRanToday = Double.parseDouble(tfDistanceRan.getText().trim());
        user.todaysPace = user.timeWorkout / user.milesRanToday;
        BigDecimal bd = new BigDecimal(Double.toString(user.todaysPace));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        user.todaysPace = bd.doubleValue();
        user.caloriesBurned = (int)(user.milesRanToday * user.weight * .75);

        addOrUpdateRunInDatabase(user);
    }

    /**
     * addOrUpdateRunInDatabase
     * Description - Adds or updates the run data in the database, whether the runs date has
     * already been added
     * or not.
     *
     * @param user The user running the program
     */
    private void addOrUpdateRunInDatabase(User user) {
        DatabaseConnector databaseConnector = new DatabaseConnector();

        String checkSql = "SELECT * FROM userRuns WHERE userName = ? AND date = ?";
        String updateSql = "UPDATE userRuns SET name = ?, timeRan = ?, distance = ?, " +
                "caloriesBurned = ?, pace = ? WHERE userName = ? AND date = ?";
        String insertSql = "INSERT INTO userRuns (userName, date, name, timeRan, " +
                "distance, caloriesBurned, pace) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = databaseConnector.getConnection();
             PreparedStatement checkPstm = con.prepareStatement(checkSql);
             PreparedStatement updatePstm = con.prepareStatement(updateSql);
             PreparedStatement insertPstm = con.prepareStatement(insertSql)) {

            checkPstm.setString(1, user.userName);
            checkPstm.setString(2, user.todaysDate);
            ResultSet rs = checkPstm.executeQuery();

            PreparedStatement pstm;
            if (rs.next()) {
                updatePstm.setString(1, user.name);
                updatePstm.setDouble(2, user.timeWorkout);
                updatePstm.setDouble(3, user.milesRanToday);
                updatePstm.setInt(4, user.caloriesBurned);
                updatePstm.setDouble(5, user.todaysPace);
                updatePstm.setString(6, user.userName);
                updatePstm.setString(7, user.todaysDate);
                pstm = updatePstm;
            } else {
                insertPstm.setString(1, user.userName);
                insertPstm.setString(2, user.todaysDate);
                insertPstm.setString(3, user.name);
                insertPstm.setDouble(4, user.timeWorkout);
                insertPstm.setDouble(5, user.milesRanToday);
                insertPstm.setInt(6, user.caloriesBurned);
                insertPstm.setDouble(7, user.todaysPace);
                pstm = insertPstm;
            }

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Run data saved " +
                                "successfully! Returning to the home page.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new HomePage(null, user);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save " +
                                "run data.",
                        "Try again", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " +
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}