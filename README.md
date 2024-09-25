# My Spring Boot App

This is a Spring Boot application that provides endpoints for user management. It allows you to create, retrieve, update, and delete user records. It deals with manager of user also.

## Getting Started

1. Clone the repository:

   ```shell
   git clone https://github.com/jameelashaik/my-spring-boot-app.git
   ```

2. Navigate to the project directory:

   ```shell
   cd my-spring-boot-app
   ```

3. Build the application:

   ```shell
   mvn clean install
   ```

4. Run the application:

   ```shell
   mvn spring-boot:run
   ```

   The application will start running on `http://localhost:8080`.

## Endpoints

### Create User

- URL: `/create_user`
- Method: POST
- Request Body: JSON object representing the user data
- Response: JSON object representing the created user

### Get Users

- URL: `/get_users`
- Method: GET
- Response: JSON array representing all the users

### Delete User

- URL: `/delete_user/{user_id}`
- Method: DELETE
- Path Variable: `user_id` - the ID of the user to delete
- Response: JSON object representing the deleted user

### Update User

- URL: `/update_user/{user_id}`
- Method: PUT
- Path Variable: `user_id` - the ID of the user to update
- Request Body: JSON object representing the updated user data
- Response: JSON object representing the updated user

## Sample API Calls

Here are some sample API calls using cURL:

1. Create User:

   ```shell
   curl -X POST -H "Content-Type: application/json" -d '{
     "full_name": "John Doe",
     "mob_num": "1234567890",
     "pan_num": "ABCDE1234F"
   }' http://localhost:8080/create_user
   ```

2. Get Users:

   ```shell
   curl http://localhost:8080/get_users
   ```

3. Delete User:

   ```shell
   curl -X DELETE http://localhost:8080/delete_user/1
   ```

4. Update User:

   ```shell
   curl -X PUT -H "Content-Type: application/json" -d '{
     "full_name": "Jane Smith",
     "mob_num": "9876543210",
     "pan_num": "FGHIJ5678K",
     "manager_id": 2
   }' http://localhost:8080/update_user/2
   ```

