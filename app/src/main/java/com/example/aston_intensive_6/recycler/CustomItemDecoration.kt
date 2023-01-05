package com.example.aston_intensive_6.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.roundToInt

class CustomItemDecoration(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int,
    private val divider: Drawable
) : ItemDecoration() {

    private val rect = Rect()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(left, top, right, bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()

        val left = 0
        val right = parent.width

        for (i in 0 until parent.childCount) {
            if (i != parent.childCount - 1) {
                val child = parent.getChildAt(i)
                parent.layoutManager?.getDecoratedBoundsWithMargins(child, rect)
                val bottom = rect.bottom + child.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
        c.restore()
    }

}