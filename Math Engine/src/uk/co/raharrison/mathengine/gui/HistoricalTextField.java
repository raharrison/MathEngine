package uk.co.raharrison.mathengine.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class HistoricalTextField extends JTextArea
{
	private static final long serialVersionUID = 8211505575286267909L;

	private ArrayList<String> history;
	private int currentIndex;

	public HistoricalTextField(int rows, int columns)
	{
		super(rows, columns);
		history = new ArrayList<String>();
		currentIndex = 0;

		addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					int index = history.indexOf(getText());
					if (index == -1)
						history.add(getText());
					else
					{
						history.remove(index);
						history.add(getText());
					}
					currentIndex = history.size();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					if (currentIndex - 1 >= 0)
						setText(history.get(--currentIndex));
				}
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

			@Override
			public void keyReleased(KeyEvent e)
			{
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
			}
		});
	}
}
