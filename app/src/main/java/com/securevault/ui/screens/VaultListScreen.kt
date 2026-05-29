package com.securevault.ui.screens

// ✅ ВСЕ НУЖНЫЕ ИМПОРТЫ — НЕ УДАЛЯЙ НИ ОДИН
import androidx.compose.foundation.background
import androidx.compose.foundation.border  // ← ЭТОГО НЕ ХВАТАЛО!
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securevault.ui.theme.SvColors

// Простая модель
data class DemoItem(val id: Long, val title: String, val username: String, val category: String)

@Composable
fun VaultListScreen(
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    onLock: () -> Unit,
    favOnly: Boolean = false
) {
    val demoItems = remember {
        listOf(
            DemoItem(1, "Google", "user@gmail.com", "email"),
            DemoItem(2, "Telegram", "@username", "social"),
            DemoItem(3, "Банк", "1234****", "bank")
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(SvColors.BgDeep)
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (favOnly) "★ Избранное" else "🔐 Все записи",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SvColors.TextPrimary
            )
            IconButton(onClick = onLock) {
                Icon(Icons.Default.Lock, "Заблокировать", tint = SvColors.TextMuted)
            }
        }

        // List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(demoItems, key = { it.id }) { item ->
                DemoCard(
                    title = item.title,
                    username = item.username,
                    category = item.category,
                    onClick = { onEdit(item.id) }
                )
            }
        }
    }

    // FAB
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = onAdd,
            containerColor = SvColors.Blue,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, "Добавить")
        }
    }
}

@Composable
fun DemoCard(title: String, username: String, category: String, onClick: () -> Unit) {
    val accent = when (category.lowercase()) {
        "social" -> SvColors.Purple
        "bank" -> SvColors.Gold
        "email" -> SvColors.Teal
        else -> SvColors.Blue
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SvColors.BgCard),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SvColors.BgCard)
            .border(1.dp, SvColors.Border, RoundedCornerShape(16.dp))  // ✅ Теперь работает!
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.2f))
                    .border(1.dp, accent.copy(alpha = 0.4f), CircleShape),  // ✅ И здесь!
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.firstOrNull()?.uppercase() ?: "?",
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Info
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SvColors.TextPrimary
                )
                Text(
                    text = username,
                    fontSize = 13.sp,
                    color = SvColors.TextSecond
                )
            }
        }
    }
}
