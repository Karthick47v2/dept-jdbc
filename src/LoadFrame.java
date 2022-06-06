import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoadFrame extends JFrame {
    private JProgressBar progressBar;

    LoadFrame() {
        super("Loading...");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Dept.cleanUp();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setSize(480, 640);
        this.setLayout(null);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(140, 280, 200, 40);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        this.add(progressBar);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void updateProgress(String str, int val) {
        progressBar.setString(str);
        progressBar.setValue(val);

        if (val == 100) {
            updateFrame();
        }
    }

    private void updateFrame() {
        new MenuFrame();
        this.dispose();
    }
}
