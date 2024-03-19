/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.EventOverviewController;
import client.scenes.ExpenseController;
import client.scenes.StartPageController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    /**
     * START method
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if there is an exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set up Guice injector
        Injector injector = Guice.createInjector(new MyModule());

        // Use MyFXML to load ExpenseController and its view
        MyFXML myFXML = new MyFXML(injector);
        Pair<StartPageController, Parent> expensePair = myFXML.load(StartPageController.class,
                "client/scenes/StartPage.fxml");

        // Retrieve the loaded controller and root node for the scene
        StartPageController controller = expensePair.getKey();
        Parent root = expensePair.getValue();

        // TODO Initialize the controller (this part is broken)
        // TODO FXML loader does not instantiate the ExpenseController as expected (it's null)
        //      controller.initialize();

        // Setup and show the primary stage
        primaryStage.setTitle("Matrix Start Page");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * main method calls launch
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}