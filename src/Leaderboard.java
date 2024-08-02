/**
 * Author - Kyle Chambers
 * File name - Leaderboard
 * Date of last update - 6/29/2024
 * Leaderboard file description - Creates the leaderboard page, where all the users are printed
 * to the screen in descending order based on their total weight
 * Updates to come - I am going to add ways to see leaderboards based on different lifts, days
 * logged, and total minutes working out
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Leaderboard extends JDialog {
    private JTextArea taLeaderboard;
    private JPanel liftingLeaderboard;
    private JButton returnToHomePageButton;
    private int place;

    /**
     * Leaderboard main method
     * Description - Creates the leaderboard page, where all users are printed in descending
     * order of their total lifts
     *
     * @param parent - JFrame used in order to center the leaderboard page
     * @param user   - represents the current user of the program
     */
    public Leaderboard(JFrame parent, User user) {
        super(parent);
        setTitle("View Total Weight Data");
        setContentPane(liftingLeaderboard);
        setMinimumSize(new Dimension(1200, 600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int place = 0;

        initializeUI(user);
        fetchAndDisplayLiftsData();

        setVisible(true);
    }

    /**
     * initializeUI
     * Description - Creates the return button of the leaderboard page
     *
     * @param user
     */
    private void initializeUI(User user) {
        returnToHomePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });
    }

    /**
     * fetchAndDisplayLiftData
     * Description - Connects the leaderboard page to the database using a JDBC and orders the
     * data based on descending order of the total weight field. Then calls the method to show the
     * data on the screen.
     */
    private void fetchAndDisplayLiftsData() {
        final String url = "jdbc:mysql://localhost:3306/myfitnesstracker";
        final String username = "root";
        final String dataBasePassword = "Thehybridapp12!";

        String sql = "SELECT name, maxBench, maxSquat, maxDeadlift, maxPullups, " +
                "maxBarbellRow, totalWeight FROM registerUsers ORDER BY totalWeight DESC";

        try (Connection con = DriverManager.getConnection(url, username, dataBasePassword);
             PreparedStatement pstm = con.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            StringBuilder liftsData = new StringBuilder();
            while (rs.next()) {
                liftsData.append(formatLiftData(rs));
            }
            taLeaderboard.setText(liftsData.toString());

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Failed to fetch data from the database.");
        }
    }

    /**
     * formatLiftData
     * Description - Formats each line of the leader board
     *
     * @param rs - the result set holding all the users data
     * @return a string representing a single users data
     * @throws Exception ensures the result set is not empty and is created
     */
    private String formatLiftData(ResultSet rs) throws Exception {
        String name = rs.getString("name");
        double maxBench = rs.getDouble("maxBench");
        double maxSquat = rs.getDouble("maxSquat");
        double maxDeadlift = rs.getDouble("maxDeadlift");
        double maxPullups = rs.getDouble("maxPullups");
        double maxBarbellRow = rs.getDouble("maxBarbellRow");
        double totalWeight = rs.getDouble("totalWeight");
        place++;
        return String.format("%d. <%s> Total Weight: %.2f lbs (Max Bench: %.2f lbs - Max Squat: " +
                        "%.2f lbs - Max " +
                        "Deadlift: %.2f lbs - " +
                        "Max Pullups: %.2f reps - Max Barbell Row: %.2f lbs)%n" +
                        "-------------------------------------------------------------------------" +
                        "-------------------------------------------------------------------------" +
                        "-------------------------------------------------------------------------" +
                        "-------------------------------------------------------------------------" +
                        "----\n",
                place, name, totalWeight, maxBench, maxSquat, maxDeadlift, maxPullups,
                maxBarbellRow);
    }

    /**
     * showErrorDialog
     * Description - is used to show the error message on screen in the case there is one
     *
     * @param message - the string shown on the screen in case of an error
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}