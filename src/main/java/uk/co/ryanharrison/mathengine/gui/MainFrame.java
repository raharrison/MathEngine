package uk.co.ryanharrison.mathengine.gui;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.Evaluator;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.utils.Utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Professional GUI REPL for evaluating mathematical expressions.
 *
 * @author Ryan Harrison
 */
public final class MainFrame extends JFrame {

    private static final String TITLE = "Math Engine - Expression Evaluator";
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 700;
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 500;

    // Fonts
    private static final Font OUTPUT_FONT = new Font("Consolas", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Consolas", Font.PLAIN, 16);

    // Colors
    private static final Color PROMPT_COLOR = new Color(0, 120, 215);
    private static final Color RESULT_COLOR = new Color(16, 124, 16);
    private static final Color ERROR_COLOR = new Color(200, 50, 50);
    private static final Color BORDER_COLOR = new Color(210, 210, 210);

    static void main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private final SimpleAttributeSet promptStyle;
    private final SimpleAttributeSet resultStyle;
    private final SimpleAttributeSet errorStyle;
    private final JTextPane output;
    private final JTextArea input;
    private final Evaluator evaluator;

    public MainFrame() {
        super(TITLE);

        // Initialize styles
        promptStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(promptStyle, PROMPT_COLOR);
        StyleConstants.setBold(promptStyle, true);

        resultStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(resultStyle, RESULT_COLOR);
        StyleConstants.setBold(resultStyle, true);

        errorStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(errorStyle, ERROR_COLOR);

        // Initialize evaluator
        evaluator = Evaluator.newEvaluator();
        evaluator.setAngleUnit(AngleUnit.Radians);

        // Main panel
        JPanel panel = new JPanel();
        BorderLayout layout = new BorderLayout(0, 0);
        panel.setLayout(layout);

        // Output area
        output = new JTextPane();
        output.setFont(OUTPUT_FONT);
        output.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outputScroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(outputScroll, BorderLayout.CENTER);

        // Input area
        input = new HistoricalTextField(3, 40);
        input.setFont(INPUT_FONT);
        input.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane inputScroll = new JScrollPane(input);
        inputScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        input.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }

                String expression = input.getText().trim();
                if (!expression.isEmpty()) {
                    if (expression.equalsIgnoreCase("clear")) {
                        output.setText("");
                    } else {
                        evaluateAndDisplay(expression);
                    }
                }

                event.consume();
                input.setText("");
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        panel.add(inputScroll, BorderLayout.SOUTH);

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        SwingUtilities.invokeLater(input::grabFocus);
    }

    private void evaluateAndDisplay(String expression) {
        try {
            Node tree = evaluator.generateTree(expression);
            String simplified = Utils.removeOuterParenthesis(tree.toString());

            appendToOutput("> ", promptStyle);
            appendToOutput(simplified + "\n", null);

            String result = evaluator.parseTree(tree).toString();
            appendToOutput("  " + result + "\n\n", resultStyle);

        } catch (Exception e) {
            appendToOutput("Error: ", errorStyle);
            appendToOutput(e.getMessage() + "\n\n", errorStyle);
        }
    }

    private void appendToOutput(String text, SimpleAttributeSet style) {
        try {
            output.getDocument().insertString(output.getDocument().getLength(), text, style);
        } catch (BadLocationException ignore) {
        }
    }
}
