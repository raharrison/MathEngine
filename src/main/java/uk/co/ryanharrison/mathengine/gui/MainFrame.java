package uk.co.ryanharrison.mathengine.gui;

import uk.co.ryanharrison.mathengine.Utils;
import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.Evaluator;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Frame window to act as a visual interface into the {@link Evaluator} class.
 * <p>
 * Users can insert an expression and upon the enter key being pressed, the
 * expression will be evaluated and displayed in the output field
 *
 * @author Ryan Harrison
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -3046696925938478655L;

    /**
     * Main entry point of the application
     *
     * @param args Command line parameters
     */
    public static void main(String[] args) {
        // Run and display the frame on the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            // Show in the center of the screen
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Attributes to give a specific colour to the output
     */
    private SimpleAttributeSet bold, red;

    /**
     * TextPane to show the results of the expression evaluations
     */
    private JTextPane output;

    /**
     * Input area where the user can type in an expression
     */
    private JTextArea input;

    /**
     * Evaluator to evaluate the expressions given by the user
     */
    private Evaluator evaluator;

    /**
     * Construct a new frame, setting up the components and adding event
     * handlers
     */
    public MainFrame() {
        super("Expression Evaluator");

        // Initialise fields
        bold = new SimpleAttributeSet();
        StyleConstants.setBold(bold, true);

        red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.RED);

        evaluator = Evaluator.newEvaluator();
        evaluator.setAngleUnit(AngleUnit.Radians);

        JPanel panel = new JPanel();

        // Construct the layout of the frame
        BorderLayout layout = new BorderLayout(25, 25);
        panel.setLayout(layout);

        output = new JTextPane();
        output.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        output.setEditable(false);
        JScrollPane pane = new JScrollPane(output);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(pane, BorderLayout.CENTER);

        input = new HistoricalTextField(4, 40);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane pane2 = new JScrollPane(input);
        input.addKeyListener(new KeyListener() {
            /**
             * Handle the key pressed event of the input field
             *
             * If the enter key is pressed handle evaluate the input and display
             * the message
             *
             * If 'clear' is inserted, clear the output area of all text
             * {@inheritDoc}
             */
            @Override
            public void keyPressed(KeyEvent event) {
                // We are only concerned when the user presses the enter key
                if (!(event.getKeyCode() == KeyEvent.VK_ENTER))
                    return;

                String expression = input.getText().trim();
                if (!expression.equals("")) {
                    // If the input is clear, clear the output area of all text
                    if (expression.equalsIgnoreCase("clear")) {
                        output.setText("");
                    }
                    // Otherwise evaluate the input and display the results
                    else {
                        evaluateAndDisplayResult(expression);
                    }
                }

                // Clear the input text to get ready for new input from the user
                event.consume();
                input.setText("");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });

        panel.add(pane2, BorderLayout.SOUTH);

        // Add the panel to the frame and set the frame parameters
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setResizable(false);

        // Focus the input area
        input.requestFocus();
    }

    /**
     * Evaluates the given expression through the {@link Evaluator} object and
     * displays the result in the output
     * <p>
     * If the expression was successfully evaluated, display the result in
     * black, if there was an error during evaluation, display the error message
     * in red
     *
     * @param expression The expression to evaluate
     */
    private void evaluateAndDisplayResult(String expression) {
        try {
            Node tree = evaluator.generateTree(expression);

            // Construct a message containing the input which appears before the output
            String inputTreeMessage = "> " + Utils.removeOuterParenthesis(tree.toString()) + "\n";

            // Add the input to the end of the output area
            output.getDocument().insertString(output.getDocument().getLength(), inputTreeMessage, null);

            // Construct the output message
            String outputTreeMessage = evaluator.parseTree(tree).toString() + "\n";

            // Add the output message to the end of the output area in bold
            output.getDocument().insertString(output.getDocument().getLength(), outputTreeMessage,
                    bold);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                // If there was an error during evaluation, add the error
                // message to the end of the output area in red
                output.getDocument().insertString(output.getDocument().getLength(),
                        e.toString() + "\n", red);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
}
