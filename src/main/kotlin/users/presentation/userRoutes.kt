package users.presentation

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.koin.java.KoinJavaComponent.getKoin
import org.netanel.users.repository.model.User
import org.netanel.users.usecase.DeleteUserUseCase
import org.netanel.users.usecase.EditUserUseCase
import org.netanel.users.usecase.GetUserUseCase
import users.usecase.AddUserUseCase
import users.usecase.GetAllUsersUseCase
import users.usecase.AssignQuizForUserUseCase
import users.usecase.DeleteQuizForUserUseCase
import util.JwtConfig

fun Route.userRoutes(jwtConfig: JwtConfig) {
    val getUserUseCase: GetUserUseCase = getKoin().get()
    val getAllUsersUseCase: GetAllUsersUseCase = getKoin().get()
    val addUserUseCase: AddUserUseCase = getKoin().get()
    val editUserUseCase: EditUserUseCase = getKoin().get()
    val updateUserUseCase: AssignQuizForUserUseCase = getKoin().get()
    val deleteQuizForUserUseCase: DeleteQuizForUserUseCase = getKoin().get()
    val deleteUserUseCase: DeleteUserUseCase = getKoin().get()

    route("/users") {
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

            // Generate JWT token
            val token = jwtConfig.generateToken(requestBody.phone_number)

            val userWithToken = requestBody.copy(
                quiz_list = requestBody.quiz_list ?: emptyList(),
                token = token
            )

            val inserted = addUserUseCase.execute(userWithToken)
            if (inserted) {
                call.respond(
                    HttpStatusCode.Created,
                    ServerResponse.success(userWithToken, HttpStatusCode.Created.value)
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to create user.", HttpStatusCode.InternalServerError.value)
                )
            }
        }

        get("{phoneNumber}") {
            val phoneNumber = call.parameters["phoneNumber"]
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

        authenticate("authJWT") {
            patch("{userId}/quizzes") {
                val userId = call.parameters["userId"]
                if (userId.isNullOrEmpty()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ServerResponse.error("User ID is required", HttpStatusCode.BadRequest.value)
                    )
                    return@patch
                }

                try {
                    // Receive Quiz object from the body
                    val quiz = call.receive<User.Quiz>()

                    if (quiz.id.isNullOrEmpty() || quiz.title.isNullOrEmpty()) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ServerResponse.error("Quiz ID and title are required", HttpStatusCode.BadRequest.value)
                        )
                        return@patch
                    }

                    val isUpdated = updateUserUseCase.execute(userId, quiz)

                    if (isUpdated) {
                        call.respond(
                            HttpStatusCode.OK,
                            ServerResponse.success("Quiz successfully assigned", HttpStatusCode.OK.value)
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            ServerResponse.error("Failed to assign quiz", HttpStatusCode.InternalServerError.value)
                        )
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ServerResponse.error("An error occurred: ${e.message}", HttpStatusCode.InternalServerError.value)
                    )
                }
            }

            put("{userId}") {
                val userId = call.parameters["userId"]
                val updatedUser = call.receive<User>()

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ServerResponse.error("User ID is required", HttpStatusCode.BadRequest.value))
                    return@put
                }

                val updated = editUserUseCase.execute(userId, updatedUser)
                if (updated) {
                    call.respond(HttpStatusCode.OK, ServerResponse.success("User updated successfully", HttpStatusCode.OK.value))
                } else {
                    call.respond(HttpStatusCode.NotFound, ServerResponse.error("User not found", HttpStatusCode.NotFound.value))
                }
            }

            delete("{userId}") {
                val userId = call.parameters["userId"]
                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ServerResponse.error("User ID is required", HttpStatusCode.BadRequest.value))
                    return@delete
                }

                val deleted = deleteUserUseCase.execute(userId)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, ServerResponse.success("User deleted successfully", HttpStatusCode.OK.value))
                } else {
                    call.respond(HttpStatusCode.NotFound, ServerResponse.error("User not found", HttpStatusCode.NotFound.value))
                }
            }

            delete("{userId}/quizzes/{quizId}") {
                val userId = call.parameters["userId"]
                val quizId = call.parameters["quizId"]

                if (userId.isNullOrEmpty() || quizId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ServerResponse.error("User ID and Quiz ID are required", HttpStatusCode.BadRequest.value))
                    return@delete
                }

                val removed = deleteQuizForUserUseCase.execute(userId, quizId)
                if (removed) {
                    call.respond(HttpStatusCode.OK, ServerResponse.success("Quiz removed from user successfully", HttpStatusCode.OK.value))
                } else {
                    call.respond(HttpStatusCode.NotFound, ServerResponse.error("Quiz or user not found", HttpStatusCode.NotFound.value))
                }
            }


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
        }
    }
}
