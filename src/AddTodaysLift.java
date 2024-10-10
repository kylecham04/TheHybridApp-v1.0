/**
 * Author - Kyle Chambers
 * File name - AddTodaysLift
 * Date of last update - 6/29/2024
 * AddTodaysLift file description - Creates the add today's lift page and all of its inputs
 * Updates to come - I am going to make it cleaner and update the fields
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddTodaysLift extends JDialog {
    private JTextField tfExercise1;
    private JTextField tfExercise2;
    private JTextField tfExercise3;
    private JTextField tfExercise4;
    private JTextField tfExercise6;
    private JTextField tfExercise5;
    private JTextField tfExercise7;
    private JTextField tfExercise8;
    private JButton backButton;
    private JButton submitButton;
    private JTextField tfExercise10;
    private JTextField tfExercise9;
    private JTextField tfExercise12;
    private JTextField tfExercise11;
    private JPanel AddTodaysLift;
    private JTextField tfDate;

    /**
     * AddTodaysLift main method
     * Description - Creates the page and takes a user inputted lift and adds it to a lift
     * table in the database
     *
     * @param parent - JFrame used to center the page
     * @param user   - the user running the program
     */
    public AddTodaysLift(JFrame parent, User user) {
        super(parent);
        setTitle("AddTodaysLift");
        setContentPane(AddTodaysLift);
        setMinimumSize(new Dimension(1200, 600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setAddTodaysLiftButtons(user);
    }

    /**
     * setAddTodaysLiftButtons
     * Description - Creates all the working buttons for the Today's Lift Page
     *
     * @param user - the user running the program
     */
    private void setAddTodaysLiftButtons(User user) {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChooseRunOrLiftPage chooseRunOrLiftPage = new ChooseRunOrLiftPage(null, user);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser(user);
                HomePage homePage = new HomePage(null, user);
            }
        });
        setVisible(true);
    }

    /**
     * updateUser
     * Description - takes all the inpputed lifts and joins them together into one lift
     *
     * @param user - the user running the program
     */
    private boolean updateUser(User user) {
        String date = tfDate.getText().trim();
        String[] exercises = {
                "(" + tfExercise1.getText().trim() + ")",
                "(" + tfExercise2.getText().trim() + ")",
                "(" + tfExercise3.getText().trim() + ")",
                "(" + tfExercise4.getText().trim() + ")",
                "(" + tfExercise5.getText().trim() + ")",
                "(" + tfExercise6.getText().trim() + ")",
                "(" + tfExercise7.getText().trim() + ")",
                "(" + tfExercise8.getText().trim() + ")",
                "(" + tfExercise9.getText().trim() + ")",
                "(" + tfExercise10.getText().trim() + ")",
                "(" + tfExercise11.getText().trim() + ")",
                "(" + tfExercise12.getText().trim() + ")"
        };

        StringBuilder todaysLift = new StringBuilder();
        for (String exercise : exercises) {
            if (!exercise.isEmpty()) {
                if (todaysLift.length() > 0) {
                    todaysLift.append("\n");
                }
                todaysLift.append(exercise);
            }
        }
        user.todaysDate = date;
        user.todaysLift = todaysLift.toString();
        return updateOrAddLiftToDatabase(user);
    }

    /**
     * updateOrAddLiftToDatabase
     * Description - Adds today's lift to the database or updates a corresponding one if duplicate
     * date is added
     *
     * @param user - the user running the program
     */
    private boolean updateOrAddLiftToDatabase(User user) {
        DatabaseConnector databaseConnector = new DatabaseConnector();

        String sqlCheck = "SELECT * FROM userLifts WHERE date = ? AND userName = ?";
        String sqlUpdate = "UPDATE userLifts SET todaysLift = ?, name = ? WHERE date = ? " +
                "AND userName = ?";
        String sqlAdd = "INSERT INTO userLifts (date, userName, name, todaysLift) VALUES " +
                "(?, ?, ?, ?)";

        try (Connection con = databaseConnector.getConnection();
            PreparedStatement pstmCheck = con.prepareStatement(sqlCheck);
            PreparedStatement pstmUpdate = con.prepareStatement(sqlUpdate);
            PreparedStatement pstmAdd = con.prepareStatement(sqlAdd)){

            pstmCheck.setString(1, user.todaysDate);
            pstmCheck.setString(2, user.userName);

            ResultSet resultSet = pstmCheck.executeQuery();
            PreparedStatement pstm;

            if (resultSet.next()) {
                pstmUpdate.setString(1, user.todaysLift);
                pstmUpdate.setString(2, user.name);
                pstmUpdate.setString(3, user.todaysDate);
                pstmUpdate.setString(4, user.userName);
                pstm = pstmUpdate;
            } else {
                pstmAdd.setString(1, user.todaysDate);
                pstmAdd.setString(2, user.userName);
                pstmAdd.setString(3, user.name);
                pstmAdd.setString(4, user.todaysLift);
                pstm = pstmAdd;
            }

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Today's Lift saved successfully! Returning to the home page.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save today's lift.", "Try again",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
