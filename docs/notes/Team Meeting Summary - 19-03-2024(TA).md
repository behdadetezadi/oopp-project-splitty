## Introduction

- The meeting started on time, at 13:45, with 5 out of our 6 members present. Maks arrived 4 minutes late, but since our Code of Conduct gives a 5 minute grace period, there was no need for a warning to be issued to him.
- The chair recapped what was covered the past few weeks with the most notable mentions including:
    - Setting up the different JavaFX scenes.
    - Testing backend with POSTMAN
    - Ensuring our theme was accessible (by putting it through a color contrsast website)
- The chair went around the table asking if everyone managed to finish their code contributions of the week.

## TA Section (Progress, Announcements, Questions )

- Since the frontend and backend of our project were not yet connected, the team didn't really have much to present to the TA this week. We re-assured that the frontend had scenes, and that the backend was working through POSTMAN. We agreed to show the connected variant of our project next Tuesday.
- We announced the date we were unavailable for our OOPP presentation in week 10. With Behdad and the TA having an exam on Thursday, 18 April, it is not likely for us to present that day.
- The TA advised us to have the connected frontend and backend as quickly as possible, to use dependency injection testing, and use his feedback on Brightspace to improve.
- Another question was about the backlog and the interpretation of a basic requirement (to have a button that takes us to a participant scene). He responded that if it's vague and general, we can interpret it the way we like, but be confident that the requirement is fulfilled.
- Finally, there was an issue of figuring out how we will be graded for having 10 clients being able to connect simultaneously. The TA is also unsure and told us he will get back to us.


## Planing and GitLab Usage
- In order to get the points for "Tasks and Planning" from the rubric, we agreed on creating the issues and milestones right after the meeting. The chair created the issues on GitLab corresponding to the user stories, and it was agreed instead of dynamic creation of issues, we will just check the basic requirements off. 
- The issues are created with appropriate labels, and estimations and time spent will be added in the coming weeks as we work on the project.

## Implementation this week
- The main priority is connecting the remaining scenes to each other and trying to connect the backend to the frontend. 
- Admin logins and language switching will be easier to accomplish when we do the above, so that will be further discussed on Thursday's online meeting. 
- The action points before Thursday's meeting are as follows:
    - Lotte: Adding confirmation/Abort buttons to Expense Class and working on scene-switching from eventOverview to addExpense scene.
    - Yanran: Adding a back button that takes you back to the eventOverview scene without recompilation.
    - Tijn: Switching scenes from eventOverview to the invitation scene (which upon clicking will send the invitation to participants.)
    - Behdad: Working on making and joining an event and being able to connect to the backend part of the project to be able to save details to the H2 database and have them be persistent.
    - Vuk: Add/Edit/Delete options on the participant page and also being able to connect to the backend part of the project to be able to save details to the H2 database and have them be persistent.
    - Maks: Fix any errors that we may have and be able to also provide consistent support into scene switching along with better error handling and accessibility for the project

## Feedback
- All team members recognize that putting in more work now to be safe later is beneficial and that merge requests should be created earlier during the week.
- The chair ends the official TA meeting a couple of minutes earlier than scheduled with no complaints being heard from anyone.