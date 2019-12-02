package com.example.android.roomwordssample;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.AlertDialog;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final WordViewModel mWordViewModel;
    private Context context;
    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private final TextView languageItemView;
        private Word currentElement;
        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            languageItemView = itemView.findViewById(R.id.textView2);
            wordItemView.setOnLongClickListener(v->{
                AlertDialog.Builder confirmation = new AlertDialog.Builder(context);
                confirmation.setTitle("Confirmar eliminación")
                        .setMessage("Estas a punto de eliminar un registro, deseas continuar?")
                        .setPositiveButton("Si",(dialog,id)->{
                            mWordViewModel.remove(currentElement);
                            Toast.makeText(context,"Item eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No",(dialog, id) -> {
                            dialog.cancel();
                        })
                        .show();
                return true;
            });
            wordItemView.setOnClickListener(v->{
                Intent intent = new Intent(context, NewWordActivity.class);
                intent.putExtra("CURRENTWORD", currentElement);
                context.startActivity(intent);
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words

    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mWordViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(WordViewModel.class);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
            holder.languageItemView.setText(current.getLanguage());
            holder.currentElement = current;
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }
    }

    void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }
}


