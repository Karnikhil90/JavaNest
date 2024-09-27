import javax.swing.*;
import java.awt.*;

public class LineNumberComponent extends JComponent {
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
