import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel LoginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(email, password);

                if(user != null){
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password is Invalid",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public User user;
    public User getAuthenticatedUser(String email, String password){
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/my_strore?Timezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection con  = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected to database successfully...

            Statement st = con.createStatement();
            String sql = "SELECT * FROM uses WHERE email=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                user = new User();
                user.name = rs.getString("name");
                user.email = rs.getString("email");
                user.phone = rs.getString("phone");
                user.address = rs.getString("address");
                user.password = rs.getString("password");
            }
            st.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if(user != null){
            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("            Email: " + user.email);
            System.out.println("            Phone: " + user.phone);
            System.out.println("            Address: " + user.address);
        }else{
            System.out.println("Authentication Canceled");
        }
    }
}
