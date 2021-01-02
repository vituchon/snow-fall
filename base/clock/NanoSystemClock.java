/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.clock;

/**
 *
 * @author Administrador
 */
public class NanoSystemClock implements Clock {

	private static final long FACTOR = 1000000L;

	@Override
	public long getTicks() {
		return System.nanoTime();
	}

	@Override
	public long getTicksPerMilisecond() {
		return FACTOR;
	}
}
