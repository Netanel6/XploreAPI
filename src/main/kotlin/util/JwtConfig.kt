package util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*

object JwtConfig {
    private const val secret = "XploreSecretKey"
    private const val issuer = "XploreIssuer"
    private const val audience = "XploreAudience"
    const val realm = "xplore_realm"

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(phoneNumber: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("phoneNumber", phoneNumber)
            .sign(algorithm)
    }
}
