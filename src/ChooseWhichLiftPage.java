import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseWhichLiftPage extends JDialog {
    private JButton chestTricepsButton;
    private JButton backBicepsButton;
    private JButton legsButton;
    private JButton shouldersArmsButton;
    private JButton backToAddLiftButton;
    private JPanel chooseRecommendedLift;

    public ChooseWhichLiftPage(JFrame parent, User user) {
        super(parent);
        setTitle("Choose Recommended Lift");
        setContentPane(chooseRecommendedLift);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createActionListeners(user);
    }

    private void createActionListeners(User user) {
        backToAddLiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage(null, user);
            }
        });

        chestTricepsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RecommendedLiftsForUser(null, user, "Chest");
            }
        });

        backBicepsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RecommendedLiftsForUser(null, user, "Back");
            }
        });

        legsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RecommendedLiftsForUser(null, user, "Legs");
            }
        });

        shouldersArmsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RecommendedLiftsForUser(null, user, "Shoulders");
            }
        });

        setVisible(true);
    }
}