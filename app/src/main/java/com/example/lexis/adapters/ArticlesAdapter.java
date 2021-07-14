package com.example.lexis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lexis.R;
import com.example.lexis.databinding.ItemArticleBinding;
import com.example.lexis.fragments.ArticleFragment;
import com.example.lexis.models.Article;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {
    List<Article> articles;
    Fragment fragment;

    public ArticlesAdapter(Fragment fragment, List<Article> articles) {
        this.fragment = fragment;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(LayoutInflater.from(fragment.getActivity()), parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Article> list) {
        articles.addAll(list);
        notifyDataSetChanged();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemArticleBinding binding;

        public ArticleViewHolder(@NonNull ItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        /*
        Bind Article data into the ViewHolder.
        */
        public void bind(Article article) {
            binding.tvTitle.setText(article.getTitle());
            binding.tvSnippet.setText(article.getBody());
            if (article.getSource().equals("Wikipedia")) {
                binding.ivSource.setImageResource(R.drawable.wikipedia_logo);
            }
        }

        /*
        Show ArticleFragment when an item is clicked.
        */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
            final Fragment articleFragment = ArticleFragment.newInstance(articles.get(position));

            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, articleFragment)
                    .addToBackStack(null) // add to back stack so we can return to this fragment
                    .commit();
        }
    }
}