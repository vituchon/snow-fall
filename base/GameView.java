/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.awt.image.BufferedImage;

/**
 *
 * @author Administrador
 */
public interface GameView {
	public BufferedImage getCanvas();

	public void flush();
}
