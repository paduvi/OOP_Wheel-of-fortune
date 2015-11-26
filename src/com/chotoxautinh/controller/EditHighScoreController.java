package com.chotoxautinh.controller;

import com.chotoxautinh.model.HighScore;
import com.chotoxautinh.model.Player;
import com.chotoxautinh.util.InsertionSortUtil;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditHighScoreController {
	@FXML
	private TableView<HighScore> catalogTable;

	@FXML
	private TableColumn<HighScore, Integer> indexColumn;

	@FXML
	private TableColumn<HighScore, String> nameColumn;

	@FXML
	private TableColumn<HighScore, Long> pointColumn;

	@FXML
	private Label pointLabel;

	private int selectedIndex;

	private Stage dialogStage;

	public void setDialogStage(Stage dialogStage, ObservableList<HighScore> highScoreList, Player player) {
		this.dialogStage = dialogStage;
		HighScore obj = new HighScore(player.getName(), player.getPoint());
		this.selectedIndex = InsertionSortUtil.getInsertionIndex(highScoreList, 10, obj);
		catalogTable.setItems(highScoreList);
		if (selectedIndex != -1)
			catalogTable.getSelectionModel().select(selectedIndex);
		pointLabel.setText("Số điểm mà bạn đạt được là " + player.getPoint() + " điểm");
	}

	public ObservableList<HighScore> getTableList() {
		return catalogTable.getItems();
	}

	@FXML
	private void initialize() {

		indexColumn.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<>(catalogTable.getItems().indexOf(column.getValue()) + 1));
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		pointColumn.setCellValueFactory(cellData -> cellData.getValue().pointProperty().asObject());

		Callback<TableColumn<HighScore, String>, TableCell<HighScore, String>> defaultTextFieldCellFactory = TextFieldTableCell
				.<HighScore> forTableColumn();

		nameColumn.setCellFactory(col -> {
			TableCell<HighScore, String> cell = defaultTextFieldCellFactory.call(col);
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				TableRow<?> row = cell.getTableRow();
				if (row == null) {
					cell.setEditable(false);
				} else {
					if (row.getIndex() != selectedIndex) {
						cell.setEditable(false);
					} else {
						cell.setEditable(true);
					}
				}
			});
			return cell;
		});

		catalogTable.setRowFactory(tv -> {
			TableRow<HighScore> row = new TableRow<>();

			row.itemProperty().addListener((obs, oldValue, newValue) -> {
				if (row.getIndex() != selectedIndex) {
					row.setDisable(true);
				} else {
					row.setDisable(false);
				}
			});

			return row;
		});

	}

	@FXML
	private void okHandle() {
		dialogStage.close();
	}
}
