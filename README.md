# OOPP Template Project




# Instructions on how to execute this project:

 **Login Page:**

By clicking User Login:


By clicking Admin Login:



**Start Page:**

This page includes two text-fields to make a new event or join an event via invite code.

The recent events list gets populated as the user navigates to different events and back.(Initially during each runtime it is empty since no login for users exists in compliance with the basic requirements)


**Event Overview Page:**

Send Invites instruction:

Copy Invite Code:

Change Event Title: 


Show Participants instructions:


Show All Expense instruction:


Select Participant instruction:


Add Expense instruction:


Show Expense instruction:




# long-polling/webSockets Implementation












# Extensions and HCI features implementation list:

**Live Language Switch**

Live Language Switch is available in Login page, Start page and Event Ovrerview Page.

Current Live Language Switch fully supports three languages (Dutch, German, English) but it is very flexible the user can download the keys of the words to translate (via download template option) and fill them in with the desired language and email them to the developers and the developers can easily include that language in the switch. 

The template file is made and opened automatically a folder called splitty_files in the users home directory.

The default language is set to english but through runtime the language choice persists (until changed in one of the three mentioned pages)

In addition language choice persists through restarts. 

**Statistics page:**



**Send Invitation code to email:**
You can write the emails of the people you want to invite in the text-field (one email in each line), if emails are sent successfully you will see a prompt that says so. The email contains the invite code of the event and the server.

Users can enter their email credentials in the email.properties file located in the root of the project (same directory as this readme file) (Initially we wanted to include the properties file in the resource section of the client but in the last TA meeting we were advised not too)

The email should support current smtp requirements (for example for gmail accounts "Less Secure Apps" or 2FA alongside application passwords should be enabled)

In case of any changes for smtp hosts user can use their own smtp host and port.

We use SSL and StartTLS enables properties for the mail server.



**Undo button:**




# keyboard shortcut navigation:

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



# Extra Notes
 
The user can change and modify the application.properties file in the project home directory (same directory as this readme files) in order to connect to a splitty server of their choice.

(Like the email.properties, initially the file was located in resource section of the client but was refactored to a more accessible location based on last TA meeting's feedback)