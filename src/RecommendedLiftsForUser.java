/**
 * Author - Kyle Chambers
 * File name - RecommendLiftsForUser
 * Date of last update - 7/12/2024
 * AddMaxLifts file description - Creates the add max lifts landing page and takes the user inputted
 * maxes and updates the table in the database with them, all maxes are set to 0 if not updated.
 * Updates to come - I am going to make it cleaner and add/ remove fields
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecommendedLiftsForUser extends JDialog {
    private JButton backButton;
    private JButton addToDailyLiftButton; // Make text fields public
    private JPanel RecommendedLift;
    private JTable suggestedLifts;

    /**
     * Constructor
     * @param parent - JFrame used to center the page
     * @param user - User using the program
     * @param lift - String representing the type of
     */
    public RecommendedLiftsForUser(JFrame parent, User user, String lift) {
        super(parent);
        setTitle("Recommended Lifts");
        setContentPane(RecommendedLift);
        setMinimumSize(new Dimension(800, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create and set up the JTable
        suggestedLifts = setTable(user, lift);
        JScrollPane scrollPane = new JScrollPane(suggestedLifts);
        RecommendedLift.setLayout(new BorderLayout());
        RecommendedLift.add(scrollPane, BorderLayout.CENTER);

        createActionListeners(user);
    }

    private void createActionListeners(User user) {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChooseWhichLiftPage chooseWhichLiftPage = new ChooseWhichLiftPage(null, user);
            }
        });
        setVisible(true);
    }

    // Add this workout to workout log
    private JTable setTable(User user, String lift) {
        String[] headers = {"Exercise", "Number of Sets"};
        LiftsForRecommending lifts = new LiftsForRecommending();
        String[][] suggestedLift = lifts.getLift(lift);
        return new JTable(suggestedLift, headers);
    }
}