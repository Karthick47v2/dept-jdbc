import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class MenuFrame extends JFrame implements ActionListener {
    private JPanel menuPanel;
    private JButton shwTable;

    MenuFrame() {
        super("Menu"); // window title
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

        // instantiate btns
        shwTable = new JButton("Show Table");
        shwTable.addActionListener(this);

        // // add to array to add properties easily
        // opBtns[0] = mulBtn;

        // // add props to func bbtns
        // for(int i = 0; i < 1; i++){
        // // opBtns[i].addActionListener(this); // listen to action
        // opBtns[i].setFont(dbFont);
        // opBtns[i].setBackground(Color.GREEN);
        // opBtns[i].setFocusable(false); // to make btn look clean
        // opBtns[i].setBorderPainted(false); // set no painted border
        // }

        // instantiate panels and add btns inside it -- to make clean UI
        menuPanel = new JPanel();
        menuPanel.setBounds(5, 140, 455, 430);
        menuPanel.setLayout(new GridLayout(5, 5, 10, 10));
        menuPanel.setBackground(Color.BLACK);

        menuPanel.add(shwTable);

        // add panels and txtfield to frame aka window frame
        this.add(menuPanel);
        this.getContentPane().setBackground(Color.BLACK);
        this.setResizable(false); // disable ability to resize
        this.setVisible(true); // make frame visible to user
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shwTable) {
            new TableFrame();
            this.dispose();
        }
    }
}