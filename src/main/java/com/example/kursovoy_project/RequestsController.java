package com.example.kursovoy_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {

    Connection connection;
    ObservableList<Requests> list = FXCollections.observableArrayList();
    public static int id;

    @FXML TableView<Requests> table;
    @FXML TableColumn<Requests, Integer> col_id;
    @FXML TableColumn<Requests, String> col_name;
    @FXML TableColumn<Requests, String> col_surname;
    @FXML TableColumn<Requests, String> col_type;
    @FXML TableColumn<Requests, String> col_nameService;
    @FXML TableColumn<Requests, String> col_number;
    @FXML TableColumn<Requests, String> col_status;
    @FXML TableColumn<Requests, String> col_address;
    @FXML TableColumn<Requests, String> col_access;
    @FXML Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            toDisplay();
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void toDisplay() throws Exception {
        table.getItems().clear();
        connection = Connector.ConnectDb();
        ResultSet rs = connection.createStatement().executeQuery("select * from clientservices");

        while (rs.next()) {
            list.add(new Requests(Integer.parseInt(rs.getString(1)),
                    rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    rs.getString(7), rs.getString(8), rs.getString(9)));
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_nameService.setCellValueFactory(new PropertyValueFactory<>("nameService"));
        col_number.setCellValueFactory(new PropertyValueFactory<>("number"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_access.setCellValueFactory(new PropertyValueFactory<>("access"));

        table.setItems(list);
    }

    public void getSelected() {
        try{
            int index = table.getSelectionModel().getSelectedIndex();
            if(index < -1){
                return;
            }
            id = col_id.getCellData(index);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    public void toAccess() throws Exception{
        connection = Connector.ConnectDb();
        String sql = String.format("update clientservices set ??????????????????='????' where id='%s'", id);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        JOptionPane.showMessageDialog(null, "???????????? ??????????????????!");
        toDisplay();
    }

    public void toDelete() throws Exception{
        connection = Connector.ConnectDb();
        String sql = String.format("DELETE from clientservices WHERE id='%s'", id);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        JOptionPane.showMessageDialog(null, "???????????? ??????????????!");
        toDisplay();
    }

    public void goBack() throws Exception{
        Stage stageCLose = (Stage) backButton.getScene().getWindow();
        stageCLose.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(getClass().getResource("administrator-window.fxml").openStream());
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Table");
        stage.show();
        stage.setResizable(false);
    }
}
