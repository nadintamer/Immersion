package com.example.lexis.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lexis.R;
import com.example.lexis.adapters.WordListAdapter;
import com.example.lexis.adapters.WordSearchAdapter;
import com.example.lexis.databinding.FragmentWordSearchBinding;
import com.example.lexis.models.Word;
import com.example.lexis.models.WordSearch;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class WordSearchFragment extends Fragment {

    private static final String ARG_LANGUAGE = "targetLanguage";
    private static final String TAG = "WordSearchFragment";

    private FragmentWordSearchBinding binding;
    private String targetLanguage;
    private List<Word> words;
    private WordSearch wordSearch;

    private WordSearchAdapter wordSearchAdapter;
    private WordListAdapter wordListAdapter;
    private char[] letters;
    private List<String> clues;

    public WordSearchFragment() {}

    public static WordSearchFragment newInstance(String targetLanguage) {
        WordSearchFragment fragment = new WordSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LANGUAGE, targetLanguage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetLanguage = getArguments().getString(ARG_LANGUAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWordSearchBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (words == null) {
            words = new ArrayList<>();
            clues = new ArrayList<>();
            fetchWords(targetLanguage);
        }

        wordSearchAdapter = new WordSearchAdapter(this, letters);

        wordListAdapter = new WordListAdapter(this, clues);
        binding.rvWordList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rvWordList.setAdapter(wordListAdapter);

        setUpToolbar();
    }

    /*
    Fetch words for the word search and add them to the respective adapters.
    */
    private void fetchWords(String targetLanguage) {
        ParseQuery<Word> query = ParseQuery.getQuery(Word.class);
        query.include(Word.KEY_USER);
        query.whereEqualTo(Word.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Word.KEY_TARGET_LANGUAGE, targetLanguage);
        query.setLimit(6);
        query.findInBackground((words, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting vocabulary", e);
                return;
            }

            wordSearch = new WordSearch(words);
            binding.rvWordSearch.setLayoutManager(new GridLayoutManager(getActivity(), wordSearch.getWidth()));
            binding.rvWordSearch.setAdapter(wordSearchAdapter);

            wordSearchAdapter.setLetters(wordSearch.getFlatGrid());
            wordListAdapter.addAll(wordSearch.getClues());
        });
    }

    /*
    Set up toolbar with a custom exit button.
    */
    private void setUpToolbar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar.getRoot());
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("");
            }

            binding.toolbar.getRoot().setNavigationIcon(R.drawable.clear_icon);
            Drawable navigationIcon = binding.toolbar.getRoot().getNavigationIcon();
            if (navigationIcon != null) {
                navigationIcon.setTint(getResources().getColor(R.color.black));
            }
            binding.toolbar.getRoot().setNavigationOnClickListener(v -> returnToPracticeTab());
        }
    }

    /*
    Exit the practice session and return to the vocabulary view.
    */
    private void returnToPracticeTab() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new PracticeFragment())
                    .commit();
        }
    }
}