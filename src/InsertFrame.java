import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Map;

public class InsertFrame extends JFrame implements ActionListener {
    private JPanel insertPanel, btnPanel;
    private JButton deptBtn, locBtn, bkBtn, confBtn;

    private ArrayList<JTextField> txtFields = new ArrayList<JTextField>();
    private String tableName = "";

    InsertFrame() {
        super("Insert");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        this.setSize(480, 640);
        this.setLayout(null);

        insertPanel = new JPanel();

        btnPanel = new JPanel();
        btnPanel.setBounds(60, 480, 360, 60);
        btnPanel.setLayout(new GridLayout(2, 2, 10, 10));

        deptBtn = new JButton("DEPARTMENT");
        deptBtn.addActionListener(this);
        locBtn = new JButton("DEPT_LOCATIONS");
        locBtn.addActionListener(this);
        bkBtn = new JButton("Back");
        bkBtn.addActionListener(this);
        confBtn = new JButton("Confirm");
        confBtn.addActionListener(this);

        btnPanel.add(deptBtn);
        btnPanel.add(locBtn);
        btnPanel.add(bkBtn);
        btnPanel.add(confBtn);

        this.add(insertPanel);
        this.add(btnPanel);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bkBtn) {
            new MenuFrame();
            this.dispose();
            return;
        } else if (e.getSource() == confBtn) {
            try {
                DB.insertValue(tableName, txtFields);
                for (JTextField txt : txtFields) {
                    txt.setText("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nex) {
                JOptionPane.showMessageDialog(this, nex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException iex) {
                JOptionPane.showMessageDialog(this, iex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            generateForm(e.getSource() == deptBtn ? "DEPARTMENT" : "DEPT_LOCATIONS");
        }
    }

    private void generateForm(String table) {
        insertPanel.removeAll();
        txtFields.clear();
        tableName = table;
        try {
            Map<String, Integer> colInfo = DB.getColInfo(table);

            insertPanel.setBounds(20, 20, 440, colInfo.size() * 80);
            insertPanel.setLayout(new GridLayout(colInfo.size() * 2, 1, 10, 10));

            int i = 0;
            for (Map.Entry<String, Integer> entry : colInfo.entrySet()) {
                txtFields.add(new JTextField());

                insertPanel.add(new JLabel(entry.getKey()));
                insertPanel.add(txtFields.get(i++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }

}