package monopoly.ui.controller.gameplay;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Coordinate {
	private int row;
	private int col;

	public Coordinate(int row, int col) {
		this.row = (row + 9) % 9;
		this.col = (col + 16) % 16;

		if (this.row == 0 || this.row == 8)
			this.col = 0;
	}

	public List<Coordinate> getNeighbours() {
		List<Coordinate> neighbours = new ArrayList<>();
		if (row == 0 || row == 8) {
			for (int i = 0; i < 4; i++) {
				neighbours.add(new Coordinate(row == 0 ? 1 : 7, 4 * i));
			}
		} else {
			neighbours.add(up());
			neighbours.add(down());
			neighbours.add(left());
			neighbours.add(right());
		}
		return neighbours;
	}

	public Coordinate up() {
		return new Coordinate(row - 1, col);
	}

	public Coordinate down() {
		return new Coordinate(row + 1, col);
	}

	public Coordinate left() {
		return new Coordinate(row, col - 1);
	}

	public Coordinate right() {
		return new Coordinate(row, col + 1);
	}
}
