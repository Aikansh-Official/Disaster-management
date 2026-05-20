package com.example.disastermanagement.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ModuleCategory(val label: String) {
    FIRE("Fire"),
    FLOOD("Flood"),
    WIND("Wind"),
    EARTHQUAKE("Earthquake"),
    CHEMICAL("Chemical")
}

data class TrainingModule(
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: ModuleCategory,
    val progress: Float
)

data class SafeZone(
    val title: String,
    val badge: String,
    val distance: String,
    val details: String,
    val latitude: Double,
    val longitude: Double
)

data class BadgeState(
    val key: String,
    val title: String,
    val unlocked: Boolean
)

data class DrillOption(
    val id: String,
    val text: String
)

data class DrillQuestion(
    val prompt: String,
    val options: List<DrillOption>,
    val correctOptionId: String,
    val explanation: String
)

data class DrillScenario(
    val moduleTitle: String,
    val imageUrl: String,
    val questions: List<DrillQuestion>
)

data class AppUiState(
    val modules: List<TrainingModule>,
    val drillsCompleted: Int,
    val tipsRead: Int,
    val readinessScore: Float,
    val badges: List<BadgeState>,
    val safeZones: List<SafeZone>,
    val drillScenarios: List<DrillScenario>
)

class AppViewModel : ViewModel() {

    private val initialModules = listOf(
        TrainingModule(
            title = "Home Fire Safety",
            description = "Learn essential prevention techniques and create a reliable evacuation plan.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAuBggjOioal6m1kooeTyiPQaHrV2F2cN48T6hunbO829NIvyl-WaR9GIwo9ACaEPEbmnFygNynks_KtB8CTXmbR3yA9ddeW6eWdOnerv3aS40ev-i0m8poPANtbD_il4qT8f98S2Nra1DgTKql0FLt3KMvs9g3abxPMEijZ3rt8xqm375Mx5hS4kzd4knCjzdVdThLTBx4HyqrQ86vgABdEighvyioPY_83lq3XNcC9WvbEkEE4Ko1QnB4g0qeuEaehu5hT_Z67uRI",
            category = ModuleCategory.FIRE,
            progress = 0.7f
        ),
        TrainingModule(
            title = "Flood Evacuation",
            description = "Understand local flood zones and pack emergency kits.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDvVcXXZ4CNMqGCgULVs4GLXe77ksxZGiP1knjW_JHSEf_UkbSSqi97znWsNk5UBlsSwdNnJKw0D7YXPs4neXrRIEIRKRL5iQrroflamrBr_Oa9neNLH9ujVTf1p-cgJ-OO5HLA8k88RAlTBHk99Mh123UxnAi-2JZoc__b6irgDwCbTCbeTQr5VXK57oCn5NL5qsMFQbH4EKVSC4oT-4SgybCvWxoznwhVGPfsY0jQd4FrZ_3TwOM50mxUQisLnxCubkzSFOMk-tOu",
            category = ModuleCategory.FLOOD,
            progress = 1f
        ),
        TrainingModule(
            title = "Earthquake Basics",
            description = "Master the 'Drop, Cover, Hold On' technique.",
            imageUrl = "https://images.unsplash.com/photo-1527489377706-5bf97e608852?w=1200",
            category = ModuleCategory.EARTHQUAKE,
            progress = 0f
        ),
        TrainingModule(
            title = "Wind Storm Safety",
            description = "Build a storm plan and secure loose outdoor items.",
            imageUrl = "https://images.unsplash.com/photo-1513002749550-c59d786b8e6c?w=1200",
            category = ModuleCategory.WIND,
            progress = 0.4f
        ),
        TrainingModule(
            title = "Chemical Hazard",
            description = "Identify hazardous leaks and execute shelter-in-place safely.",
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=1200",
            category = ModuleCategory.FIRE,
            progress = 0f
        )
    )

    private val safeZones = listOf(
        SafeZone(
            title = "Assembly Point",
            badge = "Primary Shelter",
            distance = "0.8 km",
            details = "Community field designated for evacuation assembly.",
            latitude = 26.4526,
            longitude = 80.3301
        ),
        SafeZone(
            title = "City Hospital",
            badge = "Medical Triage",
            distance = "1.2 km",
            details = "Emergency medical services and disaster response coordination.",
            latitude = 26.4588,
            longitude = 80.3216
        ),
        SafeZone(
            title = "Fire Station",
            badge = "Rapid Response",
            distance = "2.0 km",
            details = "Nearest fire and rescue command center.",
            latitude = 26.4429,
            longitude = 80.3365
        )
    )

