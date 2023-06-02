import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DashBoardForm extends JFrame{
    private JPanel DashBoardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashBoardForm(){
        setTitle("Dashboard");
        setContentPane(DashBoardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();
        if(hasRegisteredUsers){
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if(user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else{
                dispose();
            }
        }else{
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if(user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else{
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashBoardForm.this);
                User user = registrationForm.user;

                if(user != null){
                    JOptionPane.showMessageDialog(DashBoardForm.this,
                            "New User: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    private boolean connectToDatabase(){
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/my_strore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection con  = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected to database successfully...

            Statement st = con.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS my_strore");
            st.close();
            con.close();

            con  = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            st = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS uses(" +
                    "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(200) NOT NULL, " +
                    "email VARCHAR(200) NOT NULL UNIQUE, " +
                    "phone VARCHAR(200), " +
                    "address VARCHAR(200), " +
                    "password VARCHAR(200) NOT NULL)";
            st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM uses");
            if(rs.next()){
                int numUsers = rs.getInt(1);
                if(numUsers > 0){
                    hasRegisteredUsers = true;
                }
            }
            st.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return hasRegisteredUsers;

    }

    public static void main(String[] args) {
        DashBoardForm myForm = new DashBoardForm();
    }
}
