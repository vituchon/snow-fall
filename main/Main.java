/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Dimension;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class Main
{
    private static final int DEFAULT_FPS = 30;

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_MAX_PARTICLES = 40000;
    private final static Logger LOOGER = Logger.getLogger(Main.class.getCanonicalName());

    public static void main(String[] args)
    {

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
            	Arguments arguments = parseCmdLineArguments(args);
            	System.out.println(arguments.fps + " " + arguments.dimension);
            	MainWindow mainWindow = new MainWindow(arguments.fps,arguments.dimension, arguments.maxParticles);
            	mainWindow.setLocationRelativeTo(null);
            	mainWindow.setVisible(true);
            }
        });
    }
    
    public static Arguments parseCmdLineArguments(String[] args) {
    	int fps   = DEFAULT_FPS;
    	int width = DEFAULT_WIDTH;
    	int height = DEFAULT_HEIGHT;
    	int maxParticles = DEFAULT_MAX_PARTICLES;
    	try {
			switch (args.length) {
				case 3:
					maxParticles = Integer.parseInt(args[2]);
				case 2:
					String[] parts = args[1].split(",");
					width = Integer.parseInt(parts[0]);
					height = Integer.parseInt(parts[1]);
				case 1:
					fps = Integer.parseInt(args[0]);
					break;
				case 0:
					LOOGER.info("No hay argumentos, usando 30  600,400 40000 como FPS, Canvas Dimension y cantidad máxima de particulas.. respectivamente.");
			}
		} catch (RuntimeException e) {
			LOOGER.warning("Error reconociendo argumentos, argumentos van: '33 600,800' ó bien '33'. El primero es el FPS y el segundo las dimensiones de la ventana.");
		}
		return new Arguments(fps, new Dimension(width, height), maxParticles);
    }
    
    static class Arguments {
    	final int fps;
    	final Dimension dimension;
    	final int maxParticles;
    	
    	Arguments(int fps, Dimension dimension, int maxParticles) {
    		this.fps = fps;
    		this.dimension = dimension;
    		this.maxParticles = maxParticles;
    	}
    } 
}
