package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import base.GameController;
import base.GamePhysics;
import base.GameRender;
import base.GameView;
import model.Cloud;
import model.Particle;
import model.Physics;
import model.Scenario;

public class DecoupledGamePanel extends JPanel implements GameView, MouseListener
{

    private BufferedImage canvas;
    private Dimension dimension;
    private GameController controller;
    private GamePhysics physics;
    private GameRender view;
    private Scenario scenario;
    private static final Logger LOGGER = Logger.getLogger(DecoupledGamePanel.class.getName());
    
    public DecoupledGamePanel(int fps, Dimension dimension, int maxParticles)
    {
        setBackground(Color.white); // white background
        this.dimension = dimension;
        this.canvas = new BufferedImage(dimension.width,dimension.height, BufferedImage.TYPE_INT_ARGB);
        scenario = new Scenario(maxParticles,canvas);
        physics = new Physics(scenario);
        view = new CustomGameRender(scenario);
        controller = new GameController(fps,view,physics,this);
        this.addMouseListener(this);
        this.setOpaque(false);
    }

    @Override
    public void addNotify()
    {
        super.addNotify();
        startGame();
    }

    private void startGame()
    {
        controller.start();
    }

    public void stopGame()
    {
        controller.stop();
    }

    public void pauseGame()
    {
        controller.pause();
    }
    
    public void resumeGame()
    {
        controller.resume();
    }

    @Override
    public BufferedImage getCanvas()
    {
        return this.canvas;
    }

    @Override
    public void flush()
    {
        Graphics g = null;
        try
        {
            g = this.getGraphics();
            printReportStats(canvas.getGraphics());
            g.drawImage(canvas, 0, 0, null);
            Toolkit.getDefaultToolkit().sync(); // sync the display on some systems
            g.dispose();
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, null, e);
        }
        finally {
            if (g != null) {
                g.dispose();
            }
        }
    }
    
    private static long MAX_STATS_INTERVAL = 2000L;
    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    private long last = System.nanoTime();
    private String report = "Gathering...";

    public String getReportStats()
    {
        long now = System.nanoTime();
        long diff = now - last;
        long diffms = diff / 1000000L;
        if (diffms > MAX_STATS_INTERVAL)
        {
            last = System.nanoTime();
            double segs = (double) diffms / 1000D;
            double fps = controller.getRenderCount() / segs;
            double ups = controller.getUpdateCount() / segs;
            int totalParticles = scenario.getParticles().size();
            report = "During the last " + segs + " second(s), the FPS is " + df.format(fps) + " and UPS is " + df.format(ups) + ", count(particles)=" + totalParticles;
            controller.resetStats();
        }
        return report;
    }
    
    // set up message font
    Font font = new Font("SansSerif", Font.BOLD, 12);
    FontMetrics metrics = this.getFontMetrics(font);
    
    private void printReportStats (Graphics g)
    {
        String msg = getReportStats();
        int x = (this.dimension.width - metrics.stringWidth(msg))/2; 
        int y = metrics.getHeight();//(PHEIGHT - metrics.getHeight())/2;
        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(msg, x, y);
    }

    final Random r = new Random();
    public void dropParticules(int x,int y) {
	final int[] offsets = {-3,-2,-1,0,1,2,3}; 
		for (int i = 0; i < 10; i++) {
			final int offsetX = offsets[r.nextInt(offsets.length)];
			final int offsetY = offsets[r.nextInt(offsets.length)];
			Particle particle = new Particle(x+ offsetX,y+ offsetY);        
			particle.vy = 1;
			particle.ay = Physics.G;
			this.scenario.addParticle(particle);
		}
    }
	@Override
	public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Cloud cloud = new Cloud(x - (Cloud.w/2), y - (Cloud.h/2));
        this.scenario.getClouds().add(cloud);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



}
