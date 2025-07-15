package com.openlysis.feature.results.components.detail

import android.content.ClipData
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.feature.results.R
import kotlinx.coroutines.launch

/**
 * A section with hash values (SHA-256, SHA-1, SHA-512, MD5) in an accordion UI.
 *
 * @param hashValues The hash values to display.
 * @param isPrimarySection Whether this section is a primary section in the UI.
 * @param modifier Modifier for styling and layout.
 */
@Composable
internal fun HashValuesSection(
    hashValues: HashValues,
    isPrimarySection: Boolean,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val copiedHashMessage = stringResource(R.string.details_screen_hash_value_copied)
    val copyableHashValues =
        listOf(
            Pair(
                stringResource(R.string.details_screen_hash_sha_256),
                hashValues.sha256
            ),
            Pair(
                stringResource(R.string.details_screen_hash_sha_1),
                hashValues.sha1
            ),
            Pair(
                stringResource(R.string.details_screen_hash_sha_512),
                hashValues.sha512
            ),
            Pair(
                stringResource(R.string.details_screen_hash_md5),
                hashValues.md5
            )
        )

    fun copyHashToClipboard(labeledHash: Pair<String, String>) {
        val data = ClipData.newPlainText(labeledHash.first, labeledHash.second)
        val entry = ClipEntry(data)
        coroutineScope.launch {
            clipboard.setClipEntry(entry)
            Toast.makeText(context, copiedHashMessage, Toast.LENGTH_SHORT).show()
        }
    }

    SectionAccordion(
        title = stringResource(R.string.details_screen_hash),
        isPrimarySection = isPrimarySection,
        modifier = modifier
    ) {
        copyableHashValues.forEach { labeledHash ->
            InformationCard(
                label = labeledHash.first,
                information = labeledHash.second,
                showCopyButton = true,
                onCopyClick = { copyHashToClipboard(labeledHash) }
            )
        }
    }
}