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

    fun generateToken(username: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("username", username)
            .sign(algorithm)
    }

    fun configureKtor(config: JWTAuthenticationProvider.Config) {
        config.verifier(
            JWT
                .require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
        )
        config.validate { credential ->
            if (credential.payload.getClaim("username").asString() != null) {
                JWTPrincipal(credential.payload)
            } else null
        }
        config.realm = realm
    }
}
