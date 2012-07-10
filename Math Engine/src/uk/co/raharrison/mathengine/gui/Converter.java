package uk.co.raharrison.mathengine.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.co.raharrison.mathengine.MathUtils;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;
import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;

public class Converter extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -5327414551785314846L;
	private static final Font largeFont = new Font("segoe UI", Font.BOLD, 14);
	private static final Font smallFont = new Font("segoe UI", Font.PLAIN, 12);

	private static void createAndShowGui()
	{
		JFrame frame = new JFrame("Unit Converter © Ryan Harrison 2012");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new Converter();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setSize(635, 275);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				createAndShowGui();
			}
		});
	}

	private JButton convertButton;
	private JButton copyButton;

	private JComboBox<String> unitGroupBox;
	private JComboBox<String> fromUnit;
	private JComboBox<String> toUnit;

	private JTextField fromValue;
	private JTextField resultValue;
	private JTextField stringConversion;

	private JLabel equalsLabel;

	private ConversionEngine engine;

	public Converter()
	{
		super(new GridBagLayout());

		engine = new ConversionEngine();

		makeUI();

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(!stringConversion.getText().equals(""))
		{
			convertString(stringConversion.getText());
			return;
		}
		
		if (fromValue.getText().equals(""))
		{
			resultValue.setText("");
		}
		else
		{
			String from = (String) fromUnit.getSelectedItem();
			String to = (String) toUnit.getSelectedItem();

			if (from != null && to != null)
			{
				try
				{
					double val = Double.parseDouble(fromValue.getText());

					resultValue.setText(Double.toString(MathUtils.round(
							engine.convert(val, from, to), 6)));
				}
				catch (NumberFormatException e2)
				{
					JOptionPane.showMessageDialog(this, "Numerical input expected", "Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IllegalArgumentException e3)
				{
				}
			}
		}
	}

	private void makeUI()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		unitGroupBox = new JComboBox<String>(engine.getUnitGroups());
		unitGroupBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String[] newUnits = engine.getUnitsFor(unitGroupBox.getItemAt(unitGroupBox
						.getSelectedIndex()));

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

		String[] defaultUnits = engine.getUnitsFor(unitGroupBox.getItemAt(unitGroupBox
				.getSelectedIndex()));

		fromUnit = new JComboBox<String>(defaultUnits);
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

		fromValue = new JTextField(20);
		fromValue.addActionListener(this);
		fromValue.setFont(smallFont);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1.0;

		add(fromValue, c);

		equalsLabel = new JLabel("=");
		equalsLabel.setFont(new Font("segoe UI", Font.BOLD, 16));

		c.gridx = 2;
		c.fill = GridBagConstraints.NONE;

		add(equalsLabel, c);

		resultValue = new JTextField(20);
		resultValue.setFont(smallFont);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;

		add(resultValue, c);

		convertButton = new JButton("Convert");
		convertButton.addActionListener(this);
		convertButton.setFont(largeFont);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 3;
		c.insets = new Insets(10, 10, 10, 10);

		add(convertButton, c);

		stringConversion = new JTextField(30);
		stringConversion.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				convertString(stringConversion.getText());
			}
		});
		
		stringConversion.setFont(new Font("segoe UI", Font.PLAIN, 14));

		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;

		add(stringConversion, c);

		copyButton = new JButton("Copy");
		copyButton.setFont(largeFont);
		copyButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!resultValue.getText().equals(""))
				{
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

	private void replaceItems(JComboBox<String> box, String[] newUnits)
	{
		box.removeAllItems();

		for (String element : newUnits)
		{
			box.addItem(element);
		}
	}
	

	private void convertString(String text)
	{
		if(!text.equals(""))
		{
			try
			{
				ConversionParams result = engine.getResultConversionParams(text);
				
				unitGroupBox.setSelectedItem(engine.getUnitGroupOfSubUnit(result.getFrom()));
				
				fromUnit.setSelectedItem(result.getFrom().toString());
				toUnit.setSelectedItem(result.getTo().toString());
				resultValue.setText(Double.toString(result.getResult()));
				fromValue.setText(Double.toString(result.getValue()));
			}
			catch (IllegalArgumentException e)
			{
				JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
