package com.securevault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securevault.ui.theme.SvColors

// ═══════════════════════════════════════════════════════════════════════════
// Простая модель для демо
// ═══════════════════════════════════════════════════════════════════════════
data class DemoItem(val id: Long, val title: String, val username: String, val category: String)

// ═══════════════════════════════════════════════════════════════════════════
// Vault List Screen — минимальная рабочая версия
// ═══════════════════════════════════════════════════════════════════════════
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

// ═══════════════════════════════════════════════════════════════════════════
// Простая карточка
// ═══════════════════════════════════════════════════════════════════════════
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
            .border(1.dp, SvColors.Border, RoundedCornerShape(16.dp))  // ✅ Теперь импорт есть!
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.2f))
                    .border(1.dp, accent.copy(alpha = 0.4f), CircleShape),  // ✅ И здесь
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.firstOrNull()?.uppercase() ?: "?",
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

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
