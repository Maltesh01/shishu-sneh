

#  SHISHU-SNEH — Android App Specification

> **Build Instruction:** Generate complete Android app per this spec. Follow build order. Use exact
tech stack.

## ---

## ##  PROJECT META
- **Name:** Shishu-Sneh (Digital Diary for Newborns 0-12 months)
- **Package:** `com.shishusneh.app`
- **Min SDK:** 24 | **Target SDK:** 34
- **Language:** Kotlin | **UI:** Jetpack Compose | **Architecture:** MVVM + Clean
- **Target Users:** Rural Indian mothers (multilingual, voice-first, offline-capable)

## ---

## ## ️ TECH STACK
## ```yaml
UI: Jetpack Compose, Material 3, Navigation Compose, Coil, MPAndroidChart
Local: Room DB, DataStore, WorkManager, AlarmManager
Cloud: Firebase Auth (Phone OTP), Firestore, FCM
AI: Google Gemini API, Android TextToSpeech, SpeechRecognizer
Network: Retrofit 2, OkHttp, Kotlinx Serialization
DI: Hilt
## Async: Coroutines, Flow
PDF: iText7
## ```

## ### Key Gradle Dependencies:
## ```kotlin
implementation("androidx.compose.material3:material3:1.2.0")

implementation("androidx.navigation:navigation-compose:2.7.6")
implementation("com.google.dagger:hilt-android:2.50")
implementation("androidx.room:room-ktx:2.6.1")
implementation("androidx.work:work-runtime-ktx:2.9.0")
implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
implementation("io.coil-kt:coil-compose:2.5.0")
implementation("com.itextpdf:itext7-core:7.2.5")
## ```

## ---

## ##  DESIGN SYSTEM
## ```kotlin
SoftPink = #F8BBD0    PrimaryPink = #F06292
MintGreen = #A5D6A7   WarmCream = #FFF8E7
DeepCharcoal = #3E2723 SoftRed = #EF9A9A
## ```
- **Font:** Poppins / Noto Sans (Indian scripts)
- **Sizes:** Headings 24-32sp, Body 16sp, Touch targets min 56dp
- **Corners:** 16dp cards, 12dp buttons
- **Style:** Soft pastels, mother-friendly, large readable text, light + dark mode

## ---

## ##  PROJECT STRUCTURE
## ```
app/src/main/java/com/shishusneh/app/
├── MainActivity.kt, ShishuSnehApplication.kt

├── di/ (DatabaseModule, NetworkModule, RepositoryModule)
├── data/
│   ├── local/ (Database, dao/, entity/)
│   ├── remote/ (GeminiApiService, FirebaseAuthService)
│   └── repository/ (one per domain)
├── ui/
│   ├── theme/ (Color, Theme, Type, Shape)
│   ├── navigation/ (AppNavGraph, Screen)
│   ├── components/ (PrimaryButton, BabyAvatar, TipCard, VoiceMicButton, BottomNavBar)
│   └── screens/ (one folder per screen)
├── viewmodel/ (one per screen)
├── workers/ (VaccinationReminderWorker, DailyTipWorker)
└── utils/ (DateUtils, LocaleManager, TextToSpeechHelper, PdfGenerator)

app/src/main/res/values-{hi,mr,ta,te,bn}/strings.xml (multi-lang)
app/src/main/assets/ (schemes.json, vaccinations.json, milestones.json)
## ```

## ---

## ️ DATABASE (Room — 9 Entities)

## ```kotlin
@Entity(tableName = "baby")
data class BabyEntity(@PrimaryKey val id: Int = 1, val name: String, val dob: Long,
val gender: String, val birthWeight: Float, val birthHeight: Float?,
val photoUri: String?, val motherName: String, val motherPhone: String, val state: String?)

@Entity(tableName = "feeding")
data class FeedingEntity(@PrimaryKey(autoGenerate=true) val id: Int=0, val timestamp: Long,

val type: String, val durationMinutes: Int?, val amountMl: Int?)

@Entity(tableName = "growth")
data class GrowthEntity(@PrimaryKey(autoGenerate=true) val id: Int=0, val date: Long,
val weightKg: Float, val heightCm: Float?, val headCircumferenceCm: Float?)

