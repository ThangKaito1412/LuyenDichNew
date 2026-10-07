package com.example.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MAX_AUTO_REPEAT
import com.example.viewmodel.MIN_AUTO_REPEAT

/** Click có hiệu ứng nhún nhẹ (spring) cho cảm giác mượt, hiện đại. */
@Composable
fun Modifier.bounceClick(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "bounceScale"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick)
}

/** Bộ chỉnh số lần lặp đọc mỗi từ vựng khi bật chế độ "Tự cuộn". */
@Composable
fun AutoRepeatControl(
    value: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepperButton(Icons.Default.Remove, "Giảm", enabled = value > MIN_AUTO_REPEAT) {
                onChange(value - 1)
            }
            Column(
                modifier = Modifier.width(96.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$value",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.primary
                )
                Text(
                    text = "lần / câu",
                    fontSize = 11.sp,
                    color = colors.onSurface.copy(alpha = 0.55f)
                )
            }
            StepperButton(Icons.Default.Add, "Tăng", enabled = value < MAX_AUTO_REPEAT) {
                onChange(value + 1)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            listOf(1, 2, 3, 5, 7, 10).forEach { n ->
                FilterChip(
                    selected = value == n,
                    onClick = { onChange(n) },
                    label = { Text("${n}x", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        Text(
            text = "Mỗi câu hỏi và mỗi đáp án sẽ được đọc lặp lại $value lần trước khi chuyển tiếp.",
            fontSize = 11.5.sp,
            lineHeight = 16.sp,
            color = colors.onSurface.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun StepperButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(48.dp)
            .bounceClick(enabled = enabled, onClick = onClick)
            .clip(CircleShape)
            .background(if (enabled) colors.primary.copy(alpha = 0.14f) else colors.onSurface.copy(alpha = 0.06f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = if (enabled) colors.primary else colors.onSurface.copy(alpha = 0.3f)
        )
    }
}

val SubtleBorder: BorderStroke
    @Composable get() = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.8.sp,
        color = MaterialTheme.colorScheme.primary
    )
}
