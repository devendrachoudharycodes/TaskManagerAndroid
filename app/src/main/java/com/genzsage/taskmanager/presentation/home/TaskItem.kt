package com.genzsage.taskmanager.presentation.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.genzsage.taskmanager.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(task: Task,
             onDelete:()-> Unit,
             onChecked:()-> Unit,
             onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.onPrimaryContainer),
        ){


        TaskCardContent(
            title = task.title,
            description = task.description,
            priority = task.priority.toString(),
            dueDate = formatDate(task.timeDateDue),
            creationDate = formatDate(task.timeDateAdded),
            isCompleted = task.isCompleted,
            onDelete = { onDelete() },
            onToggleComplete = { onChecked() },
            onEdit = onEdit,
        )

    }

}

// --- UI Helper Functions ---

@Composable
fun TaskCardContent(
    title: String,
    description: String,
    priority: String,
    dueDate: String,
    creationDate: String,
    isCompleted: Boolean,
    onDelete: () -> Unit,
    onToggleComplete: () -> Unit,
    onEdit:()->Unit={}
) {
    var showEditDialog by remember { mutableStateOf(false) }
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit this task?") },
            text = { Text("Will navigate to edit screen") },
            confirmButton = {
                TextButton(onClick = { onEdit()
                    showEditDialog = false }) {
                    Text("Edit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // Top row -> title + action icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

               ExpandableText(
                   text = description,
                   minimizedMaxLines = 3
               )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row {Column() {
                IconButton(onClick = onToggleComplete) {
                    Icon(
                        imageVector = if (isCompleted)
                            Icons.Default.CheckCircle
                        else
                            Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Toggle Complete"
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task"
                    )
                }

                IconButton(onClick = {
                    showEditDialog=!showEditDialog

                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Task"
                    )
                }
            }}
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom info section
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row{Text(
                text = "Priority: $priority",
                style = MaterialTheme.typography.bodySmall
            )
            PriorityChip(priority= Integer.parseInt(priority))
            }

            Text(
                text = "Due: $dueDate",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Created: $creationDate",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 3
) {
    // Local UI state for expansion and overflow detection
    var isExpanded by remember { mutableStateOf(false) }
    var hasVisualOverflow by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            // Toggle between max lines and infinite lines
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            // Check if the text actually exceeds our max lines
            onTextLayout = { textLayoutResult ->
                if (!isExpanded) {
                    hasVisualOverflow = textLayoutResult.hasVisualOverflow
                }
            },
            // Adds a smooth transition when expanding/collapsing
            modifier = Modifier.animateContentSize()
        )

        // Only show the toggle if the text is long enough to overflow, or if it's already expanded
        if (hasVisualOverflow || isExpanded) {
            Text(
                text = if (isExpanded) "Read Less" else "Read More",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { isExpanded=!isExpanded }
            )
        }
    }
}

@Composable
fun PriorityChip(priority: Int) {
    // Assuming 1 = Low, 2 = Medium, 3 = High
    val (label, color) = when (priority) {
        5 -> "Critical" to Color(0xFFB71C1C) // Deep Red
        4 -> "High" to Color(0xFFE53935)     // Standard Red
        3 -> "Medium" to Color(0xFFF57C00)   // Orange
        2 -> "Low" to Color(0xFF388E3C)      // Green
        else -> "Lowest" to Color(0xFF0288D1)   // Blue
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}