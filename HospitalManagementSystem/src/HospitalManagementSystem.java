import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HospitalManagementSystem {
    public static void main(String[] args) {
        new LoginUI();
    }
}

class LoginUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        frame = new JFrame("Hospital Management System - Login");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("Username: "));
        usernameField = new JTextField();
        frame.add(usernameField);

        frame.add(new JLabel("Password: "));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        frame.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> System.exit(0));
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + username);
                new DashboardUI();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Connection connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/hospital";
        String user = "root"; // Remplacez par votre nom d'utilisateur
        String password = "admin123"; // Remplacez par votre mot de passe
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to the database.", e);
        }
    }
}

class DashboardUI {
    private JFrame frame;

    public DashboardUI() {
        frame = new JFrame("Hospital Management System - Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2, 10, 10)); // Espacement entre les boutons

        JButton managePatientsButton = new JButton("Manage Patients");
        managePatientsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Manage Patients functionality coming soon!"));
        frame.add(managePatientsButton);

        JButton manageDoctorsButton = new JButton("Manage Doctors");
        manageDoctorsButton.addActionListener(e -> new ManageDoctorsUI()); // Ouvrir la fenêtre de gestion des médecins
        frame.add(manageDoctorsButton);


        JButton manageAppointmentsButton = new JButton("Manage Appointments");
        manageAppointmentsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Manage Appointments functionality coming soon!"));
        frame.add(manageAppointmentsButton);

        JButton reportsButton = new JButton("Generate Reports");
        reportsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Reports functionality coming soon!"));
        frame.add(reportsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginUI(); // Retour à l'écran de connexion
        });
        frame.add(logoutButton);

        frame.setVisible(true);
    }
}


class ManagePatientsUI {
    public ManagePatientsUI() {
        JOptionPane.showMessageDialog(null, "Manage Patients UI coming soon!");
    }
}

class ManageDoctorsUI {
    private JFrame frame;

    public ManageDoctorsUI() {
        frame = new JFrame("Manage Doctors");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fermer cette fenêtre uniquement
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Doctors", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Exemple d'actions
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Doctor Name:"));
        JTextField doctorNameField = new JTextField();
        panel.add(doctorNameField);

        panel.add(new JLabel("Specialty:"));
        JTextField specialtyField = new JTextField();
        panel.add(specialtyField);

        JButton addDoctorButton = new JButton("Add Doctor");
        addDoctorButton.addActionListener(e -> {
            String name = doctorNameField.getText();
            String specialty = specialtyField.getText();
            if (!name.isEmpty() && !specialty.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Doctor added: " + name + " (" + specialty + ")");
                doctorNameField.setText("");
                specialtyField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addDoctorButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}


class ManageAppointmentsUI {
    public ManageAppointmentsUI() {
        JOptionPane.showMessageDialog(null, "Manage Appointments UI coming soon!");
    }
}
