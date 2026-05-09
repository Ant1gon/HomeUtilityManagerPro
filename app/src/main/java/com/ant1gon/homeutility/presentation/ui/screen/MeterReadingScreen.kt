package com.ant1gon.homeutility.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ant1gon.homeutility.R
import com.ant1gon.homeutility.domain.model.Meter
import com.ant1gon.homeutility.domain.model.MeterType
import com.ant1gon.homeutility.domain.calculator.UtilityCalculator
import com.ant1gon.homeutility.presentation.viewmodel.MeterViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MeterReadingScreen(
    meter: Meter,
    viewModel: MeterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var currentReading by remember { mutableStateOf("") }
    var previousReading by remember { mutableStateOf("") }
    var dayZoneReading by remember { mutableStateOf("") }
    var nightZoneReading by remember { mutableStateOf("") }
    var calculatedCost by remember { mutableStateOf(0.0) }
    var consumption by remember { mutableStateOf(0.0) }
    val now = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${meter.customName} - Reading Entry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Save meter reading
                    if (currentReading.isNotEmpty()) {
                        viewModel.createMeter(
                            householdId = meter.householdId,
                            customName = meter.customName,
                            meterType = meter.meterType,
                            location = meter.location,
                            electricityZones = meter.electricityZones,
                            waterType = meter.waterType,
                            tariffId = meter.tariffId
                        )
                        onNavigateBack()
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Month Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Period: ${selectedMonth.format(java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy"))}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Meter Type Specific Fields
            when (meter.meterType) {
                MeterType.ELECTRICITY -> {
                    if (meter.electricityZones >= 2) {
                        OutlinedTextField(
                            value = dayZoneReading,
                            onValueChange = {
                                dayZoneReading = it
                                updateConsumptionAndCost(
                                    dayZoneReading.toDoubleOrNull() ?: 0.0,
                                    nightZoneReading.toDoubleOrNull() ?: 0.0,
                                    meter.meterType
                                ) { newConsumption, newCost ->
                                    consumption = newConsumption
                                    calculatedCost = newCost
                                }
                            },
                            label = { Text("Day Zone Reading (kWh)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        OutlinedTextField(
                            value = nightZoneReading,
                            onValueChange = {
                                nightZoneReading = it
                                updateConsumptionAndCost(
                                    dayZoneReading.toDoubleOrNull() ?: 0.0,
                                    nightZoneReading.toDoubleOrNull() ?: 0.0,
                                    meter.meterType
                                ) { newConsumption, newCost ->
                                    consumption = newConsumption
                                    calculatedCost = newCost
                                }
                            },
                            label = { Text("Night Zone Reading (kWh)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    } else {
                        OutlinedTextField(
                            value = currentReading,
                            onValueChange = {
                                currentReading = it
                                updateConsumptionAndCost(
                                    currentReading.toDoubleOrNull() ?: 0.0,
                                    previousReading.toDoubleOrNull() ?: 0.0,
                                    meter.meterType
                                ) { newConsumption, newCost ->
                                    consumption = newConsumption
                                    calculatedCost = newCost
                                }
                            },
                            label = { Text("Current Reading (kWh)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }

                MeterType.WATER -> {
                    OutlinedTextField(
                        value = currentReading,
                        onValueChange = {
                            currentReading = it
                            updateConsumptionAndCost(
                                currentReading.toDoubleOrNull() ?: 0.0,
                                previousReading.toDoubleOrNull() ?: 0.0,
                                meter.meterType
                            ) { newConsumption, newCost ->
                                consumption = newConsumption
                                calculatedCost = newCost
                            }
                        },
                        label = { Text("Current Reading (m³)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                MeterType.GAS -> {
                    OutlinedTextField(
                        value = currentReading,
                        onValueChange = {
                            currentReading = it
                            updateConsumptionAndCost(
                                currentReading.toDoubleOrNull() ?: 0.0,
                                previousReading.toDoubleOrNull() ?: 0.0,
                                meter.meterType
                            ) { newConsumption, newCost ->
                                consumption = newConsumption
                                calculatedCost = newCost
                            }
                        },
                        label = { Text("Current Reading (m³)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                MeterType.HEATING -> {
                    Text(
                        text = "Heating - Fixed Monthly Cost",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            OutlinedTextField(
                value = previousReading,
                onValueChange = {
                    previousReading = it
                    updateConsumptionAndCost(
                        currentReading.toDoubleOrNull() ?: 0.0,
                        previousReading.toDoubleOrNull() ?: 0.0,
                        meter.meterType
                    ) { newConsumption, newCost ->
                        consumption = newConsumption
                        calculatedCost = newCost
                    }
                },
                label = { Text("Previous Reading") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Summary Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCard(
                    label = "Consumption",
                    value = "$consumption units",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SummaryCard(
                    label = "Estimated Cost",
                    value = "$calculatedCost UAH",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun updateConsumptionAndCost(
    current: Double,
    previous: Double,
    meterType: MeterType,
    onUpdate: (consumption: Double, cost: Double) -> Unit
) {
    val delta = UtilityCalculator.calculateDelta(current, previous)
    val cost = when (meterType) {
        MeterType.ELECTRICITY -> {
            UtilityCalculator.calculateElectricityCost(
                dayZoneDelta = delta,
                nightZoneDelta = 0.0,
                tariffPerUnit = 5.0 // Example tariff
            )
        }
        MeterType.WATER -> {
            UtilityCalculator.calculateWaterCost(
                hotWaterDelta = delta,
                coldWaterDelta = 0.0,
                hotWaterPrice = 80.0,
                coldWaterPrice = 30.0,
                sewagePrice = 25.0
            )
        }
        MeterType.GAS -> {
            UtilityCalculator.calculateGasCost(
                delta = delta,
                basePrice = 10.0
            )
        }
        MeterType.HEATING -> 0.0
    }
    onUpdate(delta, cost)
}
