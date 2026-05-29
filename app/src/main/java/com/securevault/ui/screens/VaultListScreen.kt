package com.securevault.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.securevault.data.Entry
import com.securevault.ui.theme.SvColors
import com.securevault.viewmodel.VaultViewModel

// ═══════════════════════════════════════════════════════════════════════════
// Vault List Screen — main password list
// ═══════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaultListScreen(
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    onLock: () -> Unit,
    favOnly: Boolean = false,
    viewModel: VaultViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsState()
    val filtered = if (favOnly) entries.filter { it.isFavorite } else entries

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
                Icon(
                    Icons.Default.Lock,
                    "Заблокировать",
                    tint = SvColors.TextMuted
                )
            }
        }

        // List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp) // Space for FAB
        ) {
            items(filtered, key = { it.id }) { entry ->
                EntryCard(
                    e = entry,
                    onClick = { onEdit(entry.id) },
                    onFav = { viewModel.toggleFavorite(entry.id) },
                    onDelete = { viewModel.deleteEntry(entry.id) }
                )
            }
            if (filtered.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (favOnly) "Нет избранных записей" else "Список пуст — добавь первую запись!",
                            color = SvColors.TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
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

// ═══════════════════════════════════════════════════════════════════════════
// Entry Card — individual password item
// ═══════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryCard(
    e: Entry,
    onClick: () -> Unit,
    onFav: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val accent = categoryColor(e.category)
    val isExpired = e.isPasswordExpired

    // ✅ Вспомогательная функция: ВСЕГДА возвращает Brush (исправляет ошибку компиляции)
    fun getAccentBrush(): Brush {
        return if (isExpired) {
            Brush.verticalGradient(
                listOf(
                    SvColors.Coral,
                    SvColors.Coral.copy(alpha = 0.3f)
                )
            )
        } else {
            Brush.verticalGradient(
                listOf(
                    accent,
                    accent.copy(alpha = 0.3f)
                )
            )
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 16.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { menuExpanded = true }
            )
    ) {
        // Left accent bar — теперь используем функцию с гарантированным типом Brush
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .width(4.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                .background(getAccentBrush()) // ✅ Теперь компилятор доволен!
        )

        // Content
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
                    .border(1.dp, accent.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = e.title.firstOrNull()?.uppercase() ?: "?",
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Info
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = e.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SvColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = e.username.takeIf { it.isNotBlank() } ?: "Нет логина",
                    fontSize = 13.sp,
                    color = SvColors.TextSecond,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Expiry hint
                if (isExpired) {
                    Text(
                        text = "⚠ Пароль устарел",
                        fontSize = 11.sp,
                        color = SvColors.Coral,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Favorite button
            IconButton(onClick = onFav) {
                Icon(
                    if (e.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    "Избранное",
                    tint = if (e.isFavorite) SvColors.Gold else SvColors.TextMuted
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            containerColor = SvColors.BgElevated,
            modifier = Modifier.border(1.dp, SvColors.Border, RoundedCornerShape(12.dp))
        ) {
            DropdownMenuItem(
                text = { Text("Редактировать", color = SvColors.TextPrimary) },
                onClick = { menuExpanded = false; onClick() },
                leadingIcon = { Icon(Icons.Default.Edit, null, tint = SvColors.Blue) }
            )
            DropdownMenuItem(
                text = { Text("Удалить", color = SvColors.Coral) },
                onClick = { menuExpanded = false; onDelete() },
                leadingIcon = { Icon(Icons.Default.Delete, null, tint = SvColors.Coral) }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Helpers
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun categoryColor(category: String): Color {
    return when (category.lowercase()) {
        "social" -> SvColors.Purple
        "bank" -> SvColors.Gold
        "email" -> SvColors.Teal
        "work" -> SvColors.Blue
        else -> SvColors.Blue
    }
}

// Glass card style
fun Modifier.glassCard(cornerRadius: Dp = 16.dp): Modifier {
    return this
        .clip(RoundedCornerShape(cornerRadius))
        .background(SvColors.BgCard)
        .border(1.dp, SvColors.Border, RoundedCornerShape(cornerRadius))
}
