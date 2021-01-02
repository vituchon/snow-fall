/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import base.Background;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class SnowBackground implements Background {
	private final Point[] points;
	private int top;
	private final int size;
	private int step, last;

	private BufferedImage canvas;
	private int width, height;

	private final Color backgroundColor = Color.BLACK;
	private final Color pointColor = Color.WHITE;
	private final static Point NULL_POINT = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

	private final static Logger LOOGER = Logger.getLogger(SnowBackground.class.getCanonicalName());

	public SnowBackground(int size, BufferedImage canvas) {
		this.points = new Point[size];
		this.size = size;
		this.step = 0;
		this.last = 0;
		this.top = 0;

		for (int i = 0; i < size; i++) {
			this.points[i] = NULL_POINT;
		}
		// this.height = 10;
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		this.canvas = canvas;
	}

	@Override
	public void animate(int steps) {
		for (int i = 0; i < steps; i++) {
			this.step++;
			movePoints();
			int acc = (this.step * this.size) / this.height;
			int diff = acc - this.last;
			this.last = acc;
			putPoints(diff);
			// System.out.printf("En paso %d el total de bolas a meter es %d, y el acumulado
			// ser�a de %d\n", new Object[]{this.step, diff, acc});
			/*
			 * try { Thread.sleep(2000); } catch (InterruptedException ex) {
			 * Logger.getLogger(SnowBackground.class.getName()).log(Level.SEVERE, null, ex);
			 * }
			 */
		}
	}

	public static void main(String[] args) {
		SnowBackground sb = new SnowBackground(10, null);
		sb.animate(20);
	}

	@Override
	public void render(Graphics g) {
		fillBackground(backgroundColor);
		paintPoints(pointColor);
	}

	private void fillBackground(Color color) {
		int rgb = color.getRGB();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				canvas.setRGB(x, y, rgb);
			}
		}
	}

	private void paintPoints(Color color) {
		int rgb = color.getRGB();
		for (Point point : points) {
			if (point != NULL_POINT) {
				if (point.y > canvas.getHeight()) {
					int stop = 0;
					System.out.println(stop);
				}
				canvas.setRGB(point.x, point.y, rgb);
			}
		}
	}

	private static final Random r = new Random();

	private void putPoints(int diff) {
		while (diff > 0) {
			int slot = this.top % this.size;
			int x = r.nextInt(width);
			// System.out.printf("Ocupo en slot %d para nuevo punto, previamente ocupado por
			// %s\n ", new Object[]{slot, this.points[slot].toString()});
			if (this.points[slot] == NULL_POINT) {
				this.points[slot] = new Point(x, 0);
			} else {
				this.points[slot].setLocation(x, 0);
			}
			this.top++;
			diff--;
		}
	}

	private void movePoints() {
		for (int i = 0; i < this.size; i++) {
			Point point = points[i];
			if (point != NULL_POINT) {
				// System.out.printf("Muevo punto en slot %d de %d -> %d \n", new Object[]{i,
				// this.points[i].y, this.points[i].y + 1});
				point.y++;
				if (point.y > this.height) {
					points[i] = NULL_POINT;
				}
			}
		}
	}

}
