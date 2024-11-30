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
import org.koin.ktor.plugin.Koin
import org.netanel.di.appModule
import org.netanel.quiz.di.quizModule
import org.netanel.users.di.usersModule
import quiz.presentation.quizRoutes
import users.presentation.userRoutes


fun main() {

    // Get the port from the environment variable or default to 8080
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModule, usersModule, quizModule)
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
        userRoutes()
        quizRoutes()
    }
}