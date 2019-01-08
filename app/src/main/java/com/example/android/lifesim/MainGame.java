package com.example.android.lifesim;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Random;

public class MainGame extends AppCompatActivity {

    String doctorNames[] = {"Mike Rable", "Murphy Morgan", "John Seplin", "Morgan Johnson", "Hank Freeman"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        // Hides Action Bar on this page
        ActionBar actionBar = getActionBar();
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        /* loads in data from inputActivity screen */
        Intent intent = getIntent();
        String genderText = intent.getStringExtra("gender");
        String firstNameText = intent.getStringExtra("firstName");
        String lastNameText = intent.getStringExtra("lastName");


        TextView nameView = (TextView)findViewById(R.id.nameView);
        TextView nameViewLast = (TextView)findViewById(R.id.nameViewLast);
        nameView.setText(firstNameText);
        nameViewLast.setText(lastNameText);

        // Initializing main character
        final Person mainPerson = new Person(firstNameText + " " + lastNameText, 0, 0, randomNumberInBetweenMaxMin(70, 100), 100);
        printFirstTextView(mainPerson);


        // BankAccount Button onClick Function
        Button bankButton = (Button) findViewById(R.id.bankView);
        bankButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                AlertDialog alertDialog = new AlertDialog.Builder(MainGame.this).create();
                alertDialog.setTitle("Financial Information");
                alertDialog.setMessage("Bank Account Balance: " + formatToCurrency(mainPerson.getBankBalance()));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        // Up Age button onClick function
        Button upAgeButton = (Button)findViewById(R.id.progressAge);
        upAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPerson.upAge();
                nextAgeTextView(mainPerson);
            }
        });

        // Activity Popup Back Button function
        ImageButton backButton = (ImageButton) findViewById(R.id.activityPopupBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityBackButtonFunction();
            }
        });


        // Activity Button onClick function
        final Button activitiesButton = (Button)findViewById(R.id.buttonActivities);
        activitiesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                LinearLayout activityPopup =(LinearLayout)findViewById(R.id.activityPopup);
                LinearLayout topBarLayout = (LinearLayout)findViewById(R.id.topbarlayout);
                LinearLayout activityBarLinearLayout = (LinearLayout)findViewById(R.id.activityBarLinearLayout);

                if(activityPopup.getVisibility() == View.GONE){
                    activityPopup.setVisibility(View.VISIBLE);
                    topBarLayout.setVisibility(View.GONE);
                    activityBarLinearLayout.setVisibility(View.GONE);

                }

            }
        });

        // Go to doctor office onclick button
        final LinearLayout doctorOfficeButton = (LinearLayout) findViewById(R.id.doctorOfficeButton);
        doctorOfficeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if person is not sick
                if(mainPerson.getSickness() == null){
                    Toast drerrortoast = Toast.makeText(getApplicationContext(),
                            "You must be sick to go to the doctor.",
                            Toast.LENGTH_SHORT);

                    drerrortoast.show();
                }
                // if they are sick
                else{
                    hideActivityBarBringBackTopBar();
                    chooseDoctor(mainPerson);

                }
            }
        });

        // if they choose dr. 1
        Button drButton1 = (Button)findViewById(R.id.drbutton1);
        drButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                curePerson(mainPerson, 1);
            }
        });

        // if they choose dr. 2
        Button drButton2 = (Button)findViewById(R.id.drbutton2);
        drButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                curePerson(mainPerson, 2);
            }
        });

        // if they choose dr. 3
        Button drButton3 = (Button)findViewById(R.id.drbutton3);
        drButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                curePerson(mainPerson, 3);
            }
        });

        // Doctor Popup Back Button function
        ImageButton drBackButton = (ImageButton) findViewById(R.id.drbackbutton);
        drBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout doctorLayout = (RelativeLayout)findViewById(R.id.doctorPopup);
                doctorLayout.setVisibility(View.GONE);
            }
        });
    }


    // Makes back button work after you click activities button 
    void activityBackButtonFunction() {
        hideActivityBarBringBackTopBar();
    }


    void nextAgeTextView(Person mainPerson){

        maintainScrollViewDown(); // keeps scroll focused downward.


        /* DYAMICALLY ADDS TEXVIEW TO SCROLLVIEW */
        //create a TextView with Layout parameters according to your needs
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //if your parent Layout is relativeLayout, just change the word LinearLayout with RelativeLayout
        final TextView topView = new TextView(this);
        topView.setLayoutParams(lparams);
        topView.setTextSize(15);
        topView.setTextColor(Color.parseColor("#3F51B5"));
        topView.setText(getString(R.string.InitialTextViewAge, mainPerson.getAge()));

        final TextView tv = new TextView(this);
        tv.setLayoutParams(lparams);
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(0,0,0,40);

        /*Sets Drawable for border on bottom of textview in scrollview*/
        final int sdk = android.os.Build.VERSION.SDK_INT;   // gets int version of os build
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) // if os is less than Jelly Bean then make it drawable
        {
            tv.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.maingametextviewborderbottom) );
        } else {
            tv.setBackground(ContextCompat.getDrawable(this, R.drawable.maingametextviewborderbottom));
        }

        //get the parent layout for your new TextView and add the new TextView to it
        LinearLayout linearLayout = findViewById(R.id.insideScrollView);
        linearLayout.addView(topView);
        linearLayout.addView(tv);


        // Displays bankaccount balance now
        String bankBalanceString = formatToCurrency(mainPerson.getBankBalance());
        Button bankAccountView = (Button)findViewById(R.id.bankView);
        bankAccountView.setText("Bank Account\n" + bankBalanceString);

        // Updates ProgressBars
        ProgressBar healthBar = (ProgressBar)findViewById(R.id.progressbarHealth);
        healthBar.setProgress(mainPerson.getHealth());
        ProgressBar happyBar = (ProgressBar)findViewById(R.id.progressbarHappy);
        happyBar.setProgress(mainPerson.getHappiness());

        // Person possibly gets sick here
        mainPerson.randomSickness();
        printSickness(mainPerson, tv);



    }

    // Prints sickness to tv if they're sick and takes away their health/happiness and adds year to sickness
    void printSickness(Person mainPerson, TextView tv){

        // if person is sick, display to screen
        if(mainPerson.getSickness() != null){

            // if this is first year they have this sickness
            if(mainPerson.getSickness().getYearsWith() == 0){
                tv.append("You got sick with " + mainPerson.getSickness().getTitle() + ", which is " + mainPerson.getSickness().getDescription() + ". You should see a doctor");
            }
            else {
                tv.append("You're still sick with " + mainPerson.getSickness().getTitle() + ".");
            }

            mainPerson.getSickness().addYearToSickness();

            // Takes away their health/happy based on damage per turn
            int currentHappy = mainPerson.getHappiness();
            int currentHealth = mainPerson.getHealth();
            int damageHappy = mainPerson.getSickness().getHappyDamagePerTurn();
            int damageHealth = mainPerson.getSickness().getDamagePerTurn();
            mainPerson.setHealth(currentHealth - damageHealth);
            mainPerson.setHappiness(currentHappy - damageHappy);
        }
    }

    // Prints first TextView to the ScrollView
    void printFirstTextView(Person mainPerson){


        /* DYAMICALLY ADDS TEXVIEW TO SCROLLVIEW */
        //create a TextView with Layout parameters according to your needs
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //if your parent Layout is relativeLayout, just change the word LinearLayout with RelativeLayout
        final TextView topView = new TextView(this);
        topView.setLayoutParams(lparams);
        topView.setTextSize(15);
        topView.setTextColor(Color.parseColor("#3F51B5"));
        topView.setText(getString(R.string.InitialTextViewAge, mainPerson.getAge()));

        final TextView tv = new TextView(this);
        tv.setLayoutParams(lparams);
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(0,0,0,40);

        /*Sets Drawable for border on bottom of textview in scrollview*/
        final int sdk = android.os.Build.VERSION.SDK_INT;   // gets int version of os build
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) // if os is less than Jelly Bean then make it drawable
        {
            tv.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.maingametextviewborderbottom) );
        } else {
            tv.setBackground(ContextCompat.getDrawable(this, R.drawable.maingametextviewborderbottom));
        }


        // Set Text Here !!!!!!!!!!!!!!!
        tv.setText(getString(R.string.InitialTextViewIntro, mainPerson.getName()));



        //get the parent layout for your new TextView and add the new TextView to it
        LinearLayout linearLayout = findViewById(R.id.insideScrollView);
        linearLayout.addView(topView);
        linearLayout.addView(tv);


        // Displays bankaccount balance now
        String bankBalanceString = formatToCurrency(mainPerson.getBankBalance());
        Button bankAccountView = (Button)findViewById(R.id.bankView);
        bankAccountView.setText("Bank Account\n" + bankBalanceString);

        // Updates ProgressBars
        ProgressBar healthBar = (ProgressBar)findViewById(R.id.progressbarHealth);
        healthBar.setProgress(mainPerson.getHealth());
        ProgressBar happyBar = (ProgressBar)findViewById(R.id.progressbarHappy);
        happyBar.setProgress(mainPerson.getHappiness());

    }

    // Constant --> Keeps scrollview focused downwards
    void maintainScrollViewDown(){
        // All constant
        ScrollView scroll = findViewById(R.id.scrollviewmain);
        scroll.fullScroll(View.FOCUS_DOWN);
    }

    /*Takes in a double and returns a string formatted to currency*/
    public String formatToCurrency(double money){

        NumberFormat format = NumberFormat.getCurrencyInstance();

        return format.format(money);
    }

    // Returns a random number in between a min/max
    int randomNumberInBetweenMaxMin(int min, int max){
        return (new Random()).nextInt((max - min) + 1) + min;

    }

    // hides activity bar and brings back top bar
    private void hideActivityBarBringBackTopBar() {
        LinearLayout activityPopup =(LinearLayout)findViewById(R.id.activityPopup);
        LinearLayout topBarLayout = (LinearLayout)findViewById(R.id.topbarlayout);
        LinearLayout activityBarLinearLayout = (LinearLayout)findViewById(R.id.activityBarLinearLayout);

        activityPopup.setVisibility(View.GONE);
        topBarLayout.setVisibility(View.VISIBLE);
        activityBarLinearLayout.setVisibility(View.VISIBLE);
    }

    // Randomly returns a string in an array
    public String randArrayTitle(String[] jobTitles){

        int size = jobTitles.length - 1; // size of array


        int randomNum = (int)(Math.random() * ((size) + 1));
        return jobTitles[randomNum];

    }

    // Allows them to choose a doctor from the list
    private void chooseDoctor(Person mainPerson) {
        // Makes Doctor Bar visible
        RelativeLayout doctorPopup = (RelativeLayout)findViewById(R.id.doctorPopup);
        doctorPopup.setVisibility(View.VISIBLE);

        Button drbutton1 = (Button)findViewById(R.id.drbutton1);
        Button drbutton2 = (Button)findViewById(R.id.drbutton2);
        Button drbutton3 = (Button)findViewById(R.id.drbutton3);
        TextView feedbackText = (TextView)findViewById(R.id.feedbacktextdr);

        String docName1 = randArrayTitle(doctorNames);
        String docName2 = "";
        String docName3 = "";
        do{
            docName2 = randArrayTitle(doctorNames);
        }while(docName2.equals(docName1));
        do{
            docName3 = randArrayTitle(doctorNames);
        }while (docName3.equals(docName1) || docName3.equals(docName2));

        double costToTreat = mainPerson.getSickness().getCostToTreat();

        drbutton1.setText(docName1 + " " + formatToCurrency(costToTreat/2));
        drbutton2.setText(docName2 + " " + formatToCurrency(costToTreat));
        drbutton3.setText(docName3 + " " + formatToCurrency(costToTreat * 2));


    }

    private void curePerson(Person mainPerson, int doctorChosen) {
        RelativeLayout doctorPopup = (RelativeLayout)findViewById(R.id.doctorPopup);
        int randomInt = randomNumberInBetweenMaxMin(1, 10);
        double costToTreat = mainPerson.getSickness().getCostToTreat();
        Toast failToast = Toast.makeText(getApplicationContext(),
                "You failed to get cured. Better luck next time!",
                Toast.LENGTH_LONG);
        Toast successToast = Toast.makeText(getApplicationContext(),
                "You were cured!", Toast.LENGTH_LONG);



        if(doctorChosen == 1){
            // 40% chance of cure
            if(randomInt <= 4){
                mainPerson.setSickness(null);
                successToast.show();
                doctorPopup.setVisibility(View.GONE);
            }else{
                failToast.show();
                doctorPopup.setVisibility(View.GONE);
            }

            mainPerson.setBankBalance(mainPerson.getBankBalance() - (costToTreat/2));
        }
        else if(doctorChosen == 2){
            if(randomInt <= 6){
                mainPerson.setSickness(null);
                successToast.show();
                doctorPopup.setVisibility(View.GONE);
            }else{
                failToast.show();
                doctorPopup.setVisibility(View.GONE);
            }
            mainPerson.setBankBalance(mainPerson.getBankBalance() - (costToTreat));
        }
        else if(doctorChosen == 3){
            if(randomInt <= 8){
                mainPerson.setSickness(null);
                successToast.show();
                doctorPopup.setVisibility(View.GONE);
            }
            else
            {
                failToast.show();
                doctorPopup.setVisibility(View.GONE);
            }
            mainPerson.setBankBalance(mainPerson.getBankBalance() - (costToTreat*2));
        }

        Button bankButton = (Button)findViewById(R.id.bankView);
        bankButton.setText("Bank Account: \n" + formatToCurrency(mainPerson.getBankBalance()));

    }
}
