package com.genzsage.taskmanager.presentation.newtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CreateOrUpdateTask(
    taskId: Int = 0,
    viewModel: TaskCreateOrUpdateViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit = {}
) {
    val task by viewModel.taskState.collectAsState()
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    LaunchedEffect(taskId) {
        viewModel.handleTask(taskId)
    }

    fun showDateTimePicker() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)

                        viewModel.updateTask {
                            it.copy(timeDateDue = calendar.timeInMillis)
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val formattedDate = remember(task.timeDateDue) {
        if (task.timeDateDue == 0L) {
            "Select Due Date & Time"
        } else {
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(Date(task.timeDateDue))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
// 1. Track current focus and whether the user has "exited" the field
        var isFocused by remember { mutableStateOf(false) }
        var hasExited by remember { mutableStateOf(false) }

// 2. The error only shows if they have exited AND the text is blank
        val showError = hasExited && task.title.isBlank()

       ValidatedTextField(
           value = task.title,
           onValueChange = {it ->
               viewModel.updateTask { task->
                   task.copy(title = it)
               }
           },
           label ="Title",
           errorText = "Title can not be blank"
       )

        ValidatedTextField(
            value = task.description,
            onValueChange = {it ->
                viewModel.updateTask { task->
                    task.copy(description = it)
                }
            },
            label ="Description",
            errorText = "Description can not be blank"
        )


        OutlinedButton(
            onClick = { showDateTimePicker() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(formattedDate)
        }

        PriorityDropdown(
            selectedPriority = task.priority,
            onPrioritySelected = { selected ->
                viewModel.updateTask {
                    it.copy(priority = selected)
                }
            }
        )

        Row {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { checked ->
                    viewModel.updateTask {
                        it.copy(isCompleted = checked)
                    }
                }
            )
            Text("Mark Completed")
        }

        Column {
            var error by remember { mutableStateOf(false) }
            if(error ){
                Text("Select Due Date and Time",
                    color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    if(task.timeDateDue == 0L)
                        error=true
                    if( viewModel.saveTask())
                    { onSaveSuccess()}
                    else{
                        Toast.makeText(context, "Fill required fields", Toast.LENGTH_SHORT).show();                }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    selectedPriority: Int,
    onPrioritySelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPriority.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Priority") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf(1, 2, 3, 4, 5).forEach { value ->
                DropdownMenuItem(
                    text = { Text(value.toString()) },
                    onClick = {
                        onPrioritySelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    errorText: String = "This field is required"
) {
    var isFocused by remember { mutableStateOf(false) }
    var hasExited by remember { mutableStateOf(false) }

    // Only show error if the user has left the field and it is empty
    val showError = hasExited && value.isBlank()

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            // Optional: reset error while typing if you want immediate feedback
            if (it.isNotBlank()) hasExited = false
        },
        label = { Text(label) },
        isError = showError,
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                } else if (isFocused) {
                    // Logic: Trigger exit when focus moves from true to false
                    hasExited = true
                    isFocused = false
                }
            },
        supportingText = {
            if (showError) {
                Text(text = errorText)
            }
        }
    )
}