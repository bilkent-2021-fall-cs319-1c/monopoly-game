package monopoly.ui.gameplay;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lombok.Setter;

@Setter
public class Token extends Circle {
	private Board board;
	private Tile currentTile;

	private TranslateTransition translateTransition;

	public Token(double radius, Paint color) {
		super(radius, color);
		setStroke(Color.BLACK);
		setOpacity(0.9);

		currentTile = null;
		board = null;
	}

	public Token(Paint color) {
		this(6, color);
	}

	public void moveToNext() {
		Tile next = board.getNextTile(currentTile);

		currentTile.freeTokenLocation(this);
		translate(next.occupyNextFreePlace(this));
		currentTile = next;
	}

	public void fixPosition() {
		teleport(currentTile.getTokenLocation(currentTile.getTokenIndex(this)));
	}

	private void translate(Point2D endPoint) {
		if (endPoint == null)
			return;

		Point2D tokenCord = getParent().localToParent(localToParent(Point2D.ZERO));
		double fromX = tokenCord.getX();
		double fromY = tokenCord.getY();
		double toX = endPoint.getX();
		double toY = endPoint.getY();

		double byX = toX - fromX;
		double byY = toY - fromY;

		translateTransition = new TranslateTransition(Duration.millis(1000), this);
		translateTransition.setByX(byX);
		translateTransition.setByY(byY);
		translateTransition.play();

		translateTransition.setOnFinished(e -> fixPosition());
	}

	private void teleport(Point2D endPoint) {
		if (endPoint == null)
			return;

		Point2D tokenCord = getParent().localToParent(localToParent(Point2D.ZERO));
		double fromX = tokenCord.getX();
		double fromY = tokenCord.getY();
		double toX = endPoint.getX();
		double toY = endPoint.getY();

		double byX = toX - fromX;
		double byY = toY - fromY;

		setTranslateX(getTranslateX() + byX);
		setTranslateY(getTranslateY() + byY);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Token))
			return false;

		return ((Token) obj).getFill().equals(getFill());
	}

	@Override
	public int hashCode() {
		return getFill().hashCode();
	}
}