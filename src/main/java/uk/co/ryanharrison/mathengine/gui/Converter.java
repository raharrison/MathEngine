package uk.co.ryanharrison.mathengine.gui;

import uk.co.ryanharrison.mathengine.unitconversion.ConversionEngine;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;
import uk.co.ryanharrison.mathengine.utils.MathUtils;
import uk.co.ryanharrison.mathengine.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI frame that allows users to interface with the unitconversion class and
 * convert values from one unit to the other
 * <p>
 * This frame also includes a {@link TextField} which accepts a string based
 * conversion i.e - "5 metres in miles"
 *
 * @author Ryan Harrison
 *
 */
public class Converter extends JPanel implements ActionListener {

    /**
     * Font with a larger type size
     */
    private static final Font largeFont = new Font("segoe UI", Font.BOLD, 14);

    /**
     * Font with a smaller type size
     */
    private static final Font smallFont = new Font("segoe UI", Font.PLAIN, 12);

    /**
     * Create and show the frame to the user
     */
    private static void createAndShowGui() {
        // Create the frame
        JFrame frame = new JFrame("Unit Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the converter panel to the frame
        Converter newContentPane = new Converter();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // Set frame parameters and show the frame to the user
        frame.pack();
        frame.setSize(635, 275);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    static void main() {
        // Create and show the frame on the event dispatch thread
        SwingUtilities.invokeLater(Converter::createAndShowGui);
    }

    /**
     * Combobox containing each supported group of units
     */
    private JComboBox<String> unitGroupBox;

    /**
     * The unit to convert from
     */
    private JComboBox<String> fromUnit;

    /**
     * The unit to convert to
     */
    private JComboBox<String> toUnit;

    /**
     * The value to convert into the new unit
     */
    private JTextField fromValue;

    /**
     * The result value in the new unit
     */
    private JTextField resultValue;

    /**
     * Entry field for string based conversions
     */
    private JTextField stringConversion;

    /**
     * {@link ConversionEngine} to perform all of the unit conversions
     */
    private final ConversionEngine engine;

    /**
     * Construct a new frame instance, creating the ui and setting the custom
     * look and feel
     */
    public Converter() {
        super(new GridBagLayout());

        // Initialise the engine
        engine = new ConversionEngine();

        // Initialise the components and make the GUI
        makeUI();

        try {
            // Set a custom look and feel for the frame determined by the
            // current OS
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called whenever action is taken on the components in the UI
     * <p>
     * Performs the conversion and displays the result in the UI
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // First check the conversion string, if it is not empty, perform the
        // conversion using the string and not the comboboxes
        if (!stringConversion.getText().isEmpty()) {
            convertString(stringConversion.getText());
            return;
        }

        // If the from value has no value then the result is empty
        if (fromValue.getText().isEmpty()) {
            resultValue.setText("");
        } else {
            // Get the from and to units
            String from = (String) fromUnit.getSelectedItem();
            String to = (String) toUnit.getSelectedItem();

            // Both of them must have a value
            if (from != null && to != null) {
                try {
                    // Value needs to be parsed into a double first
                    double val = Double.parseDouble(fromValue.getText());

                    // Perform the conversion and show it in the results field
                    resultValue.setText(Double.toString(MathUtils.round(
                            engine.convertAsDouble(val, from, to), 6)));
                }
                // Prompt the user that the input was not numerical
                catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(this, "Numerical input expected", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Use a conversion string to convert a value from one unit to another
     *
     * @param text The conversion string to use
     */
    private void convertString(String text) {
        // Make sure the conversion string is not empty
        if (!text.isEmpty()) {
            try {
                // Perform the conversion using the conversion string
                Conversion result = engine.convert(text);

                // Set the unit group to the one that matches the conversion
                unitGroupBox.setSelectedItem(Utils.toTitleCase(engine.getUnitGroupOfSubUnit(result
                        .getFrom())));

                // Set the from unit combobox value to the from unit in the
                // conversion string
                fromUnit.setSelectedItem(result.getFrom().toString());

                // Set the from unit combobox value to the from unit in the
                // conversion string
                toUnit.setSelectedItem(result.getTo().toString());

                // Set the result and from values to their respective values
                // from the conversion string
                resultValue.setText(Double.toString(result.getResult().doubleValue()));
                fromValue.setText(Double.toString(result.getValue().doubleValue()));
            }
            // If the conversion string was invalid show an error message
            catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Initialise the components, set up the main UI and add event handlers to
     * the components
     */
    private void makeUI() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // Get a list of all supported unit groups from the engine
        String[] groups = engine.getUnitGroups();
        for (int i = 0; i < groups.length; i++) {
            groups[i] = Utils.toTitleCase(groups[i]);
        }

        // Populate the unit group box with the list of unit groups
        unitGroupBox = new JComboBox<String>(groups);
        unitGroupBox.addActionListener(new ActionListener() {
            /**
             * Called when a new unit group is selected from the unit group
             * combobox {@inheritDoc}
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get a list of the new units corresponding to the newly
                // selected unit group
                String[] newUnits = engine.getUnitsFor(unitGroupBox.getItemAt(unitGroupBox
                        .getSelectedIndex()));

                // Replace the units in the from and to comboboxes with the new
                // units for the newly selected unit group
                replaceItems(fromUnit, newUnits);
                replaceItems(toUnit, newUnits);

                fromUnit.setSelectedIndex(2);
                toUnit.setSelectedIndex(3);
            }
        });

        unitGroupBox.setFont(largeFont);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;

        add(unitGroupBox, c);

        // Get a list of the default units for the default unit group
        String[] defaultUnits = engine.getUnitsFor(unitGroupBox.getItemAt(unitGroupBox
                .getSelectedIndex()));

        // Initialise the from and to comboboxes with the default set of units
        fromUnit = new JComboBox<>(defaultUnits);
        fromUnit.setMaximumRowCount(15);
        fromUnit.setSelectedIndex(2);
        fromUnit.addActionListener(this);
        fromUnit.setFont(largeFont);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;

        add(fromUnit, c);

        toUnit = new JComboBox<String>(defaultUnits);
        toUnit.setMaximumRowCount(15);
        toUnit.setSelectedIndex(3);
        toUnit.addActionListener(this);
        toUnit.setFont(largeFont);

        c.gridx = 3;

        add(toUnit, c);

        // Initialise the textfield that contains the value to convert from
        fromValue = new JTextField(20);
        fromValue.addActionListener(this);
        fromValue.setFont(smallFont);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;

        add(fromValue, c);

        // Initialise the equals label
        JLabel equalsLabel = new JLabel("=");
        equalsLabel.setFont(new Font("segoe UI", Font.BOLD, 16));

        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;

        add(equalsLabel, c);

        // Initialise the results value text field
        resultValue = new JTextField(20);
        resultValue.setFont(smallFont);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;

        add(resultValue, c);

        // Initialise the convert button
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(this);
        convertButton.setFont(largeFont);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        c.insets = new Insets(10, 10, 10, 10);

        add(convertButton, c);

        // Initialise the string conversion text field
        stringConversion = new JTextField(30);
        stringConversion.addActionListener(new ActionListener() {
            /**
             * When the enter key is pressed in the text field, perform the
             * string conversion and show the result in the UI {@inheritDoc}
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                convertString(stringConversion.getText());
            }
        });

        stringConversion.setFont(new Font("segoe UI", Font.PLAIN, 14));

        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 5;

        add(stringConversion, c);

        // Initialise the copy to clipboard button
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(largeFont);
        copyButton.addActionListener(new ActionListener() {
            /**
             * Run whenever the copy button is clicked {@inheritDoc}
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the results text field is not empty, add its contents to
                // the system clipboard
                if (!resultValue.getText().equals("")) {
                    StringSelection text = new StringSelection(resultValue.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, null);
                }
            }
        });

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridx = 3;

        add(copyButton, c);
    }

    /**
     * Replace the items in a {@link JComboBox} with an array of new items
     *
     * @param box      The {@link JComboBox} to replace the items of
     * @param newUnits The new items to populate the {@link JComboBox} with
     */
    private void replaceItems(JComboBox<String> box, String[] newUnits) {
        // Remove all the existing items
        box.removeAllItems();

        // Add each of the new items to the combobox
        for (String element : newUnits) {
            box.addItem(element);
        }
    }
}
