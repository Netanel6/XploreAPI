package users.presentation

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.koin.java.KoinJavaComponent.getKoin
import org.netanel.users.repository.model.User

import org.netanel.users.usecase.GetUserUseCase
import users.usecase.AddUserUseCase
import users.usecase.GetAllUsersUseCase

fun Route.userRoutes() {
    val getUserUseCase: GetUserUseCase = getKoin().get()
    val getAllUsersUseCase: GetAllUsersUseCase = getKoin().get()
    val addUserUseCase: AddUserUseCase = getKoin().get()

    route("/users") {
        // Retrieve a single user by phone number
        get {
            val phoneNumber = call.request.queryParameters["phoneNumber"]
            if (phoneNumber.isNullOrEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ServerResponse.error("Phone number is required", HttpStatusCode.BadRequest.value)
                )
                return@get
            }

            val user = getUserUseCase.execute(phoneNumber)
            if (user != null) {
                call.respond(
                    HttpStatusCode.OK,
                    ServerResponse.success(user, HttpStatusCode.OK.value)
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ServerResponse.error("User not found", HttpStatusCode.NotFound.value)
                )
            }
        }

        // Retrieve all users
        get("/all") {
            val users = getAllUsersUseCase.execute()
            if (users?.isNotEmpty() == true) {
                call.respond(
                    HttpStatusCode.OK,
                    ServerResponse.success(users, HttpStatusCode.OK.value)
                )
            } else {
                call.respond(
                    HttpStatusCode.NoContent,
                    ServerResponse.error("No users found", HttpStatusCode.NoContent.value)
                )
            }
        }
        // POST /users
        post {
            val requestBody = call.receive<User>()

            // Validate input
            if (requestBody.phone_number.isEmpty() || requestBody.name.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ServerResponse.error("Phone number and name are required.", HttpStatusCode.BadRequest.value)
                )
                return@post
            }

            // Ensure quiz_list is empty if not provided
            val user = requestBody.copy(quiz_list = requestBody.quiz_list ?: emptyList())

            // Insert user into the database
            val inserted = addUserUseCase.execute(user) // Implement `addUser` in your repository
            if (inserted) {
                call.respond(
                    HttpStatusCode.Created,
                    ServerResponse.success(user, HttpStatusCode.Created.value)
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to create user.", HttpStatusCode.InternalServerError.value)
                )
            }
        }
    }
}