package hotkitchen.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import hotkitchen.dto.user.UserDTO
import io.ktor.server.config.*
import java.util.*

class TokenManager(config: HoconApplicationConfig) {
    private val secret = config.property("jwt.secret").getString()
    private val issuer = config.property("jwt.issuer").getString()
    private val audience = config.property("jwt.audience").getString()
    private val expirationDate = System.currentTimeMillis() + (60000 * 60) // 60 seconds * 60 = 1 hour

    fun generateJWTToken(user: UserDTO): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", user.email)
            .withClaim("userType", user.userType.toString())
            .withClaim("userId", user.id)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
    }

    fun verifyJWTToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}