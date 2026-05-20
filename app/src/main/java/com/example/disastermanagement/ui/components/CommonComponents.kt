package com.example.disastermanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.disastermanagement.navigation.NavigationItem
import com.example.disastermanagement.ui.theme.OnPrimaryContainer
import com.example.disastermanagement.ui.theme.OnSurfaceVariant
import com.example.disastermanagement.ui.theme.Primary
import com.example.disastermanagement.ui.theme.PrimaryContainer
import com.example.disastermanagement.ui.theme.SurfaceContainerLowest

@Composable
fun DisasterTrainingTopAppBar(
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest)
            .statusBarsPadding()
            .shadow(elevation = 4.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = "Menu",
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = "Disaster Training",
            style = MaterialTheme.typography.headlineSmall,
            color = Primary
        )

        IconButton(onClick = onNotificationsClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest)
            .navigationBarsPadding()
            .shadow(elevation = 8.dp, spotColor = Primary.copy(alpha = 0.08f))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Outlined.Dashboard,
            label = "Dashboard",
            isSelected = currentRoute == NavigationItem.Dashboard.route,
            onClick = { onNavigate(NavigationItem.Dashboard) }
        )

        BottomNavItem(
            icon = Icons.Outlined.School,
            label = "Learn",
            isSelected = currentRoute == NavigationItem.TrainingHub.route,
            onClick = { onNavigate(NavigationItem.TrainingHub) }
        )

        BottomNavItem(
            icon = Icons.Outlined.FitnessCenter,
            label = "Drills",
            isSelected = currentRoute == NavigationItem.DrillSimulator.route,
            onClick = { onNavigate(NavigationItem.DrillSimulator) }
        )

        BottomNavItem(
            icon = Icons.Outlined.Person,
            label = "Profile",
            isSelected = currentRoute == NavigationItem.Profile.route,
            onClick = { onNavigate(NavigationItem.Profile) }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Primary else OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Primary else OnSurfaceVariant
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Primary)
            )
        }
    }
}

@Composable
fun Card(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(SurfaceContainerLowest, RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp, spotColor = Primary.copy(alpha = 0.08f), shape = RoundedCornerShape(16.dp))
            .padding(1.dp)
    ) {
        content()
    }
}

