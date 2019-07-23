package com.example.myapplication.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.util.EditTextHelper;

public class ProvideFdbkActivity extends AppCompatActivity implements DroidListener {
    private final int TEXT_SIZE = 13;
    private boolean activityNotSetUp = true;
    private EditText[] answersET;
    private TextView[] questionsTV;
    private Button saveBtn;
    private static int NUMBER_OF_QUESTIONS;
    private static final String ANSWER_NOT_FILLED_MESSAGE = "Please fill in the answer...";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.feedback_activity);

//        LayoutInflater inflater = LayoutInflater.from(this);
//        ScrollView scrollLayout = (ScrollView)inflater.inflate(R.layout.feedback_activity, null, false);

        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);
    }

    private void createActivityLayout() {
        ScrollView scrollLayout = new ScrollView(this);
        LinearLayout.LayoutParams layParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollLayout.setLayoutParams(layParams);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(llParams);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollLayout.addView(linearLayout);

        //TODO get number of questions
        String[] fdbkQuestions = getIntent().
                getExtras().
                getStringArray(MainActivity.FEEDBACK_QUESTIONS_TAG);

        NUMBER_OF_QUESTIONS = fdbkQuestions.length;

        answersET = new EditText[NUMBER_OF_QUESTIONS];
        questionsTV = new TextView[NUMBER_OF_QUESTIONS];

        LinearLayout.LayoutParams smallViewsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int fiveDP = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                        .getDisplayMetrics());
        smallViewsParams.setMargins(fiveDP, fiveDP, fiveDP, fiveDP);

        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            TextView questionTV = new TextView(this);
            questionTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
            questionTV.setLayoutParams(smallViewsParams);
            questionTV.setPadding(fiveDP, fiveDP, fiveDP, fiveDP);
            //set text type to bold
            questionTV.setTypeface(questionTV.getTypeface(), Typeface.BOLD);
            questionTV.setText(fdbkQuestions[i]);

            EditText answerEt = new EditText(this);
            answerEt.setLayoutParams(smallViewsParams);
            answerEt.setHint(R.string.answer_question);
            answerEt.setBackgroundResource(R.drawable.edit_text_border);
            answerEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
            answerEt.setPadding(fiveDP, fiveDP, fiveDP, fiveDP);
            answerEt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            answerEt.setSingleLine(false);
            answerEt.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            answerEt.setMaxLines(3);
            answerEt.setLines(3);

            answersET[i] = answerEt;
            questionsTV[i] = questionTV;

            linearLayout.addView(questionTV);
            linearLayout.addView(answerEt);
        }

        saveBtn = new Button(this);
        saveBtn.setLayoutParams(smallViewsParams);
        saveBtn.setBackgroundColor(getResources().getColor(R.color.greenSave));
        saveBtn.setText(R.string.save_btn_text);
        saveBtn.setAllCaps(false);

        linearLayout.addView(saveBtn);

        setContentView(scrollLayout);
    }

    private void setUp() {
        createActivityLayout();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] answers = new String[NUMBER_OF_QUESTIONS];

                for (int i = 0; i < answers.length; i++) {
                    answers[i] = answersET[i].getText().toString();

                    if (EditTextHelper.isET_empty(answersET[i], answers[i], ANSWER_NOT_FILLED_MESSAGE))
                        return;

                }

                Intent intent = new Intent();
                intent.putExtra(MainActivity.ANSWERS_ARRAY_CODE, answers);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        activityNotSetUp = false;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected && activityNotSetUp) {
                setUp();
        } else {
            Toast.makeText(this, "Turn on the Interent!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return true;
    }
}
