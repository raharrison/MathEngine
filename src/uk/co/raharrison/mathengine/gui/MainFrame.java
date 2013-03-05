package uk.co.raharrison.mathengine.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.Evaluator;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -3046696925938478655L;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				MainFrame frame = new MainFrame();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private SimpleAttributeSet bold, red;
	private JPanel panel;
	private JTextPane output;
	private JTextArea input;

	private Evaluator evaluator;

	public MainFrame()
	{
		super("Expression evaluator");

		bold = new SimpleAttributeSet();
		StyleConstants.setBold(bold, true);

		red = new SimpleAttributeSet();
		StyleConstants.setForeground(red, Color.RED);

		evaluator = Evaluator.newEvaluator();
		evaluator.setAngleUnit(AngleUnit.Radians);

		panel = new JPanel();

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
		input.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent event)
			{
				if (event.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String expression = input.getText().trim();
					if (!expression.equals(""))
					{
						if (expression.equalsIgnoreCase("clear"))
						{
							output.setText("");
						}
						else
						{
							try
							{
								evaluator.compileTree(expression);
								output.getDocument().insertString(
										output.getDocument().getLength(),
										"> "
												+ Utils.removeOuterParenthesis(evaluator
														.getCachedTreeToString()) + "\n", null);
								output.getDocument().insertString(output.getDocument().getLength(),
										evaluator.evaluateCachedTreeString() + "\n", bold);
							}
							catch (Exception e)
							{
								try
								{
									output.getDocument().insertString(
											output.getDocument().getLength(), e.toString() + "\n",
											red);
								}
								catch (BadLocationException e1)
								{
									e1.printStackTrace();
								}
							}
						}
					}

					event.consume();
					input.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
			}

			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}
		});

		panel.add(pane2, BorderLayout.SOUTH);

		add(panel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setResizable(false);

		input.requestFocus();
	}
}
