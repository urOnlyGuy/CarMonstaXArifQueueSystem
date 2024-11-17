/**
 * 
 * @author Arif (AKA Jai)
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class Login extends JFrame implements ActionListener {
    // Declare components
    private JPanel panelLog, panelBot, panelTop;
    private JLabel lblUsername, lblPass, lblTop,lblTopSub, lblBot, lblEmpty, lblEmpty2;
    private JTextField txtUsername;
    private JPasswordField passPassword;
    private JButton btnLogin;
    private BackgroundPanel panelWithBg;
    private JCheckBox checkboxShowPass = new JCheckBox("Show");

    // Setup GUI
    public Login() {
        // Setup text field and password field
        txtUsername = new JTextField(15);
        passPassword = new JPasswordField(15);

        // Setup labels
        lblUsername = new JLabel("Username :");
        lblPass = new JLabel("Password :");
        lblEmpty = new JLabel("");
        lblEmpty2 = new JLabel("");
        lblBot = new JLabel("Developed by Jai (Arif), in collaboration with CarMonsta");
        lblTop = new JLabel("Welcome to CxAQS", SwingConstants.CENTER);
        lblTopSub = new JLabel ("-CarMonsta x Arif Queue System-", SwingConstants.CENTER);

        // Setup button
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);

        // Instantiation custom panel
        panelWithBg = new BackgroundPanel("bg-pexels.jpg");
        panelWithBg.setLayout(new BorderLayout());

        // Setup login panel
        panelLog = new JPanel(){ //transparent panel to fix visual checkbox bug
            protected void paintComponent(Graphics g)
            {
                g.setColor( getBackground() );
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        panelLog.setOpaque(false);
        panelLog.setBackground(new Color(255,255,255,140)); // Color for panel
        panelLog.setLayout(new GridLayout(4, 2, 5, 5)); //grid 3 row, 2 column with 5 gap
        panelLog.setBorder(BorderFactory.createEmptyBorder(80, 50, 50, 50)); // Add padding inside the login panel
        panelLog.setBorder(new TitledBorder(new EtchedBorder(), "Login", TitledBorder.CENTER, TitledBorder.TOP));

        checkboxShowPass.setOpaque(false);
        checkboxShowPass.addActionListener(this);
        // Add components to login panel
        panelLog.add(lblUsername);
        panelLog.add(txtUsername);//
        panelLog.add(lblPass);
        panelLog.add(passPassword);//
        panelLog.add(lblEmpty2);
        panelLog.add(checkboxShowPass);
        panelLog.add(lblEmpty);
        panelLog.add(btnLogin);

        //setup top panel
        panelTop = new JPanel();
        panelTop.setLayout(new BorderLayout());
        panelTop.setOpaque(false);
        panelTop.add(lblTop, BorderLayout.CENTER);
        panelTop.add(lblTopSub, BorderLayout.SOUTH);

        //customize lblTop & lblTopSub font
        lblTop.setFont(new Font("Gill Sans MT", Font.PLAIN, 30));        
        lblTop.setForeground(Color.white);
        lblTopSub.setFont(new Font("Gill Sans MT", Font.BOLD, 32));
        lblTopSub.setForeground(Color.white);

        //setup bottom panel
        panelBot = new JPanel();
        panelBot.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelBot.setOpaque(false);
        panelBot.add(lblBot);

        // Create empty panels to create margins
        JPanel north = new JPanel();
        north.setOpaque(false);
        JPanel east = new JPanel();
        east.setOpaque(false);
        JPanel west = new JPanel();
        west.setOpaque(false);

        // Set empty space for empty panels
        panelTop.setBorder(BorderFactory.createEmptyBorder(165, 0, 100, 0));
        west.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 0));
        east.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 0));

        // Add components to panelWithBg
        panelWithBg.add(panelTop, BorderLayout.NORTH);
        panelWithBg.add(east, BorderLayout.EAST);
        panelWithBg.add(west, BorderLayout.WEST);
        panelWithBg.add(panelLog, BorderLayout.CENTER);
        panelWithBg.add(panelBot, BorderLayout.SOUTH);        

        //set properties for the frame
        ImageIcon icon = new ImageIcon("icon.png");  // Instantiation icon      
        setTitle("CxAQS - CarMonsta x Arif Queue System (by Arif x CarMonsta)");
        setIconImage(icon.getImage()); // Set icon for frame
        setVisible(true);
        setSize(700, 520);
        setLocationRelativeTo(null); // Display frame at the center of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add panelWithBg to the frame
        add(panelWithBg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle button click event
        if (e.getSource() == btnLogin) {
            // placeholder action
            System.out.println("Login button clicked!");
            {   String userField, passField;
                userField = txtUsername.getText();
                passField = passPassword.getText();
                if (userField.equalsIgnoreCase("admin") && passField.equals("123"))
                {
                    this.dispose(); //close this window
                    new MainDashboard();  //launch new window from another class

                    //JOptionPane.showMessageDialog(this, "Login Successful");
                    System.out.println("Logged in");

                } else
                {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password");
                    System.out.println("Wrong login");

                }
            }

        }
        
        if (checkboxShowPass.isSelected()) {
            passPassword.setEchoChar((char) 0); //to show the password
        } else {
            passPassword.setEchoChar('*'); // hide the password
        }
    }

    public static void main (String[]args)
    {
        SwingUtilities.invokeLater(() -> {
                    new Login();
            });
    }
}