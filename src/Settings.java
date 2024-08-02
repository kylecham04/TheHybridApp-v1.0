/**
 * Author - Kyle Chambers
 * File name - Settings
 * Date of last update - 6/29/2024
 * Settings file description - Creates the settings main page and all action buttons. Those being
 * sign out, delete user, and back to home page.
 * Updates to come - I am going to make the user interface more appealing, and add a way to reset
 * both the username and password
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JDialog {
    private JButton signOutButton;
    private JButton deleteAccountButton;
    private JButton backButton;
    private JPanel theHybridAppSettings;

    /**
     * Settings main method
     * Description - creates the settings window and corresponding buttons
     *
     * @param parent - JFrame used in order to center the settings page
     * @param user   - the current user of the program
     */
    public Settings(JFrame parent, User user) {
        super(parent);
        setTitle("Settings");
        setContentPane(theHybridAppSettings);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createButtons(user);
    }

    /**
     * createButtons
     * Description - creates the signout, delete account, and back buttons and listeners
     *
     * @param user - user representing the current user of the program
     */
    private void createButtons(User user) {
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLogIn userLogIn = new UserLogIn(null);
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ConfirmDelete confirmDelete = new ConfirmDelete(null, user);
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HomePage homePage = new HomePage(null, user);
            }
        });
        setVisible(true);
    }
}
