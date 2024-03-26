## Introduction
- The meeting started on time, everyone was present. 
- The chair went over what we did last week:
    - Refactorisation of main controller for the scene switching
    - Making the admin page: frontend and json related methods
    - Work on finishing up basic requirements for participant and event
    - Make sure that the event title can be edited
    - Work on expense backend and frontend (still in progress)
- The chair went around asking what issues we ran into:
    - Issue with deleting participants in serverUtils (got fixed)
    - It is difficult to make the expense work, because it is dependent on the event and participant (in progress, should be fixed this week)

## TA Section (Progress, Announcements, Questions)
- We presented our current product to the TA. We showed how we could join an event, edit its title, get the invite code, add/edit/show/delete participants (something went wrong on Behdad’s laptop during this part, but we could continue on maks’). The TA liked our progress for this week. 
- We had a question about how we are graded for supporting simultaneous clients. The TA responded that we aren’t expected to do very extensive testing in this course and that he doesn’t really know how to do it, but we should look into having different instances of long polling and websockets.
- The TA announced that he would try to finish the feedback on the assignments soon (2 days at most)
- We also asked if we had to delete participants from the database or the event alone. The TA said that the backlog is vague enough to let you choose whatever. We came to the conclusion to delete it from the database to not store unnecessary data.
- We asked what we needed to do for the product pitch. We have to submit something we want to use for the final presentation (for example) . It is an opportunity to get feedback, we have to submit it on brightspace.

## Using websockets and long polling
- We came to the conclusion we can use long polling for things that are less frequently updated and websockets for things that are more frequently updated (like admin) to ensure real time communication.
- We decided to talk about this more during our Thursday's meeting so that we can do more research on how to use it.

## Implementation this week
- The main priority is finishing up all our basic requirements.
- General goals for the week (Will be discussed during Thursday’s meeting)
    - Make the eventOverview fully work with the workflow
    - The TA said we need a config file for the language switch where you put in all the settings with pre configured values.
    - Admin login with generated passwords
    - Keyboard shortcuts
    - Using jackson
- What we plan to have finished before our Thursday’s meeting:
    - Vuk: the validation for the participant, add a hover description for adding/deleting (+-) the participant so it is clearer for the user, try to do a colour change for the project
    - Behdad: generate unique invite code for each event, add a hover description for editing the event title so it is clearer for the user, add a back button from event overview to startpage
    - Maks: finalise the admin part, adding the events to admin, making the slides for the product pitch
    - Tijn: finalise the JSON methods with the import and export
    - Yanran: make sure that the expense overview works properly + make a new controller for it
    - Lotte: make sure the methods for adding/deleting/editing an expense work

## Feedback
- Feedback from the TA: 
    - The current setup with +- for participants is not clear enough for older users
    - The logo is awesome, check if it is contrasting enough.
    - He advised us to finish every basic requirement by next week, so we can fix bugs and test in the next 2 weeks. 
    - He will grade the HCI soon. He advised us to look at the rubric again, because he will try to break it.
    - We have to show extensive use of jackson
- The chair ends the official TA meeting 5 minutes earlier than scheduled.
