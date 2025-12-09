package uk.co.ryanharrison.mathengine.plotting;

import uk.co.ryanharrison.mathengine.core.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Modern interactive function grapher application.
 * <p>
 * This application provides professional-grade function plotting with:
 * </p>
 * <ul>
 *     <li><b>Multi-function support:</b> Plot multiple functions simultaneously with different colors</li>
 *     <li><b>Interactive navigation:</b> Mouse drag to pan, wheel to zoom, keyboard shortcuts</li>
 *     <li><b>Adaptive rendering:</b> Smart sampling handles discontinuities and extreme values</li>
 *     <li><b>Function management:</b> Add, edit, remove, and toggle visibility of functions</li>
 *     <li><b>Real-time feedback:</b> Status bar shows zoom level, bounds, and coordinates</li>
 * </ul>
 *
 * <h2>Controls:</h2>
 * <ul>
 *     <li><b>Mouse:</b> Drag to pan, wheel to zoom, double-click to zoom in</li>
 *     <li><b>Keyboard:</b> Arrow keys to pan, +/- to zoom, Space to reset view</li>
 *     <li><b>Menu:</b> File menu for common operations, Help menu for controls</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create and show grapher
 * Grapher grapher = new Grapher();
 * grapher.setVisible(true);
 *
 * // Set equation programmatically
 * grapher.setEquation("sin(x) * exp(-x/10)");
 * }</pre>
 */
public class Grapher extends JFrame {
    private final GrapherPanel grapherPanel;
    private final FunctionListPanel functionListPanel;
    private final GrapherStatusBar statusBar;

    private static final Color[] SAMPLE_COLORS = {
            new Color(0, 102, 204),      // Blue
            new Color(204, 0, 0),        // Red
            new Color(0, 153, 0)         // Green
    };

