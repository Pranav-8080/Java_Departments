import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class SyntaxSquad_departments extends JFrame {
    private static final String URL = "jdbc:sqlite:C:/Users/vamsi/Desktop/Apps/javaapp.db";
    private JTextField txtDeptId, txtDeptCode, txtDeptName, txtDeptLocation, txtDeptEmail, txtSchId;
    private JTextArea txtDisplay;
    public SyntaxSquad_departments() {
        setTitle("Department Manager");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setBackground(new Color(245, 245, 245));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Department Form"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        int y = 0;
        formPanel.add(createLabel("Department ID:", labelFont), gbc(0, y));
        txtDeptId = createTextField(fieldFont); formPanel.add(txtDeptId, gbc(1, y++));

        formPanel.add(createLabel("Dept Code:", labelFont), gbc(0, y));
        txtDeptCode = createTextField(fieldFont); formPanel.add(txtDeptCode, gbc(1, y++));

        formPanel.add(createLabel("Dept Name:", labelFont), gbc(0, y));
        txtDeptName = createTextField(fieldFont); formPanel.add(txtDeptName, gbc(1, y++));

        formPanel.add(createLabel("Location:", labelFont), gbc(0, y));
        txtDeptLocation = createTextField(fieldFont); formPanel.add(txtDeptLocation, gbc(1, y++));

        formPanel.add(createLabel("Email:", labelFont), gbc(0, y));
        txtDeptEmail = createTextField(fieldFont); formPanel.add(txtDeptEmail, gbc(1, y++));

        formPanel.add(createLabel("School ID:", labelFont), gbc(0, y));
        txtSchId = createTextField(fieldFont); formPanel.add(txtSchId, gbc(1, y++));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnAdd = createButton("Add");
        JButton btnRetrieve = createButton("Retrieve");
        JButton btnUpdate = createButton("Update");
        JButton btnDelete = createButton("Delete");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRetrieve);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        txtDisplay = new JTextArea(15, 60);
        txtDisplay.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtDisplay);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Department Records"));

        content.add(formPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);
        content.add(scrollPane, BorderLayout.SOUTH);
        setContentPane(content);

        btnAdd.addActionListener(e -> AP22110010337_departments_create());
        btnRetrieve.addActionListener(e -> AP22110010337_departments_retrive());
        btnUpdate.addActionListener(e -> AP22110010337_departments_update());
        btnDelete.addActionListener(e -> AP22110010337_departments_delete());

        setVisible(true);
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField tf = new JTextField(20);
        tf.setFont(font);
        tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return tf;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(130, 40));
        return btn;
    }

    private Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection Error: " + e.getMessage());
            return null;
        }
    }

    private void clearFields() {
        txtDeptId.setText("");
        txtDeptCode.setText("");
        txtDeptName.setText("");
        txtDeptLocation.setText("");
        txtDeptEmail.setText("");
        txtSchId.setText("");
    }

    private boolean validateIdField() {
        if (txtDeptId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Department ID.");
            return false;
        }
        try {
            Integer.parseInt(txtDeptId.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Department ID must be numeric.");
            return false;
        }
    }

    private void AP22110010337_departments_create() {
        String sql = "INSERT INTO departments (dept_code, dept_name, dept_location, dept_email, sch_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtDeptCode.getText());
            ps.setString(2, txtDeptName.getText());
            ps.setString(3, txtDeptLocation.getText());
            ps.setString(4, txtDeptEmail.getText());
            ps.setInt(5, Integer.parseInt(txtSchId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Added successfully!");
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Insert Error: " + e.getMessage());
        }
    }

    private void AP22110010337_departments_retrive() {
        String sql = "SELECT * FROM departments";
        txtDisplay.setText("");
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                txtDisplay.append("ID: " + rs.getInt("ID") +
                        " | Code: " + rs.getString("dept_code") +
                        " | Name: " + rs.getString("dept_name") +
                        " | Location: " + rs.getString("dept_location") +
                        " | Email: " + rs.getString("dept_email") +
                        " | School ID: " + rs.getInt("sch_id") + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fetch Error: " + e.getMessage());
        }
    }

    private void AP22110010337_departments_update() {
        if (!validateIdField()) return;
        String sql = "UPDATE departments SET dept_code=?, dept_name=?, dept_location=?, dept_email=?, sch_id=? WHERE ID=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtDeptCode.getText());
            ps.setString(2, txtDeptName.getText());
            ps.setString(3, txtDeptLocation.getText());
            ps.setString(4, txtDeptEmail.getText());
            ps.setInt(5, Integer.parseInt(txtSchId.getText()));
            ps.setInt(6, Integer.parseInt(txtDeptId.getText()));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No matching record found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }

    private void AP22110010337_departments_delete() {
        if (!validateIdField()) return;
        String sql = "DELETE FROM departments WHERE ID=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtDeptId.getText()));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Deleted successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No matching record found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SyntaxSquad_departments::new);
    }
}
