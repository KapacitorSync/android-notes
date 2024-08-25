package tech.kapacitor.android.notes.ui.motion

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import tech.kapacitor.android.notes.ui.composables.initialOffset

object MotionConstants {
    const val ENTER_DURATION: Int = 400
    const val EXIT_DURATION: Int = 300
}

private val Int.ForOutgoing: Int
    get() = (this * 0.25f).toInt()

private val Int.ForIncoming: Int
    get() = this - this.ForOutgoing

fun slideIn(
    initialOffsetX: (fullWidth: Int) -> Int,
    durationMillis: Int = MotionConstants.ENTER_DURATION,
): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        initialOffsetX = initialOffsetX,
    ) +
        fadeIn(
            animationSpec =
            tween(
                durationMillis = durationMillis.ForIncoming,
                delayMillis = durationMillis.ForOutgoing,
                easing = LinearOutSlowInEasing,
            ),
        )

fun slideOut(
    targetOffsetX: (fullWidth: Int) -> Int,
    durationMillis: Int = MotionConstants.EXIT_DURATION,
): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        targetOffsetX = targetOffsetX,
    ) +
        fadeOut(
            animationSpec =
            tween(
                durationMillis = durationMillis.ForOutgoing,
                delayMillis = 0,
                easing = FastOutLinearInEasing,
            ),
        )

fun slideInY(
    initialOffsetY: (fullHeight: Int) -> Int = { -(it * initialOffset).toInt() },
    durationMillis: Int = MotionConstants.ENTER_DURATION,
    delayMillis: Int = 0,
): EnterTransition =
    slideInVertically(
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        initialOffsetY = initialOffsetY,
    ) +
        fadeIn(
            animationSpec =
            tween(
                durationMillis = durationMillis.ForIncoming,
                delayMillis = durationMillis.ForOutgoing + delayMillis,
                easing = LinearOutSlowInEasing,
            ),
        )

fun slideOutY(
    targetOffsetY: (fullHeight: Int) -> Int = { (it * initialOffset).toInt() },
    durationMillis: Int = MotionConstants.EXIT_DURATION,
    delayMillis: Int = 0,
): ExitTransition =
    slideOutVertically(
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        targetOffsetY = targetOffsetY,
    ) +
        fadeOut(
            animationSpec =
            tween(
                durationMillis = durationMillis.ForOutgoing,
                delayMillis = delayMillis,
                easing = FastOutLinearInEasing,
            ),
        )