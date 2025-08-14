package com.openlysis.data.remote.jwt

import com.openlysis.data.remote.constant.JwtStructure
import com.squareup.moshi.Moshi
import java.util.Base64
import javax.inject.Inject

/**
 * Default implementation of [JwtPayloadDecoder] that decodes JWT payloads using Moshi.
 *
 * @property moshi Moshi instance used for JSON parsing.
 */
internal class DefaultJwtPayloadDecoder
    @Inject
    constructor(
        private val moshi: Moshi
    ) : JwtPayloadDecoder {
        override fun decode(encodedJwt: String): JwtPayload {
            val encodedPayload = getPayload(encodedJwt)
            var adapter = moshi.adapter(JwtPayload::class.java)

            val decodedPayloadBytes = Base64.getUrlDecoder().decode(encodedPayload)
            var decodedPayload = decodedPayloadBytes.toString(Charsets.UTF_8)

            var jwtPayload = adapter.fromJson(decodedPayload)
            if (jwtPayload == null) {
                throw IllegalArgumentException("Invalid JWT payload: cannot parse expiration")
            }

            return jwtPayload
        }

        private fun getPayload(encodedJwt: String): String {
            val jwtParts = encodedJwt.split(JwtStructure.CHAR_SEPARATOR)
            return jwtParts[JwtStructure.PAYLOAD_POSITION_IN_ARRAY]
        }
    }