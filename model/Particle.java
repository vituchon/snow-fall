/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Administrador
 */
public class Particle {
	public int x, y;
	public double vx, vy;
	public double ax, ay;

	public Particle(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Particle other = (Particle) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + this.x;
		hash = 43 * hash + this.y;
		return hash;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
