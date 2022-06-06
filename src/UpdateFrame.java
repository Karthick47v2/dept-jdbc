import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateFrame extends JFrame implements ActionListener {
    private JPanel updatePanel;
    private JPanel btnPanel;
    private JButton deptBtn;
    private JButton locBtn;
    private JButton bkBtn;
    private JButton confBtn;

    private ArrayList<JTextField> setTxtFields = new ArrayList<>();
    private ArrayList<JTextField> whereTxtFields = new ArrayList<>();
    private ArrayList<JLabel> setTxt = new ArrayList<>();
    private ArrayList<JLabel> whereTxt = new ArrayList<>();

    private String tableName = "";

    UpdateFrame() {
        super("Update");
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

        updatePanel = new JPanel();

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

        JLabel info = new JLabel("<html><p>Replaces the value provided in left side" +
                " for all matching entries for right side</p></html>");
        info.setBounds(100, 20, 300, 40);

        btnPanel.add(deptBtn);
        btnPanel.add(locBtn);
        btnPanel.add(bkBtn);
        btnPanel.add(confBtn);

        this.add(updatePanel);
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
                update();
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

    private void update() throws SQLException {
        Map<String, String> filteredSET = new HashMap<>();
        Map<String, String> filteredWHERE = new HashMap<>();

        int i = 0;
        for (JTextField txt : setTxtFields) {
            if (!txt.getText().isEmpty()) {
                filteredSET.put(setTxt.get(i).getText(), txt.getText());
            }
            i++;
            txt.setText("");
        }

        i = 0;
        for (JTextField txt : whereTxtFields) {
            if (!txt.getText().isEmpty()) {
                filteredWHERE.put(whereTxt.get(i).getText(), txt.getText());
            }
            i++;
            txt.setText("");
        }

        Dept.updateValue(tableName, filteredSET, filteredWHERE);
    }

    private void generateForm(String table) {
        updatePanel.removeAll();
        setTxtFields.clear();
        whereTxtFields.clear();

        tableName = table;
        try {
            Map<String, Integer> colInfo = Dept.getColInfo(table);

            updatePanel.setBounds(20, 80, 440, (colInfo.size() + 1) * 60);
            updatePanel.setLayout(new GridLayout(colInfo.size() * 2 + 1, 2, 20, 10));

            updatePanel.add(new JLabel("SET:"));
            updatePanel.add(new JLabel("WHERE:"));

            int i = 0;
            for (Map.Entry<String, Integer> entry : colInfo.entrySet()) {
                setTxtFields.add(new JTextField());
                whereTxtFields.add(new JTextField());

                setTxt.add(new JLabel(entry.getKey()));
                whereTxt.add(new JLabel(entry.getKey()));

                updatePanel.add(setTxt.get(i));
                updatePanel.add(whereTxt.get(i));
                updatePanel.add(setTxtFields.get(i));
                updatePanel.add(whereTxtFields.get(i++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }

}