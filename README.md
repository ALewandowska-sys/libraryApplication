## What I use to build this application?

### Spring Security
This application use basic authorization, and recognize 2 roles. 
`Admin` was created by static (inside the code), but `Reader` is reading from database.
Application has sides for admin, readers and for quests (without authorization).

### Vaadin
Vaadin help me to show the main uses. 
People often use Swagger to create a documentation, but I wanted to try something new.
I added simply components inside my Views, so it's very basic.

### PDF generator, ZIP and mailling
Application can generate a PDF with available books and send a mail with zipped PDF files.
Informations for sending mail (like email, password, ect) is inside application.propertise, but I remove my values.

### History
When user borrow book, then application save this information in history. 
I think that I could make it by listener, but for now it's created inside the function 
(I create a history with information about book, but rest of information like date or author was created automatically)

## How does it work?
  > I will show a some eindpoints, using Vaadin and Postman

### Register

