package com.ant1gon.homeutility.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ant1gon.homeutility.R
import com.ant1gon.homeutility.domain.model.Tariff
import com.ant1gon.homeutility.presentation.viewmodel.TariffViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TariffScreen(
    householdId: Long,
    meterType: String,
    viewModel: TariffViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val tariffs by viewModel.tariffs.collectAsState()
    val tariffHistory by viewModel.tariffHistory.collectAsState()
    val activeTariff by viewModel.activeTariff.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(householdId, meterType) {
        viewModel.loadTariffsByHousehold(householdId)
        viewModel.loadActiveTariff(householdId, meterType)
        viewModel.loadTariffHistory(householdId, meterType)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$meterType Tariffs") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Add, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tariff")
                    }
                    IconButton(onClick = { showHistoryDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "View History")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Tariff")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Error Message
            error?.let { errorMsg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = errorMsg,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Active Tariff Section
            if (activeTariff != null) {
                Text(
                    text = "Active Tariff (${stringResource(R.string.current)})",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                activeTariff?.let { tariff ->
                    TariffCard(
                        tariff = tariff,
                        isActive = true,
                        onDelete = { viewModel.deleteTariff(tariff.id, householdId) }
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Tariff History Section
            Text(
                text = "Tariff History",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (tariffHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tariff history available")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(tariffHistory) { tariff ->
                        TariffCard(
                            tariff = tariff,
                            isActive = activeTariff?.id == tariff.id,
                            onDelete = { viewModel.deleteTariff(tariff.id, householdId) }
                        )
                    }
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog) {
        AddTariffDialog(
            householdId = householdId,
            meterType = meterType,
            onDismiss = { showAddDialog = false },
            onSave = { basePrice, tieredThreshold, tieredPrice, effectiveDate ->
                viewModel.createTariff(
                    householdId = householdId,
                    meterType = meterType,
                    effectiveDate = effectiveDate,
                    basePrice = basePrice,
                    tieredThreshold = tieredThreshold,
                    tieredPrice = tieredPrice
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TariffCard(
    tariff: Tariff,
    isActive: Boolean = false,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        border = if (isActive) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Effective from: ${tariff.effectiveDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isActive) {
                        Badge(
                            modifier = Modifier.padding(top = 4.dp),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text("Active", modifier = Modifier.padding(4.dp))
                        }
                    }
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TariffInfoItem(
                    label = "Base Price",
                    value = "${tariff.basePrice} UAH/unit"
                )
            }

            if (tariff.tieredThreshold > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tiered Pricing Enabled",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TariffInfoItem(
                        label = "Threshold",
                        value = "${tariff.tieredThreshold} units"
                    )
                    TariffInfoItem(
                        label = "Tiered Price",
                        value = "${tariff.tieredPrice} UAH/unit"
                    )
                }
            }
        }
    }
}

@Composable
fun TariffInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AddTariffDialog(
    householdId: Long,
    meterType: String,
    onDismiss: () -> Unit,
    onSave: (basePrice: Double, tieredThreshold: Double, tieredPrice: Double, effectiveDate: LocalDate) -> Unit
) {
    var basePrice by remember { mutableStateOf("") }
    var tieredThreshold by remember { mutableStateOf("") }
    var tieredPrice by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var enableTiered by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Tariff for $meterType") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                OutlinedTextField(
                    value = basePrice,
                    onValueChange = { basePrice = it },
                    label = { Text("Base Price (UAH/unit)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Tiered Pricing")
                    Switch(
                        checked = enableTiered,
                        onCheckedChange = { enableTiered = it }
                    )
                }

                if (enableTiered) {
                    OutlinedTextField(
                        value = tieredThreshold,
                        onValueChange = { tieredThreshold = it },
                        label = { Text("Threshold (units)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                        )
                    )

                    OutlinedTextField(
                        value = tieredPrice,
                        onValueChange = { tieredPrice = it },
                        label = { Text("Tiered Price (UAH/unit)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                        )
                    )
                }

                Text(
                    text = "Effective Date: ${selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                    modifier = Modifier.padding(vertical = 12.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = {
                        // Open date picker - simplified for now
                        selectedDate = LocalDate.now()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Date")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (basePrice.isNotEmpty()) {
                        onSave(
                            basePrice.toDoubleOrNull() ?: 0.0,
                            tieredThreshold.toDoubleOrNull() ?: 0.0,
                            tieredPrice.toDoubleOrNull() ?: 0.0,
                            selectedDate
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
