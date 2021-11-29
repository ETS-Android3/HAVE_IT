# HAVE_IT

## Intro

This is the application project for CMPUT301 team project of android application for team CMPUT301F21T14.

This application is used for logging user's habit and their execution of the habit, review their own performance on the habits logged, and as well follow other users for viewing their habits and how they are doing.

The intention is to provide user with easy use of habit tracking, and by watching other's work, motivate themself to keep on habits.

## Requirement for app

This application requires minimum android sdk version of 19, but the latest is recommended.

This application requires Google play installed to run.

## Main functions

#### User athunecation

The user system requires an accessible email address to register, login, and reset password. 

By registering, a mail will be sent to the user email for verificaiton.

By resetting password, a mail will be sent to the user email for password re-entering.

For all account related pages, the proper entry of the field is requires, otherwise will have notificaiton on it.

#### Habit page

After logged into the system, the user can see the page for their own habits first. The list of all habits can be reordered to custom order. The today list only show the habits planned for today.

#### Habit adding

To add new habit, click on the plus icon button on bottom right. This will lead to the page for adding habit.

Habit added requires a title and a reason. The start date selector pops up a calendar for selecting the date to start the habit. The weekday selector indicates what weekdays the habit will be planned. The switch indicates if the habit can be viewed by other users following you, by default keep the habit private.

**Only one habit of same title can exisit.**

#### Habit editing and deleting

By clicking on any item in the habit list in habit page, it will go to the viewing page. In this page, layout is similar to adding habit. The fields can be changed at will. 

By pressing confrim button, any changes to the habit will be applied. By pressing delete button, the habit will be deleted, along with all habit events of it.

#### Habit event adding

By clicking on event list button of the habit, it will go to the event list of that habit. This page shows all events related to that habit.

By clicking on plus icon button on the bottom right, it will go to the page for adding habit. The event to be added has a mandetory comment (title), date set default as today, oprtional location and optional photo.

The location can be selected through the use of google map. Default location in map is current location of user.

The photo can be added through either camera or the gallery of the user.

**Only one event at a certain date can be exsisting**

#### Habit event editing and deleting

Similar to habits, by clicking on any item in the list, it will go to the page for viewing the event's detail. Any of the field can be changed.

By pressing confrim button, any changes to the event will be applied. By pressing delete button, the event will be deleted.

#### Visual indicator for habit

By clicking on the calendar icon button in the habit viewing page, it will go to a visual indicator for the habit. The indicator is of monthly calendar with dots of different color. See the indicator page for details of the representation of each colored dots.

#### Following Page

Through the navigation menu in the bottom of habit page, it can go to teh following page by clicking on the middle item. 

The following page consists of four tabs. The first tab is for viewing all users current following. The second tab is for searching through a list of all users to send request to another user for granting permission to follow. The third tab is for receivend following requests. The fourth tab is for reviewing the reply of the requests sent.

##### Following working flow

To start a new following, first go to the second tab for a whole list of users. The  text on the side of each item indicates that the user has already sent a request or already following. If neither, it will show a button which click on it will send request to follow that user.

Once the request is replied, it will be notified on the tab icon of fourth tab with number. The four tab's items will show if the request is replied yet or not, and if the request is allowed or rejected. Once confrim on the reply, it will be deleted from the reply list. If allowed, the user will automatically be added to the following user list.

##### Request reply working flow

One a following request is received, it will be notified on the tab icon of third tab with number. In this tab, each item represents the received request. By confriming, the user will be added to the sender's following list. By rejecting, the user will not be added. In either case, it will send a reply to the sender of the request.

##### Following list

By clicking on any user in the following list, it will show a page of all public habits of that user. By clicking on any of the habits, it will show the visual indicator of that habit.

By clicking on delete button, the user will be de-followed.

#### Account page

Through the third item of the navigation menu, it will go to the account page. This version of application only has simple logout function in this page. Futher customization may be added in this page, but beyond the scope of the project.

### Third party used

DavidProdinger/Weekdays-Selector: https://github.com/DavidProdinger/Weekdays-Selector

SundeepK/CompactCalendarView: https://github.com/SundeepK/CompactCalendarView

All use of the source are under MIT lisence of the providor.

