package users.presentation

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.koin.java.KoinJavaComponent.getKoin

import org.netanel.users.usecase.GetUserUseCase
import users.usecase.GetAllUsersUseCase

fun Route.userRoutes() {
    val getUserUseCase: GetUserUseCase = getKoin().get()
    val getAllUsersUseCase: GetAllUsersUseCase = getKoin().get()

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
    }
}