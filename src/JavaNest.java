
/**
 * @since v0.1a [alpha version]
 * @author Nikhil Karmakar <email=nikhilbroo@hotmail.com>
 * 
 * JavaNest CodeEditor - v0.3a
 * 
 * JavaNest: The name represents a space where Java is at the core of development.
 * "Java" signifies the language used for building this project, while "Nest" symbolizes 
 * a safe, comfortable, and organized space where code can grow and evolve. 
 * Just as birds create nests to nurture their young, JavaNest is designed to foster the 
 * creation and nurturing of ideas in the form of code.
 * 
 * This Code Editor is built to provide a simple, effective, and customizable environment for editing
 * and managing source code files, with basic functionality like opening, editing, saving files, 
 * and future plans to include syntax highlighting, font size adjustment, and more.
 * 
 * Features of v0.2a:
 * - Basic text editing capabilities.
 * - Ability to open and save files with metadata embedded.
 * - Adjustable font size (via View menu).
 * - Minimal user interface to provide an uncluttered experience.
 * - Line numbering support for improved readability.
 * - Customizable font selection for better user experience.
 * - Future plans to include syntax highlighting for multiple programming languages.
 * - Enhanced file management features.
 *
 * Features of v0.3a:
 * - Added support for opening files directly via the 'Open with' context menu.
 *
 * 
 * Future Enhancements:
 * - Syntax highlighting for multiple programming languages.
 * - Line numbering support.
 * - Theme selection and custom color schemes.
 * - Auto-complete and real-time syntax checking.
 * 
 * Disclaimer:
 * This is an alpha version of the software, and it is still under development. Expect bugs, incomplete 
 * features, and performance issues. Please report any bugs or suggestions to the author at the given email.
 * 
 */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
// import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class JavaNest {
    private JTextArea textArea;
    private JFrame frame;
    private Font currentFont;
    private final int FONT_INCREMENT = 3;
    private String lastOpenedFilePath;
    private final String logoPath = "util/logo/logo.png";
    private final int defaultFontSize = 25;
    private LineNumberComponent lineNumberComponent; // Line number component reference

    public JavaNest() {
        frame = new JFrame("JavaNest CodeEditor - v0.2a");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource(logoPath));
            frame.setIconImage(logo.getImage());
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        textArea = new JTextArea();
        currentFont = new Font("Arial", Font.PLAIN, defaultFontSize);
        textArea.setFont(currentFont);

        // Create line number component
        lineNumberComponent = new LineNumberComponent(textArea, currentFont);

        // Create a JScrollPane for text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(lineNumberComponent); // Attach line number component to scroll pane
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add a DocumentListener to update line numbers on text changes
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lineNumberComponent.updatePreferredSize(); // Update the width
                lineNumberComponent.repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lineNumberComponent.updatePreferredSize(); // Update the width
                lineNumberComponent.repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lineNumberComponent.updatePreferredSize(); // Update the width
                lineNumberComponent.repaint();
            }
        });

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> {
            textArea.setText("JavaNest CodeEditor - v0.2a");
            lastOpenedFilePath = null;
        });

        openItem.addActionListener(e -> openFile(null));
        saveItem.addActionListener(e -> saveFileWithMetadata());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem increaseFontItem = new JMenuItem("Increase Font Size");
        JMenuItem decreaseFontItem = new JMenuItem("Decrease Font Size");

        increaseFontItem.addActionListener(e -> increaseFontSize());
        decreaseFontItem.addActionListener(e -> decreaseFontSize());

        viewMenu.add(increaseFontItem);
        viewMenu.add(decreaseFontItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    // Method to open a file
    // Overloaded openFile method to accept a file path
    private void openFile(String filePath) {
        lastOpenedFilePath = filePath;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "File not found: " + filePath);
        }
    }

    // Method to save the current text and preserve metadata
    private void saveFileWithMetadata() {
        if (lastOpenedFilePath != null) {
            Path path = Paths.get(lastOpenedFilePath);
            try {
                // Get the file's current metadata before saving
                BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime lastModifiedTime = attributes.lastModifiedTime();
                FileTime creationTime = attributes.creationTime();

                // Save the file content
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastOpenedFilePath))) {
                    writer.write(textArea.getText());
                }

                // Restore the metadata after saving
                Files.setAttribute(path, "basic:lastModifiedTime", lastModifiedTime);
                Files.setAttribute(path, "basic:creationTime", creationTime);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
            }
        } else {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lastOpenedFilePath = file.getAbsolutePath();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(textArea.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
                }
            }
        }
    }

    // Method to increase the font size
    private void increaseFontSize() {
        currentFont = currentFont.deriveFont((float) (currentFont.getSize() + FONT_INCREMENT));
        textArea.setFont(currentFont);
        lineNumberComponent.setFont(currentFont); // Update line numbers font if needed
    }

    // Method to decrease the font size
    private void decreaseFontSize() {
        currentFont = currentFont.deriveFont((float) (currentFont.getSize() - FONT_INCREMENT));
        textArea.setFont(currentFont);
        lineNumberComponent.setFont(currentFont); // Update line numbers font if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JavaNest editor = new JavaNest();
            if (args.length > 0) {
                String filePath = args[0];
                editor.openFile(filePath); // Open the file automatically if a file path is passed
            }
        });
    }

}

class LineNumberComponent extends JComponent {
    private JTextArea textArea;
    private Font lineNumberFont;
    private final int LINE_NUMBER_MARGIN = 10; // Increased margin for line numbers
    private final int DEFAULT_WIDTH = 30; // Set a default width for line numbers

    public LineNumberComponent(JTextArea textArea, Font lineNumberFont) {
        this.textArea = textArea;
        this.lineNumberFont = lineNumberFont;
        setBackground(Color.LIGHT_GRAY); // Set a background color for visibility
        updatePreferredSize(); // Set initial preferred size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLineNumbers(g);
    }

    private void drawLineNumbers(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(lineNumberFont); // Use specified font for line numbers
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
        int startLine = 0;

        // Calculate the number of lines in the text area
        int endLine = textArea.getDocument().getDefaultRootElement().getElementCount() - 1;

        for (int i = startLine; i <= endLine; i++) {
            // Draw line numbers at the appropriate position
            int y = (i + 1) * lineHeight - 9; // Y coordinate, adjusted by 5 pixels
            String num = String.valueOf(i + 1) + " |";
            // Calculate the x position based on the fixed margin
            int x = LINE_NUMBER_MARGIN; // Fixed margin
            g.drawString(num, x, y); // Draw the line number
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        setPreferredSize(new Dimension(getWidth(), d.height)); // Maintain current width and update height
    }

    public void setFont(Font font) {
        this.lineNumberFont = font;
        updatePreferredSize(); // Update preferred size when font changes
        repaint(); // Repaint to update the line numbers with the new font
    }

    public void updatePreferredSize() {
        // Calculate width based on the number of lines and font metrics
        int maxLineNumber = textArea.getDocument().getDefaultRootElement().getElementCount();
        FontMetrics metrics = getFontMetrics(lineNumberFont); // Use getFontMetrics to retrieve font metrics
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();

        // Calculate width needed for the line numbers
        int width = Math.max(metrics.stringWidth(String.valueOf(maxLineNumber)), DEFAULT_WIDTH); // Ensure minimum width
        width += LINE_NUMBER_MARGIN * 2; // Add margins to width
        setPreferredSize(new Dimension(width, Integer.MAX_VALUE)); // Update preferred size
        revalidate(); // Revalidate to refresh layout
    }
}
