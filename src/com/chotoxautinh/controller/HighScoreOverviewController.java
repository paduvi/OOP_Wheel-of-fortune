package com.chotoxautinh.controller;

import java.util.Map;

import com.chotoxautinh.model.HighScore;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class HighScoreOverviewController {
	@FXML
	private TableView<HighScore> classicTable;
	@FXML
	private TableView<HighScore> endlessTable;
	@FXML
	private TableView<HighScore> multiTable;

	@FXML
	private TableColumn<HighScore, Integer> classicPlaceCol;
	@FXML
	private TableColumn<HighScore, String> classicNameCol;
	@FXML
	private TableColumn<HighScore, Long> classicScoreCol;
	
	@FXML
	private TableColumn<HighScore, Integer> endlessPlaceCol;
	@FXML
	private TableColumn<HighScore, String> endlessNameCol;
	@FXML
	private TableColumn<HighScore, Long> endlessScoreCol;
	
	@FXML
	private TableColumn<HighScore, Integer> multiPlaceCol;
	@FXML
	private TableColumn<HighScore, String> multiNameCol;
	@FXML
	private TableColumn<HighScore, Long> multiScoreCol;
	
	private Stage dialogStage;
	
	public void setDialogStage(Stage dialogStage, Map<Integer, ObservableList<HighScore>> highScoreMap) {
		this.dialogStage = dialogStage;
		classicTable.setItems(highScoreMap.get(0));
		endlessTable.setItems(highScoreMap.get(1));
		multiTable.setItems(highScoreMap.get(2));
	}
	
	@FXML
	private void initialize() {

		classicPlaceCol.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<>(classicTable.getItems().indexOf(column.getValue()) + 1));
		endlessPlaceCol.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<>(endlessTable.getItems().indexOf(column.getValue()) + 1));
		multiPlaceCol.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<>(multiTable.getItems().indexOf(column.getValue()) + 1));
		
		classicNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		endlessNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		multiNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		
		classicScoreCol.setCellValueFactory(cellData -> cellData.getValue().pointProperty().asObject());
		endlessScoreCol.setCellValueFactory(cellData -> cellData.getValue().pointProperty().asObject());
		multiScoreCol.setCellValueFactory(cellData -> cellData.getValue().pointProperty().asObject());
		
	}
	
	@FXML
	private void okHandle() {
		dialogStage.close();
	}
}
