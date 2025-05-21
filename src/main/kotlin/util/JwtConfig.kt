package util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*

class JwtConfig(
    private val secret: String,
    private val issuer: String,
    private val audience: String,
    val realm: String
) {
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(phoneNumber: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("phoneNumber", phoneNumber)
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
            if (credential.payload.getClaim("phoneNumber").asString() != null) {
                JWTPrincipal(credential.payload)
            } else null
        }
        config.realm = realm
    }
}
