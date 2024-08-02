/**
 * Author - Kyle Chambers
 * File name - ViewWorkoutLog
 * Date of last update - 7/1/2024
 * ViewWorkoutPage description - The View workout page shows all the user inputted runs and
 * lifts on screen through the use of getting the data from a mySQL table
 * Updates to come - I am going to make the window resizeable and make the user interface more
 * appealing
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewWorkoutLog extends JDialog {
    private JPanel TodaysWorkouts;
    private JTextArea taRunData;
    private JTextArea taLiftData;
    private JButton backButton;
    private JLabel Date;

    /**
     * TodaysWorkout constructor
     * Description - Creates the today's workout window where the user can view all of there lifting
     * and running data for that given day
     * @param parent - the JFrame used to center the window
     * @param user - the user running the program
     */
    public ViewWorkoutLog(JFrame parent, User user) {
        super(parent);
        setTitle("Settings");
        setMinimumSize(new Dimension(800, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        Date.setText("Today's date: " + user.todaysDate);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createViewWorkoutButton(user);
    }

    /**
     * createViewWorkoutButton
     * Description - Creates the button listener that takes the user back to the home page
     * @param user - the user running the program
     */
    private void createViewWorkoutButton(User user) {
        setRunText(user);
        setLiftText(user);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });
        setVisible(true);
    }

    /**
     * setLiftText
     * Description - sends the sql command string to the set text fields method
     * @param user - the user running the program
     */
    private void setLiftText(User user) {
        String liftSql = "SELECT date, todaysLift FROM userLifts WHERE userName = ?";
        setTextFields(liftSql, "lift", user);
    }

    /**
     * setRunText
     * Description - sends the sql command string to the set text fields method
     * @param user - the user running the program
     */
    private void setRunText(User user) {
        String runSql = "SELECT date, timeRan, distance, caloriesBurned, pace FROM userRuns " +
                "WHERE userName = ?";
        setTextFields(runSql, "run", user);
    }

    /**
     * setTextFields
     * Description - Sets both the run and lift text fields with the data found in the mySQL tables
     * @param sql - command string used to access the data
     * @param whichWorkout - the string representing either run or lift, the text box were filling
     *                     out
     * @param user - the user running the program
     */
    private void setTextFields(String sql, String whichWorkout, User user) {
        final String url = "jdbc:mysql://localhost:3306/myfitnesstracker";
        final String username = "root";
        final String dataBasePassword = "Thehybridapp12!";

        try (Connection con = DriverManager.getConnection(url, username, dataBasePassword);
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, user.userName);
            try (ResultSet rs = pstm.executeQuery()) {
                StringBuilder liftData = new StringBuilder();
                StringBuilder runData = new StringBuilder();
                while (rs.next()) {
                    if (whichWorkout.equals("run")) {
                        runData.append(formatWorkoutData(rs, whichWorkout));
                    } else {
                        liftData.append(formatWorkoutData(rs, whichWorkout));
                    }
                }
                if (whichWorkout.equals("run")) {
                    taRunData.setText(runData.toString());
                } else {
                    taLiftData.setText(liftData.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " +
                            e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * formatWorkoutData
     * Description - Formats the strings we are using to show the user their respective workouts
     * @param rs - result set containing one workout line
     * @param whichWorkout - a string representing either a run or lifting workout
     * @return a string formatted to put on the screen
     * @throws SQLException
     */
    private String formatWorkoutData(ResultSet rs, String whichWorkout) throws SQLException {
        StringBuilder formattedData = new StringBuilder();
        String date = rs.getString("date");

        if (whichWorkout.equals("run")) {
            double distance = rs.getDouble("distance");
            double time = rs.getDouble("timeRan");
            double pace = rs.getDouble("pace");
            int caloriesBurned = rs.getInt("caloriesBurned");
            formattedData.append("- ").append(date).append(" (")
                    .append(distance).append(" miles, ")
                    .append(time).append(" minutes, ")
                    .append(pace).append(" minute pace, ")
                    .append(caloriesBurned).append(" calories burned!)\n");
        } else {
            String workout = rs.getString("todaysLift");
            formattedData.append("- ").append(date).append(" -> ")
                    .append(workout).append("<-\n-------------------------------------------------"
                            + "--------------------------------------------------------------");
        }
        return formattedData.toString();
    }
}