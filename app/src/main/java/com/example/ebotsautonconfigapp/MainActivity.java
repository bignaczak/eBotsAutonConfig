package com.example.ebotsautonconfigapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Context myContext;
    private Switch allianceSwitch;
    private Switch fieldSideSwitch;
    private Switch delayedStartSwitch;
    private Switch foundationSwitch;
    private TextView statusText;
    private ImageView sideImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String logTag = "BTI_onCreateMainAct";
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myContext = this;

        allianceSwitch = (Switch) findViewById(R.id.allianceSwitch);
        fieldSideSwitch = (Switch) findViewById(R.id.fieldsideSwitch);
        delayedStartSwitch = (Switch) findViewById(R.id.delaySwitch);
        foundationSwitch = (Switch) findViewById(R.id.foundationSwitch);
        statusText = (TextView) findViewById(R.id.writeStatus);
        sideImage = (ImageView) findViewById(R.id.imageView);
        statusText.setText("Configuration not saved");

        updateSideImage();

        Button saveConfigButton = findViewById(R.id.saveButton);
        saveConfigButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Intent myIntent = new Intent(view.getContext(), DayThree.class);
                //startActivityForResult(myIntent,0);
                Log.d(logTag, "Save button pressed");

                String allianceString = getSwitchStringValue(allianceSwitch);
                String fieldSideString = getSwitchStringValue(fieldSideSwitch);
                String delayedStartString = getSwitchStringValue(delayedStartSwitch);
                String foundationString = getSwitchStringValue(foundationSwitch);

                String configOptions = allianceString + " | " + fieldSideString + " | " +
                        delayedStartString + " | " + foundationString;
                Log.d(logTag, "About to write file with these options: " + configOptions);
                String fileName = eBotsConfigIO.writeConfigFile(configOptions);
                Log.d(logTag, "File " + fileName + " written");
                statusText.setText("Configuration SAVED!\n" + configOptions);
            }
        });

        allianceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(logTag, "Captured checked change event");
                updateSideImage();
                Log.d(logTag, "Completed check change, Alliance: " + getSwitchStringValue(allianceSwitch));
            }
        });

        fieldSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSideImage();
            }
        });

    }

    private  String getSwitchStringValue(Switch switchIn){
        CharSequence returnString = (switchIn.isChecked()) ? switchIn.getTextOn() : switchIn.getTextOff();
        return returnString.toString();
    }

    private void updateSideImage(){
        boolean isRed;
        boolean isQuarry;
        Drawable d;

        if (getSwitchStringValue(allianceSwitch).equalsIgnoreCase("RED")) {
            isRed = true;
        } else{
            isRed = false;
        }

        if (getSwitchStringValue(fieldSideSwitch).equalsIgnoreCase("QUARRY")){
            isQuarry = true;
        } else{
            isQuarry = false;
        }

        if (isQuarry && isRed) {
            d = getResources().getDrawable(R.drawable.quarry_red, myContext.getTheme());
        } else if(isQuarry && !isRed){
            d = getResources().getDrawable(R.drawable.quarry_blue, myContext.getTheme());
        } else if (isRed){
            d=getResources().getDrawable(R.drawable.foundation_red, myContext.getTheme());
        }else {
            d=getResources().getDrawable(R.drawable.foundation_blue, myContext.getTheme());
        }
        sideImage.setImageDrawable(d);
    }

}
