package monopoly.ui.join_lobby;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import monopoly.ui.UIUtil;

public class LobbyIsPrivateTableCellFactory
		implements Callback<TableColumn<LobbyDisplayData, Boolean>, TableCell<LobbyDisplayData, Boolean>> {
	@Override
	public TableCell<LobbyDisplayData, Boolean> call(TableColumn<LobbyDisplayData, Boolean> param) {
		return new TableCell<LobbyDisplayData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);

				setText(null);
				setGraphic(null);

				if (!empty && Boolean.TRUE.equals(item)) {
					setGraphic(new ImageView(UIUtil.LOCK_ICON));
				}
			}
		};
	}

}