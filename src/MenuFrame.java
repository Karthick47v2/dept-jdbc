import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class MenuFrame extends JFrame implements ActionListener {
    private JPanel menuPanel;
    private JButton shwBtn;
    private JButton insertBtn;
    private JButton updateBtn;
    private JButton deleteBtn;

    MenuFrame() {
        super("Menu"); // window title
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

        shwBtn = new JButton("Show Table");
        shwBtn.addActionListener(this);
        insertBtn = new JButton("Insert");
        insertBtn.addActionListener(this);
        updateBtn = new JButton("Update");
        updateBtn.addActionListener(this);
        deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(this);

        menuPanel = new JPanel();
        menuPanel.setBounds(5, 140, 455, 430);
        menuPanel.setLayout(new GridLayout(5, 5, 10, 10));

        menuPanel.add(shwBtn);
        menuPanel.add(insertBtn);
        menuPanel.add(updateBtn);
        menuPanel.add(deleteBtn);

        this.add(menuPanel);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shwBtn) {
            new TableFrame();
        } else if (e.getSource() == insertBtn) {
            new InsertFrame();
        } else if (e.getSource() == updateBtn) {
            new UpdateFrame();
        } else {
            new DeleteFrame();
        }
        this.dispose();
    }
}