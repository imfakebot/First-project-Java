package com.atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame implements handleImageResizing {
    JFrame frame;

    private static final String IMAGE_PATH = "src\\main\\java\\com\\atm\\hdghkdfd.png";

    private final JTextField userNameFieldInLogin;
    private final JPasswordField passwordFieldInlogin;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JTextField securityQuestionField;
    private final JTextField answerField;
    private final JTextField fullNameField;
    private final JTextField phoneNumberField;
    private final JTextField userNameFieldInSignup;
    private final JPasswordField passwordFieldInSignup;
    private final JPasswordField confirmPasswordField;

    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JRadioButton otherButton;
    private RoundedButton loginButton;
    private RoundedButton signUpButton;

    private JComboBox<String> cbDay;
    private JComboBox<String> cbMonth;
    private JComboBox<String> cbYear;

    private final String[] days = new String[31];
    private final String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
    private final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    private final String[] years = new String[101];

    public LoginFrame() {
        userNameFieldInLogin = new JTextField();
        passwordFieldInlogin = new JPasswordField();
        fullNameField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();
        securityQuestionField = new JTextField();
        answerField = new JTextField();
        phoneNumberField = new JTextField();
        userNameFieldInSignup = new JTextField();
        passwordFieldInSignup = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        frame = createFrame("LOGIN", 900, 600);

        JPanel leftPanel = createLeftPanel();

        JPanel rightPanel = createRightPanel();

        frame.add(leftPanel);
        frame.add(rightPanel);

        initialize(frame, rightPanel);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    /**
     * Initializes the frame and right panel.
     *
     * @param frame      The JFrame to be initialized.
     * @param rightPanel The JPanel to be initialized.
     */
    private void initialize(JFrame frame, JPanel rightPanel) {
        resizeIMG(frame, rightPanel);
    }

    /**
     * Creates and initializes a new JFrame with the given title, width, and height.
     *
     * @param title  The title of the frame.
     * @param width  The width of the frame.
     * @param height The height of the frame.
     *
     * @return The newly created and initialized JFrame.
     */
    private JFrame createFrame(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setLayout(new GridLayout(1, 2));
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("src\\main\\java\\com\\atm\\icon-removebg-preview.png")
                .getImage());
        return frame;
    }

    /**
     * Creates and initializes the left panel of the frame, which contains
     * the login and signup tabs with their respective input fields and buttons.
     *
     * @return The newly created and initialized JPanel for the left side of the
     *         frame.
     */
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        JLabel loginTitleLabel = createLabel("LOGIN", new Font("MV Boli", Font.BOLD, 38), new Color(55, 72, 189));
        loginPanel.add(loginTitleLabel, BorderLayout.NORTH);

        JPanel bothPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bothPanel.add(createInputPanel("Username ", userNameFieldInLogin), gbc);

        gbc.gridy++;
        bothPanel.add(createInputPanel("Password  ", passwordFieldInlogin), gbc);

        loginButton = new RoundedButton("Login", 55, 72, 189);
        loginButton.setActionCommand("Login");
        loginButton.setToolTipText("Login");
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(350, 40));
        loginButton.addActionListener(new ButtonHandler(this));
        frame.getRootPane().setDefaultButton(loginButton);

        gbc.gridy++;
        bothPanel.add(loginButton, gbc);

        JLabel forgotLabel = createForgotPasswordLabel();
        gbc.gridy++;
        bothPanel.add(forgotLabel, gbc);

        loginPanel.add(bothPanel, BorderLayout.CENTER);

        JPanel signupPanel = new JPanel(new BorderLayout());

        JLabel signupTitleLabel = createLabel("SIGNUP", new Font("MV Boli", Font.BOLD, 38), new Color(55, 72, 189));
        signupPanel.add(signupTitleLabel, BorderLayout.NORTH);

        JPanel signupFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 0, 5, 0);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;

        signupFieldsPanel.add(createInputPanel("New Username     ", userNameFieldInSignup), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("New Password      ", passwordFieldInSignup), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Confirm Password ", confirmPasswordField), gbc2);
        gbc2.gridy++;

        signupFieldsPanel.add(createInputPanel("Full Name             ", fullNameField), gbc2);
        gbc2.gridy++;

        JPanel genderPanel = new JPanel(new GridLayout(1, 4));
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);

        genderPanel.add(genderLabel);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        genderPanel.add(otherButton);

        signupFieldsPanel.add(genderPanel, gbc2);
        gbc2.gridy++;

        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }
        cbDay = new JComboBox<>(days);
        cbMonth = new JComboBox<>(months);
        for (int i = 0; i <= 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        cbYear = new JComboBox<>(years);
        cbYear = new JComboBox<>(years);
        for (int i = 0; i <= 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        cbYear = new JComboBox<>(years);

        signupFieldsPanel.add(createDOBPanel("DOB", cbDay, cbMonth, cbYear), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Phone number       ", phoneNumberField), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Email                     ", emailField), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Address                 ", addressField), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Security Question  ", securityQuestionField), gbc2);
        gbc2.gridy++;
        signupFieldsPanel.add(createInputPanel("Answer                  ", answerField), gbc2);
        gbc2.gridy++;

        signUpButton = new RoundedButton("Signup", 55, 72, 189);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setPreferredSize(new Dimension(350, 40));
        signUpButton.setActionCommand("Signup");
        signUpButton.addActionListener(new ButtonHandler(this));

        signupFieldsPanel.add(signUpButton, gbc2);

        signupPanel.add(signupFieldsPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Signup", signupPanel);

        tabbedPane.addChangeListener(_ -> {

            if (tabbedPane.getSelectedIndex() == 0) {
                userNameFieldInLogin.setText(null);
                passwordFieldInlogin.setText("");

                frame.getRootPane().setDefaultButton(loginButton);
            } else {
                for (int i = 0; i < signupFieldsPanel.getComponentCount(); i++) {
                    if (signupFieldsPanel.getComponent(i) instanceof JPanel inputPanel) {
                        for (var comp : inputPanel.getComponents()) {
                            switch (comp) {
                                case JTextField textField -> textField.setText(null);
                                case JComboBox<?> comboBox -> comboBox.setSelectedIndex(0);
                                case JCheckBox checkBox -> checkBox.setSelected(false);
                                default -> {
                                }
                            }
                        }
                    }
                }

                frame.getRootPane().setDefaultButton(signUpButton);
            }
        });

        leftPanel.add(tabbedPane, BorderLayout.CENTER);
        return leftPanel;
    }

    /**
     * Creates and initializes a new JPanel for the right side of the frame.
     *
     * @return The newly created and initialized JPanel.
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(new ImageIcon(IMAGE_PATH));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        rightPanel.add(imageLabel, BorderLayout.CENTER);
        rightPanel.putClientProperty("imageLabel", imageLabel);
        return rightPanel;
    }

    /**
     * Creates and initializes a new JLabel with the given text, font, and color.
     *
     * @param text  The text to be displayed on the JLabel.
     * @param font  The font to be used for the text.
     * @param color The color to be used for the text.
     *
     * @return The newly created and initialized JLabel.
     */
    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    /**
     * Creates and initializes a new JPanel for input fields.
     *
     * @param labelText  The text to be displayed on the JLabel.
     * @param inputField The JTextField to be added to the panel.
     *
     * @return The newly created and initialized JPanel.
     */
    private JPanel createInputPanel(String labelText, JTextField inputField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputField.setPreferredSize(new Dimension(200, 18));

        panel.add(label);
        panel.add(inputField);

        if (inputField instanceof JPasswordField jPasswordField) {
            JButton hideOrShowPassword = new JButton();
            hideOrShowPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
            hideOrShowPassword.setToolTipText("Hide/Show Password");
            ImageIcon hideIcon = new ButtonHandler(this).scaleImage(
                    new ImageIcon("src/main/java/com/atm/hidePassword.png"),
                    18, 18);
            ImageIcon showIcon = new ButtonHandler(this).scaleImage(
                    new ImageIcon("src/main/java/com/atm/showPassword.png"),
                    18, 18);
            hideOrShowPassword.setIcon(hideIcon);
            hideOrShowPassword.setPreferredSize(new Dimension(18, 18));
            hideOrShowPassword.setBorder(BorderFactory.createEmptyBorder());
            hideOrShowPassword.setContentAreaFilled(false);

            hideOrShowPassword.addActionListener(_ -> {
                if (jPasswordField.getEchoChar() == '\u2022') {
                    jPasswordField.setEchoChar((char) 0);
                    hideOrShowPassword.setIcon(showIcon);
                } else {
                    jPasswordField.setEchoChar('\u2022');
                    hideOrShowPassword.setIcon(hideIcon);
                }
            });

            panel.add(hideOrShowPassword);
        }
        return panel;
    }

    /**
     * Creates and initializes a JLabel for the "Forgot username or password?" link.
     * This label is styled with a specific font, color, and cursor, and it adds
     * a mouse listener to handle click events for password recovery.
     *
     * @return A JLabel configured as a clickable link for password recovery.
     */
    private JLabel createForgotPasswordLabel() {
        JLabel forgotPassWordLabel = new JLabel("Forgot username or password? Click here");
        forgotPassWordLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        forgotPassWordLabel.setForeground(new Color(232, 19, 118));
        forgotPassWordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        forgotPassWordLabel.addMouseListener(new ForgotPassword(this));
        return forgotPassWordLabel;
    }

    /**
     * Creates and initializes a JPanel for the date of birth (DOB) selection.
     *
     * @param labelText The text to be displayed on the JLabel.
     * @param cbDay     The JComboBox for selecting the day of the DOB.
     * @param cbMonth   The JComboBox for selecting the month of the DOB.
     * @param cbYear    The JComboBox for selecting the year of the DOB.
     *
     * @return The newly created and initialized JPanel.
     */
    private JPanel createDOBPanel(String labelText, JComboBox<String> cbDay, JComboBox<String> cbMonth,
            JComboBox<String> cbYear) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
        cbDay.setPreferredSize(new Dimension(60, 30));
        cbMonth.setPreferredSize(new Dimension(60, 30));
        cbYear.setPreferredSize(new Dimension(80, 30));

        comboBoxPanel.add(cbDay);
        comboBoxPanel.add(cbMonth);
        comboBoxPanel.add(cbYear);

        panel.add(label, BorderLayout.WEST);
        panel.add(comboBoxPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Retrieves the username entered in the login username field.
     *
     * @return A JTextField representing the username entered in the login username
     *         field.
     *         The returned JTextField contains the username characters in the same
     *         order
     *         as they were entered by the user.
     */
    public JTextField getUserNameFieldInLogin() {
        return userNameFieldInLogin;
    }

    /**
     * Retrieves the username entered in the signup username field.
     *
     * @return A String representing the username entered in the signup username
     *         field.
     *         The returned String contains the username characters in the same
     *         order
     *         as they were entered by the user.
     */
    public String getUserNameFieldInSignup() {
        return userNameFieldInSignup.getText();
    }

    /**
     * Retrieves the password entered in the password field during the signup
     * process.
     *
     * @return A character array representing the password entered in the password
     *         field.
     *         The returned array contains the password characters in the same order
     *         as they were entered by the user.
     */
    public char[] getPasswordInSignUp() {
        return passwordFieldInSignup.getPassword();
    }

    /**
     * Retrieves the password entered in the password field during the login
     * process.
     *
     * @return A JTextField representing the password entered in the password field.
     *         The returned JTextField contains the password characters in the same
     *         order
     *         as they were entered by the user.
     */
    public JTextField getPaswordFieldInLogin() {
        return passwordFieldInlogin;
    }

    /**
     * Retrieves the full name entered in the full name field.
     *
     * @return A String representing the full name entered in the full name field.
     *         The returned String contains the full name characters in the same
     *         order as they were entered by the user.
     */
    public String getFullNameField() {
        return fullNameField.getText();
    }

    /**
     * Closes the login frame by disposing of the frame.
     * This method should be called when the user is done with the login process.
     *
     * @return void
     */
    public void closeLoginFrame() {
        frame.dispose();
    }

    /**
     * Retrieves the email entered in the email field.
     *
     * @return A JTextField representing the email entered in the email field.
     *         The returned JTextField contains the email characters in the same
     *         order as they were entered by the user.
     */
    public JTextField getemailField() {
        return emailField;
    }

    /**
     * Retrieves the address entered in the address field.
     *
     * @return A JTextField representing the address entered in the address field.
     *         The returned JTextField contains the address characters in the same
     *         order as they were entered by the user.
     */
    public JTextField getAddressField() {
        return addressField;
    }

    /**
     * Retrieves the security question entered in the security question field.
     *
     * @return A JTextField representing the security question entered in the
     *         security
     *         question field. The returned JTextField contains the security
     *         question
     *         characters in the same order as they were entered by the user.
     */
    public JTextField getSecurityQuestionField() {
        return securityQuestionField;
    }

    /**
     * Retrieves the answer entered in the answer field.
     *
     * @return A JTextField representing the answer entered in the answer field.
     *         The returned JTextField contains the answer characters in the same
     *         order as they were entered by the user.
     */
    public JTextField getAnswerField() {
        return answerField;
    }

    /**
     * Retrieves the selected gender from the radio buttons.
     *
     * @return A character representing the selected gender.
     *         Returns 'M' if the male button is selected, 'F' if the female button
     *         is selected,
     *         'O' if the other button is selected, and 'N' if no button is
     *         selected.
     */
    public char getGender() {
        if (maleButton.isSelected()) {
            return 'M';
        }

        if (femaleButton.isSelected()) {
            return 'F';
        }

        if (otherButton.isSelected()) {
            return 'O';
        }

        return 'N';
    }

    /**
     * Checks if a gender has been selected by the user.
     *
     * @return A boolean value indicating whether a gender has been selected.
     *         Returns true if a gender (male, female, or other) is selected;
     *         otherwise, returns false.
     */
    public boolean isGenderSelected() {
        return maleButton.isSelected() || femaleButton.isSelected() || otherButton.isSelected();
    }

    /**
     * Retrieves the date of birth (DOB) selected by the user from the combo boxes.
     * The DOB is formatted as a string in the format "YYYY-MM-DD".
     *
     * @return A string representing the selected DOB.
     *         The returned string contains the DOB in the format "YYYY-MM-DD".
     */
    public String getDOB() {
        String DOB = (String) cbYear.getSelectedItem() + "-" + cbMonth.getSelectedItem() + "-"
                + cbDay.getSelectedItem();
        return DOB;
    }

    /**
     * Retrieves the password entered in the confirm password field.
     *
     * @return A character array representing the password entered in the confirm
     *         password field.
     *         The returned array contains the password characters in the same order
     *         as they
     *         were entered by the user.
     */
    public char[] getConfirmPasswordField() {
        return confirmPasswordField.getPassword();
    }

    /**
     * Retrieves the phone number entered in the phone number field.
     *
     * @return A String representing the phone number entered in the phone number
     *         field.
     *         The returned String contains the phone number characters in the same
     *         order
     *         as they were entered by the user.
     */
    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    /**
     * Resizes the image displayed in the given panel to fit the dimensions of the
     * frame.
     *
     * @param frame The JFrame containing the panel.
     * @param Panel The JPanel containing the image to be resized.
     */
    @Override
    public void resizeIMG(JFrame frame, JPanel Panel) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel imageLabel = (JLabel) Panel.getClientProperty("imageLabel");
                if (imageLabel != null) {
                    int width = frame.getWidth();
                    int height = frame.getHeight();
                    imageLabel.setIcon(
                            new ButtonHandler(LoginFrame.this).scaleImage(new ImageIcon(IMAGE_PATH), width, height));
                }
            }
        });
    }
}