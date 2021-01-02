/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Administrador
 */
public class Hill {
	private final int width, height;
	private final int[] tops;
	private double f1 = 0.005;
	private double c1 = 0.015;
	private double f2 = 0.01;
	private double c2 = 0.8;
	private double m = 100;
	private double o = 280;

	public Hill(int width, int height) {
		this.width = width;
		this.height = height;
		this.tops = new int[width];
		plot();
	}

	private void plot() {
		for (int x = 0; x < width; x++) {
			tops[x] = (int) y(x);
		}
	}

	private int y(int x) {
		return (int) Math.round((Math.sin((x) * f1) * c1 + Math.cos((x) * f2) * c2) * m + o);
	}

	public int getTop(int x) {
		return tops[x];
	}

	public void setTop(int x, int newY) {
		this.tops[x] = newY;
	}

}
