### **This is a simple REST service for articles recommendation**

*Functionality:*

* Users can sign up for the website and they will receive a confirmation email. After a user has clicked on a link his account becomes activated and he becomes an authenticated user.
* Authenticated Users can write, delete or edit articles.
* Authentication is based on JWT tokens implemented with Spring Security.
* After Users have clicked on an article, there are articles being recommended to them. The recommendation algoritm is based on tags and author's name.
* Users are able to search for articles by author's name, tags, title and words in the text they contain.

*Features:*
* JWT tokens expire in 10 minutes
* After a token has expired another one is generated with a refresh token which user receives when he has logged in.
* The refresh token expires in six days. During that time users do not have to log in every 10 minutes.
* After six days the refresh token expires and users need to log in again to receive a new refresh token.

### **Links: **

* Website: http://diletant-media-test.tk/
* Swagger documentation: http://diletant-media-test.tk:8080/swagger-ui.html
