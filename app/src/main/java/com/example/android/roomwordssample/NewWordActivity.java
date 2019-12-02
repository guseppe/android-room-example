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

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity for entering a word.
 */

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String LANGUAGE = "com.example.android.wordlistsql.LANGUAGE";

    private EditText mEditWordView;
    private EditText mEditLanguageView;
    private Word wordForUpdate;
    private WordViewModel mWordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordView = findViewById(R.id.edit_word);
        mEditLanguageView = findViewById(R.id.edit_language);
        mWordViewModel = new ViewModelProvider((ViewModelStoreOwner)MainActivity.getContext()).get(WordViewModel.class);
        wordForUpdate = (Word)getIntent().getSerializableExtra("CURRENTWORD");
        if(wordForUpdate != null){
            mEditWordView.setText(wordForUpdate.getWord());
            mEditLanguageView.setText(wordForUpdate.getLanguage());
        }
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText()) || TextUtils.isEmpty(mEditLanguageView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    String language = mEditLanguageView.getText().toString();
                    if(wordForUpdate == null) {
                        replyIntent.putExtra(EXTRA_REPLY, word);
                        replyIntent.putExtra(LANGUAGE, language);
                        setResult(RESULT_OK, replyIntent);
                    }else{
                        wordForUpdate.setWord(word);
                        wordForUpdate.setLanguage(language);
                        mWordViewModel.update(wordForUpdate);
                        setResult(MainActivity.RESULT_UPDATED, replyIntent);
                    }
                }
                finish();
            }
        });
    }
}

