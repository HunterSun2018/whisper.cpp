package com.whispercppdemo.ui.main

import android.text.method.ScrollingMovementMethod
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.whispercppdemo.R

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    MainScreen(
        canTranscribe = viewModel.canTranscribe,
        isRecording = viewModel.isRecording,
        messageLog = viewModel.dataLog,
        onBenchmarkTapped = viewModel::benchmark,
        onTranscribeSampleTapped = viewModel::transcribeSample,
        onRecordTapped = viewModel::toggleRecord
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    canTranscribe: Boolean,
    isRecording: Boolean,
    messageLog: String,
    onBenchmarkTapped: () -> Unit,
    onTranscribeSampleTapped: () -> Unit,
    onRecordTapped: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        floatingActionButton = {
            RecordButton(
                enabled = canTranscribe,
                isRecording = isRecording,
                onClick = onRecordTapped
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start=16.dp, top=16.dp, end=16.dp, bottom = 100.dp)
        ) {

            MessageLog(messageLog)

//            Column(verticalArrangement = Arrangement.SpaceBetween) {
//                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                    BenchmarkButton(enabled = canTranscribe, onClick = onBenchmarkTapped)
//                    TranscribeSampleButton(enabled = canTranscribe, onClick = onTranscribeSampleTapped)
//                }
//                RecordButton(
//                    enabled = canTranscribe,
//                    isRecording = isRecording,
//                    onClick = onRecordTapped
//                )
//            }

        }
    }
}

@Composable
private fun MessageLog(log: String) {
    SelectionContainer (
    ){
        var logState = rememberScrollState(0)
        LaunchedEffect(log) {
            logState.scrollTo(logState.maxValue)
        }

        Text(modifier = Modifier.verticalScroll(logState), text = log)
    }
}

@Composable
private fun BenchmarkButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled) {
        Text("Benchmark")
    }
}

@Composable
private fun TranscribeSampleButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled) {
        Text("Transcribe sample")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RecordButton(enabled: Boolean, isRecording: Boolean, onClick: () -> Unit) {
    val micPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.RECORD_AUDIO,
        onPermissionResult = { granted ->
            if (granted) {
                onClick()
            }
        }
    )
    Button(onClick = {
        if (micPermissionState.status.isGranted) {
            onClick()
        } else {
            micPermissionState.launchPermissionRequest()
        }
     }, enabled = enabled) {
        Text(
            if (isRecording) {
                "Stop recording"
            } else {
                "Start recording"
            }
        )
    }
}