@Entity(tableName = "vaccination")
data class VaccinationEntity(@PrimaryKey(autoGenerate=true) val id: Int=0,
val vaccineName: String, val vaccineNameLocal: String, val ageInWeeks: Int,
val dueDate: Long, val isCompleted: Boolean=false, val completedDate: Long?,
val diseasePrevented: String)

@Entity(tableName = "milestone")
data class MilestoneEntity(@PrimaryKey(autoGenerate=true) val id: Int=0,
val ageGroup: String, val question: String, val questionLocal: String,
val achieved: Boolean=false, val achievedDate: Long?)

@Entity(tableName = "sleep")
data class SleepEntity(@PrimaryKey(autoGenerate=true) val id: Int=0,
val startTime: Long, val endTime: Long?, val durationMinutes: Int?)

@Entity(tableName = "diaper")
data class DiaperEntity(@PrimaryKey(autoGenerate=true) val id: Int=0,
val timestamp: Long, val type: String, val stoolColor: String?)

@Entity(tableName = "chat")
data class ChatEntity(@PrimaryKey(autoGenerate=true) val id: Int=0,
val message: String, val isUser: Boolean, val timestamp: Long)

@Entity(tableName = "schemes")
data class SchemeEntity(@PrimaryKey val id: Int, val name: String, val nameLocal: String,

val category: String, val benefit: String, val eligibility: String,
val documents: String, val applyProcess: String, val officialUrl: String,
val helplineNumber: String, val state: String?, val isFavorite: Boolean=false,
val applicationStatus: String?=null)
## ```

## ---

## ## 吝 NAVIGATION FLOW
## ```
Splash → Language → Onboarding → Login → BabyProfileSetup → Dashboard
├→ FeedingLog
├→ GrowthChart
## ├→ Vaccination
## ├→ Milestone
## ├→ Sleep
## ├→ Diaper
## ├→ Insights
├→ AIChat
├→ MotherHealth
## ├→ Emergency
├→ HealthReport
├→ GovernmentSchemes
## └→ Profile
## ```

## ---

## ## ️ BUILD ORDER (8 PHASES)
- **Foundation:** Project structure, Gradle, Hilt, Theme, Database, Navigation

- **Onboarding:** Splash → Language → Onboarding → Login → BabyProfileSetup
- **Dashboard:** Home with bottom nav
- **Tracking:** Feeding, Growth, Vaccination, Milestone, Sleep, Diaper
- **AI:** Gemini integration, Insights, AI Chat
- **Mother & Safety:** Mother Health, Emergency
- **Reports & Schemes:** Health Report PDF, Government Schemes
- **Polish:** Profile, Multi-language, Voice, Test, Sign APK

## ---

## ##  ALL 19 SCREENS — SPECIFICATIONS

### S1: SplashScreen
Logo (pink heart + footprint), name "Shishu-Sneh", tagline. Fade-in. Auto-navigate after 2.5s. Skip to
Dashboard if onboarded.

### S2: LanguageScreen
Title bilingual. 2-col grid: English, हहहहह, हहहहह, हहहहह, हहहहहह, हहहहह. Save to
DataStore + apply LocaleManager → Onboarding.

### S3: OnboardingScreen
HorizontalPager 3 slides: "Track Every Moment", "Never Miss a Vaccine", "Watch Your Baby Grow".
Skip + Next, last shows "Get Started" → Login.

### S4: LoginScreen
"Welcome, Maa! " Phone +91 field, Send OTP → Firebase PhoneAuth → 6-digit OTP → Verify →
BabyProfileSetup. Loading & error states.

### S5: BabyProfileSetupScreen
Fields: Name, DOB (DatePicker), Gender (Radio), Birth Weight kg, Height cm, Photo. Validation. Save
→ BabyEntity → triggers VaccinationReminderWorker → Dashboard.

### S6: DashboardScreen

- Greeting (time-based) + Baby card (photo, name, age from DOB)
- Today's Tip card (mint green) — Gemini API + TTS Listen button
## - Quick Actions Grid 2x3: Feeding, Weight, Vaccines, Milestones, Sleep, Diaper
## - Upcoming Reminders (next 3)
- Bottom Nav: Home | Insights | AI Chat | Schemes  | Profile
- FAB: Voice Mic

