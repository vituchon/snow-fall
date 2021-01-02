/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class Scenario {
	private volatile Collection<Particle> particles;
	private volatile List<Cloud> clouds;
	private Hill hill;
	private int maxParticles;
	private BufferedImage canvas;

	public Scenario(int maxParticles, BufferedImage canvas) {
		this.particles = new FixedSizeArray<>(maxParticles);
		this.maxParticles = maxParticles;
		this.hill = new Hill(canvas.getWidth(), canvas.getHeight());
		this.clouds = new ArrayList<Cloud>();
		this.canvas = canvas;
	}

	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

	public Collection<Particle> getParticles() {
		return this.particles;
	}

	public int getMaxParticles() {
		return maxParticles;
	}

	public Hill getHill() {
		return hill;
	}

	public BufferedImage getCanvas() {
		return canvas;
	}

	public boolean removeParticle(Particle particle) {
		return this.particles.remove(particle);
	}

	public List<Cloud> getClouds() {
		return this.clouds;
	}

	private static class FixedSizeArray<T> extends ArrayList<T> {

		private final int maxSize;
		private int top;

		public FixedSizeArray(int maxSize) {
			super(maxSize);
			this.top = 0;
			this.maxSize = maxSize;
		}

		@Override
		public boolean add(T e) {
			int ocupation = super.size();
			if (ocupation < this.maxSize) {
				super.add(e);
			} else {
				int slot = this.top % this.maxSize;
				super.set(slot, e);
			}
			this.top++;
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			for (T t : c) {
				this.add(t);
			}
			return c.size() == 0 ? false : true;
		}

	}

}
