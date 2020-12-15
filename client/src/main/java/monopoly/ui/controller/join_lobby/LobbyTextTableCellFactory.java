package monopoly.ui.controller.join_lobby;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import monopoly.ui.UIUtil;

/**
 * The lobby cell factory for a JavaFX table that displays the text fitting in
 * the cell
 * 
 * @author Ziya Mukhtarov
 * @version Dec 15, 2020
 */
public class LobbyTextTableCellFactory
		implements Callback<TableColumn<LobbyDisplayData, Boolean>, TableCell<LobbyDisplayData, Boolean>> {
	@Override
	public TableCell<LobbyDisplayData, Boolean> call(TableColumn<LobbyDisplayData, Boolean> param) {
		return new TableCell<LobbyDisplayData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setGraphic(null);

				UIUtil.fitFont(this, 5, 5);
			}
		};
	}
}
