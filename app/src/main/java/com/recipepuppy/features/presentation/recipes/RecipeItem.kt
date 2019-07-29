package com.recipepuppy.features.presentation.recipes

import android.view.animation.OvershootInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.like.LikeButton
import com.like.OnLikeListener
import com.recipepuppy.R
import com.recipepuppy.core.extension.gone
import com.recipepuppy.core.extension.visible
import com.recipepuppy.features.presentation.model.RecipeView
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.layout_row_recipe.view.*

/**
 * Created by danieh on 29/07/2019.
 */
class RecipeItem(
        private val recipeView: RecipeView,
        private val clickListenerRecipe: (RecipeView) -> Unit = { _ -> },
        private val clickListenerFav: (RecipeView, isFavorite: Boolean) -> Unit = { _, _ -> }
) : Item() {

    override fun getLayout(): Int = R.layout.layout_row_recipe

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            Glide.with(itemView.context)
                    .load(recipeView.thumbnail)
                    .error(Glide.with(itemView.context).load(R.mipmap.ic_logo_viewholder))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.recipe_image)
            itemView.recipe_name.text = recipeView.title
            itemView.recipe_ingredients.text = recipeView.ingredients

            if (recipeView.ingredients.contains("milk") or recipeView.ingredients.contains("cheese")) {
                animateLactoseLabel()
                itemView.recipe_lactose_image.visible()
            } else {
                itemView.recipe_lactose_image.gone()
            }

            itemView.setOnClickListener { clickListenerRecipe(recipeView) }
            itemView.recipe_favorite.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    likeButton?.isLiked = true
                    clickListenerFav(recipeView, true)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    likeButton?.isLiked = false
                    clickListenerFav(recipeView, false)
                }

            })
        }
    }

    private fun ViewHolder.animateLactoseLabel() {
        itemView.recipe_lactose_image.apply {
            scaleX = 0f
            scaleY = 0f
        }
        if (itemView.recipe_lactose_image.animation != null) {
            itemView.recipe_lactose_image.animation.cancel()
        }
        itemView.recipe_lactose_image.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(OvershootInterpolator(3.0f))
                .setDuration(500)
                .setStartDelay(300)
                .start()
    }
}
