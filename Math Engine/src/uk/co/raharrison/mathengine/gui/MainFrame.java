package uk.co.raharrison.mathengine.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.Evaluator;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

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

		evaluator = new Evaluator();
		evaluator.setAngleUnit(AngleUnit.Radians);

		panel = new JPanel();

		BorderLayout layout = new BorderLayout(25, 25);
		panel.setLayout(layout);

		output = new JTextPane();
		output.setEditable(false);
		JScrollPane pane = new JScrollPane(output);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(pane, BorderLayout.CENTER);

		input = new JTextArea(3, 40);
		JScrollPane pane2 = new JScrollPane(input);
		input.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent event)
			{
				if (event.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (!input.getText().trim().equals(""))
					{
						if (input.getText().trim().toLowerCase().equals("clear"))
						{
							output.setText("");
						}
						else
						{
							try
							{
								output.getDocument().insertString(output.getDocument().getLength(),
										"> " + input.getText() + "\n", null);
								output.getDocument().insertString(output.getDocument().getLength(),
										evaluator.evaluateString(input.getText()) + "\n", bold);
							}
							catch (Exception e)
							{
								try
								{
									output.getDocument().insertString(
											output.getDocument().getLength(),
											e.getMessage() + "\n", red);
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
