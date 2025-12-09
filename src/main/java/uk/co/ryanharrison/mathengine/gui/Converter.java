package uk.co.ryanharrison.mathengine.gui;

import uk.co.ryanharrison.mathengine.unitconversion.ConversionEngine;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;
import uk.co.ryanharrison.mathengine.utils.MathUtils;
import uk.co.ryanharrison.mathengine.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Interactive GUI for converting values between different units of measurement.
 * <p>
 * The converter supports multiple unit categories (length, mass, temperature, etc.)
 * and provides two input methods:
 * </p>
 * <ul>
 *     <li><b>Dropdown selection</b> - Choose unit group, from/to units, and enter value</li>
 *     <li><b>String conversion</b> - Enter natural language like "5 metres in miles"</li>
 * </ul>
 * <p>
 * Results can be copied to the clipboard with the Copy button.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Launch the application
 * SwingUtilities.invokeLater(() -> {
 *     Converter.createAndShow();
 * });
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class Converter extends JPanel {

    private static final String TITLE = "Unit Converter";
    private static final int FRAME_WIDTH = 635;
    private static final int FRAME_HEIGHT = 275;
    private static final Font LARGE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font MEDIUM_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font EQUALS_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final int DECIMAL_PLACES = 6;

    static void main() {
        SwingUtilities.invokeLater(Converter::createAndShow);
    }

    /**
     * Creates and displays the converter GUI frame.
     */
    public static void createAndShow() {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Converter converter = new Converter();
        converter.setOpaque(true);
        frame.setContentPane(converter);

        frame.pack();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // UI Components
    private final JComboBox<String> unitGroupBox;
    private final JComboBox<String> fromUnitBox;
    private final JComboBox<String> toUnitBox;
    private final JTextField fromValueField;
    private final JTextField resultValueField;
    private final JTextField stringConversionField;
    private final ConversionEngine engine;

    /**
     * Constructs a new Converter panel with all UI components initialized.
     */
    public Converter() {
        super(new GridBagLayout());

        this.engine = new ConversionEngine();

        // Initialize components
        String[] unitGroups = getTitleCasedUnitGroups();
        this.unitGroupBox = createUnitGroupBox(unitGroups);

        String[] defaultUnits = engine.getUnitsFor(unitGroupBox.getItemAt(0));
        this.fromUnitBox = createFromUnitBox(defaultUnits);
        this.toUnitBox = createToUnitBox(defaultUnits);

        this.fromValueField = createFromValueField();
        this.resultValueField = createResultValueField();
        this.stringConversionField = createStringConversionField();

        // Build UI layout
        buildLayout();

        // Apply system look and feel
        applySystemLookAndFeel();
    }

    /**
     * Gets unit groups from the engine and converts them to title case.
     */
    private String[] getTitleCasedUnitGroups() {
        String[] groups = engine.getUnitGroups();
        String[] titleCased = new String[groups.length];
        for (int i = 0; i < groups.length; i++) {
            titleCased[i] = Utils.toTitleCase(groups[i]);
        }
        return titleCased;
    }

    /**
     * Creates the unit group selection combobox.
     */
    private JComboBox<String> createUnitGroupBox(String[] unitGroups) {
        JComboBox<String> box = new JComboBox<>(unitGroups);
        box.setFont(LARGE_FONT);
        box.addActionListener(e -> onUnitGroupChanged());
        return box;
    }

    /**
     * Creates the "from" unit selection combobox.
     */
    private JComboBox<String> createFromUnitBox(String[] units) {
        JComboBox<String> box = new JComboBox<>(units);
        box.setMaximumRowCount(15);
        box.setSelectedIndex(2);
        box.setFont(LARGE_FONT);
        box.addActionListener(e -> performConversion());
        return box;
    }

    /**
     * Creates the "to" unit selection combobox.
     */
    private JComboBox<String> createToUnitBox(String[] units) {
        JComboBox<String> box = new JComboBox<>(units);
        box.setMaximumRowCount(15);
        box.setSelectedIndex(3);
        box.setFont(LARGE_FONT);
        box.addActionListener(e -> performConversion());
        return box;
    }

    /**
     * Creates the input value text field.
     */
    private JTextField createFromValueField() {
        JTextField field = new JTextField(20);
        field.setFont(SMALL_FONT);
        field.addActionListener(e -> performConversion());
        return field;
    }

    /**
     * Creates the result value text field (read-only).
     */
    private JTextField createResultValueField() {
        JTextField field = new JTextField(20);
        field.setFont(SMALL_FONT);
        field.setEditable(false);
        return field;
    }

    /**
     * Creates the string-based conversion input field.
     */
    private JTextField createStringConversionField() {
        JTextField field = new JTextField(30);
        field.setFont(MEDIUM_FONT);
        field.addActionListener(e -> convertFromString(field.getText()));
        return field;
    }

    /**
     * Builds the complete UI layout using GridBagLayout.
     */
    private void buildLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // Unit group selector (row 1)
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(unitGroupBox, c);

        // From unit selector (row 2, left)
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        add(fromUnitBox, c);

        // To unit selector (row 2, right)
        c.gridx = 3;
        add(toUnitBox, c);

        // From value field (row 3, left)
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1.0;
        add(fromValueField, c);

        // Equals label (row 3, center)
        JLabel equalsLabel = new JLabel("=");
        equalsLabel.setFont(EQUALS_FONT);
        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        add(equalsLabel, c);

        // Result value field (row 3, right)
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        add(resultValueField, c);

        // Convert button (row 4)
        JButton convertButton = new JButton("Convert");
        convertButton.setFont(LARGE_FONT);
        convertButton.addActionListener(e -> performConversion());
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        add(convertButton, c);

        // String conversion field (row 5, left)
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 5;
        add(stringConversionField, c);

        // Copy button (row 5, right)
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(LARGE_FONT);
        copyButton.addActionListener(e -> copyResultToClipboard());
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridx = 3;
        add(copyButton, c);
    }

    /**
     * Handles unit group selection change - updates available units.
     */
    private void onUnitGroupChanged() {
        String selectedGroup = unitGroupBox.getItemAt(unitGroupBox.getSelectedIndex());
        String[] newUnits = engine.getUnitsFor(selectedGroup);

        replaceComboBoxItems(fromUnitBox, newUnits);
        replaceComboBoxItems(toUnitBox, newUnits);

        fromUnitBox.setSelectedIndex(2);
        toUnitBox.setSelectedIndex(3);
    }

    /**
     * Performs conversion using the current dropdown selections.
     */
    private void performConversion() {
        // String conversion takes priority
        if (!stringConversionField.getText().isEmpty()) {
            convertFromString(stringConversionField.getText());
            return;
        }

        // Clear result if no input
        if (fromValueField.getText().isEmpty()) {
            resultValueField.setText("");
            return;
        }

        String fromUnit = (String) fromUnitBox.getSelectedItem();
        String toUnit = (String) toUnitBox.getSelectedItem();

        if (fromUnit == null || toUnit == null) {
            return;
        }

        try {
            double value = Double.parseDouble(fromValueField.getText());
            double result = engine.convertAsDouble(value, fromUnit, toUnit);
            double rounded = MathUtils.round(result, DECIMAL_PLACES);
            resultValueField.setText(Double.toString(rounded));
        } catch (NumberFormatException ex) {
            showErrorDialog("Numerical input expected");
        }
    }

    /**
     * Performs conversion using a natural language string (e.g., "5 metres in miles").
     *
     * @param text the conversion string to parse and evaluate
     */
    private void convertFromString(String text) {
        if (text.isEmpty()) {
            return;
        }

        try {
            Conversion result = engine.convert(text);

            // Update UI to reflect the conversion
            String unitGroup = Utils.toTitleCase(engine.getUnitGroupOfSubUnit(result.getFrom()));
            unitGroupBox.setSelectedItem(unitGroup);

            fromUnitBox.setSelectedItem(result.getFrom().toString());
            toUnitBox.setSelectedItem(result.getTo().toString());

            resultValueField.setText(Double.toString(result.getResult().doubleValue()));
            fromValueField.setText(Double.toString(result.getValue().doubleValue()));

        } catch (IllegalArgumentException ex) {
            showErrorDialog(ex.getLocalizedMessage());
        }
    }

    /**
     * Copies the result value to the system clipboard.
     */
    private void copyResultToClipboard() {
        String result = resultValueField.getText();
        if (!result.isEmpty()) {
            StringSelection selection = new StringSelection(result);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        }
    }

    /**
     * Replaces all items in a combobox with new items.
     *
     * @param box      the combobox to update
     * @param newItems the new items to populate the combobox with
     */
    private void replaceComboBoxItems(JComboBox<String> box, String[] newItems) {
        box.removeAllItems();
        for (String item : newItems) {
            box.addItem(item);
        }
    }

    /**
     * Displays an error dialog to the user.
     *
     * @param message the error message to display
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Applies the system look and feel to the panel.
     */
    private void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
