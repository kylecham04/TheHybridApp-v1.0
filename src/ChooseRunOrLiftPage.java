/**
 * Author - Kyle Chambers
 * File name - ChooseRunOrLift
 * Date of last update - 6/29/2024
 * ChooseRunOrLift file description - Creates the page used to choose between adding a run
 * or lift for today's workouts
 * Updates to come - I am going to add a more selective UI and add more options
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseRunOrLiftPage extends JDialog{
    private JButton addRunButton;
    private JButton addLiftButton;
    private JButton returnToHomePageButton;
    private JPanel chooseRunOrLiftPage;

    /**
     * ChooseRunOrLiftPage main method
     * Description - Creates the page for the user to either enter a run or lift from today
     * @param parent - JFrame used to center the page
     * @param user - the user running the program
     */
    public ChooseRunOrLiftPage(JFrame parent, User user){
        super(parent);
        setTitle("Choose Run or Lift");
        setContentPane(chooseRunOrLiftPage);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createRunOrLiftButtons(user);
    }

    /**
     * createRunOrLiftButtons
     * Description - creates the reactive buttons for choosing a run, lift, or going back
     * @param user - the user running the program
     */
    private void createRunOrLiftButtons(User user) {
        returnToHomePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });

        addRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddRun addRun = new AddRun(null, user);
            }
        });

        addLiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddTodaysLift addTodaysLift = new AddTodaysLift(null, user);
            }
        });
        setVisible(true);
    }
}
