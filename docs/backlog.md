# Team 66 Backlog
## Stakeholders
* __User__ - any person that is able to use the board
* __Admin__ - a person that manages the server

## Terminology
* __Card__ - an object with a title that shows what task needs to be finished or is already finished.
* __Lists__ - a container with a name that holds cards
* __Board__ - a container with a name that holds lists
* __Tag__ - a label that helps with categorizing different cards
* __Card description__ - additional field for a more descriptive information about a card
* __Subtask__ - a checkbox item which divides the original card into different subsections
* __(Board) Key__ - a unique identifier which allows users to join boards

## Epics Description
* __Minimal app__ - essential requirements encompassing only the very basic functionality, i.e. having editable lists and cards that are synchronized
* __Drag and drop__ - drag and drop cards from one list to another and reorder cards within one list
* __Multi-Board__ - allow a user to manage multiple projects at the same time
* __Card details__ - allow a user to add more information to a card so it is more descriptive
* __Customization__ - allow users to customize the app to fit their preferences
* __Key mapping__- having keyboard shortcuts for most common actions
* __Password protection__ - allow users to create and change board passwords for improved security
* __Visual Feedback__ - show users how close they are to completing a task
* __List Reordering__ - reorder lists within a board
* __Board Names__ - give each board a name so that it is immediately clear which project it belongs to
* __Search by Tag__ - filter out the cards based on the tag they have
* __Restricting Users__ - reduce the load on the server by having an upper bound for the number of objects a user can create

## Minimal app 
### As a user I want:
* to add, remove and edit board’s lists so that I can subdivide the process into multiple parts
	* added lists are displayed for me
	* removed lists are displayed for me
	* lists are updated in the database
* to edit list’s name so that the board stays organized
	* list names are updated for me
	* list names are updated for other users
* to add and remove cards to and from the lists so that the board is kept flexible
	* cards are displayed for me when added
	* cards are removed for me when deleted
	* cards are updated in the database when added and removed
* to add and edit cards text so that I can see what task I need to complete
	* card text is displayed for me when added
	* card text is updated for me after an edit
	* card text is updated in the database
* to work on a board simultaneously with someone else so that we can work efficiently as a team
	* card information is updated for other users
	* list information is updated for other users
	* general board information is updated for other users
* to see all the updates of the board in real time so that I don’t miss any changes made by others
	* added cards are updated for everyone
	* removed cards are updated for everyone
	* edited cards are updated for everyone

### As an admin I want:
* to be able to restart the server in case of some problems
	* after restarting the server should be running again normally
* to clear the board so that users can start with a clean board
	* all lists are deleted from the board for users using the board
	* all lists are updated in the database

## Drag and drop
### As a user I want:
* to drag and drop cards from one list to another so that I can be more productive
	* while dragging the card you can see it moving in real time with the cursor
	* when the card is dropped in a new list, it disappears from it’s previous one,
	but the order of the other cards is kept in that list
	* when the card is dropped in a new list, it appears first, last or anywhere in-between the other cards, depending on where the cursor is pointing to
	* other users using the board can see the changes happening immediately
after dropping the card
* to reorder the cards inside a list so that I can prioritize tasks
	* reordered cards are visible for everyone

## Multi-Board
### As a user I want:
* to be able to create multiple new boards so that I can keep track of multiple projects at the same time
	* the boards are displayed for me
	* the boards are updated in the database
* to share a unique key of each of my boards with other users so that they can join the boards
	* the key of the board is displayed for me
	* each board has a unique key
	* the keys are stored in the database
* to open the boards by searching with a unique key so I can get an overview of the content there
	* after searching with the correct unique key, the correct board is opened
	* if there is no password, I can edit the content on the board
	* the content of the board is all there and is correct
* to see all my boards so that I can quickly switch between them
	* there is an overview of all boards for the user

### As an admin I want:
* to be able to delete other user’s boards so that unused boards do not occupy server space
	* in the menu containing every board there is an option to delete a specific board

## Card details
### As a user I want:
* to add, edit and remove card’s description and subtasks so that I can have more information for each card
	* description is shown when the card is clicked
	* when the card overview is opened the description can be edited and removed
* to add subtasks with checkboxes to cards so that I can complete a card step-by-step
	* subtasks are shown when the card is clicked
	* when the card overview is opened the subtasks can be edited and removed
	* when a checkbox is ticked it describes the task as finished by crossing that task
* to create new tags, apply existing ones so that cards can be separated into categories
	* when the card overview is opened there is an option to add a tag with text

## Customization
### As a user I want:
* to change tags colors for each card so that they can be visually split into groups
	* there is a menu to change the color of that tag
* to change the background of the board so that I can personalize the board
	* there is a menu for the board customization in which there are color or image
	options
* to change color of each card so that the board looks better
	* inside the card overview there is an option to change the color of the card

## Key mapping
### As a user I want:
* to use keyboard shortcuts, so that I can save time
	* when ESC is pressed in any window overlaying the main overview, that window is closed and the user is returned to the main board
	* when CTRL+L is pressed a new list is created
	* when CTRL+N is pressed a new card is created
	* when Enter is pressed in  the card is added to 
	* when Del is pressed the selected card or list is deleted

## Password protection
### As a user I want:
* to set an optional password for a board so that no one without access will be able to make changes to the board
	* there is a menu for the board security which has a field for setting a password and saving it
* to join other user’s password-protected boards with a correct password so that I can edit them
	* in the general UI there is a button to join an existing board
	* when password is entered if it is correct you can see and edit that board
* the application to tell me that the typed password was incorrect so that I can try again with a correct password
	* when the password entered inside the field to join another board is incorrect there is a message shown to the user that he should type another password
* to change my board’s password so that my board is kept secure
	* inside the board’s security settings there is a menu for changing the current password and saving it

### As an admin I want:
* to be able to unlock all boards and edit them so that I have control over the content
	* there is a menu containing every board which gives access to those boards’
	* inside the settings menu for those boards there is an option to unblock it
* to be able to change or delete the password of any board so that I can help to restore user’s access to their board
	* after changing the password you can’t use the old password to gain access
	* after deleting the password, it does not ask you for giving a password anymore
	
## Visual Feedback
### As a user I want:
* to see what portion of the tasks is completed in a whole list so that I can keep progress of each individual card
	* there is a progress bar under each list and card (if that card has subtasks) that
	shows a percentage of the finished tasks
	* when a card’s task(s) is/are finished then the card is grayed out
	
## List Reordering
### As a user I want:
* to drag and drop lists so that I can adjust priority of each list
	* the drag and drop of lists can be seen in real time by everyone on the board
	
## Board Names
### As a user I want:
* to add, remove and edit board’s name so that the purpose of each board is clear to me
	* inside the board’s menu there is a field to change the board’s name

## Search by Tag
### As a user I want:
* to search cards by tag so that all cards of one category will be shown together
	* in the general board UI there is a search field where you type a tag or a card and
	the results are shown in an organized way

## Restricting Users
### As an admin I want:
* users to be restricted in terms of how many boards and lists in each board they can create so that they cannot abuse the system
	* when the user is near the limit a message is shown
	* when the user has reached the specified limit his/her actions are restricted
	* if the user frees space of the specified item his/her actions are unrestricted