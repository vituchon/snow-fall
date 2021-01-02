/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Random;

/**
 *
 * @author Administrador
 */
public class ParticlesPerStepCalculator {
	private int size, last, height;

	public ParticlesPerStepCalculator(int maxParticles, int height) {
		this.size = maxParticles;
		this.last = 0;
		this.height = height;
	}

	Random r = new Random();

	public int calc(int step) {
		int acc = (step * this.size) / this.height;
		// int diff = acc - this.last;
		this.last = acc;
		// return diff;
		return quant[r.nextInt(quant.length)];
		// return r.nextFloat() > 0.90 ? 1 : 0;
	}

	private int[] quant = new int[] { 0, 0, 1 };
}
