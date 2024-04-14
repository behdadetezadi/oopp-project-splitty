# Splitty
This is our implementation of Splitty.

# Instructions on how to execute this project:

You run the server first, and then you run the client.

//TODO : SOMEONE EXPLAIN THE CONFIG FILE!

 **Login Page:**

This is the first page you will see.

You have the option to click on a user login or an admin login.
- The user login takes you to the user side of the application (start page).
- The admin login takes you to the admin side of the application.


**Start Page:**
Here, you have the ability to either create a new event or join an existing event through a unique invite code.

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


**ADMIN FEATURES**


The admin password is shown on the server terminal when you run the application.
You can sort the event by clicking on the column headers of what you want to sort by. Then, the buttons give you various options of what you can do with events.




**WEBSOCKETS AND LONG-POLLING**
The backend implementation for both of these lies on server/src/main/server/api/EventController.
Whichever method has @MessageMapping annotation on top works with websockets (with the exception of the method for updating event title which was not completed).
The addParticipant() method in the backend uses long polling to add a participant. The respective logic for both websockets and long-polling is also continued in the server utils method at client/src/main/java/client/utils/ServerUtils.

Moreover, in the tableOfParticipantsController in the client side uses long-polling for addition of participants and modification/deletion.
This can be shown by registering for updates/messages as soon as the scene is opened and updating the UI after something is done with participants (communication with WS through server.send())

Other locations where websockets were implemented include adding an expense to show in both the show all expenses overview and participantsExpenseOverview.
Moreover, deleting a participant using websockets removes the opportunity of another client to select that participant through the drop-down menu.



# Extensions and HCI features implementation list:

**Statistics page:**



**Send Invitation code to email:**



**Undo button:**




# Keyboard Shortcut Navigations:

Ctrl + C: Create a new event

Ctrl + J: Join an existing event

Ctrl + E: Cycle through recent events

Ctrl + Enter: Join the selected event from a list

Ctrl + W: Add an expense to the event

Ctrl + I: Display page for sending invitations

Ctrl + T: Change the title of the event

Ctrl + P: Display event participants

Ctrl + Q: Display all expenses from the event

Ctrl + S: Send invitations to participants

Ctrl + S: Display statistics in the expenses overview

Ctrl + Z: Undo the last action

Ctrl + U: Log in as a regular user

Ctrl + A: Log in as an administrator

Ctrl + L: Switch between the languages of the application

ESCAPE: Go back to the previous page



