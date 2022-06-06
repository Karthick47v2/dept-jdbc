import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeleteFrame extends JFrame implements ActionListener {
    private JPanel deletePanel;
    private JPanel btnPanel;
    private JButton deptBtn;
    private JButton locBtn;
    private JButton bkBtn;
    private JButton confBtn;

    private ArrayList<JTextField> whereTxtFields = new ArrayList<>();
    private ArrayList<JLabel> whereTxt = new ArrayList<>();

    private String tableName = "";

    DeleteFrame() {
        super("Delete");
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

        deletePanel = new JPanel();

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

        JLabel info = new JLabel("Delete all records with values");
        info.setBounds(100, 20, 300, 40);

        btnPanel.add(deptBtn);
        btnPanel.add(locBtn);
        btnPanel.add(bkBtn);
        btnPanel.add(confBtn);

        this.add(deletePanel);
        this.add(btnPanel);
        this.add(info);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bkBtn) {
            new MenuFrame();
            this.dispose();
        } else if (e.getSource() == confBtn) {
            try {
                delete();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "SQL_ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nex) {
                JOptionPane.showMessageDialog(this, nex.toString(), "FORMAT_ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException iex) {
                JOptionPane.showMessageDialog(this, iex.toString(), "ARGUMENT_ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            generateForm(e.getSource() == deptBtn ? "DEPARTMENT" : "DEPT_LOCATIONS");
        }
    }

    private void delete() throws SQLException {
        Map<String, String> filteredWHERE = new HashMap<>();

        int i = 0;
        for (JTextField txt : whereTxtFields) {
            if (!txt.getText().isEmpty()) {
                filteredWHERE.put(whereTxt.get(i).getText(), txt.getText());
            }
            i++;
            txt.setText("");
        }

        Dept.deleteValue(tableName, filteredWHERE);
    }

    private void generateForm(String table) {
        deletePanel.removeAll();
        whereTxtFields.clear();

        tableName = table;
        try {
            Map<String, Integer> colInfo = Dept.getColInfo(table);

            deletePanel.setBounds(20, 80, 440, (colInfo.size() + 1) * 60);
            deletePanel.setLayout(new GridLayout(colInfo.size() * 2, 1, 20, 10));

            int i = 0;
            for (Map.Entry<String, Integer> entry : colInfo.entrySet()) {
                whereTxtFields.add(new JTextField());
                whereTxt.add(new JLabel(entry.getKey()));

                deletePanel.add(whereTxt.get(i));
                deletePanel.add(whereTxtFields.get(i++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }

}