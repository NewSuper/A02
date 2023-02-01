import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun GameOverDialog(
    title: String,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        confirmButton = { TextButton(onClick = { onConfirmListener.invoke() }) { Text("OK") } },
        onDismissRequest = { onDismissListener.invoke() },
    )
}