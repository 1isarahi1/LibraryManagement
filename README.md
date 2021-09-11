# LibraryManagement
A Library portal to manage the book requests and fines for any physical library management.  

Functionality for User
-A user can request a book from the library.
-The book request can be approved or denied through the librarian.
-A user can return a book through the software.
-A user can pay any late fines.
-The user can post a profile picture which can be attached to their user information.
-The user has access to an edit profile page allowing for a change of password, a change of email or a change of name.
-The user automatically receives email notifications and in website notifications for when a book request is approved or denied, and when a book needs to be returned soon.
-The user can see all the books in the library that are available by clicking the show all books button. This button is linked to a page with auto generated cards which is connected to the book database. All books are visible and can be scrolled through similar to a shopping cart.


Functionality for Librarian
-A librarian can approve or request any and all user requests for a book. They can grant user access.
-A librarian can auto generate all user fines that they have to pay for late books. (This should be done weekly so as to update the database)
-A librarian can change a user password upon request. The password is double validated prior to updating in the database.
-A librarian can see all the books in the library that are available. This is the same functionality as the user.

Functionality for Admin (Recommended to only have 1-2 Admins)
-The admin can add new books to the database. This will lead to new books being linked to the show all books functionality for user/librarian.
-The admin can approve or deny the accounts to make new librarians.
-The admin also has the option to change passwords and check validation but, the admin can also change the password for a librarian if need be.
-The admin can see all the books in the library that are available. This is the same functionality as the user/librarian.
-The admin can send the auto generate user return book notifications to all users email ids and homepage notifications. This happens in real time.


Methods:
-General functionalities used throughout the software.

-Function Approve/Deny: This function is used twice. To approve user book requests and new librarian accounts.
-Function Calculate Fine: Function is used to calculate how much money users owe on a late book. With a single click we can generate fines for all users within seconds.
-Function Validation: Password and Email validation. Used multiple times. Allows for users to make email and passwords that meet case sensitive and numeric criteria. Also allows for password comparison when trying to login, making sure that the password matches the database.
-Function Email Notifications: Used for all notifications sent to user. Used multiple times for general purposes such as when a user owes a money or when a user changes their account information.

HTML Details:
-General HTML
-Flippable cards are used to display books visually for user design.
-The navigation bar is designed to allow access to each page without ever having to click the back button.

->In general the goal for the html was to be as minimalistic as possible so as to recreate the feeling of a in person library. Cool toned colors where used alongside easily navigable interface.


In Summation
->This digitable library software is designed to be as adaptable as possible to customer wants and needs for design. This means new functionality can be added wihtout impacting prior design. The digitable library software can be used both as an ebook provider or as a software for a real in person library that is trying to convert to how users borrow and pay for books. The html can be adapted to use different colors and buttons, whilst also having the option to add paid facebook/google API and/or paid paypal/paypass api to sign in or pay for books. 


Details:

-Technical specifications include java, javascript, html, sql, JDBC, Servlets, JSP.