### S7: FeedingLogScreen
Big timer toggle "Start Breastfeeding". Chips: Left/Right Breast. Or "Log Formula" (ml). List today's
feedings. Daily summary. AI tip card. FAB for manual entry. Save to FeedingEntity.

### S8: GrowthChartScreen
Top card: latest weight/height/percentile. MPAndroidChart LineChart with WHO standard overlay
(gray reference lines). "+Add Entry" dialog. Save to GrowthEntity. Color-coded AI insight
## (green/yellow/red).

### S9: VaccinationScreen
Auto-generate from `vaccinations.json` based on DOB. Sections: Completed/Upcoming/Missed.
Cards: name, due date, disease prevented, checkbox. WorkManager schedules notifications 1 day
before. Save to VaccinationEntity.

### S10: MilestoneScreen
Grouped by 0-3, 3-6, 6-9, 9-12 months. Load from `milestones.json`. Yes/Not Yet toggles. "Not Yet"
after expected age → encouragement. Top progress bar (% achieved). Save to MilestoneEntity.

### S11: InsightsScreen
"Tips for You ". Category chips: Breastfeeding, Nutrition, Sleep, Hygiene, Mental Health. Fetch
from Gemini in selected language. Cards with Listen  + Save ❤️. "Myth Busters" section. Pull-to-
refresh.

### S12: ProfileScreen
Mother photo+name+phone. Editable baby card. Settings: Language, Notifications, Cloud Backup,
Privacy, About, Logout. Material 3 ListItem.


### S13: AIChatScreen
"Ask Shishu-Sneh AI 烙". Chat bubbles (user pink right, AI green left). TextField + Mic + Send. Gemini
system prompt: "Friendly pediatric assistant for rural Indian mothers. Reply in [lang]. Simple words.
Recommend doctor for serious issues." Typing indicator. TTS toggle. Save to ChatEntity. Suggested
chips.

### S14: SleepTrackerScreen
"Start Sleep"/"Wake Up" toggle. Auto-track duration. 24-hour visual clock. Daily summary (total hrs,
naps). AI insight comparing WHO recommended hours. Save to SleepEntity.

### S15: DiaperLogScreen
Quick log:  Wet |  Dirty |  Both. Stool color picker with health indicator (green=normal,
white=consult doctor). Daily counter. Dehydration alert if too few wet diapers. Save to DiaperEntity.

### S16: MotherHealthScreen
"Mother's Health Corner ". Postpartum recovery tracker (weekly tips). Mood emoji selector. PPD
screening (Edinburgh scale 10 Y/N questions). Diet tips for breastfeeding. Iron/calcium reminders.
Helpline cards: ASHA, 102, 108.

### S17: EmergencyScreen
Big pulsing red SOS button. One-tap call:  Hospital, 吝 ASHA,  108,  Family.
Symptoms Checker: high fever, blue lips, no breathing, vomiting, no wet diapers. First-aid steps with
images. Auto-share GPS via SMS.

### S18: HealthReportScreen
Generate PDF (iText7) with: baby info, growth chart (export image), vaccination history, feeding
patterns (30 days), milestones, sleep. Share via WhatsApp/Email (Intent.ACTION_SEND). Save to
`Downloads/ShishuSneh/`. A4 print-ready.

### S19: GovernmentSchemesScreen 
- Title + Tricolor accent bar + Search bar
- Filter chips: All,  Cash,  Health,  Nutrition,  Education, 擄 Pregnancy,  Child Care, 
## State
## - Cards: Icon + Name + Category + Benefit + Eligibility + Documents
- Buttons per card: [Check Eligibility] [Apply Now] [Listen ] [Save ❤️] [Share ]

- **Pre-load 16 schemes from `schemes.json`:**
## 1. PMMVY (₹5,000)
## 2. Janani Suraksha Yojana (₹1,400)
- JSSK (free delivery)
- Mission Indradhanush (free vaccines)
## 5. ICDS
- POSHAN Abhiyaan
## 7. Sukanya Samriddhi Yojana
## 8. Beti Bachao Beti Padhao
- Ayushman Bharat (₹5L cover)
## 10. RBSK
- Mid-Day Meal
## 12. PMSMA
## 13. Maternity Benefit Act
- LaQshya
## 15. Anganwadi Services
- State-specific (auto-detect via GPS)
- AI recommendations via Gemini, deadline alerts, status tracker, helpline calls, WebView for "Apply
## Now"
- Save to SchemeEntity

