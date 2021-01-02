/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import base.GameRender;
import model.Cloud;
import model.Particle;
import model.Scenario;

/**
 *
 * @author Administrador
 */
public class CustomGameRender implements GameRender {
	private final Color backgroundColor = Color.BLACK;
	private final Color pointColor = Color.WHITE;
	private BufferedImage cloudImage;

	private final Scenario scenario;

	public CustomGameRender(Scenario scenario) {
		this.scenario = scenario;
		this.cloudImage = Cloud.cloudImage;
	}

	@Override
	public void render(BufferedImage canvas) {
		Graphics g = canvas.createGraphics();
		fillBackground(canvas, backgroundColor);
		paintClouds(canvas, g);
		paintParticles(canvas, pointColor);
		paintHill(canvas, g);
		g.dispose();
	}

	private void fillBackground(BufferedImage canvas, Color color) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		Graphics g = canvas.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

	private void paintParticles(BufferedImage canvas, Color color) {
		for (Particle particle : scenario.getParticles()) {
			if (particle.y < canvas.getHeight()) {
				int rgb = color.getRGB(); // (particle.vy == 1) ? Color.WHITE.getRGB() : color.getRGB();
				if (particle.x > 0 && particle.x < canvas.getWidth()) {
					canvas.setRGB(particle.x, particle.y, rgb);
				}
				// System.out.println(particle.vy);
				/*
				 * int trace = (int)particle.vy; while (trace > 10) { canvas.setRGB(particle.x,
				 * particle.y - (int)(trace-10), rgb); trace--; }
				 */
			}
		}
	}

	public void paintHill(BufferedImage canvas, Graphics g) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		g.setColor(Color.GREEN);
		for (int x = 0; x < width; x++) {
			int y = this.scenario.getHill().getTop(x);
			g.drawRect(x, y, 1, height - y);
			x++;
		}
	}

	public void paintClouds(BufferedImage canvas, Graphics g) {
		List<Cloud> clouds = this.scenario.getClouds();
		for (Cloud cloud : clouds) {
			g.drawImage(this.cloudImage, cloud.x, cloud.y, null);
		}

	}

}
