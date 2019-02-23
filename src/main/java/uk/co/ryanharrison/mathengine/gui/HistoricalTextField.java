package uk.co.ryanharrison.mathengine.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom {@link TextArea} which maintains a history of previously entered
 * commands which can be accessed through the up/down arrows.
 * 
 * This mimics the command history in a console/terminal window
 * 
 * @author Ryan Harrison
 * 
 */
public class HistoricalTextField extends JTextArea
{
	/** Serialization identifier */
	private static final long serialVersionUID = 8211505575286267909L;

	/** A history of all previously entered commands */
	private List<String> history;

	/** The current command being shown in the textfield */
	private int currentIndex;

	/**
	 * Construct a new {@link HistoricalTextField} with specified number of rows
	 * and columns
	 * 
	 * @param rows
	 *            The number of rows to display
	 * @param columns
	 *            The number of columns to display
	 */
	public HistoricalTextField(int rows, int columns)
	{
		super(rows, columns);
		history = new ArrayList<String>();
		currentIndex = 0;

		addKeyListener(new KeyListener()
		{
			/**
			 * Respond to the key pressed event
			 * 
			 * If the enter key is pressed, add the current command to the
			 * history and clear the field
			 * 
			 * If the up/down arrows are pressed, show the corresponding command
			 * in the history
			 * 
			 * {@inheritDoc}
			 */
			@Override
			public void keyPressed(KeyEvent e)
			{
				// If the enter key was pressed
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					// If the current command is not already in the history, add
					// it
					int index = history.indexOf(getText());
					if (index == -1)
						history.add(getText());
					// Otherwise move the command to the front of the list
					else
					{
						history.remove(index);
						history.add(getText());
					}
					currentIndex = history.size();
				}
				// If the up arrow key was pressed, show the previous command in
				// the history if present
				else if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					if (currentIndex - 1 >= 0)
						setText(history.get(--currentIndex));
				}
				// If the down arrow key is pressed show the next command in the
				// history until we reach the current command being entered
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if (currentIndex + 1 < history.size())
						setText(history.get(++currentIndex));
					else
					{
						setText("");
						currentIndex = history.size();
					}
				}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void keyReleased(KeyEvent e)
			{
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void keyTyped(KeyEvent e)
			{
			}
		});
	}
}
