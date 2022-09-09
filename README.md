## What I use to build this application?

### Spring Security
This application use basic authorization, and recognize 2 roles. 
`Admin` was created by static (inside the code), but `Reader` is reading from database.
Application has sides for admin, readers and for quests (without authorization).

### Vaadin
Vaadin help me to show the main uses. It's enable on own side (/vaadin), because it's disable with spring security.
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
### Vaadin menu
![menu](https://user-images.githubusercontent.com/82601472/189430886-a366cf2c-25fc-43b2-9edb-effbcb5ed525.png)

### Catalog, where we can filter a books
![filter](https://user-images.githubusercontent.com/82601472/189430815-dafe209d-334a-4b50-be2a-ca285ea861ef.png)

### Register
![register](https://user-images.githubusercontent.com/82601472/189430963-f8ad05f8-5a4d-44f2-8ef1-d33cd81f025c.png)
![showAccounts](https://user-images.githubusercontent.com/82601472/189431017-8a0d40df-a7b8-4a08-b739-e3e54590a57d.png)

### Add book to catalog (in real application, what we can trying in Postman, only Admin can add a new book)
![addBook](https://user-images.githubusercontent.com/82601472/189430668-e0659116-ff10-45b4-b183-1b3c70609eee.png)
![notification](https://user-images.githubusercontent.com/82601472/189430912-345c5a0f-332a-4c42-b925-b3a66597b983.png)

### Borrow and return book (when reader name is empty we don't have a options like borrow and return)
![borrow](https://user-images.githubusercontent.com/82601472/189430758-5c452d1b-db43-4e79-b253-8704cad2096e.png)
![notificationBorrow](https://user-images.githubusercontent.com/82601472/189430929-db2abcc4-0905-4c7f-b9b0-212928dd8bae.png)
![return](https://user-images.githubusercontent.com/82601472/189430989-8a2e3d7f-5290-44e7-97d8-7a492f5bed9f.png)

### History (unfortunately Vaadin give a wrong information about author, so when I borrow book using Vaadin view, history was created by admin, but in Postman we have authorization, so it's work well)
![history](https://user-images.githubusercontent.com/82601472/189430836-581e1e94-3bec-47fd-a1f5-302a3042bff0.png)

### Sending mail with zip file (send to reader, using his email from database)
![mail1](https://user-images.githubusercontent.com/82601472/189430855-60673c9f-368c-49d5-97ab-fd8f7d8a843e.png)
![mail2](https://user-images.githubusercontent.com/82601472/189430874-299dfacc-8299-4024-be80-3dd42eb9bbe6.png)