## ---

## ##  MULTI-LANGUAGE
6 locales: `values/`, `values-hi/`, `values-mr/`, `values-ta/`, `values-te/`, `values-bn/`
Use `LocaleManager.kt` for runtime language switching.

## ---

## ##  VOICE-FIRST INTERFACE
`VoiceMicButton.kt` FAB on every screen.

- SpeechRecognizer in selected language
- Commands: "Log feeding", "Show vaccinations", "How is my baby?"
- TextToSpeech confirms response

## ---

## ##  BACKGROUND WORKERS
- **VaccinationReminderWorker:** Schedules notification 1 day before each vaccine due date
(PeriodicWorkRequest)
- **DailyTipWorker:** Daily 8 AM, fetches Gemini tip, shows notification

## ---

##  PERMISSIONS (AndroidManifest.xml)
## ```xml
## INTERNET, POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM, RECORD_AUDIO,
## READ_MEDIA_IMAGES, RECEIVE_BOOT_COMPLETED, CALL_PHONE,
## ACCESS_FINE_LOCATION, SEND_SMS, WRITE_EXTERNAL_STORAGE
## ```

## ---

## ##  API KEYS
## In `local.properties` (git-ignored):
## ```
GEMINI_API_KEY=your_key_here
FIREBASE_PROJECT_ID=your_project
## ```
Access via `BuildConfig.GEMINI_API_KEY`.

## ---


## ##  ASSET FILES TO CREATE

### `assets/vaccinations.json` (Indian Immunization Schedule)
## ```json
## [
{"name":"BCG","ageWeeks":0,"disease":"Tuberculosis"},
{"name":"OPV-0","ageWeeks":0,"disease":"Polio"},
{"name":"Hepatitis B","ageWeeks":0,"disease":"Hepatitis B"},
{"name":"OPV-1","ageWeeks":6,"disease":"Polio"},
{"name":"Pentavalent-1","ageWeeks":6,"disease":"DPT+Hib+HepB"},
{"name":"Rotavirus-1","ageWeeks":6,"disease":"Rotavirus"},
{"name":"PCV-1","ageWeeks":6,"disease":"Pneumococcal"},
{"name":"OPV-2","ageWeeks":10,"disease":"Polio"},
{"name":"Pentavalent-2","ageWeeks":10,"disease":"DPT+Hib+HepB"},
{"name":"Rotavirus-2","ageWeeks":10,"disease":"Rotavirus"},
{"name":"OPV-3","ageWeeks":14,"disease":"Polio"},
{"name":"Pentavalent-3","ageWeeks":14,"disease":"DPT+Hib+HepB"},
{"name":"Rotavirus-3","ageWeeks":14,"disease":"Rotavirus"},
{"name":"PCV-2","ageWeeks":14,"disease":"Pneumococcal"},
{"name":"Measles-Rubella-1","ageWeeks":36,"disease":"Measles+Rubella"},
{"name":"PCV Booster","ageWeeks":36,"disease":"Pneumococcal"},
{"name":"Vitamin A-1","ageWeeks":36,"disease":"Vit A deficiency"}
## ]
## ```

### `assets/schemes.json` (16 govt schemes per S19 list)
### `assets/milestones.json` (developmental milestones by age group)

## ---


## ## ✅ SUCCESS CRITERIA CHECKLIST
- [ ] All 19 screens implemented & navigable
- [ ] Room DB working offline
- [ ] Gemini API integrated (tips + chat)
- [ ] WorkManager schedules vaccination alerts from DOB
- [ ] 6-language support working
- [ ] Voice input/output functional
- [ ] PDF report generation works
- [ ] Government schemes loaded from JSON
- [ ] Runs on Android 7.0+ (API 24+)
- [ ] Signed APK builds successfully
- [ ] Performance optimized for 1GB RAM devices

## ---

## ##  IMPACT MISSION
Reduce infant mortality, prevent stunting, empower millions of rural Indian mothers with:
- 24/7 digital pediatric guidance in local language
- Government scheme access (potential ₹50K-₹5L benefits per family)
- Life-saving vaccination tracking
- Mother's mental & physical health support
- Works offline, voice-first design

**Build with love. This app saves lives. **

## ---

## # END OF SPECIFICATION