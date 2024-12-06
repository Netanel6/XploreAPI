package quiz.presentation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.bson.types.ObjectId
import org.koin.java.KoinJavaComponent.getKoin
import org.netanel.quiz.usecase.GetQuestionsUseCase
import org.netanel.quiz.usecase.GetQuizUseCase

fun Route.quizRoutes() {
    val getQuestionsUseCase: GetQuestionsUseCase = getKoin().get()
    val getQuizUseCase: GetQuizUseCase = getKoin().get()

    route("/quiz") {

            get("{quizId}"){
                val quizId = call.parameters["quizId"]
                if (quizId == null || !ObjectId.isValid(quizId)) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Quiz ID")
                    return@get
                }
                try {
                    val quiz = getQuizUseCase.execute(quizId)
                    call.respond(HttpStatusCode.OK, ServerResponse.success(quiz, HttpStatusCode.OK.value))
                } catch (e: Exception) {
                    application.log.error("Error fetching quiz", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ServerResponse.error("Failed to fetch quiz", HttpStatusCode.InternalServerError.value)
                    )
                }
            }
    }
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