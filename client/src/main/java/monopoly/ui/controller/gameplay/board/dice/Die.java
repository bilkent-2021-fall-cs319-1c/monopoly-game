package monopoly.ui.controller.gameplay.board.dice;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import lombok.Getter;
import monopoly.ui.UIUtil;

public class Die extends ImageView {
	private DieCoordinate prevLocation;
	private DieCoordinate currentLocation;
	private Random random;
	@Getter
	private boolean rolling;

	public Die() {
		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Die.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentLocation = new DieCoordinate(0, 0);
		prevLocation = null;
		updateImage();
		random = new Random();
		rolling = false;
	}

	private void goToDestiantion(DieCoordinate dest) {
		while (currentLocation.getRow() < dest.getRow()) {
			currentLocation = currentLocation.down();
			updateImage();
		}
		while (currentLocation.getRow() > dest.getRow()) {
			currentLocation = currentLocation.up();
			updateImage();
		}
		while (currentLocation.getCol() < dest.getCol()) {
			currentLocation = currentLocation.right();
			updateImage();
		}
		while (currentLocation.getCol() > dest.getCol()) {
			currentLocation = currentLocation.left();
			updateImage();
		}
	}

	private void updateImage() {
		Platform.runLater(() -> setImage(UIUtil.DICE[currentLocation.getRow()][currentLocation.getCol()]));
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	private void randomTraverse() {
		int duration = random.nextInt(25) + 25;
		for (int i = 0; i < duration; i++) {
//			Never go to previous location
			List<DieCoordinate> neighbors = currentLocation.getNeighbors();
			Collections.shuffle(neighbors);

			DieCoordinate nextLocation = neighbors.get(0);
			if (nextLocation.equals(prevLocation))
				nextLocation = neighbors.get(1);

			prevLocation = currentLocation;
			currentLocation = nextLocation;

			updateImage();
		}
	}

	private DieCoordinate getCoordinateOfFace(int face) {
		if (face == 1) {
			return new DieCoordinate(4, 0);
		}
		if (face == 2) {
			return new DieCoordinate(4, 4);
		}
		if (face == 3) {
			return new DieCoordinate(8, 0);
		}
		if (face == 4) {
			return new DieCoordinate(0, 0);
		}
		if (face == 5) {
			return new DieCoordinate(4, 12);
		}
		return new DieCoordinate(4, 8);
	}

	public void roll(int destination) {
		rolling = true;
		new Thread(() -> {
			randomTraverse();
			goToDestiantion(getCoordinateOfFace(destination));
			rolling = false;
		}).start();
	}

}
