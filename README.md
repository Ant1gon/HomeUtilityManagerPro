# Home Utility Manager Pro

A comprehensive Android application for managing household utility expenses with precision, data safety, and seamless reporting.

## Features (Phase 1 - MVP)

✅ **Multi-Household Management**
- Create and manage multiple households
- Track ownership type (Owned/Rented)
- Support for rent cost tracking
- Monthly maintenance fees (OSBB)
- Cumulative balance tracking (overpayment/debt)

✅ **Meter Management**
- Multiple meter types: Electricity, Water, Gas, Heating
- Customizable meter names and locations
- Electricity zone support (1, 2, or 3 zones)
- Water type differentiation (Hot/Cold)

✅ **Advanced Calculation Logic**
- **Electricity**: Dual-zone with 0.5 night coefficient: `Cost = (ΔDay × Tariff) + (ΔNight × Tariff × 0.5)`
- **Water & Sewage**: `Cost = (ΔHot × Price_hot) + (ΔCold × Price_cold) + (ΔTotal × Price_sewage) + Fixed Fees`
- **Gas & Heating**: Tiered pricing support with thresholds
- **Precise Delta Calculation**: Tracks consumption changes

✅ **Payment Tracking**
- Actual amount paid vs. calculated cost
- Monthly balance calculation
- Cumulative household balance

✅ **Localization**
- Ukrainian (UA) - Default
- English (EN)
- Runtime language switching

✅ **Modern Architecture**
- MVVM with Jetpack Compose UI
- Room Database for data persistence
- Hilt Dependency Injection
- Coroutines & Flow for reactive programming

## Tech Stack

- **Language**: Kotlin 1.9.10
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room Persistence Library (SQLite)
- **Dependency Injection**: Hilt
- **Async Programming**: Coroutines & Flow
- **Settings**: DataStore
- **Background Tasks**: WorkManager
- **API Integration**: Google Drive API (Phase 2+)

## Project Structure

```
app/src/main/java/com/ant1gon/homeutility/
├── data/
│   ├── entity/              # Room database entities
│   ├── dao/                 # Data Access Objects
│   ├── database/            # Room Database configuration
│   ├── converter/           # Type converters
│   └── repository/          # Repository pattern implementation
├── domain/
│   ├── model/               # Domain models
│   └── calculator/          # Utility calculation logic
├── presentation/
│   ├── viewmodel/           # MVVM ViewModels
│   └── ui/                  # Jetpack Compose screens
└── di/                      # Dependency Injection modules
```

## Database Schema

### Households
- id, name, address, ownershipType, rentCost, balance, maintenanceFeeMontly

### Meters
- id, householdId, customName, meterType, location, electricityZones, waterType, tariffId

### Tariffs (Versioned & Tiered)
- id, householdId, meterType, effectiveDate, basePrice, tieredThreshold, tieredPrice, version

### Meter Records
- id, meterId, currentReading, previousReading, delta, dayZoneReading, nightZoneReading, month, year

### Payments
- id, householdId, amountPaid, totalCalculated, balance, month, year

## Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Java 17 or later
- Android SDK 26+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Ant1gon/HomeUtilityManagerPro.git
cd HomeUtilityManagerPro
```

2. Open in Android Studio

3. Sync Gradle dependencies:
```bash
./gradlew build
```

4. Build and run:
```bash
./gradlew installDebug
```

## Development Roadmap

### Phase 1: MVP ✅ (In Progress)
- [x] Database setup
- [x] Household management
- [x] Meter management
- [x] Calculation engine
- [x] Payment tracking
- [ ] UI Screens (Jetpack Compose)

### Phase 2: Advanced Logic
- [ ] Tariff versioning UI
- [ ] Tiered pricing UI
- [ ] Language switcher UI
- [ ] PDF report generation
- [ ] Excel/CSV export
- [ ] Local file backup
- [ ] Text report generation

### Phase 3: Automation & UX
- [ ] Google Drive sync
- [ ] Push notifications
- [ ] Consumption charts
- [ ] Dashboard analytics
- [ ] Material 3 Dark Theme
- [ ] Advanced filtering & search

## Key Formulas

### Electricity (Dual-zone)
```
Cost = (ΔDay × Tariff) + (ΔNight × Tariff × 0.5)
```

### Water & Sewage
```
ΔTotal Water = ΔHot + ΔCold
Cost = (ΔHot × Price_hot) + (ΔCold × Price_cold) + (ΔTotal Water × Price_sewage) + Fixed Fees
```

### Tiered Pricing
```
If consumption > threshold:
  Cost = (threshold × base_price) + ((consumption - threshold) × tiered_price)
Else:
  Cost = consumption × base_price
```

### Monthly Balance
```
Month Balance = TotalCalculated - AmountPaid
Household Balance = Previous Balance + Month Balance
```

## API Documentation

### Utility Calculator

```kotlin
// Electricity
UtilityCalculator.calculateElectricityCost(
    dayZoneDelta: Double,
    nightZoneDelta: Double,
    tariffPerUnit: Double,
    tieredThreshold: Double = 0.0,
    tieredPrice: Double = 0.0
): Double

// Water
UtilityCalculator.calculateWaterCost(
    hotWaterDelta: Double,
    coldWaterDelta: Double,
    hotWaterPrice: Double,
    coldWaterPrice: Double,
    sewagePrice: Double,
    fixedFees: Double = 0.0
): Double
```

## Contributing

Contributions are welcome! Please follow the existing code style and submit pull requests.

## License

MIT License - see LICENSE file for details

## Author

**Ant1gon** - [GitHub Profile](https://github.com/Ant1gon)

## Support

For issues and feature requests, please use the [GitHub Issues](https://github.com/Ant1gon/HomeUtilityManagerPro/issues) page.
