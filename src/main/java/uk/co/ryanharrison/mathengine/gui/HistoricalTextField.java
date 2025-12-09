package uk.co.ryanharrison.mathengine.gui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A text area that maintains a history of previously entered commands accessible via arrow keys.
 * <p>
 * This component mimics the command history behavior found in terminal/console applications:
 * </p>
 * <ul>
 *     <li><b>Enter key</b> - Adds current command to history</li>
 *     <li><b>Up arrow</b> - Navigate to previous command in history</li>
 *     <li><b>Down arrow</b> - Navigate to next command in history</li>
 * </ul>
 * <p>
 * When a command is entered that already exists in the history, it is moved to the
 * most recent position rather than being duplicated.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * HistoricalTextField textField = new HistoricalTextField(4, 40);
 * panel.add(textField);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class HistoricalTextField extends JTextArea {

    private final List<String> history;
    private int currentIndex;

    /**
     * Constructs a new HistoricalTextField with the specified dimensions.
     *
     * @param rows    the number of rows to display
     * @param columns the number of columns to display
     */
    public HistoricalTextField(int rows, int columns) {
        super(rows, columns);
        this.history = new ArrayList<>();
        this.currentIndex = 0;

        addKeyListener(new HistoryNavigationHandler());
    }

    /**
     * Adds the current text to the command history.
     * If the command already exists, it is moved to the end of the history.
     */
    private void addToHistory(String command) {
        int existingIndex = history.indexOf(command);

        if (existingIndex == -1) {
            // New command - add to history
            history.add(command);
        } else {
            // Existing command - move to end
            history.remove(existingIndex);
            history.add(command);
        }

        currentIndex = history.size();
    }

    /**
     * Navigates to the previous command in history if available.
     */
    private void navigateToPrevious() {
        if (currentIndex - 1 >= 0) {
            currentIndex--;
            setText(history.get(currentIndex));
        }
    }

    /**
     * Navigates to the next command in history if available.
     * If at the end of history, clears the text field.
     */
    private void navigateToNext() {
        if (currentIndex + 1 < history.size()) {
            currentIndex++;
            setText(history.get(currentIndex));
        } else {
            // At the end of history - clear field for new input
            setText("");
            currentIndex = history.size();
        }
    }

    /**
     * Handles keyboard events for history navigation and command submission.
     */
    private class HistoryNavigationHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_UP) {
                navigateToPrevious();
                e.consume();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                navigateToNext();
                e.consume();
            } else if (keyCode == KeyEvent.VK_ENTER) {
                String command = getText().trim();
                if (!command.isEmpty()) {
                    addToHistory(command);
                }
                e.consume(); // Consume to prevent newline
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Consume newline character to prevent it being inserted
            if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                e.consume();
            }
        }
    }
}
