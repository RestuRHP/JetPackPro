package net.learn.jetpack.ui.movies

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_movie.view.img_poster
import kotlinx.android.synthetic.main.item_movie.view.tv_title
import net.learn.jetpack.BuildConfig
import net.learn.jetpack.R
import net.learn.jetpack.data.model.movies.Movie
import net.learn.jetpack.ui.detail.movie.DetailMovieActivity

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val movieItems = mutableListOf<Movie>()

    fun updateData(items: MutableList<Movie>) {
        movieItems.clear()
        movieItems.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(items: Movie) {
            with(itemView) {
                tv_title.text = items.title
                Glide.with(itemView.context)
                    .load(BuildConfig.POSTER_PATH + items.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                    )
                    .listener(
                        GlidePalette.with(BuildConfig.POSTER_PATH + items.posterPath)
                            .use(BitmapPalette.Profile.VIBRANT)
                            .intoBackground(itemView.item_poster_palette)
                            .crossfade(true)
                    )
                    .dontAnimate()
                    .into(img_poster)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailMovieActivity::class.java)
                    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, items)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieItems[position])
    }

    override fun getItemCount(): Int = movieItems.size
}