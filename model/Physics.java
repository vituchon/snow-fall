/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import base.GamePhysics;
import main.SnowBackground;

/**
 *
 * @author Administrador
 */
public class Physics implements GamePhysics {
	private final Scenario scenario;
	private final ParticlesPerStepCalculator particlesPerStepCalculator;
	private int step;

	private int width, height;

	public final static Point NULL_POINT = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

	private final static Logger LOOGER = Logger.getLogger(SnowBackground.class.getCanonicalName());

	public Physics(Scenario scenario) {
		BufferedImage canvas = scenario.getCanvas();
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		this.scenario = scenario;
		this.step = 0;
		this.particlesPerStepCalculator = new ParticlesPerStepCalculator(scenario.getMaxParticles(), height);
	}

	@Override
	public void apply() {
		this.step++;
		// addParticle(this.particlesPerStepCalculator.calc(this.step));
		animateClouds(5);
		moveParticles();
	}

	public static final double G = 1.0D;
	private static final Random r = new Random();

	private void addParticle(int quantity) {
		while (quantity > 0) {
			int x = r.nextInt(width);
			Particle particle = new Particle(x, 0);
			particle.vy = 1;
			particle.ay = G;
			this.scenario.addParticle(particle);
			quantity--;
		}
	}

	public void animateClouds(int maxDroppedParticles) {
		List<Cloud> clouds = this.scenario.getClouds();
		Collection<Particle> particles = this.scenario.getParticles();
		for (Cloud cloud : clouds) {
			if (r.nextFloat() > 0.005) {
				particles.addAll(dropParticles(cloud, maxDroppedParticles));
			}
		}
	}

	public List<Particle> dropParticles(Cloud cloud, int quantity) {
		final int amplitude = cloud.w / 2;
		final int[] offsetsX = { -(amplitude - 10), -((amplitude * 2) / 3), -((amplitude * 1) / 3), 0,
				((amplitude * 1) / 3), ((amplitude * 2) / 3), (amplitude - 10) };
		final int[] offsetsY = { -25, -20, -15 };
		List<Particle> particles = new ArrayList<Particle>(quantity);
		for (int i = 0; i < quantity; i++) {
			final int offsetX = offsetsX[r.nextInt(offsetsX.length)];
			final int offsetY = offsetsY[r.nextInt(offsetsY.length)];
			Particle particle = new Particle(cloud.x + amplitude + offsetX, cloud.y + offsetY + cloud.h);
			particle.vy = 1;
			particle.ay = Physics.G;
			particles.add(particle);
		}
		return particles;
	}

	private void moveParticles() {
		Collection<Particle> particles = this.scenario.getParticles();
		Hill hill = this.scenario.getHill();

		Collection<Particle> landedParticles = new ArrayList<>(5);
		Collection<Particle> movingParticles = new ArrayList<>(5);
		for (Particle particle : particles) {
			boolean particleNotFalling = (particle.vy == 0);
			if (particleNotFalling) { // it landed on the hill or on top of another particle
				landedParticles.add(particle);
			} else {
				movingParticles.add(particle);
			}
		}

		final int[] thrust = { -1, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
		for (Particle particle : movingParticles) {
			final int windThrust = thrust[r.nextInt(thrust.length)];
			particle.vy *= particle.ay;
			particle.y += particle.vy;

			particle.vx *= particle.ax;
			particle.x += particle.vx + windThrust;

			if (particle.x < 0) {
				particle.ax = 0;
				particle.vx = 0;
				particle.x = 0;
			}
			if (particle.x > this.width - 1) {
				particle.ax = 0;
				particle.vx = 0;
				particle.x = this.width - 1;
			}

			for (Particle landedParticle : landedParticles) {
				if (particle.x == landedParticle.x && particle.y >= landedParticle.y) {
					particle.vy = 0;
					particle.ay = 0;
					particle.y = landedParticle.y - 1;
				}
			}

			int hillTop = hill.getTop(particle.x);
			if (particle.y >= hillTop) {
				particle.vy = 0;
				particle.ay = 0;
				particle.y = hillTop - 1;
			}
		}

		for (Particle particle : landedParticles) {
			int hillTop = hill.getTop(particle.x);
			boolean isNotOnHillTop = (hillTop - particle.y) > 1;
			if (isNotOnHillTop) {
				// Particle landedParticleBelow = searchParticleAt(particle.x, particle.y +
				// 1,landedParticles);
				// System.out.println("Particula en posicion (" + particle.x + "," + particle.y
				// + ") se encuentra por encima de otra particula");
				// System.out.println("hillTop :" + hillTop + ", particle.y: " + particle.y + ",
				// diff: " + (hillTop - particle.y));
				// System.out.println("La otra particula es (" + landedParticleBelow.x + "," +
				// landedParticleBelow.y + ")");
				Particle landedParticuleAtRight = searchParticleAt(particle.x + 1, particle.y + 1, landedParticles);
				Particle landedParticuleAtLeft = searchParticleAt(particle.x - 1, particle.y + 1, landedParticles);
				if (landedParticuleAtRight == null && particle.x < this.width - 1) { // there is void space to the right
					particle.y += 1;
					particle.x += 1;
				} else if (landedParticuleAtLeft == null && particle.x > 0) { // there is void space to the left
					particle.y += 1;
					particle.x -= 1;
				} else {
					// System.out.println("Particula en posicion (" + particle.x + "," + particle.y
					// + ") se encuentra detenida");
				}
			}
		}

	}

	private Particle searchParticleAt(int x, int y, Collection<Particle> landedParticles) {
		for (Particle particle : landedParticles) {
			if (particle.x == x && particle.y == y) {
				return particle;
			}
		}
		return null;
	}

}
