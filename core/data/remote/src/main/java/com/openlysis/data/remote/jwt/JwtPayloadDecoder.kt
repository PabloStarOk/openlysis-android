package com.openlysis.data.remote.jwt

/**
 * Interface for decoding the payload from a JWT string.
 */
internal interface JwtPayloadDecoder {
    /**
     * Decodes the given encoded JWT string and returns its payload.
     *
     * @param encodedJwt The JWT string to decode.
     * @return The decoded JWT payload.
     */
    fun decode(encodedJwt: String): JwtPayload
}