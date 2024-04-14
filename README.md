# Splitty
This is our implementation of Splitty.

### Instructions on how to execute this project:

Follow these simple steps to run the server and client components of our project.

### Server

1. **Run the Server**:
    - Navigate to the server's main class directory:
      ```
      server/src/main/java/server
      ```
    - Execute `Main.java`:
      ```
      java Main.java
      ```

### Client

2. **Run the Client with JavaFX**:
    - Navigate to the client's main class directory:
      ```
      client/src/main/java/client
      ```
    - Set the VM options for JavaFX. Be sure to replace the module path with your own JavaFX SDK path:
      ```
      java --module-path "YOUR_JAVAFX_SDK_PATH\lib" --add-modules javafx.controls,javafx.fxml Main.java
      ```
    - Example with a specific path:
      ```
      java --module-path "C:\Users\Default\Downloads\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml Main.java
      ```
   - Execute `Main.java`:
     ```
     java Main.java
     ```

Make sure to replace `"YOUR_JAVAFX_SDK_PATH\lib"` in the VM options with the path to your JavaFX SDK's `lib` directory.

**Login Page:**

This is the first page you will see.

You have the option to click on a user login or an admin login.
- The user login takes you to the user side of the application (start page).
- The admin login takes you to the admin side of the application.


**Start Page:**
Here, you have the ability to either create a new event or join an existing event through a unique invite code.

The Recent Events List gets populated as you navigate through events in runtime and is initially empty since there is no login in compliance with the basic requirements.
By clicking on the events in this list you can navigate to them faster.

**USER FEATURES**


**Event Overview Page:**
Here, a large overview of the page is given, with the buttons taking you to various locations in our applications.

Send Invites instruction:
- Entering the email in the provided text box sends you a corresponding invite.


Show Participants instructions:
- You are taken to a page where you can add, modify, and delete participants respectively. You only need to provide your first and last name. The rest of the fields are optional, but if anything is entered it will be checked to see if it's in the correct format.

Show All Expense instruction:
- This shows all the expenses and how much each person owes to the person who paid for the expense. 


Select Participant instruction:
- You select the participant from the dropdown menu to add an expense or show an expense (where you can also modify or delete it)


Add Expense instruction:
- You have the participant you selected. You add a title, cost, and a tag. After adding, you can immediately undo which deletes it, too.


Show Expense instruction:
- This only shows the expenses for a participant. You can modify or delete them here.

The user can also change the event's title and copy the invite code in this page.

**ADMIN FEATURES**


The admin password is shown on the server terminal when you run the application.
You can sort the event by clicking on the column headers of what you want to sort by. Then, the buttons give you various options of what you can do with events.




**WEBSOCKETS AND LONG-POLLING**

The backend implementation for both of these lies on server/src/main/server/api/EventController.
Whichever method has @MessageMapping annotation on top works with websockets (except for the method for updating event title which was not completed).
The addParticipant() method in the backend uses long polling to add a participant. The respective logic for both websockets and long-polling is also continued in the server utils method at client/src/main/java/client/utils/ServerUtils.

Moreover, in the tableOfParticipantsController in the client side uses long-polling for addition of participants and modification/deletion.
This can be shown by registering for updates/messages as soon as the scene is opened and updating the UI after something is done with participants (communication with WS through server.send())

Other locations where websockets were implemented include adding an expense to show in both the show all expenses overview and participantsExpenseOverview.
Moreover, deleting a participant using websockets removes the opportunity of another client to select that participant through the drop-down menu.



# Extensions and HCI features implementation list:

**Live Language Switch**

Live Language Switch is available in Login page, Start page and Event Overview Page.

Current Live Language Switch fully supports three languages (Dutch, German, English) but it is very flexible the user can download the keys of the words to translate (via download template option) and fill them in with the desired language and email them to the developers and the developers can easily include that language in the switch.

The template file is made and opened automatically a folder called splitty_files in the users home directory.

The default language is set to english but through runtime the language choice persists (until changed in one of the three mentioned pages)

In addition, language choice persists through restarts.

**Statistics page:**
This page can be accessed through the expense overview and shows a pie chart containing the amount of money spent per tag, and also shows the total amount of money spent on the event


**Send Invitation code to email:**
You can write the emails of the people you want to invite in the text-field (one email in each line), if emails are sent successfully you will see a prompt that says so. The email contains the invite code of the event and the server.

Users can enter their email credentials in the email.properties file located in the root of the project (same directory as this readme file) (Initially we wanted to include the properties file in the resource section of the client but in the last TA meeting we were advised not too)

The email should support current smtp requirements (for example for gmail accounts "Less Secure Apps" or 2FA alongside application passwords should be enabled)

In case of any changes for smtp hosts user can use their own smtp host and port.

We use SSL and StartTLS enabled properties for the mail server.


**Undo button:**
This undo button shows in both Add Expense page and Show Expense page.
It supports undo operation for all the expense related operations including add,delete and edit.

When user click the button in Add Expense page,the text field while shows the last added expense's information,or empty field if no expense be added before.
It supports mutiple undo for both pages.



# Keyboard Shortcut Navigations:

`Ctrl + C`: Create a new event

`Ctrl + J`: Join an existing event

`Ctrl + E`: Cycle through recent events

`Ctrl + Enter`: Join the selected event from a list

`Ctrl + Q`: Display all expenses from the event

`Ctrl + W`: Add an expense for the selected participant

`Ctrl + E`: View all expenses for the selected participant

`Ctrl + I`: Display page for sending invitations

`Ctrl + S`: Send invitations to participants

`Ctrl + T`: Change the title of the event

`Ctrl + P`: Display event participants

`Ctrl + S`: Display statistics in the expenses overview

`Ctrl + Z`: Undo the last action

`Ctrl + U`: Log in as a regular user

`Ctrl + A`: Log in as an administrator

`Ctrl + L`: Switch between the languages of the application

`ESCAPE`: Go back to the previous page


# Extra Notes

When you hover over the title, or the plus and minus in the participant overview, it will show you a tooltip.

The user can change and modify the application.properties file in the project home directory (same directory as this readme file) in order to connect to a splitty server of their choice.
(Naturally the server should exist and running)
(Like the email.properties, initially the file was located in resource section of the client but was refactored to a more accessible location based on last TA meeting's feedback)



