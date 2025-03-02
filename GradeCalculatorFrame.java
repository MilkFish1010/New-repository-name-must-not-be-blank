import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GradeCalculatorFrame extends JFrame {

    public GradeCalculatorFrame() {
        setTitle("Professional Lecture & Lab Grade Calculators");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#3f0f17")); // dark background
        setLayout(new BorderLayout());

        // Create tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Lecture Calculator", new LecturePanel());
        tabbedPane.addTab("Lab Calculator", new LabPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    // Utility method for random integers within range
    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    // Utility method to create a PASS/FAIL stamp (a label with border and color)
    public static JLabel createStamp(double finalScore) {
        JLabel stamp = new JLabel();
        stamp.setFont(new Font("Arial", Font.BOLD, 12));
        stamp.setOpaque(true);
        stamp.setHorizontalAlignment(SwingConstants.CENTER);
        stamp.setPreferredSize(new Dimension(80, 30));
        // Use light background colors with black text
        if (finalScore >= 75) {
            stamp.setText("PASSED");
            stamp.setForeground(Color.BLACK);
            stamp.setBackground(new Color(144, 238, 144)); // light green
            stamp.setBorder(new LineBorder(new Color(144, 238, 144), 4, true));
        } else {
            stamp.setText("FAIL");
            stamp.setForeground(Color.BLACK);
            stamp.setBackground(new Color(240, 128, 128)); // light red
            stamp.setBorder(new LineBorder(new Color(240, 128, 128), 4, true));
        }
        return stamp;
    }

    // ------------------ Lecture Calculator Panel ------------------
    class LecturePanel extends JPanel {
        // Input fields
        private JTextField examField, essayField, pvmField, javaField, jsField, absencesField;
        private JCheckBox showStepsCheck;
        private JPanel stepsPanel;
        private JTextArea errorArea;
        private JButton calculateBtn, randomBtn;
        private JPanel container; // main container
        private JPanel resultPanel; // holds final result (grade, stamp)
        private final Font labelFont = new Font("Helvetica Neue", Font.PLAIN, 14);

        public LecturePanel() {
            setLayout(new BorderLayout());
            setBackground(Color.decode("#3f0f17")); // panel background same as frame

            container = new JPanel();
            container.setBackground(Color.decode("#f1e893"));
            container.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 20, 20),
                    new LineBorder(Color.LIGHT_GRAY, 1, true)));
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            // Title
            JLabel title = new JLabel("Lecture Grade Calculator");
            title.setFont(new Font("Helvetica Neue", Font.BOLD, 20));
            title.setForeground(Color.BLACK);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(title);
            container.add(Box.createRigidArea(new Dimension(0, 15)));

            
            // Random generator button
            randomBtn = new JButton("Random Grade Generator Button");
            randomBtn.setBackground(new Color(111, 66, 193));
            randomBtn.setForeground(Color.BLACK);
            randomBtn.setFocusPainted(false);
            randomBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            randomBtn.addActionListener(e -> setRandomValidLecture());
            container.add(randomBtn);
            container.add(Box.createRigidArea(new Dimension(0, 20)));

             // Exam header
             JLabel examHeader = new JLabel("Exam");
             examHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
             examHeader.setForeground(Color.BLACK);
             examHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
             container.add(examHeader);
             
             
            // Input groups
            examField   = createInputGroup(container, "Exam Score (0 - 100)", 100);
            container.add(Box.createRigidArea(new Dimension(0,10)));
            

            // Lab Works header
            JLabel labHeader = new JLabel("Quizzes");
            labHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
            labHeader.setForeground(Color.BLACK);
            labHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(labHeader);
            
            essayField  = createInputGroup(container, "Essay Score (0 - 100)", 100);
            pvmField    = createInputGroup(container, "PVM Score (0 - 60)", 60);
            javaField   = createInputGroup(container, "Java Score (0 - 40)", 40);
            jsField     = createInputGroup(container, "JavaScript Score (0 - 40)", 40);
            container.add(Box.createRigidArea(new Dimension(0,10)));
            
            JLabel attendanceHeader = new JLabel("Attendance");
            attendanceHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
            attendanceHeader.setForeground(Color.BLACK);
            attendanceHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(attendanceHeader);
            absencesField = createInputGroup(container, "Number of Absences (0+)", 0);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            // Toggle for step-by-step
            showStepsCheck = new JCheckBox("Show Step-by-Step Calculation");
            showStepsCheck.setSelected(true);
            showStepsCheck.setFont(labelFont);
            showStepsCheck.setForeground(Color.BLACK);
            showStepsCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(showStepsCheck);
            container.add(Box.createRigidArea(new Dimension(0, 10)));

            // Calculate button
            calculateBtn = new JButton("Calculate Lecture Grade");
            calculateBtn.setBackground(new Color(40, 167, 69));
            calculateBtn.setForeground(Color.BLACK);
            calculateBtn.setFocusPainted(false);
            calculateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            calculateBtn.addActionListener(e -> calculateLectureGrade());
            container.add(calculateBtn);
            container.add(Box.createRigidArea(new Dimension(0, 10)));

            // Error/Result area wrapped in a center-aligned panel
            errorArea = new JTextArea(3, 40);
            errorArea.setEditable(false);
            errorArea.setLineWrap(true);
            errorArea.setWrapStyleWord(true);
            errorArea.setFont(labelFont);
            errorArea.setForeground(Color.BLACK);
            errorArea.setOpaque(false);
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            errorPanel.setOpaque(false);
            errorPanel.add(errorArea);
            container.add(errorPanel);
            container.add(Box.createRigidArea(new Dimension(0, 10)));

            // Steps panel (initially hidden)
            stepsPanel = new JPanel();
            stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
            stepsPanel.setBackground(Color.decode("#f1e893"));
            stepsPanel.setVisible(false);
            stepsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(stepsPanel);

            // Dedicated result panel for final grade and stamp
            resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            resultPanel.setOpaque(false);
            container.add(resultPanel);

            add(new JScrollPane(container), BorderLayout.CENTER);
        }

        // Helper to create input group with label, text field, and perfect score button
        private JTextField createInputGroup(JPanel parent, String labelText, int perfectValue) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(false);
            JLabel label = new JLabel(labelText);
            label.setFont(labelFont);
            label.setForeground(Color.BLACK);
            panel.add(label);
            JButton perfectBtn = new JButton("Perfect Score Button!");
            perfectBtn.setFont(new Font("Arial", Font.PLAIN, 12));
            perfectBtn.setBackground(new Color(187, 131, 131));
            perfectBtn.setForeground(Color.BLACK);
            perfectBtn.setFocusPainted(false);
            perfectBtn.addActionListener(e -> {
                JTextField sourceField = (JTextField) panel.getClientProperty("field");
                if(sourceField != null) {
                    sourceField.setText(String.valueOf(perfectValue));
                    sourceField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            });
            panel.add(perfectBtn);

            JTextField textField = new JTextField(5);
            textField.setFont(labelFont);
            textField.setForeground(Color.BLACK);
            panel.add(textField);
            panel.putClientProperty("field", textField);
            parent.add(panel);
            parent.add(Box.createRigidArea(new Dimension(0, 10)));
            return textField;
        }

        // Set random valid values (keeping absences low to avoid fail)
        private void setRandomValidLecture() {
            errorArea.setText("");
            resultPanel.removeAll();
            stepsPanel.removeAll();
            stepsPanel.setVisible(false);
            examField.setText(String.valueOf(getRandomInt(0, 100)));
            essayField.setText(String.valueOf(getRandomInt(0, 100)));
            pvmField.setText(String.valueOf(getRandomInt(0, 60)));
            javaField.setText(String.valueOf(getRandomInt(0, 40)));
            jsField.setText(String.valueOf(getRandomInt(0, 40)));
            absencesField.setText(String.valueOf(getRandomInt(0, 3)));
        }

        // Validate input fields; returns error string or empty string if ok
        private String validateLecture() {
            StringBuilder errors = new StringBuilder();
            Object[][] fields = {
                { examField, "Exam Score", 0, 100 },
                { essayField, "Essay Score", 0, 100 },
                { pvmField, "PVM Score", 0, 60 },
                { javaField, "Java Score", 0, 40 },
                { jsField, "JavaScript Score", 0, 40 },
                { absencesField, "Number of Absences", 0, 100 }
            };
            for (Object[] fieldInfo : fields) {
                JTextField field = (JTextField) fieldInfo[0];
                String label = (String) fieldInfo[1];
                int min = (int) fieldInfo[2];
                int max = (int) fieldInfo[3];
                String text = field.getText().trim();
                if(text.isEmpty()) {
                    errors.append(label).append(" is empty.\n");
                    field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                } else {
                    try {
                        double val = Double.parseDouble(text);
                        if(val < min || val > max) {
                            errors.append(label).append(" should be between ").append(min)
                                  .append(" and ").append(max).append(".\n");
                            field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                        } else {
                            field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                        }
                    } catch(NumberFormatException ex) {
                        errors.append(label).append(" is not a valid number.\n");
                        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }
            }
            return errors.toString();
        }

        // Calculation logic with step-by-step reveal
        private void calculateLectureGrade() {
            errorArea.setText("");
            resultPanel.removeAll();
            stepsPanel.removeAll();
            stepsPanel.setVisible(false);

            String errorMsg = validateLecture();
            if(!errorMsg.isEmpty()) {
                errorArea.setText(errorMsg);
                return;
            }

            // Parse inputs
            double exam = Double.parseDouble(examField.getText().trim());
            double essay = Double.parseDouble(essayField.getText().trim());
            double pvm   = Double.parseDouble(pvmField.getText().trim());
            double java  = Double.parseDouble(javaField.getText().trim());
            double js    = Double.parseDouble(jsField.getText().trim());
            int absences = Integer.parseInt(absencesField.getText().trim());

            // Immediate fail if absences >= 4
            if(absences >= 4) {
                errorArea.setText("FAIL: Absences are 4 or more.");
                JPanel stampPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                stampPanel.setOpaque(false);
                stampPanel.add(createStamp(0));
                resultPanel.add(stampPanel);
                container.revalidate();
                container.repaint();
                return;
            }

            // Convert quizzes to 100 scale
            double essayPercent = (essay / 100) * 100;
            double pvmPercent   = (pvm / 60) * 100;
            double javaPercent  = (java / 40) * 100;
            double jsPercent    = (js / 40) * 100;
            double sumOfQuizzes = essayPercent + pvmPercent + javaPercent + jsPercent;
            double prelimQuizzes = sumOfQuizzes / 4;
            double attendance = Math.max(100 - (absences * 10), 0);
            double prelimClassStanding = (prelimQuizzes * 0.60) + (attendance * 0.40);
            double finalGrade = (exam * 0.60) + (prelimClassStanding * 0.40);

            // Prepare steps text
            String[] steps = {
                String.format("Essay: ( %.2f / 100 ) * 100 = %.2f%%", essay, essayPercent),
                String.format("PVM: ( %.2f / 60 ) * 100 = %.2f%%", pvm, pvmPercent),
                String.format("Java: ( %.2f / 40 ) * 100 = %.2f%%", java, javaPercent),
                String.format("JS: ( %.2f / 40 ) * 100 = %.2f%%", js, jsPercent),
                String.format("Average Quizzes = (%.2f + %.2f + %.2f + %.2f)/4 = %.2f%%", 
                              essayPercent, pvmPercent, javaPercent, jsPercent, prelimQuizzes),
                String.format("Attendance = 100 - ( %d * 10 ) = %.2f%%", absences, attendance),
                String.format("Prelim Class Standing = ( %.2f%% * 0.60 ) + ( %.2f%% * 0.40 ) = %.2f%%", 
                              prelimQuizzes, attendance, prelimClassStanding),
                String.format("Final Grade = ( %.2f * 0.60 ) + ( %.2f%% * 0.40 ) = %.2f", 
                              exam, prelimClassStanding, finalGrade)
            };

            if(!showStepsCheck.isSelected()) {
                errorArea.setText(String.format("Your Final Lecture Grade: %.1f", finalGrade));
                if(finalGrade % 1 != 0) {
                    int roundUp = (int)Math.ceil(finalGrade);
                    int roundDown = (int)Math.floor(finalGrade);
                    errorArea.append(String.format("\nRounded Up Standard: %d | Rounded Down Standard: %d", roundUp, roundDown));
                }
                JPanel stampPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                stampPanel.setOpaque(false);
                stampPanel.add(createStamp(finalGrade));
                resultPanel.add(stampPanel);
                container.revalidate();
                container.repaint();
                return;
            }

            // Show step-by-step using a Swing Timer
            stepsPanel.setVisible(true);
            Timer timer = new Timer(1500, null);
            final int[] index = {0};
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(index[0] < steps.length) {
                        JLabel stepLabel = new JLabel(steps[index[0]]);
                        stepLabel.setFont(labelFont);
                        stepLabel.setForeground(Color.BLACK);
                        stepsPanel.add(stepLabel);
                        stepsPanel.revalidate();
                        index[0]++;
                    } else {
                        JLabel finalLabel = new JLabel(String.format("Your Final Lecture Grade: %.1f", finalGrade));
                        finalLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
                        finalLabel.setForeground(Color.BLACK);
                        stepsPanel.add(finalLabel);
                        if(finalGrade % 1 != 0) {
                            int roundUp = (int)Math.ceil(finalGrade);
                            int roundDown = (int)Math.floor(finalGrade);
                            JLabel roundingLabel = new JLabel(String.format("Rounded Up Standard: %d | Rounded Down Standard: %d", roundUp, roundDown));
                            roundingLabel.setFont(labelFont);
                            roundingLabel.setForeground(Color.BLACK);
                            stepsPanel.add(roundingLabel);
                        }
                        stepsPanel.add(createStamp(finalGrade));
                        stepsPanel.revalidate();
                        timer.stop();
                    }
                }
            });
            timer.start();
        }
    }

    // ------------------ Lab Calculator Panel ------------------
    class LabPanel extends JPanel {
        // Input fields
        private JTextField java1Field, java2Field, js1Field, js2Field;
        private JTextField mp1Field, mp2Field, mp3Field, mp4Field;
        private JTextField absencesField;
        private JCheckBox showStepsCheck;
        private JPanel stepsPanel;
        private JTextArea errorArea;
        private JButton calculateBtn, randomBtn;
        private JPanel container; // main container
        private JPanel resultPanel; // panel to hold final result
        private final Font labelFont = new Font("Helvetica Neue", Font.PLAIN, 14);

        public LabPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.decode("#3f0f17"));

            container = new JPanel();
            container.setBackground(Color.decode("#f1e893"));
            container.setBorder(new CompoundBorder(new EmptyBorder(20,20,20,20),
                    new LineBorder(Color.LIGHT_GRAY, 1, true)));
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("Lab Grade Calculator");
            title.setFont(new Font("Helvetica Neue", Font.BOLD, 20));
            title.setForeground(Color.BLACK);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(title);
            container.add(Box.createRigidArea(new Dimension(0, 15)));

            // Random generator button
            randomBtn = new JButton("Random Grade Generator Button");
            randomBtn.setBackground(new Color(111,66,193));
            randomBtn.setForeground(Color.BLACK);
            randomBtn.setFocusPainted(false);
            randomBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            randomBtn.addActionListener(e -> setRandomValidLab());
            container.add(randomBtn);
            container.add(Box.createRigidArea(new Dimension(0,20)));

            // Exams header
            JLabel examHeader = new JLabel("Exams");
            examHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
            examHeader.setForeground(Color.BLACK);
            examHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(examHeader);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            java1Field = createInputGroup(container, "Java 1 (0 - 100)", 100);
            java2Field = createInputGroup(container, "Java 2 (0 - 100)", 100);
            js1Field   = createInputGroup(container, "JS 1 (0 - 100)", 100);
            js2Field   = createInputGroup(container, "JS 2 (0 - 100)", 100);

            container.add(Box.createRigidArea(new Dimension(0,10)));
            // Lab Works header
            JLabel labHeader = new JLabel("Lab Works");
            labHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
            labHeader.setForeground(Color.BLACK);
            labHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(labHeader);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            mp1Field = createInputGroup(container, "MP1 (0 - 100)", 100);
            mp2Field = createInputGroup(container, "MP2 (0 - 100)", 100);
            mp3Field = createInputGroup(container, "MP3 (0 - 100)", 100);
            mp4Field = createInputGroup(container, "MP4 (0 - 100)", 100);

            container.add(Box.createRigidArea(new Dimension(0,10)));
            // Absences
            JLabel attendanceHeader = new JLabel("Attendance");
            attendanceHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
            attendanceHeader.setForeground(Color.BLACK);
            attendanceHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(attendanceHeader);
            absencesField = createInputGroup(container, "Number of Absences (0+)", 0);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            // Toggle for step-by-step
            showStepsCheck = new JCheckBox("Show Step-by-Step Calculation");
            showStepsCheck.setSelected(true);
            showStepsCheck.setFont(labelFont);
            showStepsCheck.setForeground(Color.BLACK);
            showStepsCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(showStepsCheck);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            // Calculate button
            calculateBtn = new JButton("Calculate Lab Grade");
            calculateBtn.setBackground(new Color(40,167,69));
            calculateBtn.setForeground(Color.BLACK);
            calculateBtn.setFocusPainted(false);
            calculateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            calculateBtn.addActionListener(e -> calculateLabGrade());
            container.add(calculateBtn);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            // Error/Result area wrapped in a center-aligned panel
            errorArea = new JTextArea(3,40);
            errorArea.setEditable(false);
            errorArea.setLineWrap(true);
            errorArea.setWrapStyleWord(true);
            errorArea.setFont(labelFont);
            errorArea.setForeground(Color.BLACK);
            errorArea.setOpaque(false);
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            errorPanel.setOpaque(false);
            errorPanel.add(errorArea);
            container.add(errorPanel);
            container.add(Box.createRigidArea(new Dimension(0,10)));

            // Steps panel
            stepsPanel = new JPanel();
            stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
            stepsPanel.setBackground(Color.decode("#f1e893"));
            stepsPanel.setVisible(false);
            stepsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(stepsPanel);

            // Dedicated result panel for final grade and stamp
            resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            resultPanel.setOpaque(false);
            container.add(resultPanel);

            add(new JScrollPane(container), BorderLayout.CENTER);
        }

        // Helper to create input group (similar to LecturePanel)
        private JTextField createInputGroup(JPanel parent, String labelText, int perfectValue) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(false);
            JLabel label = new JLabel(labelText);
            label.setFont(labelFont);
            label.setForeground(Color.BLACK);
            panel.add(label);
            JButton perfectBtn = new JButton("Perfect Score Button!");
            perfectBtn.setFont(new Font("Arial", Font.PLAIN, 12));
            perfectBtn.setBackground(new Color(187,131,131));
            perfectBtn.setForeground(Color.BLACK);
            perfectBtn.setFocusPainted(false);
            perfectBtn.addActionListener(e -> {
                JTextField sourceField = (JTextField) panel.getClientProperty("field");
                if(sourceField != null) {
                    sourceField.setText(String.valueOf(perfectValue));
                    sourceField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            });
            panel.add(perfectBtn);

            JTextField textField = new JTextField(5);
            textField.setFont(labelFont);
            textField.setForeground(Color.BLACK);
            panel.add(textField);
            panel.putClientProperty("field", textField);
            parent.add(panel);
            parent.add(Box.createRigidArea(new Dimension(0,10)));
            return textField;
        }

        private void setRandomValidLab() {
            errorArea.setText("");
            resultPanel.removeAll();
            stepsPanel.removeAll();
            stepsPanel.setVisible(false);
            java1Field.setText(String.valueOf(getRandomInt(0, 100)));
            java2Field.setText(String.valueOf(getRandomInt(0, 100)));
            js1Field.setText(String.valueOf(getRandomInt(0, 100)));
            js2Field.setText(String.valueOf(getRandomInt(0, 100)));
            mp1Field.setText(String.valueOf(getRandomInt(0, 100)));
            mp2Field.setText(String.valueOf(getRandomInt(0, 100)));
            mp3Field.setText(String.valueOf(getRandomInt(0, 100)));
            mp4Field.setText(String.valueOf(getRandomInt(0, 100)));
            absencesField.setText(String.valueOf(getRandomInt(0, 3)));
        }

        private String validateLab() {
            StringBuilder errors = new StringBuilder();
            Object[][] fields = {
                { java1Field, "Java 1", 0, 100 },
                { java2Field, "Java 2", 0, 100 },
                { js1Field, "JS 1", 0, 100 },
                { js2Field, "JS 2", 0, 100 },
                { mp1Field, "MP1", 0, 100 },
                { mp2Field, "MP2", 0, 100 },
                { mp3Field, "MP3", 0, 100 },
                { mp4Field, "MP4", 0, 100 },
                { absencesField, "Absences", 0, 100 }
            };
            for(Object[] fieldInfo : fields) {
                JTextField field = (JTextField) fieldInfo[0];
                String label = (String) fieldInfo[1];
                int min = (int) fieldInfo[2];
                int max = (int) fieldInfo[3];
                String text = field.getText().trim();
                if(text.isEmpty()) {
                    errors.append(label).append(" is empty.\n");
                    field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                } else {
                    try {
                        double val = Double.parseDouble(text);
                        if(val < min || val > max) {
                            errors.append(label).append(" should be between ").append(min)
                                  .append(" and ").append(max).append(".\n");
                            field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                        } else {
                            field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                        }
                    } catch(NumberFormatException ex) {
                        errors.append(label).append(" is not a valid number.\n");
                        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }
            }
            return errors.toString();
        }

        private void calculateLabGrade() {
            errorArea.setText("");
            resultPanel.removeAll();
            stepsPanel.removeAll();
            stepsPanel.setVisible(false);

            String errorMsg = validateLab();
            if(!errorMsg.isEmpty()) {
                errorArea.setText(errorMsg);
                return;
            }

            // Parse inputs
            double java1 = Double.parseDouble(java1Field.getText().trim());
            double java2 = Double.parseDouble(java2Field.getText().trim());
            double js1   = Double.parseDouble(js1Field.getText().trim());
            double js2   = Double.parseDouble(js2Field.getText().trim());
            double mp1   = Double.parseDouble(mp1Field.getText().trim());
            double mp2   = Double.parseDouble(mp2Field.getText().trim());
            double mp3   = Double.parseDouble(mp3Field.getText().trim());
            double mp4   = Double.parseDouble(mp4Field.getText().trim());
            int absences = Integer.parseInt(absencesField.getText().trim());

            if(absences >= 4) {
                errorArea.setText("FAIL: Absences are 4 or more.");
                JPanel stampPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                stampPanel.setOpaque(false);
                stampPanel.add(createStamp(0));
                resultPanel.add(stampPanel);
                container.revalidate();
                container.repaint();
                return;
            }

            // Calculate exam score, lab work, attendance and class standing
            double labExam = (java1 * 0.20) + (java2 * 0.30) + (js1 * 0.20) + (js2 * 0.30);
            double labWork = (mp1 + mp2 + mp3 + mp4) / 4.0;
            double attendance = Math.max(100 - (absences * 10), 0);
            double labClassStanding = (labWork * 0.60) + (attendance * 0.40);
            double labGrade = (labExam * 0.60) + (labClassStanding * 0.40);

            String[] steps = {
                String.format("Lab Exam = ( %.2f*0.20 ) + ( %.2f*0.30 ) + ( %.2f*0.20 ) + ( %.2f*0.30 ) = %.2f", java1, java2, js1, js2, labExam),
                String.format("Lab Work = ( %.2f + %.2f + %.2f + %.2f ) / 4 = %.2f", mp1, mp2, mp3, mp4, labWork),
                String.format("Attendance = 100 - ( %d * 10 ) = %.2f", absences, attendance),
                String.format("Class Standing = ( %.2f*0.60 ) + ( %.2f*0.40 ) = %.2f", labWork, attendance, labClassStanding),
                String.format("Final Grade = ( %.2f*0.60 ) + ( %.2f*0.40 ) = %.2f", labExam, labClassStanding, labGrade)
            };

            if(!showStepsCheck.isSelected()) {
                errorArea.setText(String.format("Your Final Lab Grade: %.2f", labGrade));
                if(labGrade % 1 != 0) {
                    int roundUp = (int)Math.ceil(labGrade);
                    int roundDown = (int)Math.floor(labGrade);
                    errorArea.append(String.format("\nRounded Up Standard: %d | Rounded Down Standard: %d", roundUp, roundDown));
                }
                JPanel stampPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                stampPanel.setOpaque(false);
                stampPanel.add(createStamp(labGrade));
                resultPanel.add(stampPanel);
                container.revalidate();
                container.repaint();
                return;
            }

            stepsPanel.setVisible(true);
            Timer timer = new Timer(1500, null);
            final int[] index = {0};
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(index[0] < steps.length) {
                        JLabel stepLabel = new JLabel(steps[index[0]]);
                        stepLabel.setFont(labelFont);
                        stepLabel.setForeground(Color.BLACK);
                        stepsPanel.add(stepLabel);
                        stepsPanel.revalidate();
                        index[0]++;
                    } else {
                        JLabel finalLabel = new JLabel(String.format("Your Final Lab Grade: %.1f", labGrade));
                        finalLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
                        finalLabel.setForeground(Color.BLACK);
                        stepsPanel.add(finalLabel);
                        if(labGrade % 1 != 0) {
                            int roundUp = (int)Math.ceil(labGrade);
                            int roundDown = (int)Math.floor(labGrade);
                            JLabel roundingLabel = new JLabel(String.format("Rounded Up Standard: %d | Rounded Down Standard: %d", roundUp, roundDown));
                            roundingLabel.setFont(labelFont);
                            roundingLabel.setForeground(Color.BLACK);
                            stepsPanel.add(roundingLabel);
                        }
                        stepsPanel.add(createStamp(labGrade));
                        stepsPanel.revalidate();
                        timer.stop();
                    }
                }
            });
            timer.start();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) { }
        SwingUtilities.invokeLater(() -> new GradeCalculatorFrame());
    }
}
