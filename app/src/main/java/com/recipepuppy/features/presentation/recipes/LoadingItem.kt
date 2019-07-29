package com.recipepuppy.features.presentation.recipes

import com.recipepuppy.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

/**
 * Created by danieh on 29/07/2019.
 */
class LoadingItem() : Item() {

    override fun getLayout(): Int = R.layout.layout_row_loader

    override fun bind(viewHolder: ViewHolder, position: Int) {}
}
