package com.example.teethkids

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private val reviews = mutableListOf<Review>()

    // Método para definir os dados da lista de avaliações
    fun setData(data: List<Review>) {
        reviews.clear() // Limpa a lista atual de avaliações
        reviews.addAll(data) // Adiciona todas as novas avaliações à lista
        notifyDataSetChanged() // Notifica o RecyclerView que os dados foram atualizados
    }

    // Método chamado quando o RecyclerView precisa criar uma nova instância de ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // Infla o layout do item de avaliação usando o LayoutInflater
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view) // Retorna uma nova instância de ReviewViewHolder
    }

    // Método chamado para associar os dados de uma avaliação a um ViewHolder
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position] // Obtém a avaliação na posição atual
        holder.bind(review) // Chama o método bind() do ViewHolder para exibir os dados da avaliação
    }

    // Método para obter o número total de itens na lista de avaliações
    override fun getItemCount(): Int {
        return reviews.size
    }

    // Classe interna que representa o ViewHolder para exibir cada item de avaliação
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        // Método para exibir os dados de uma avaliação no ViewHolder
        fun bind(review: Review) {
            commentTextView.text = review.reviewText // Define o texto do comentário da avaliação
            ratingBar.rating = review.rating // Define a classificação da avaliação na RatingBar
            ratingBar.max = review.maxRating.toInt() // Define o valor máximo da RatingBar
            ratingBar.stepSize = 1.0f // Define o tamanho do passo da RatingBar
            ratingBar.progressTintList = ColorStateList.valueOf(Color.YELLOW) // Define a cor de preenchimento da RatingBar

            // Desabilita a interação do usuário com a RatingBar
            ratingBar.setOnTouchListener { _, _ -> true }
            ratingBar.isFocusable = false
            ratingBar.isClickable = false
        }
    }
}

