package monopoly.ui.controller.gameplay;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import monopoly.ui.UIUtil;

public class Die extends ImageView {
	private Coordinate prevLocation;
	private Coordinate currentLocation;
	private Random random;

	public Die() {
		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Die.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setFitWidth(200);
		setPreserveRatio(true);

		currentLocation = new Coordinate(0, 0);
		prevLocation = null;
		updateImage();
		random = new Random();

		// TODO Testing
		setOnMouseClicked(e -> {
			new Thread(() -> roll(random.nextInt(6) + 1)).start();
		});
	}

	private void goToDestiantion(Coordinate dest) {
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
		int duration = random.nextInt(100) + 100;
		for (int i = 0; i < duration; i++) {
			List<Coordinate> neighbours = currentLocation.getNeighbours();
			Collections.shuffle(neighbours);
			
			Coordinate nextLocation = neighbours.get(0);
			if (nextLocation.equals(prevLocation))
				nextLocation = neighbours.get(1);
			
			prevLocation = currentLocation;
			currentLocation = nextLocation;
			updateImage();
		}
	}

	public void roll(int destination) {
		randomTraverse();
		if (destination == 1) {
			goToDestiantion(new Coordinate(4, 0));
		}
		if (destination == 2) {
			goToDestiantion(new Coordinate(4, 4));
		}
		if (destination == 3) {
			goToDestiantion(new Coordinate(8, 0));
		}
		if (destination == 4) {
			goToDestiantion(new Coordinate(0, 0));
		}
		if (destination == 5) {
			goToDestiantion(new Coordinate(4, 12));
		}
		if (destination == 6) {
			goToDestiantion(new Coordinate(4, 8));
		}
	}

}
