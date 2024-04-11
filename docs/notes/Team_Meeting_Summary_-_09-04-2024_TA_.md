## Introduction
- The meeting started 2 minutes late, Vuk, our chair, had slept in, so he was 7 minutes late. Since this was his first time being late, he only got a warning. Behdad took over the role of chair when Vuk wasn’t there yet 
- The TA had an exam today, so Ivar acted like the TA today
- The chair went over what we did last week:
    - Server+client tests
    - Work on HCI: keyboard navigation+shortcuts, make undo actions
    - Improve language switch: added flags, and made more scenes language switch compatible 
    - Added tags for expenses
    - Finish up admin
    - Property file
- The chair went around asking what issues we ran into:
    - Template for language switch: got cleared up by the TA(see below), so can be implemented this week
    - Client testing, we decided to not focus on this, because the rest is already well tested and we have more important things to focus on first.

## TA Section (Progress, Announcements, Questions)
- We presented our current product to Ivar. He didn’t want to see everything, because he would see it next week anyway, so we mostly focussed on showing Admin and the handling of the debts, since those were the basic requirements we didn’t have yet last week. Admin is good, and the way debt is handled is sufficient enough to cover the basic requirements.
- We might need to extend websockets, the application shouldn’t crash by connecting multiple clients, so we need to check that. 
- We should make fields in the participant optional. Only first/last/username should be mandatory, because it shouldn’t store unnecessary data
- We asked about the admin password, and it needs to appear in the server instead of the client. 
- Template doens’t have to automnatically work. You can add instructions that say to send the template an email. 
- Can you give us more information about preparing for the Product Pitch next week, and what might they ask us? They can ask you questions about lecture content relating to your code. You should be familiar with everything, but not super specific for every feature.
- Ivar said the design is most probably good enough for a pass. You typically only fail if it is really unusable, because this is not a design course. He personally thought our design isn’t that pretty, but that is just subjective.
- We asked about the properties file, and we shouldn’t put it in resources, because it gets compiled into .exe. It needs to be in a different location for it to work, but we have the right idea. 
- If we passed the self-reflection formative assignment, can we submit the same document for the summative assignment? yes

## Implementation this week
- By Friday, we need to have finished everything. Ivar said the deadline might get extended to sunday, but we still want to finish everything on friday.
- We will only finish up the language switch, and the statistics. Maks potentially wanted to work on detailed expenses, but it probably won’t happen
    - Vuk: edit participant to have optional fields, make sure the server is fully tested and add more verify statements
    - Behdad: finish up the language switch and the properties file
    - Maks: work on admin, work on formatting the UI, Integrating language switch to the table of participants
    - Tijn: work on keyboard navigation and shortcuts
    - Yanran: work on undo actions and language switch
    - Lotte: work on statistics
- On Friday, we will work on cleaning up the code and fixing checkstyle, before that, everything needs to be implemented
- We will make the video for the product pitch, write a script, and divide the parts for the presentation on either friday or saturday, because everything needs to finished before we can work on the presentation.

## Feedback
- In our code of conduct, we aimed to get an 8, the chair asked us how we felt about our project and whether we were satisfied. The team agreed that we all learned a lot, and we did the best we could, and considering our limited experience, we did really well. So, even though we might not get an 8 for this project, we can still be proud of what we accomplished regarding teamwork and code.

