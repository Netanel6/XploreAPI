package quiz.presentation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.koin.java.KoinJavaComponent.getKoin
import org.netanel.quiz.usecase.GetQuestionsUseCase

fun Route.quizRoutes() {
    val getQuestionsUseCase: GetQuestionsUseCase = getKoin().get()

    route("/questions") {
        get {
            try {
                val questions = getQuestionsUseCase.execute()
                call.respond(HttpStatusCode.OK, ServerResponse.success(questions, HttpStatusCode.OK.value))
            } catch (e: Exception) {
                // Log the error for debugging purposes
                application.log.error("Error fetching questions", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to fetch questions", HttpStatusCode.InternalServerError.value)
                )
            }
        }
    }
}