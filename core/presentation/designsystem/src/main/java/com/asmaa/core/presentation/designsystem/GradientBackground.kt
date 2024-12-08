package com.asmaa.core.presentation.designsystem

import android.os.Build
import android.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * In Android applications, Toolbar is a kind of ViewGroup that can be placed in the XML layouts of an activity.
 * The Toolbar is basically the advanced successor of the ActionBar. It is much more flexible and customizable
 * in terms of appearance and functionality. Unlike ActionBar, its position is not hardcoded i.e., not at the top of an activity.
 * Developers can place it anywhere in the activity according to the need just like any other View in android.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    hasToolbar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    /**
     *  with is a scope function in Kotlin, similar to run, apply, also, let, and takeIf.
     *  These scope functions allow you to execute a block of code within the context of an object
     *  and provide a way to access properties and methods of the object in a more concise manner.
     *  with takes an object as its receiver and a lambda block.
     *  Inside the lambda, you can access the receiver's properties and methods without needing to refer to the receiver explicitly.
     *  you generally cannot modify the receiver object directly within the with scope in the same way you might in run or apply
     */
    val isAtleast12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val screenWidthPx = with(density)
    {
        //Understand what is really the use of Dp and Px and when to use what
        configuration.screenWidthDp.dp.roundToPx()
    }
    val smallDimension = minOf(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val smallDimensionPx = density.run {
        smallDimension.roundToPx()
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isAtleast12) {
                        Modifier.blur(smallDimension / 3f)
                    } else Modifier
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isAtleast12) primaryColor else primaryColor.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        ),
                        center = Offset(x = screenWidthPx / 2f, -100f),
                        radius = smallDimensionPx / 2f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (hasToolbar) {
                        modifier
                    } else {
                        Modifier.systemBarsPadding()
                    }
                )
        ) {
            content()
        }
    }
}