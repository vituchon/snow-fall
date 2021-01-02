/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.clock;

/**
 * Declares methods for acquiring the value of a platform specific high
 * resolution clock for time stamp and interval measurement purposes.
 */
public interface Clock {

	/**
	 * Returns a numbers of ticks elapsed since an arbirtray origin time. Note that
	 * the value is implementor specific, so the origin time is not defined in this
	 * interface. This method must be used it conjuntion with the
	 * {@link Clock#getTicksPerMilisecond()} to determine elapsed time in the
	 * desired time units.
	 * 
	 * @return
	 */
	public long getTicks();

	/**
	 * Returns the maginute of this clock, number of ticks that this clock measures
	 * per milisecond. This value can be used to covert the ticks to the meaningful
	 * maginute in time units.
	 * 
	 * @return The number of ticks that ocurrs per miliseconds.
	 */
	public long getTicksPerMilisecond();
}
