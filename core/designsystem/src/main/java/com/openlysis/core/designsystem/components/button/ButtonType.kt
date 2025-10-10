package com.openlysis.core.designsystem.components.button

/**
 * Represents the different visual styles or semantic purposes of buttons within the application.
 *
 * @property enabled Indicates whether the button is enabled or disabled.
 *
 * ## Types
 * - **Primary:** The main call-to-action button on a page or section. Typically has the most visual prominence.
 * - **Secondary:** An alternative action to the primary button. Less prominent than the primary button.
 * - **Tertiary:** Used for less important actions or actions that should blend in more with the surrounding content. Often appears as a link or ghost button.
 * - **Positive:** Indicates an action with a positive outcome, such as "Save", "Confirm", or "Accept".
 * - **Danger:** Indicates an action with a potentially negative or destructive outcome, such as "Delete", "Cancel", or "Discard".
 * - **PrimaryDisabled:** A disabled state of the primary button, not interactive.
 */
enum class ButtonType(
    val enabled: Boolean
) {
    Primary(enabled = true),
    Secondary(enabled = true),
    Tertiary(enabled = true),
    Positive(enabled = true),
    Danger(enabled = true),
    PrimaryDisabled(enabled = false)
}