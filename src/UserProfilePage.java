/**
 * Author - Kyle Chambers
 * File name - UserProfilePage
 * Date of last update - 6/29/2024
 * UserProfilePage file description - The UserProfilePage takes all the users max lifts from the
 * database and displays them on the screen. It does this through connecting to the database using a
 * JDBC and using key terms in order to abstract the data.
 * Updates to come - I am going to make the userProfilePage interface more appealing, and add more
 * features to the page.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfilePage extends JDialog {
    private JButton homeButton;
    private JPanel UserProfile;
    private JLabel nameLabel;
    private JLabel userNameLabel;
    private JLabel ageLabel;
    private JLabel weightLabel;
    private JLabel maxBenchLabel;
    private JLabel maxSquatLabel;
    private JLabel maxDeadLiftLabel;
    private JLabel totalWeightLabel;
    private JLabel maxBarbellRowLabel;
    private JLabel maxPullupsLabel;
    private JButton addOrEditMaxButton;

    /**
     * UserProfilePage main method
     * Description - The UserProfilePage is responsible for creating the User Profile pop up and
     * using the users different data fields to show the users stats
     *
     * @param parent JFrame used in order to center the user profile pop-up
     * @param user   the user that has been logged in
     */
    public UserProfilePage(JFrame parent, User user) {
        super(parent);
        setTitle("profile");
        setContentPane(UserProfile);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setText(user);
        createButton(user);
    }

    /**
     * setText
     * Description - Sets the text for all the seperate text boxes using the users given attributes
     *
     * @param user the user currently logged in
     */
    private void setText(User user) {
        nameLabel.setText("Name: " + user.name);
        userNameLabel.setText("User Name: " + user.userName);
        ageLabel.setText("Age: " + user.age);
        weightLabel.setText("Weight: " + user.weight + " lbs");
        maxBenchLabel.setText("Max Bench: " + user.maxBench + " lbs");
        maxSquatLabel.setText("Max Squat: " + user.maxSquat + " lbs");
        maxDeadLiftLabel.setText("Max Dead lift: " + user.maxDeadlift + " lbs");
        maxPullupsLabel.setText("Max Pullups: " + user.maxPullups);
        maxBarbellRowLabel.setText("Max Barbell Row: " + user.maxBarbellRow + " lbs");
        totalWeightLabel.setText("Total Weight: " + user.totalWeight + " lbs");
    }

    /**
     * createButton
     * Description - Creates the button used to go back to the home page, by making the box an
     * action listener
     *
     * @param user the user currently logged in
     */
    private void createButton(User user) {
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });

        addOrEditMaxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddMaxLifts addMaxLifts = new AddMaxLifts(null, user);
            }
        });
        setVisible(true);
    }
}
