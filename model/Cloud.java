package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Cloud {

	public int x, y;

	public Cloud(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static final int w;
	public static final int h;

	private static final Logger LOGGER = Logger.getLogger(Cloud.class.getName());
	public static BufferedImage cloudImage;
	static {
		InputStream is = Cloud.class.getResourceAsStream("/assets/cloud.png");
		int imageWidth = 0, imageHeight = 0;
		try {
			cloudImage = ImageIO.read(is);
			imageWidth = cloudImage.getWidth();
			imageHeight = cloudImage.getHeight();
		} catch (IOException e) {
			LOGGER.severe("No se puedo cargar la imagen de la nube. Logeando excepcion abajo.");
			LOGGER.severe(e.getMessage());

		}
		w = imageWidth;
		h = imageHeight;
	}

}
