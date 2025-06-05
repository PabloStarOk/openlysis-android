package com.openlysis.core.designsystem.components.button

/**
 * Represents the different visual styles or semantic purposes of buttons within the application.
 *
 * - **Primary:** The main call-to-action button on a page or section. Typically has the most visual prominence.
 * - **Secondary:** An alternative action to the primary button. Less prominent than the primary button.
 * - **Tertiary:** Used for less important actions or actions that should blend in more with the surrounding content. Often appears as a link or ghost button.
 * - **Positive:** Indicates an action with a positive outcome, such as "Save", "Confirm", or "Accept".
 * - **Danger:** Indicates an action with a potentially negative or destructive outcome, such as "Delete", "Cancel", or "Discard".
 */
enum class ButtonType {
    Primary,
    Secondary,
    Tertiary,
    Positive,
    Danger
}