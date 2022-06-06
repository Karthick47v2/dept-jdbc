import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoadFrame extends JFrame {
    private JProgressBar progressBar;

    LoadFrame() {
        super("Loading..."); // window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close when ('x') pressed
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DB.cleanUp();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setSize(480, 640); // window res
        this.setLayout(null); // discard default layout

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(140, 280, 200, 40);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        // add panels and txtfield to frame aka window frame
        this.add(progressBar);
        // this.getContentPane().setBackground(Color.BLACK);
        this.setResizable(false); // disable ability to resize
        this.setVisible(true); // make frame visible to user
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
