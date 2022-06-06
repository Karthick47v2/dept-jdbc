import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.SQLException;
import java.util.Arrays;

public class TableFrame extends JFrame implements ActionListener {
    private JPanel tablePanel, btnPanel;
    private JTable table;
    private JButton dept, loc, bk;

    TableFrame() {
        super("Tables"); // window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close when ('x') pressed
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                DB.cleanUp();
                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        });
        this.setSize(480, 640); // window res
        this.setLayout(null); // discard default layout

        tablePanel = new JPanel();
        tablePanel.setBounds(0, 20, 480, 400);

        btnPanel = new JPanel();
        btnPanel.setBounds(60, 480, 360, 40);
        btnPanel.setLayout(new GridLayout(1, 2, 10, 10));

        // instantiate btns
        dept = new JButton("DEPARTMENT");
        dept.addActionListener(this);
        loc = new JButton("DEPT_LOCATIONS");
        loc.addActionListener(this);
        bk = new JButton("Back");
        bk.addActionListener(this);
        bk.setBounds(200, 540, 80, 40);

        btnPanel.add(dept);
        btnPanel.add(loc);

        // add panels and txtfield to frame aka window frame
        this.add(tablePanel);
        this.add(btnPanel);
        this.add(bk);
        this.setResizable(false); // disable ability to resize
        this.setVisible(true); // make frame visible to user
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bk) {
            new MenuFrame();
            this.dispose();
            return;
        }
        generateTable(e.getSource() == dept ? "DEPARTMENT" : "DEPT_LOCATIONS");

    }

    private void generateTable(String tblName) {
        tablePanel.removeAll();
        try {
            String[][] result = DB.showTable(tblName);
            int col = result[0].length;
            int row = result.length;
            String[] names = new String[col];
            String[][] values = new String[row - 1][col];

            for (int i = 0; i < row; i++) {
                if (i == 0) {
                    names = Arrays.copyOfRange(result[i], 0, col);
                    continue;
                }
                values[i - 1] = Arrays.copyOfRange(result[i], 0, col);
            }

            table = new JTable(values, names);

            JScrollPane scrollPane = new JScrollPane(table);
            tablePanel.add(scrollPane);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }
}