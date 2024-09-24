import javax.swing.*; // Importing Swing components
import java.awt.*; // Importing AWT for layout and graphics management
import java.awt.event.*; // Importing AWT event classes for handling actions
import java.io.*; // Importing for file operations

public class SimpleNotepad {
    private JTextArea textArea; // Text area for input
    private JFrame frame; // Main window
    private Font currentFont; // Current font of the text area
    private final int FONT_INCREMENT = 2; // Font size increment
    private String lastOpenedFilePath; // Variable to store the last opened file path

    public SimpleNotepad() {
        // Create the main JFrame
        frame = new JFrame("Simple Notepad");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the JTextArea for text input
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 30));
        JScrollPane scrollPane = new JScrollPane(textArea); // Adding a scroll pane
        frame.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center

        // Create a JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create File menu
        JMenu fileMenu = new JMenu("File");

        // Create menu items
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add action listeners for menu items
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(""); // Clear the text area for a new file
                lastOpenedFilePath = null; // Clear the last opened file path
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile(); // Open a file
            }
        });

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(); // Save the current text
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Add menu items to the file menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator(); // Add a separator line
        fileMenu.add(exitItem);

        // Create View menu
        JMenu viewMenu = new JMenu("View");

        // Create menu items for font size control
        JMenuItem increaseFontItem = new JMenuItem("Increase Font Size");
        JMenuItem decreaseFontItem = new JMenuItem("Decrease Font Size");

        // Add action listeners for font size control
        increaseFontItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseFontSize(); // Increase font size
            }
        });

        decreaseFontItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseFontSize(); // Decrease font size
            }
        });

        // Add font size control items to the view menu
        viewMenu.add(increaseFontItem);
        viewMenu.add(decreaseFontItem);

        // Add the file and view menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        // Set the menu bar for the frame
        frame.setJMenuBar(menuBar);

        // Set the initial font
        currentFont = textArea.getFont(); // Get the current font
        frame.setVisible(true); // Set the frame to be visible
    }

    // Method to open a file
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            lastOpenedFilePath = file.getAbsolutePath(); // Save the path of the opened file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.setText(""); // Clear current text area
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n"); // Append lines from the file
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
            }
        }
    }

    // Method to save the current text
    private void saveFile() {
        if (lastOpenedFilePath != null) { // If a file was opened previously
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastOpenedFilePath))) {
                writer.write(textArea.getText()); // Write the text area content to file
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
            }
            // lastOpenedFilePath = null;
        } else { // If no previous file was opened, use file chooser
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lastOpenedFilePath = file.getAbsolutePath(); // Save the path of the saved file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(textArea.getText()); // Write the text area content to file
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
                }
            }
        }
    }

    // Method to increase the font size
    private void increaseFontSize() {
        currentFont = currentFont.deriveFont((float) (currentFont.getSize() + FONT_INCREMENT));
        textArea.setFont(currentFont); // Set the new font to the text area
    }

    // Method to decrease the font size
    private void decreaseFontSize() {
        currentFont = currentFont.deriveFont((float) (currentFont.getSize() - FONT_INCREMENT));
        textArea.setFont(currentFont); // Set the new font to the text area
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleNotepad::new); // Run GUI in the Event Dispatch Thread
    }
}