    private val drillScenarios = listOf(
        DrillScenario(
            moduleTitle = "Home Fire Safety",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAuBggjOioal6m1kooeTyiPQaHrV2F2cN48T6hunbO829NIvyl-WaR9GIwo9ACaEPEbmnFygNynks_KtB8CTXmbR3yA9ddeW6eWdOnerv3aS40ev-i0m8poPANtbD_il4qT8f98S2Nra1DgTKql0FLt3KMvs9g3abxPMEijZ3rt8xqm375Mx5hS4kzd4knCjzdVdThLTBx4HyqrQ86vgABdEighvyioPY_83lq3XNcC9WvbEkEE4Ko1QnB4g0qeuEaehu5hT_Z67uRI",
            questions = listOf(
                DrillQuestion(
                    prompt = "What should you do first if a pan catches fire on the stove?",
                    options = listOf(
                        DrillOption("A", "Turn off the heat and cover the pan with a lid"),
                        DrillOption("B", "Carry the pan outside"),
                        DrillOption("C", "Add water to the flames")
                    ),
                    correctOptionId = "A",
                    explanation = "Cut the heat source and smother the flames with a lid or baking sheet. Never use water on a grease fire."
                ),
                DrillQuestion(
                    prompt = "Which device should be tested every month?",
                    options = listOf(
                        DrillOption("A", "Smoke alarm"),
                        DrillOption("B", "Window lock"),
                        DrillOption("C", "Ceiling fan")
                    ),
                    correctOptionId = "A",
                    explanation = "Monthly smoke alarm testing helps confirm the alarm works when you need it most."
                ),
                DrillQuestion(
                    prompt = "How should you move through a smoke-filled room?",
                    options = listOf(
                        DrillOption("A", "Stay low and crawl under the smoke"),
                        DrillOption("B", "Stand upright and run"),
                        DrillOption("C", "Hold your breath and walk normally")
                    ),
                    correctOptionId = "A",
                    explanation = "Smoke rises, so staying low gives you cleaner air and better visibility."
                ),
                DrillQuestion(
                    prompt = "Where should matches and lighters be stored?",
                    options = listOf(
                        DrillOption("A", "Locked away from children"),
                        DrillOption("B", "Beside the curtains"),
                        DrillOption("C", "On the kitchen counter")
                    ),
                    correctOptionId = "A",
                    explanation = "Keeping ignition sources locked away reduces the chance of accidental fires."
                ),
                DrillQuestion(
                    prompt = "What is the best home escape plan practice?",
                    options = listOf(
                        DrillOption("A", "Have two exits from every room"),
                        DrillOption("B", "Use only one hallway exit"),
                        DrillOption("C", "Wait for firefighters before leaving")
                    ),
                    correctOptionId = "A",
                    explanation = "Two exits per room gives you a backup route if one path is blocked."
                ),
                DrillQuestion(
                    prompt = "If clothing catches fire, what should you do?",
                    options = listOf(
                        DrillOption("A", "Stop, drop, and roll"),
                        DrillOption("B", "Run to the nearest door"),
                        DrillOption("C", "Wave your arms quickly")
                    ),
                    correctOptionId = "A",
                    explanation = "Stopping, dropping, and rolling helps smother flames on your clothing."
                )
            )
        ),
        DrillScenario(
            moduleTitle = "Flood Evacuation",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDvVcXXZ4CNMqGCgULVs4GLXe77ksxZGiP1knjW_JHSEf_UkbSSqi97znWsNk5UBlsSwdNnJKw0D7YXPs4neXrRIEIRKRL5iQrroflamrBr_Oa9neNLH9ujVTf1p-cgJ-OO5HLA8k88RAlTBHk99Mh123UxnAi-2JZoc__b6irgDwCbTCbeTQr5VXK57oCn5NL5qsMFQbH4EKVSC4oT-4SgybCvWxoznwhVGPfsY0jQd4FrZ_3TwOM50mxUQisLnxCubkzSFOMk-tOu",
            questions = listOf(
                DrillQuestion(
                    prompt = "What should you do when a flood warning is issued for your area?",
                    options = listOf(
                        DrillOption("A", "Move to higher ground and prepare to evacuate"),
                        DrillOption("B", "Go sight-seeing near the river"),
                        DrillOption("C", "Wait until water enters your home")
                    ),
                    correctOptionId = "A",
                    explanation = "Early evacuation to higher ground is the safest response when flooding is expected."
                ),
                DrillQuestion(
                    prompt = "What is the safest action when you encounter floodwater on a road?",
                    options = listOf(
                        DrillOption("A", "Turn around and find another route"),
                        DrillOption("B", "Drive through quickly"),
                        DrillOption("C", "Stop in the middle of the water")
                    ),
                    correctOptionId = "A",
                    explanation = "Floodwater can hide deep potholes and strong currents, so never drive through it."
                ),
                DrillQuestion(
                    prompt = "Where should important documents be stored during flood season?",
                    options = listOf(
                        DrillOption("A", "In a waterproof folder or bag"),
                        DrillOption("B", "On the floor near the door"),
                        DrillOption("C", "Inside an open shoe box")
                    ),
                    correctOptionId = "A",
                    explanation = "Waterproof storage protects IDs, insurance papers, and emergency documents."
                ),
                DrillQuestion(
                    prompt = "Which utility should be shut off if it is safe to do so before evacuation?",
                    options = listOf(
                        DrillOption("A", "Electricity and gas"),
                        DrillOption("B", "Internet and Wi-Fi"),
                        DrillOption("C", "Television and radio")
                    ),
                    correctOptionId = "A",
                    explanation = "Turning off utilities reduces fire and electrocution risks if water enters the property."
                ),
                DrillQuestion(
                    prompt = "What should you wear when walking through shallow floodwater after evacuation instructions?",
                    options = listOf(
                        DrillOption("A", "Sturdy boots or closed shoes"),
                        DrillOption("B", "Bare feet"),
                        DrillOption("C", "Flip-flops")
                    ),
                    correctOptionId = "A",
                    explanation = "Closed footwear protects against sharp debris, bacteria, and hidden hazards."
                ),
                DrillQuestion(
                    prompt = "Before returning home after a flood, what should you check first?",
                    options = listOf(
                        DrillOption("A", "Structural damage and electrical hazards"),
                        DrillOption("B", "Your internet speed"),
                        DrillOption("C", "The paint color in each room")
                    ),
                    correctOptionId = "A",
                    explanation = "Flooded buildings may have weakened structures, damaged wiring, and contaminated water."
                )
            )
        ),
        DrillScenario(
            moduleTitle = "Earthquake Basics",
            imageUrl = "https://images.unsplash.com/photo-1527489377706-5bf97e608852?w=1200",
            questions = listOf(
                DrillQuestion(
                    prompt = "During strong shaking, what is the correct action?",
                    options = listOf(
                        DrillOption("A", "Drop, Cover, and Hold On"),
                        DrillOption("B", "Run outside immediately"),
                        DrillOption("C", "Stand near the window")
                    ),
                    correctOptionId = "A",
                    explanation = "Drop, Cover, and Hold On protects you from falling objects and debris."
                ),
                DrillQuestion(
                    prompt = "What is the safest place to shelter indoors during an earthquake?",
                    options = listOf(
                        DrillOption("A", "Under a sturdy table or desk"),
                        DrillOption("B", "In a doorway to any room"),
                        DrillOption("C", "Next to a glass cabinet")
                    ),
                    correctOptionId = "A",
                    explanation = "A sturdy table or desk shields you from falling items and collapsing debris."
                ),
                DrillQuestion(
                    prompt = "Which action should you avoid during an earthquake?",
                    options = listOf(
                        DrillOption("A", "Standing near windows"),
                        DrillOption("B", "Protecting your head"),
                        DrillOption("C", "Crouching low")
                    ),
                    correctOptionId = "A",
                    explanation = "Windows can shatter during shaking, so move away from glass."
                ),
                DrillQuestion(
                    prompt = "If you are in bed when the shaking starts, what should you do?",
                    options = listOf(
                        DrillOption("A", "Stay there and protect your head with a pillow"),
                        DrillOption("B", "Run to the kitchen"),
                        DrillOption("C", "Stand up and look for shoes")
                    ),
                    correctOptionId = "A",
                    explanation = "Staying in bed can be safer than moving through the room while debris is falling."
                ),
                DrillQuestion(
                    prompt = "After the shaking stops, what should you check first?",
                    options = listOf(
                        DrillOption("A", "For injuries and hazards around you"),
                        DrillOption("B", "Your social media notifications"),
                        DrillOption("C", "The weather forecast only")
                    ),
                    correctOptionId = "A",
                    explanation = "Look for injuries, fire, gas leaks, and broken objects before moving around."
                ),
                DrillQuestion(
                    prompt = "If you are outdoors during an earthquake, what should you do?",
                    options = listOf(
                        DrillOption("A", "Move to an open area away from buildings and power lines"),
                        DrillOption("B", "Stand under a balcony"),
                        DrillOption("C", "Go inside the nearest elevator lobby")
                    ),
                    correctOptionId = "A",
                    explanation = "Open spaces reduce the risk from falling glass, bricks, and utility lines."
                )
            )
        ),
        DrillScenario(
            moduleTitle = "Wind Storm Safety",
            imageUrl = "https://images.unsplash.com/photo-1513002749550-c59d786b8e6c?w=1200",
            questions = listOf(
                DrillQuestion(
                    prompt = "Before a windstorm arrives, what should you secure?",
                    options = listOf(
                        DrillOption("A", "Loose outdoor items"),
                        DrillOption("B", "The bathroom mirror"),
                        DrillOption("C", "Office paperwork only")
                    ),
                    correctOptionId = "A",
                    explanation = "Outdoor furniture and tools can become dangerous projectiles in strong winds."
                ),
                DrillQuestion(
                    prompt = "During high winds, what is the safest place to stay?",
                    options = listOf(
                        DrillOption("A", "Away from windows and glass doors"),
                        DrillOption("B", "On an open balcony"),
                        DrillOption("C", "Next to large tree branches")
                    ),
                    correctOptionId = "A",
                    explanation = "Glass can shatter and wind can push debris through exposed openings."
                ),
                DrillQuestion(
                    prompt = "What should you do if a power line falls nearby during a storm?",
                    options = listOf(
                        DrillOption("A", "Stay away and call emergency services"),
                        DrillOption("B", "Touch it with a wooden stick"),
                        DrillOption("C", "Step over it quickly")
                    ),
                    correctOptionId = "A",
                    explanation = "Downed lines can still be live and extremely dangerous. Keep a wide distance."
                ),
                DrillQuestion(
                    prompt = "Which device helps you monitor weather alerts when the power is out?",
                    options = listOf(
                        DrillOption("A", "Battery-powered radio"),
                        DrillOption("B", "Tablet on silent mode"),
                        DrillOption("C", "Gaming console")
                    ),
                    correctOptionId = "A",
                    explanation = "A battery-powered radio keeps you informed when internet or electricity is unavailable."
                ),
                DrillQuestion(
                    prompt = "What should you do with pets when a severe windstorm is approaching?",
                    options = listOf(
                        DrillOption("A", "Bring them indoors with supplies ready"),
                        DrillOption("B", "Leave them outside to adjust"),
                        DrillOption("C", "Tie them near loose objects")
                    ),
                    correctOptionId = "A",
                    explanation = "Pets should be brought inside early so they stay safe and calm."
                ),
                DrillQuestion(
                    prompt = "What should you inspect on the house after the storm passes?",
                    options = listOf(
                        DrillOption("A", "Roof damage, broken windows, and debris"),
                        DrillOption("B", "Wallpaper patterns"),
                        DrillOption("C", "Cabinet shelf labels")
                    ),
                    correctOptionId = "A",
                    explanation = "A post-storm inspection helps identify structural damage and hazards before re-entering normal routines."
                )
            )
        ),
        DrillScenario(
            moduleTitle = "Chemical Hazard",
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=1200",
            questions = listOf(
                DrillQuestion(
                    prompt = "If you detect a strong unknown chemical odor indoors, what should you do first?",
                    options = listOf(
                        DrillOption("A", "Leave the area and alert emergency services"),
                        DrillOption("B", "Light a candle to find the source"),
                        DrillOption("C", "Mix it with water immediately")
                    ),
                    correctOptionId = "A",
                    explanation = "Unknown vapors can be toxic or flammable, so get away and call for help."
                ),
                DrillQuestion(
                    prompt = "What should you do if a chemical container is leaking?",
                    options = listOf(
                        DrillOption("A", "Avoid touching it and isolate the area"),
                        DrillOption("B", "Pour it into another bottle"),
                        DrillOption("C", "Shake the container to see how much remains")
                    ),
                    correctOptionId = "A",
                    explanation = "Staying away reduces exposure and prevents the leak from spreading."
                ),
                DrillQuestion(
                    prompt = "Which items help protect you during a small spill cleanup?",
                    options = listOf(
                        DrillOption("A", "Gloves and goggles"),
                        DrillOption("B", "Sandals and shorts"),
                        DrillOption("C", "A wool scarf only")
                    ),
                    correctOptionId = "A",
                    explanation = "Gloves and goggles reduce skin and eye exposure to hazardous material."
                ),
                DrillQuestion(
                    prompt = "What should you always read before using a chemical?",
                    options = listOf(
                        DrillOption("A", "The label and hazard instructions"),
                        DrillOption("B", "The color of the bottle cap"),
                        DrillOption("C", "The storage box brand name")
                    ),
                    correctOptionId = "A",
                    explanation = "Labels explain safe handling, storage, and first-aid directions."
                ),
                DrillQuestion(
                    prompt = "How should household chemicals be stored?",
                    options = listOf(
                        DrillOption("A", "In their original labeled containers away from heat"),
                        DrillOption("B", "In unmarked drinking bottles"),
                        DrillOption("C", "Near food and cooking utensils")
                    ),
                    correctOptionId = "A",
                    explanation = "Original containers preserve safety labels and reduce the chance of mixing the wrong substances."
                ),
                DrillQuestion(
                    prompt = "If a chemical gets into the eyes, what should you do?",
                    options = listOf(
                        DrillOption("A", "Flush with clean water and seek medical help"),
                        DrillOption("B", "Rub the eyes firmly"),
                        DrillOption("C", "Cover the eyes and wait")
                    ),
                    correctOptionId = "A",
                    explanation = "Immediate rinsing reduces damage, and medical help may still be needed afterward."
                )
            )
        )
    )

