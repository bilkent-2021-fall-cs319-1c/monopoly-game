package monopoly.ui.controller.gameplay.board.dice;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Shows a coordinate over die images, which has 9 rows and 16 columns.
 * 
 * @author Ziya Mukhtarov
 * @version Dec 19, 2020
 */
@Data
public class DieCoordinate {
	private int row;
	private int col;

	public DieCoordinate(int row, int col) {
		this.row = (row + 9) % 9;
		this.col = (col + 16) % 16;

		if (this.row == 0 || this.row == 8)
			this.col = 0;
	}

	public List<DieCoordinate> getNeighbors() {
		List<DieCoordinate> neighbours = new ArrayList<>();
		if (row == 0 || row == 8) {
			for (int i = 0; i < 4; i++) {
				neighbours.add(new DieCoordinate(row == 0 ? 1 : 7, 4 * i));
			}
		} else {
			neighbours.add(left());
			neighbours.add(right());
			neighbours.add(up());
			neighbours.add(down());

			if (row == 1 && col % 4 != 0) {
				neighbours.remove(up());
			} else if (row == 7 && col % 4 != 0) {
				neighbours.remove(down());
			}
		}
		return neighbours;
	}

	public DieCoordinate up() {
		return new DieCoordinate(row - 1, col);
	}

	public DieCoordinate down() {
		return new DieCoordinate(row + 1, col);
	}

	public DieCoordinate left() {
		return new DieCoordinate(row, col - 1);
	}

	public DieCoordinate right() {
		return new DieCoordinate(row, col + 1);
	}
}