    /**
     * Creates a new Grapher application window.
     */
    public Grapher() {
        super("Math Engine - Function Grapher");

        // Initialize components
        grapherPanel = new GrapherPanel();
        functionListPanel = new FunctionListPanel();
        statusBar = new GrapherStatusBar();

        setupUI();
        setupEventHandlers();
        addSampleFunctions();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    /**
     * Sets the equation of the primary function (for backward compatibility).
     * <p>
     * If functions already exist, this replaces the first function.
     * Otherwise, it adds a new function.
     * </p>
     *
     * @param equation the equation string (e.g., "x^2 + 2*x + 1")
     */
    public void setEquation(String equation) {
        try {
            PlottedFunction newFunc = PlottedFunction.builder()
                    .equation(equation)
                    .name("f(x)")
                    .color(SAMPLE_COLORS[0])
                    .strokeWidth(2.5f)
                    .visible(true)
                    .build();

            if (grapherPanel.getFunctions().isEmpty()) {
                grapherPanel.addFunction(newFunc);
                functionListPanel.addFunction(newFunc);
            } else {
                grapherPanel.replaceFunction(0, newFunc);
                functionListPanel.updateFunction(0, newFunc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid equation: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Gets the equation of the primary function (for backward compatibility).
     *
     * @return the equation string, or empty string if no functions exist
     */
    public String getEquation() {
        if (grapherPanel.getFunctions().isEmpty()) {
            return "";
        }
        return grapherPanel.getFunctions().getFirst().getEquation();
    }

    /**
     * Gets the grapher panel for direct access.
     *
     * @return the grapher panel
     */
    public GrapherPanel getGrapherPanel() {
        return grapherPanel;
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        setJMenuBar(menuBar);

        // Create toolbar
        JToolBar toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);

        // Main content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(grapherPanel);
        splitPane.setRightComponent(functionListPanel);
        splitPane.setDividerLocation(900);
        splitPane.setResizeWeight(1.0);

        add(splitPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem addFunctionItem = new JMenuItem("Add Function...", KeyEvent.VK_A);
        addFunctionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        addFunctionItem.addActionListener(e -> showQuickAddDialog());

        JMenuItem clearAllItem = new JMenuItem("Clear All Functions", KeyEvent.VK_C);
        clearAllItem.addActionListener(e -> clearAllFunctions());

        JMenuItem resetViewItem = new JMenuItem("Reset View", KeyEvent.VK_R);
        resetViewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        resetViewItem.addActionListener(e -> resetView());

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(addFunctionItem);
        fileMenu.add(clearAllItem);
        fileMenu.addSeparator();
        fileMenu.add(resetViewItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        JMenuItem zoomInItem = new JMenuItem("Zoom In", KeyEvent.VK_I);
        zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        zoomInItem.addActionListener(e -> zoomIn());

        JMenuItem zoomOutItem = new JMenuItem("Zoom Out", KeyEvent.VK_O);
        zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        zoomOutItem.addActionListener(e -> zoomOut());

        JMenuItem centerOriginItem = new JMenuItem("Center on Origin", KeyEvent.VK_C);
        centerOriginItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        centerOriginItem.addActionListener(e -> resetView());

        viewMenu.add(zoomInItem);
        viewMenu.add(zoomOutItem);
        viewMenu.addSeparator();
        viewMenu.add(centerOriginItem);

        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem controlsItem = new JMenuItem("Controls", KeyEvent.VK_C);
        controlsItem.addActionListener(e -> showControlsHelp());

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAbout());

        helpMenu.add(controlsItem);
        helpMenu.add(aboutItem);

        return helpMenu;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton addButton = new JButton("Add Function");
        addButton.addActionListener(e -> showQuickAddDialog());

        JButton clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> clearAllFunctions());

        JButton resetButton = new JButton("Reset View");
        resetButton.addActionListener(e -> resetView());

        JButton zoomInButton = new JButton("Zoom In (+)");
        zoomInButton.addActionListener(e -> zoomIn());

        JButton zoomOutButton = new JButton("Zoom Out (-)");
        zoomOutButton.addActionListener(e -> zoomOut());

        toolBar.add(addButton);
        toolBar.add(clearButton);
        toolBar.addSeparator();
        toolBar.add(zoomInButton);
        toolBar.add(zoomOutButton);
        toolBar.add(resetButton);

        return toolBar;
    }

    private void setupEventHandlers() {
        // Link function list panel to grapher panel
        functionListPanel.addFunctionListListener(new FunctionListPanel.FunctionListListener() {
            @Override
            public void onFunctionAdded(PlottedFunction function) {
                grapherPanel.addFunction(function);
                updateStatusBar();
            }

            @Override
            public void onFunctionUpdated(int index, PlottedFunction function) {
                grapherPanel.replaceFunction(index, function);
                updateStatusBar();
            }

            @Override
            public void onFunctionRemoved(int index) {
                if (index >= 0 && index < grapherPanel.getFunctions().size()) {
                    grapherPanel.removeFunction(grapherPanel.getFunctions().get(index));
                }
                updateStatusBar();
            }
        });

        // Update status bar when view changes
        grapherPanel.addGrapherEventListener(new GrapherPanel.GrapherEventListener() {
            @Override
            public void onGraphChanged() {
                updateStatusBar();
            }

            @Override
            public void onViewChanged(GraphCoordinateSystem coords) {
                updateStatusBar();
            }

            @Override
            public void onMouseMoved(double x, double y) {
                statusBar.updateCoordinates(x, y);
            }

            @Override
            public void onMouseExited() {
                statusBar.clearCoordinates();
            }
        });

        // Initial status update
        updateStatusBar();
    }

    private void addSampleFunctions() {
        // Add a default function to get started
        PlottedFunction defaultFunc = PlottedFunction.builder()
                .equation("sin(x)")
                .name("sin(x)")
                .color(SAMPLE_COLORS[0])
                .strokeWidth(2.5f)
                .visible(true)
                .build();

        grapherPanel.addFunction(defaultFunc);
        functionListPanel.addFunction(defaultFunc);
        updateStatusBar();
    }

    private void showQuickAddDialog() {
        String equation = JOptionPane.showInputDialog(
                this,
                "Enter function equation (e.g., x^2, sin(x), exp(x)):",
                "Add Function",
                JOptionPane.PLAIN_MESSAGE
        );

        if (equation != null && !equation.trim().isEmpty()) {
            try {
                Function func = new Function(equation.trim());
                PlottedFunction plottedFunc = PlottedFunction.builder()
                        .function(func)
                        .name(equation.trim())
                        .color(SAMPLE_COLORS[grapherPanel.getFunctions().size() % SAMPLE_COLORS.length])
                        .strokeWidth(2.5f)
                        .visible(true)
                        .build();

                grapherPanel.addFunction(plottedFunc);
                functionListPanel.addFunction(plottedFunc);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid equation: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void clearAllFunctions() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove all functions?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            grapherPanel.clearFunctions();
            functionListPanel.clearFunctions();
        }
    }

    private void resetView() {
        GraphCoordinateSystem coords = grapherPanel.getCoordinateSystem();
        coords.resetView();
        grapherPanel.forceRedraw();
        grapherPanel.repaint();
        updateStatusBar();
    }

    private void zoomIn() {
        double centerX = grapherPanel.getWidth() / 2.0;
        double centerY = grapherPanel.getHeight() / 2.0;
        GraphCoordinateSystem coords = grapherPanel.getCoordinateSystem();
        coords.zoomToward(centerX, centerY, true);
        grapherPanel.forceRedraw();
        grapherPanel.repaint();
        updateStatusBar();
    }

    private void zoomOut() {
        double centerX = grapherPanel.getWidth() / 2.0;
        double centerY = grapherPanel.getHeight() / 2.0;
        GraphCoordinateSystem coords = grapherPanel.getCoordinateSystem();
        coords.zoomToward(centerX, centerY, false);
        grapherPanel.forceRedraw();
        grapherPanel.repaint();
        updateStatusBar();
    }

    private void updateStatusBar() {
        statusBar.updateFromCoordinateSystem(
                grapherPanel.getCoordinateSystem(),
                grapherPanel.getFunctions().size()
        );
    }

    private void showControlsHelp() {
        String helpText = """
                MOUSE CONTROLS:
                • Drag: Pan the viewport
                • Wheel: Zoom in/out (centered on cursor)
                • Double-click: Zoom in at click point
                
                KEYBOARD CONTROLS:
                • Arrow keys: Pan viewport
                • +/- or =/−: Zoom in/out
                • Space: Reset to default view
                • Ctrl+N: Add new function
                • Ctrl+R: Reset view
                
                FUNCTION MANAGEMENT:
                • Click "Add Function" to plot a new function
                • Use the function list panel to:
                  - Toggle visibility (checkbox)
                  - Edit equations and colors
                  - Remove functions
                
                TIPS:
                • Hover over the graph to see coordinates
                • The status bar shows zoom level and visible range
                • Discontinuous functions are handled automatically
                • Overflow values (like exp(1000)) are clipped gracefully
                """;

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(getBackground());

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(textArea),
                "Grapher Controls",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showAbout() {
        String aboutText = """
                Math Engine Function Grapher
                
                A professional-grade interactive function plotter with:
                • Multi-function support with customizable colors
                • Adaptive sampling for smooth curves
                • Discontinuity detection and handling
                • Overflow/precision protection
                • Real-time coordinate display
                
                Part of the Math Engine library - a comprehensive
                Java mathematical toolkit for expression evaluation,
                calculus, linear algebra, and more.
                """;

        JOptionPane.showMessageDialog(
                this,
                aboutText,
                "About Grapher",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Main entry point for the Grapher application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            Grapher grapher = new Grapher();
            grapher.setVisible(true);
        });
    }
}
