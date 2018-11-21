package com.system2override.hobbes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WelcomeScreen extends OnboardingActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        /*
        this.textView.setText("WELCOME\n\nAre you ready to reclaim your time from your phone?\n\nHobbes will help you spend your time the way you actually want to, and get more done in the real world.\n\nAll of your data remains on your phone and inaccessible to the Hobbes developers. Downtown L.A.  Noon on a hot summer day.  On an EXTREME LONG LENS the\n" +
                "lunchtime crowd stacks up into a wall of humanity.  In SLOW MOTION\n" +
                "they move in herds among the glittering rows of cars jammed bumper to\n" +
                "bumper.  Heat ripples distort the torrent of faces.  The image is\n" +
                "surreal, dreamy... and like a dream it begins very slowly to\n" +
                "\n" +
                "\t\t\t\t\t\tDISSOLVE TO:\n" +
                "\n" +
                "2\tEXT. CITY RUINS - NIGHT\n" +
                "\n" +
                "Same spot as the last shot, but now it is a landscape in Hell.  The\n" +
                "cars are stopped in rusted rows, still bumper to bumper.  The\n" +
                "skyline of buildings beyond has been shattered by some\n" +
                "unimaginable force like a row of kicked-down sandcastles.\n" +
                "Wind blows through the desolation, keening with the sound of ten\n" +
                "million dead souls.  It scurries the ashes into drifts, stark\n" +
                "white in the moonlight against the charred rubble.\n" +
                "A TITLE CARD FADES IN:\n\n");
 //       */
        this.textView.setText("WELCOME\n\nAre you ready to reclaim your time from your phone?\n\n" +
                "Hobbes will help you spend your time the way you actually want to, by helping you get more done in the real world.\n\n"
                + "All of your data remains on your phone and will always be inaccessible to the Hobbes developers. Your privacy is guaranteed.\n\n");
        this.bar.setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HowToScreen.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
