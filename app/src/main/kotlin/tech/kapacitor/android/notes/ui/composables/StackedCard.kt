package tech.kapacitor.android.notes.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StackedCard(
    modifier: Modifier = Modifier,
    length: Int = 1,
    index: Int = 1,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier, shape = when (index) {
            0 -> RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp,
                bottomStart = 2.dp,
                bottomEnd = 2.dp
            )

            length - 1 -> RoundedCornerShape(
                topStart = 2.dp,
                topEnd = 2.dp,
                bottomStart = 32.dp,
                bottomEnd = 32.dp
            )

            else -> RoundedCornerShape(size = 2.dp)
        }
    ) {
        content()
    }
}