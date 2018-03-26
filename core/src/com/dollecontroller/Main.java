//package com.dollecontroller;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.util.ArrayList;
//
//public class Main extends Application {
//
//	public static boolean running = true;
//
//	public final static ArrayList<Actuator> actuators = new ArrayList<>();
//	public final static InputProcessor INPUT_PROCESSOR = new InputProcessor();
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
//		primaryStage.setTitle("Dolle controller !!");
//		primaryStage.setScene(new Scene(root));
//		primaryStage.show();
//		primaryStage.setOnCloseRequest(event -> running = false);
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//
//}
