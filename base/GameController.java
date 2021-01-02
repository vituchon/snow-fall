/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import base.clock.Clock;
import base.clock.NanoSystemClock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class GameController {

	private final Clock clock;
	private final int ups, period;
	private final long periodTicks;
	private Thread thread;
	private volatile boolean running, paused;
	private final GamePhysics gamePhysics;
	private final GameView gameView;
	private final GameRender gameRender;
	private GameFlow gameFlow;
	private int updateCount, renderCount;
	private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

	public GameController(int ups, GameRender gameRender, GamePhysics gamePhysics, GameView gameView) {
		this.ups = ups;
		this.gameView = gameView;
		this.gameRender = gameRender;
		this.gamePhysics = gamePhysics;
		this.clock = new NanoSystemClock();
		this.period = 1000 / this.ups;
		this.periodTicks = this.period * clock.getTicksPerMilisecond();
		this.running = false;
		this.paused = false;
		this.updateCount = renderCount = 0;
		LOGGER.log(Level.INFO, "Game controller created with {0}, period in ms is {1} (in ticks {2} )",
				new Object[] { ups, period, periodTicks });
	}

	public synchronized void start() {
		if (thread == null || !running) {
			this.gameFlow = new GameFlow();
			thread = new Thread(gameFlow);
			thread.start();
		}
	}

	public void resume() {
		this.paused = false;
	}

	public void pause() {
		this.paused = true;
	}

	public void stop() {
		this.running = false;
	}

	public int getRenderCount() {
		return renderCount;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void resetStats() {
		this.renderCount = 0;
		this.updateCount = 0;
	}

	private void update() {
		gamePhysics.apply();
		updateCount++;
	}

	private void render() {
		gameRender.render(gameView.getCanvas());
		gameView.flush();
		renderCount++;
	}

	private class GameFlow implements Runnable {

		private final int SKIPS_PER_YIELD = 5;
		private final int MAX_RENDER_SKIPS = 5;

		@Override
		public void run() {
			running = true;
			int sleepSkip = 0;
			long oversleep = 0;
			long excess = 0;
			while (running) {
				// process input();
				long before = clock.getTicks();
				if (!paused) {
					update();
				}
				render();
				long after = clock.getTicks();
				long diff = after - before;
				long sleep = (periodTicks - diff) - oversleep;
				if (sleep > 0) {
					try {
						long sleepMs = sleep / clock.getTicksPerMilisecond();
						Thread.sleep(sleepMs);
					} catch (InterruptedException ex) {
						LOGGER.log(Level.SEVERE, null, ex);
					}
					oversleep = (clock.getTicks() - after) - sleep;
				} else {
					excess -= sleep;
					oversleep = 0;
					if (++sleepSkip >= SKIPS_PER_YIELD) {
						Thread.yield();
						sleepSkip = 0;
					}
				}
				int skips = 0;
				while (excess > periodTicks && skips < MAX_RENDER_SKIPS) {
					excess -= periodTicks;
					update();
					skips++;
				}
				// System.out.println("Total skips " + skips + ", excess " + excess);
			}
		}
	}
}
