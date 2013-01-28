package uk.co.raharrison.mathengine.plotting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.raharrison.mathengine.Function;

public class Grapher extends JPanel implements MouseListener, MouseMotionListener, KeyListener,
		MouseWheelListener
{
	private static final long serialVersionUID = 809862551038356226L;

	private static final double PIXELS = 80.0;

	private static final double[] UNITS = { 0.00005, 0.0001, 0.0002, 0.0005, 0.001, 0.002, 0.005,
			0.01, 0.02, 0.05, 0.1, 0.2, 0.5, 1.0, 2.0, 5.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0,
			1000.0 };

	public boolean showGrid = true;

	protected double height;
	protected double width;

	protected boolean isDrag;
	protected boolean newBackground = true;

	protected double originX;
	protected double originY;
	protected double scale;
	protected double xOfY;
	protected double yOfX;

	protected int zoom = 14;
	protected Point newPoint;

	protected Function function;

	private Image backImage;
	private Graphics2D backGraphics;

	protected BasicStroke gridline = new BasicStroke(0.25f); // gridlines
	protected BasicStroke axes = new BasicStroke(1.0f); // axes
	protected BasicStroke curve = new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); // functions

	public Grapher()
	{
		function = new Function("x^2 + 8*x + 12");
		this.setBackground(Color.WHITE);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		setDoubleBuffered(true);
	}

	public String getEquation()
	{
		return function.getEquation();
	}

	public void setEquation(String equation)
	{
		function = new Function(equation);
		newBackground = true;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		width = this.getSize().getWidth();
		height = this.getSize().getHeight();
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (backImage == null || backImage.getWidth(this) != width
				|| backImage.getHeight(this) != height)
		{
			backImage = this.createImage((int) width, (int) height);
			backGraphics = (Graphics2D) (backImage.getGraphics());
			backGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			newBackground = true;
		}

		scale = PIXELS / UNITS[zoom];

		// draw graph of function f
		if (newBackground)
		{
			backGraphics.setColor(getBackground());
			backGraphics.fillRect(0, 0, (int) width, (int) height);
			if (showGrid)
				drawGridLines(backGraphics);

			drawAxes(backGraphics);

			try
			{
				drawFunction(function, backGraphics, Color.BLACK);
			}
			catch (Exception exc)
			{
				JOptionPane.showMessageDialog(this, exc.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			newBackground = false;
		}

		g.drawImage(backImage, 0, 0, this);
		drawPoint(g, 0, 0, 2.0, Color.GRAY);
		drawCrosshair(g);
	}

	public void drawCrosshair(Graphics2D g)
	{
		g.setStroke(new BasicStroke(0.5f));
		g.setColor(Color.gray);
		g.draw(new Line2D.Double(width / 2 - 5, height / 2, width / 2 + 5, height / 2));
		g.draw(new Line2D.Double(width / 2, height / 2 - 5, width / 2, height / 2 + 5));
	}

	public void drawGridLines(Graphics2D g)
	{
		g.setColor(Color.GRAY);
		g.setStroke(gridline);
		for (double A = (int) ((originY - height / 2 / scale) / UNITS[zoom]) - 1; A < (originY + height
				/ 2 / scale)
				/ UNITS[zoom] + 1; A = A + 0.2)
		{
			g.draw(new Line2D.Double(0, height / 2 - (A * UNITS[zoom] - originY) * scale, width,
					height / 2 - (A * UNITS[zoom] - originY) * scale));
		}
		for (double A = (int) ((originX - width / 2 / scale) / UNITS[zoom]) - 1; A < (originX + width
				/ 2 / scale)
				/ UNITS[zoom] + 1; A = A + 0.2)
		{
			g.draw(new Line2D.Double(width / 2 + (A * UNITS[zoom] - originX) * scale, 0, width / 2
					+ (A * UNITS[zoom] - originX) * scale, height));
		}
	}

	public void drawPoint(Graphics2D g, double x, double y, double r, Color color)
	{
		double[] P = toScreenPoint(x, y);
		Ellipse2D arc = new Ellipse2D.Double(P[0] - r, P[1] - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(color);
		g.fill(arc);
		g.setColor(Color.BLACK);
		g.draw(arc);
	}

	public double[] toCartesianPoint(double a, double b)
	{
		double[] out = { originX + (a - width / 2) / scale, originY - (b - height / 2) / scale };
		return out;
	}

	public double[] toScreenPoint(double a, double b)
	{
		double[] out = { width / 2 + (a - originX) * scale, height / 2 - (b - originY) * scale };
		return out;
	}

	// TODO : Update drawing method
	public void drawFunction(Function function, Graphics2D g, Color color)
	{
		g.setColor(color);
		g.setStroke(curve);
		double var = originX - width / 2.0 / scale;
		double y0 = height / 2 - (function.evaluateAt(var) - originY) * scale;
		double y1;

		for (double i = 0.0; i < width; i += 1.0)
		{
			var = originX + (i + 1 - width / 2) / scale;
			y1 = height / 2 - (function.evaluateAt(var) - originY) * scale;

			try
			{
				if (!Double.isNaN(y0) && !Double.isNaN(y1))
				{
					g.draw(new Line2D.Double(i, y0, i + 1, y1));
				}
			}
			catch (Exception e)
			{
			}

			y0 = y1;
		}
	}

	public void drawAxes(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.setStroke(axes);

		double[] P = toScreenPoint(0, 0);
		xOfY = P[0];
		if (P[0] < 10)
			xOfY = 10;
		else if (P[0] > width - 10)
			xOfY = width - 10;

		yOfX = P[1];
		if (P[1] < 10)
			yOfX = 10;
		else if (P[1] > height - 20)
			yOfX = height - 20;

		// draw y-axis
		g.draw(new Line2D.Double(xOfY, 0, xOfY, height));

		// draw x-axis
		g.draw(new Line2D.Double(0, yOfX, width, yOfX));

		// label y-axis
		float ww = (float) (xOfY + 4);
		String str;
		for (int A = (int) ((originY - height / 2 / scale) / UNITS[zoom]) - 1; A < (originY + height
				/ 2 / scale)
				/ UNITS[zoom] + 1; A++)
		{
			g.draw(new Line2D.Double(xOfY - 2, height / 2 - (A * UNITS[zoom] - originY) * scale,
					xOfY + 2, height / 2 - (A * UNITS[zoom] - originY) * scale));
			if (A != 0)
			{
				if ((int) (A * UNITS[zoom]) == A * UNITS[zoom])
					str = "" + (int) (A * UNITS[zoom]);
				else
					str = "" + (float) (A * UNITS[zoom]);
				// str = "" + (float)(A*units[zoom]);
				if (xOfY != 10)
					ww = Math.max(14, (float) (xOfY - 3) - g.getFontMetrics().stringWidth(str));
				g.drawString(str, ww, (float) (height / 2 - (A * UNITS[zoom] - originY) * scale)
						+ g.getFontMetrics().getHeight() / 3);
			}
		}

		// label x-axis
		float hh = (float) (yOfX + 1) + g.getFontMetrics().getHeight();
		if (yOfX == height - 1)
		{
			hh = (float) (yOfX - 4);
		}

		for (int A = (int) ((originX - width / 2 / scale) / UNITS[zoom]) - 1; A < (originX + width
				/ 2 / scale)
				/ UNITS[zoom] + 1; A++)
		{
			g.draw(new Line2D.Double(width / 2 + (A * UNITS[zoom] - originX) * scale, yOfX - 2,
					width / 2 + (A * UNITS[zoom] - originX) * scale, yOfX + 2));
			if (A != 0)
			{
				if ((int) (A * UNITS[zoom]) == A * UNITS[zoom])
					str = "" + (int) (A * UNITS[zoom]);
				else
					str = "" + (float) (A * UNITS[zoom]);
				g.drawString(str, (float) (width / 2 + (A * UNITS[zoom] - originX) * scale)
						- g.getFontMetrics().stringWidth(str) / 2, hh);
			}
		}
	}

	public void zoomIn()
	{
		zoom = Math.max(zoom - 1, 0);
		scale = PIXELS / UNITS[zoom];
		newBackground = true;
		repaint();
	}

	public void zoomOut()
	{
		zoom = Math.min(zoom + 1, UNITS.length - 1);
		scale = PIXELS / UNITS[zoom];
		newBackground = true;
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int notches = e.getWheelRotation();

		if (notches < 0)
		{
			zoomIn();
		}
		else
		{
			zoomOut();
		}

		newBackground = true;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT)
		{
			originX -= UNITS[zoom] * 2;
		}
		else if (key == KeyEvent.VK_RIGHT)
		{
			originX += UNITS[zoom] * 2;
		}
		else if (key == KeyEvent.VK_UP)
		{
			originY += UNITS[zoom] * 2;
		}
		else if (key == KeyEvent.VK_DOWN)
		{
			originY -= UNITS[zoom] * 2;
		}
		else if (key == KeyEvent.VK_SPACE)
		{
			originX = 0;
			originY = 0;
			zoom = 14;
		}

		newBackground = true;
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		originX -= (e.getPoint().x - newPoint.getX()) / scale;
		originY += (e.getPoint().y - newPoint.getY()) / scale;
		newPoint = e.getPoint();
		newBackground = true;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		newPoint = e.getPoint();
		double[] p = toCartesianPoint(newPoint.x, newPoint.y);
		if (e.getClickCount() > 1)
		{
			// set center so that P stays in same place
			int z = Math.max(0, zoom - 1);
			originX = p[0] - UNITS[z] * (newPoint.x - width / 2) / PIXELS;
			originY = p[1] + UNITS[z] * (newPoint.y - height / 2) / PIXELS;
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		newPoint = e.getPoint();
		requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		isDrag = false;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame frame = new JFrame("Grapher");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new Grapher());
				frame.setSize(550, 500);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
	}
}
