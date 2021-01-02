/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.clock;

/**
 *
 * @author Administrador
 */
public class SystemClock implements Clock {

	private static final long FACTOR = 1L;

	@Override
	public long getTicks() {
		return System.currentTimeMillis();
	}

	@Override
	public long getTicksPerMilisecond() {
		return FACTOR;
	}
}
