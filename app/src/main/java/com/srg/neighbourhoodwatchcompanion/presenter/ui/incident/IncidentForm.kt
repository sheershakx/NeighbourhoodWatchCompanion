package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.extension.cast
import com.srg.neighbourhoodwatchcompanion.AppNavigator
import com.srg.neighbourhoodwatchcompanion.BottomNavGraph
import com.srg.neighbourhoodwatchcompanion.common.InputValidationTextField
import com.srg.neighbourhoodwatchcompanion.common.LargeSpacer
import com.srg.neighbourhoodwatchcompanion.common.PermissionHelper.handleImagePermissionAndLaunchPicker
import com.srg.neighbourhoodwatchcompanion.common.formatDateFromMillis
import com.srg.neighbourhoodwatchcompanion.common.showToast
import com.srg.neighbourhoodwatchcompanion.presenter.theme.colors
import com.srg.neighbourhoodwatchcompanion.presenter.theme.typo
import io.github.jan.supabase.exceptions.BadRequestRestException
import java.util.Calendar

@SuppressLint("InlinedApi")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@BottomNavGraph
@Composable
fun IncidentFormScreen(
    viewModel: IncidentFormViewModel = hiltViewModel(),
    appNavigator: AppNavigator,
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val incidentTypeOptions by viewModel.incidentTypeOptions.collectAsState()
    val selectedIncidentType by viewModel.selectedIncidentType.collectAsState()
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val date by viewModel.date.collectAsState()
    val time by viewModel.time.collectAsState()
    val casualties by viewModel.casualties.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val scrollState = rememberScrollState()

    val incidentLocationInformation by viewModel.incidentLocationInformation.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(true)

    var openBottomSheet by remember { mutableStateOf(false) }

    var typeExpandedState by remember { mutableStateOf(false) }
    val locationInteractionSource = remember { MutableInteractionSource() }

    //location screen related
    val predictions by viewModel.predictions.collectAsState()
    val locationSearchText by viewModel.locationSearchText.collectAsState()
    val previewSelectedLocation by viewModel.previewSelectedLocation

    //  todo( "Change imagePermissionRequested to be saved in datastore as this doesnot work if use exits the app after permanent denied and then comes back to app, as this remember state is already lost" )
    var imagePermissionRequested by rememberSaveable { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { result ->
        if (result == null) {
            return@rememberLauncherForActivityResult
        }
        viewModel.onTriggerEvent(IncidentFormEvent.AddImageToPreview(result))

    }

    val imagePermissionState =
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)

    LaunchedEffect(locationInteractionSource) {
        locationInteractionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                openBottomSheet = !openBottomSheet
            }
        }
    }
    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is BaseViewState.Data -> {
                val incidentFormState = uiState.cast<BaseViewState.Data<IncidentFormState>>().value
                if (incidentFormState.incidentFormSavedSuccessful == true) {
                    context.showToast("Incident form saved successfully !")
                    appNavigator.navigateBack()
                    viewModel.clearState()
                }

            }

            is BaseViewState.Error -> {
                val error = uiState.cast<BaseViewState.Error>().throwable
                if (error is BadRequestRestException) {
                    context.showToast(
                        error.description ?: error.error
                    )
                } else {
                    context.showToast(error.message.toString())
                }
            }

            else -> {}
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if (uiState == BaseViewState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 2.dp, bottom = 10.dp)

                .alpha(if (uiState is BaseViewState.Loading) 0.5f else 1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Incident Type", style = typo.titleMedium.copy(color = colors.onSurface))
            ExposedDropdownMenuBox(
                expanded = typeExpandedState,
                onExpandedChange = { typeExpandedState = it }) {
                InputValidationTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    inputWrapper = Pair(
                        selectedIncidentType.first.incidentType ?: "", selectedIncidentType.second
                    ),
                    placeHolder = "Select an option",
                    mTrailingIcon = {
                        Icon(
                            imageVector = if (typeExpandedState) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    },
                    onValueChange = {})
                ExposedDropdownMenu(
                    expanded = typeExpandedState,
                    onDismissRequest = { typeExpandedState = false }) {
                    incidentTypeOptions.forEach { incidentType ->
                        DropdownMenuItem(
                            text = { Text(incidentType.incidentType.toString()) },
                            onClick = {
                                viewModel.onIncidentTypeSelected(incidentType)
                                typeExpandedState = false
                            })
                    }
                }
            }
            Text("Title", style = typo.titleMedium.copy(color = colors.onSurface))
            InputValidationTextField(
                modifier = Modifier.fillMaxWidth(),
                inputWrapper = title,
                placeHolder = "Incident title",
                onValueChange = viewModel::onTitleUpdated
            )
            Text("Description", style = typo.titleMedium.copy(color = colors.onSurface))
            InputValidationTextField(
                modifier = Modifier.fillMaxWidth(),
                inputWrapper = description,
                placeHolder = "Incident description",
                onValueChange = viewModel::onDescriptionUpdated
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Date", style = typo.titleMedium.copy(color = colors.onSurface))
                    DatePickerField(selectedDate = date, onDateSelected = {
                        viewModel.onDateUpdated(it)
                    })
                }
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Time", style = typo.titleMedium.copy(color = colors.onSurface))
                    TimePickerField(time) {
                        viewModel.onTimeUpdated("${it.first}:${it.second}")

                    }
                }


            }

            Text("Location", style = typo.titleMedium.copy(color = colors.onSurface))
            InputValidationTextField(
                modifier = Modifier.fillMaxWidth(),
                inputWrapper = Pair(incidentLocationInformation.primaryText, ""),
                mReadOnly = true,
                placeHolder = "Where did the incident happen?",
                onValueChange = {},
                mInteractionSource = locationInteractionSource
            )
            if (openBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.padding(top = 40.dp),
                    sheetState = bottomSheetState,
                    onDismissRequest = {
                        openBottomSheet = false
                    }
                ) {
                    LocationInputScreen(
                        predictions,
                        locationSearchText,
                        previewSelectedLocation,
                        onLocationSearchUpdated = {
                            viewModel.onLocationSearchUpdated(it)
                        },
                        onLocationSelected = {
                            viewModel.onLocationSelected(it)
                        },
                        onLocationInformationSaved = {
                            viewModel.onTriggerEvent(IncidentFormEvent.LocationInformationSave)
                            openBottomSheet = false
                        }
                    )
                }
            }

            Text("Images", style = typo.titleMedium.copy(color = colors.onSurface))
            OutlinedButton(onClick = {
                handleImagePermissionAndLaunchPicker(
                    imagePermissionState,
                    imagePermissionRequested,
                    { imagePermissionRequested = it },
                    activity,
                    imagePickerLauncher
                )
            }) {
                Row {
                    Text("Upload image ")
                    Icon(
                        imageVector = Icons.Default.AddCircle, contentDescription = "Upload Images"
                    )

                }
            }

            context.CancellableSelectedImages(imageUris) { uri ->
                viewModel.onTriggerEvent(IncidentFormEvent.RemoveImageFromPreview(uri))
            }


            Text("Casualties", style = typo.titleMedium.copy(color = colors.onSurface))
            InputValidationTextField(
                modifier = Modifier.fillMaxWidth(),
                inputWrapper = casualties,
                placeHolder = "Any Casualties?",
                onValueChange = viewModel::onCasualtiesUpdated
            )
            LargeSpacer()
            Button(
                enabled = uiState is BaseViewState.Loading != true, onClick = {
                    viewModel.onTriggerEvent(IncidentFormEvent.SubmitIncident)
                    // Handle form submission here
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun Context.CancellableSelectedImages(imageUris: List<Uri>, onRemoveSelection: (Uri) -> Unit) {
    LazyRow {
        items(imageUris) { imageUri ->
            Box(
                modifier = Modifier.size(100.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(7.dp)),
                    model = ImageRequest.Builder(this@CancellableSelectedImages).data(
                        imageUri
                    ).build(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "selected images"
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                        .size(15.dp)
                        .background(shape = RoundedCornerShape(100), color = Color.White)

                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp)
                            .clickable {
                                onRemoveSelection(imageUri)
                            },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Image",
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: Pair<String, String>, onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }


    // Listen for input field click events
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                showDatePicker = true
            }
        }
    }

    InputValidationTextField(
        inputWrapper = selectedDate,
        placeHolder = "Incident Date",
        mReadOnly = true,
        mTrailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
        onValueChange = {},
        mInteractionSource = interactionSource
    )

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                showDatePicker = false
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(it.formatDateFromMillis())
                }
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePicker = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    selectedTime: Pair<String, String>, onTimeConfirmed: (Pair<Int, Int>) -> Unit
) {


    var showTimePicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val currentTime = Calendar.getInstance()
    fun onTimePickerDismiss() {
        showTimePicker = false

    }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    // Listen for input field click events
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                showTimePicker = true
            }
        }
    }
    InputValidationTextField(
        inputWrapper = selectedTime,
        placeHolder = "Incident Time",
        mReadOnly = true,
        mTrailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
        onValueChange = {},
        mInteractionSource = interactionSource
    )

    if (showTimePicker) {
        AlertDialog(onDismissRequest = { onTimePickerDismiss() }, dismissButton = {
            TextButton(onClick = { onTimePickerDismiss() }) {
                Text("Dismiss")
            }
        }, confirmButton = {
            TextButton(onClick = {
                onTimeConfirmed(
                    Pair(
                        timePickerState.hour, timePickerState.minute
                    )
                )
                onTimePickerDismiss()
            }) {
                Text("OK")
            }
        }, text = {
            TimeInput(timePickerState)
        })
    }

}


fun Activity.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }.also(::startActivity)

}

