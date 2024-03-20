package com.example.melodydiary.ui.components

import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class DiamondShape : Shape {

    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawDiamondPath(size)
        )
    }
}

private fun drawDiamondPath(size: androidx.compose.ui.geometry.Size): Path {
    return Path().apply {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        val halfWidth = width / 2f
        val halfHeight = height / 2f

        moveTo(x = centerX, y = 0f)
        lineTo(x = width, y = centerY)
        lineTo(x = centerX, y = height)
        lineTo(x = 0f, y = centerY)

        close()
    }
}