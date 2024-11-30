package org.netanel

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.plugin.Koin
import org.netanel.di.appModule
import org.netanel.questions.di.questionsModule
import org.netanel.questions.usecase.GetQuestionsUseCase
import org.netanel.users.usecase.GetUserUseCase
import org.netanel.users.usersModule


fun main() {

    // Get the port from the environment variable or default to 8080
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModule, usersModule, questionsModule)
    }

    install(ContentNegotiation) { json() }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
    }


    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Hello from XploreAPI ")
        }
        get("/users") {
            val getUserUseCase: GetUserUseCase = getKoin().get()
            val phoneNumber = call.request.queryParameters["phoneNumber"]
            if (phoneNumber.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "Phone number is required")
                return@get
            }

            val user = getUserUseCase.execute(phoneNumber)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }

        }
        get("/questions") {
            try {
                val getQuestionsUseCase: GetQuestionsUseCase = getKoin().get()
                val questions = getQuestionsUseCase.execute()
                call.respond(questions)
            } catch (e: Exception) {
                // Handle exception gracefully
                println("Error fetching questions: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Failed to fetch questions")
            }
        }
    }
}