    private val _uiState = MutableStateFlow(buildUiState(initialModules, drillsCompleted = 4, tipsRead = 12))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun trainingFilters(): List<String> = listOf("Fire", "Flood", "Wind", "Earthquake")

    fun defaultDrillModule(): String {
        return _uiState.value.modules.firstOrNull { it.progress < 1f }?.title
            ?: _uiState.value.modules.firstOrNull()?.title
            ?: "Earthquake Basics"
    }

    fun drillScenarioFor(moduleTitle: String): DrillScenario {
        return drillScenarios.firstOrNull { it.moduleTitle == moduleTitle }
            ?: drillScenarios.first()
    }

    fun completeDrill(moduleTitle: String) {
        _uiState.update { current ->
            val wasIncomplete = current.modules.any { module -> module.title == moduleTitle && module.progress < 1f }
            val updatedModules = current.modules.map { module ->
                if (module.title == moduleTitle) module.copy(progress = 1f) else module
            }
            buildUiState(
                modules = updatedModules,
                drillsCompleted = current.drillsCompleted + if (wasIncomplete) 1 else 0,
                tipsRead = current.tipsRead
            )
        }
    }

    private fun buildUiState(modules: List<TrainingModule>, drillsCompleted: Int, tipsRead: Int): AppUiState {
        val completedModules = modules.count { it.progress >= 1f }
        val readiness = if (modules.isEmpty()) 0f else completedModules.toFloat() / modules.size
        return AppUiState(
            modules = modules,
            drillsCompleted = drillsCompleted,
            tipsRead = tipsRead,
            readinessScore = readiness,
            badges = buildBadges(modules, drillsCompleted),
            safeZones = safeZones,
            drillScenarios = drillScenarios
        )
    }

    private fun buildBadges(modules: List<TrainingModule>, drillsCompleted: Int): List<BadgeState> {
        fun completed(title: String): Boolean = modules.any { it.title == title && it.progress >= 1f }
        return listOf(
            BadgeState("fire_safety", "Fire Safety", completed("Home Fire Safety")),
            BadgeState("first_aid", "First Aid", drillsCompleted >= 2),
            BadgeState("flood_prep", "Flood Prep", completed("Flood Evacuation")),
            BadgeState("earthquake", "Earthquake", completed("Earthquake Basics"))
        )
    }
}

