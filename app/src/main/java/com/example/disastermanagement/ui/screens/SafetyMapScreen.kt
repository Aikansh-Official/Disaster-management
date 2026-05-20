package com.example.disastermanagement.ui.screens

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.example.disastermanagement.ui.components.Card
import com.example.disastermanagement.ui.state.SafeZone
import com.example.disastermanagement.ui.theme.Background
import com.example.disastermanagement.ui.theme.OnBackground
import com.example.disastermanagement.ui.theme.OnSurfaceVariant
import com.example.disastermanagement.ui.theme.OnTertiaryFixed
import com.example.disastermanagement.ui.theme.Primary
import com.example.disastermanagement.ui.theme.SurfaceContainerLowest
import com.example.disastermanagement.ui.theme.SurfaceVariant
import com.example.disastermanagement.ui.theme.TertiaryFixed
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

@Composable
fun SafetyMapScreen(safeZones: List<SafeZone>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val defaultCenter = remember { GeoPoint(26.4499, 80.3319) }
    var bookmarks by rememberSaveable { mutableStateOf(setOf<String>()) }

    val mapView = remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
            controller.setZoom(13.0)
            controller.setCenter(defaultCenter)
        }
    }

    DisposableEffect(mapView, safeZones) {
        mapView.overlays.removeAll { it is Marker }
        safeZones.forEach { zone ->
            mapView.overlays.add(
                Marker(mapView).apply {
                    position = GeoPoint(zone.latitude, zone.longitude)
                    title = zone.title
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
            )
        }
        mapView.invalidate()
        onDispose {
            mapView.onDetach()
        }
    }

    val tilesCached = remember { hasCachedTiles(context) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = if (tilesCached) "Offline Ready" else "Download Map",
                color = if (tilesCached) Color(0xFF0A7F42) else Color(0xFFF57C00)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(SurfaceVariant, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        val center = getLastKnownGeoPoint(context) ?: defaultCenter
                        mapView.controller.animateTo(center)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceContainerLowest, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = "My Location",
                        tint = Primary
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceContainerLowest, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Layers,
                        contentDescription = "Layers",
                        tint = Primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nearby Safe Zones",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnBackground
                )
                TextButton(onClick = {}) {
                    Text(
                        text = "Filter",
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary
                    )
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = "Filter",
                        tint = Primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Safe Zone Cards
            safeZones.forEach { zone ->
                SafeZoneCard(
                    title = zone.title,
                    badge = zone.badge,
                    distance = zone.distance,
                    details = zone.details,
                    isBookmarked = bookmarks.contains(zone.title),
                    onDirectionsClick = {
                        val geoUri = Uri.parse(
                            "geo:${zone.latitude},${zone.longitude}?q=${zone.latitude},${zone.longitude}(${Uri.encode(zone.title)})"
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, geoUri))
                    },
                    onBookmarkClick = {
                        bookmarks = if (bookmarks.contains(zone.title)) {
                            bookmarks - zone.title
                        } else {
                            bookmarks + zone.title
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SafeZoneCard(
    title: String,
    badge: String,
    distance: String,
    details: String,
    isBookmarked: Boolean,
    onDirectionsClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnBackground
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(TertiaryFixed, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = OnTertiaryFixed
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Route,
                                contentDescription = "Distance",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = distance,
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDirectionsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        "Get Directions",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private fun hasCachedTiles(context: Context): Boolean {
    val basePath = Configuration.getInstance().osmdroidBasePath ?: File(context.filesDir, "osmdroid")
    val tilesPath = File(basePath, "tiles")
    return tilesPath.exists() && tilesPath.walkTopDown().any { it.isFile }
}

private fun getLastKnownGeoPoint(context: Context): GeoPoint? {
    return try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?: locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let { GeoPoint(it.latitude, it.longitude) }
    } catch (_: SecurityException) {
        null
    }
}
