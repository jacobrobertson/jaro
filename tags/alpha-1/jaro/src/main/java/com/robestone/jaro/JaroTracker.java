package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionListener;
import com.robestone.gamebase.Mover;

/**
 * Just makes sure the "last position" is always up to date
 * @author jacob
 */
public class JaroTracker implements ActionListener {

	private JaroGameState state;
	
	public JaroTracker(JaroGameState state) {
		this.state = state;
	}

	@Override
	public void actionCompleted(Action action) {
		if (action instanceof Mover)	{
			Mover mover = (Mover) action;
			state.setPreviousJaroPosition(mover.getFrom());
		}
	}

}
