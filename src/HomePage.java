/**
 * Author - Kyle Chambers
 * File name - HomePage
 * Date of last update - 6/29/2024
 * HomePage file description - Creates the home page and all the buttons responsible for getting
 * to the other screens
 * Updates to come - I am going to add more options to the page and clean it up
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HomePage extends JDialog {
    private JButton addNewWorkoutButton;
    private JButton getARecommendedWorkoutButton;
    private JButton viewLeaderboardButton;
    private JButton settingsButton;
    private JPanel homePage;
    private JButton viewProfileButton;
    private JButton workoutLogButton;

    /**
     * HomePage main method
     * Description - Creates the home page and all the buttons related to it
     *
     * @param parent - JFrame used in order to center the homepage
     * @param user   - the user object representing the user running the hybrid app
     */
    public HomePage(JFrame parent, User user) {
        super(parent);
        setTitle("Home Page");
        setContentPane(homePage);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createActionButtons(user);
    }

    /**
     * createActionButtons
     * Description - creates all the listeners corresponding to the buttons and makes it
     * functional
     *
     * @param user the user running the program
     */
    private void createActionButtons(User user) {
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Settings settings = new Settings(null, user);
            }
        });


        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserProfilePage userProfilePage = new UserProfilePage(null, user);
            }
        });


        addNewWorkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChooseRunOrLiftPage chooseRunOrLiftPage = new ChooseRunOrLiftPage(null, user);
            }
        });

        viewLeaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Leaderboard leaderboard = new Leaderboard(null, user);
            }
        });

        getARecommendedWorkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dispose();
                ChooseWhichLiftPage recommendLiftPage = new ChooseWhichLiftPage(null, user);
            }
        });

        workoutLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    ViewWorkoutLog viewWorkoutLog = new ViewWorkoutLog(null, user);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }
}
