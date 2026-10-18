/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    val scrollingUp = remember(this) { mutableStateOf(true) }

    LaunchedEffect(this) {
        var previousIndex = firstVisibleItemIndex
        var previousScrollOffset = firstVisibleItemScrollOffset

        snapshotFlow { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .map { (index, offset) ->
                val result =
                    if (previousIndex != index) {
                        previousIndex > index
                    } else {
                        previousScrollOffset >= offset
                    }

                previousIndex = index
                previousScrollOffset = offset
                result
            }
            .distinctUntilChanged()
            .collect { scrollingUp.value = it }
    }

    return scrollingUp.value
}
