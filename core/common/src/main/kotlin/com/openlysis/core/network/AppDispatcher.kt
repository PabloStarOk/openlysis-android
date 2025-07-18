package com.openlysis.core.network

/**
 * Enum representing application dispatchers for coroutine context selection.
 * - Default: Used for CPU-intensive work.
 * - IO: Used for IO-bound operations.
 */
enum class AppDispatcher {
    Default,
    IO
}