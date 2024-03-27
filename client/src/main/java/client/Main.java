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

import client.scenes.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);



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

        var startPage = FXML.load(StartPageController.class, "client", "scenes", "StartPage.fxml");
        var overviewPage = FXML.load(EventOverviewController.class, "client", "scenes", "EventOverview.fxml");
        var expensePage = FXML.load(ExpenseController.class, "client", "scenes", "AddExpense.fxml");
        var participantExpenseViewPage = FXML.load(ParticipantExpenseViewController.class, "client", "scenes", "ParticipantExpensesView.fxml");
        var participantsPage = FXML.load(TableOfParticipantsController.class,"client", "scenes", "TableOfParticipants.fxml");
        var contactDetailsPage = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "contactDetails.fxml");
        var invitePage = FXML.load(InviteController.class, "client", "scenes", "inviteScene.fxml");
        var adminPage = FXML.load(AdminController.class, "client", "scenes", "AdminOverview.fxml");
        var mainController = INJECTOR.getInstance(MainController.class);
        mainController.initialize(primaryStage, startPage, overviewPage, expensePage, participantExpenseViewPage,participantsPage, contactDetailsPage, invitePage, adminPage);

    }

    /**
     * main method calls launch
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}