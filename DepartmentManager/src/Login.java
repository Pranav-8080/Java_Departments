import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnReset;

    public Login() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel setup
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setFont(fieldFont);
        txtUsername.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(fieldFont);
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        btnLogin = createButton("Login");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(btnLogin, gbc);

        btnReset = createButton("Reset");
        gbc.gridx = 1;
        panel.add(btnReset, gbc);

        btnLogin.addActionListener(this);
        btnReset.addActionListener(this);

        setContentPane(panel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String uname = txtUsername.getText().trim();
            String pwd = new String(txtPassword.getPassword()).trim();

            if (uname.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/vamsi/Desktop/Apps/javaapp.db");
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE uname=? AND pwd=?")) {

                stmt.setString(1, uname);
                stmt.setString(2, pwd);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful");
                    dispose(); // close login window
                    new SyntaxSquad_departments(); // open main UI
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else if (e.getSource() == btnReset) {
            txtUsername.setText("");
            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
