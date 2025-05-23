package org.netanel

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.ktor.plugin.Koin
import org.netanel.di.appModule
import org.netanel.quiz.di.quizModule
import org.netanel.users.di.usersModule
import quiz.presentation.quizRoutes
import users.presentation.userRoutes
import util.EnvironmentConfig
import util.JwtConfig

fun main() {
    val port: Int = EnvironmentConfig.getInt("PORT", 8080)
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    // Initialize Koin for Dependency Injection
    install(Koin) {
        modules(appModule, usersModule, quizModule)
    }

    // Get JwtConfig instance from Koin
    val jwtConfig: JwtConfig = getKoin().get()

    // Configure Authentication
    install(Authentication) {
        jwt("authJWT") {
            jwtConfig.configureKtor(this)
        }
    }

    // Install Content Negotiation
    install(ContentNegotiation) { json() }

    configureCORS()
    // Install CORS to handle cross-origin requests
   /* install(CORS) {
        anyHost()
        allowHost("localhost:3000") // Allow requests from React app
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
    }
*/
    // Routing
    routing {
        // Root Endpoint
        get("/") {
            call.respond(HttpStatusCode.OK, "Hello from XploreAPI ")
        }

        userRoutes(jwtConfig)
        authenticate("authJWT") {
            quizRoutes()
        }
    }
}

fun Application.configureCORS() {
    install(CORS) {
        allowHost("xplore-653a16181c0e.herokuapp.com", schemes = listOf("https")) // Production URL
        allowHost("10.0.2.2:8080") // Android Emulator
        allowHost("localhost:3000") // Web frontend (React)

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Put)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.Accept)

        allowCredentials = true // Allow cookies or authentication headers
    }
}