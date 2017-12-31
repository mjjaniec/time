package com.github.plushaze.traynotification.animations;

import com.github.plushaze.traynotification.models.CustomStage;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public abstract class AbstractAnimation implements Animation {

	protected final CustomStage stage;

	private final Timeline showAnimation, dismissAnimation;
	private final SequentialTransition sq;

	volatile boolean trayIsShowing;

	AbstractAnimation(CustomStage stage) {
		this.stage = stage;

		showAnimation = setupShowAnimation();
		dismissAnimation = setupDismissAnimation();

		sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
	}

	protected abstract Timeline setupShowAnimation();

	protected abstract Timeline setupDismissAnimation();

	@Override
	public final CustomStage getStage() {
		return stage;
	}

	@Override
	public final void playDismissDelayed(Duration dismissDelay, Runnable whenFinished) {
		sq.getChildren().get(1).setDelay(dismissDelay);
		sq.play();
		EventHandler<ActionEvent> oneShotHandler = event -> {
            whenFinished.run();
            sq.setOnFinished(e -> { });
        };
		sq.setOnFinished(oneShotHandler);
	}

	@Override
	public final void playShowAnimation() {
		showAnimation.play();
	}

	@Override
	public final void playDismissAnimation() {
		dismissAnimation.play();
	}

	@Override
	public final boolean isShowing() {
		return trayIsShowing;
	}

}
