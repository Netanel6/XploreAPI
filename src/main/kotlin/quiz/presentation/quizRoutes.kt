package quiz.presentation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ServerResponse
import org.bson.types.ObjectId
import org.koin.java.KoinJavaComponent.getKoin
import org.netanel.quiz.repository.model.Quiz
import org.netanel.quiz.usecase.EditQuizUseCase
import org.netanel.quiz.usecase.GetQuestionsUseCase
import org.netanel.quiz.usecase.GetQuizListForUserUseCase
import org.netanel.quiz.usecase.GetQuizUseCase
import quiz.repository.model.UpdateScoreRequest
import quiz.usecase.AddQuizUseCase
import quiz.usecase.GetQuizListUseCase
import quiz.usecase.UpdateScoreUseCase

fun Route.quizRoutes() {
    val getQuestionsUseCase: GetQuestionsUseCase = getKoin().get()
    val getQuizListUseCase: GetQuizListUseCase = getKoin().get()
    val getQuizListForUserUseCase: GetQuizListForUserUseCase = getKoin().get()
    val getQuizUseCase: GetQuizUseCase = getKoin().get()
    val addQuizUseCase: AddQuizUseCase = getKoin().get()
    val editQuizUseCase: EditQuizUseCase = getKoin().get()
    val updateScoreUseCase: UpdateScoreUseCase = getKoin().get()

    route("/quizzes") {
        get("/all") {
            try {
                val quizzes = getQuizListUseCase.execute()
                call.respond(HttpStatusCode.OK, ServerResponse.success(quizzes, HttpStatusCode.OK.value))
            } catch (e: Exception) {
                application.log.error("Error fetching quiz list", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to fetch quiz list", HttpStatusCode.InternalServerError.value)
                )
            }
        }

        get("/{userId}") {
            try {
                val userId = call.parameters["userId"]
                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Quiz ID")
                    return@get
                }

                val quizzes = getQuizListForUserUseCase.execute(userId)
                call.respond(HttpStatusCode.OK, ServerResponse.success(quizzes, HttpStatusCode.OK.value))
            } catch (e: Exception) {
                application.log.error("Error fetching quiz list", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to fetch quiz list", HttpStatusCode.InternalServerError.value)
                )
            }
        }

        get("/quiz/{quizId}") {
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

        put("/quiz/{quizId}") {
            val quizId = call.parameters["quizId"]
            val updatedQuiz = call.receive<Quiz>()

            if (quizId.isNullOrEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ServerResponse.error("Quiz ID is required", HttpStatusCode.BadRequest.value)
                )
                return@put
            }

            val updated = editQuizUseCase.execute(quizId, updatedQuiz)
            updated?.let {
                call.respond(
                    HttpStatusCode.OK,
                    ServerResponse.success("Quiz updated successfully", HttpStatusCode.OK.value)
                )
            } ?: call.respond(
                HttpStatusCode.NotFound,
                ServerResponse.error("Quiz not found", HttpStatusCode.NotFound.value)
            )
        }

        patch("/quiz/{quizId}/scores") {
            val quizId = call.parameters["quizId"]
            if (quizId == null || !ObjectId.isValid(quizId)) {
                call.respond(HttpStatusCode.BadRequest, "Invalid Quiz ID")
                return@patch
            }

            val updateScoreRequest = call.receive<UpdateScoreRequest>()
            val updatedQuiz = updateScoreUseCase.execute(quizId, updateScoreRequest)

            updatedQuiz?.let {
                call.respond(HttpStatusCode.OK, ServerResponse.success(it, HttpStatusCode.OK.value))
            } ?: call.respond(
                HttpStatusCode.NotFound,
                ServerResponse.error("Quiz not found", HttpStatusCode.NotFound.value)
            )
        }

        post("/quiz") {
            try {
                val quiz = call.receive<Quiz>()
                val result = addQuizUseCase.execute(quiz)
                if (result) {
                    call.respond(HttpStatusCode.Created, ServerResponse.success(quiz, HttpStatusCode.Created.value))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ServerResponse.error("Failed to add quiz", HttpStatusCode.InternalServerError.value)
                    )
                }
            } catch (e: Exception) {
                application.log.error("Error adding quiz", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to add quiz", HttpStatusCode.InternalServerError.value)
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
                application.log.error("Error fetching questions", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ServerResponse.error("Failed to fetch questions", HttpStatusCode.InternalServerError.value)
                )
            }
        }
    }
}
