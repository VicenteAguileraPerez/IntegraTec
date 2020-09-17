package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import androidx.cardview.widget.CardView;

public class ButtonHelper
{
    @SuppressLint("ClickableViewAccessibility")
    public void actionClickButton(CardView cardView, final int color1, final int color2)
    {
        cardView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            CardView button_mostrar=(CardView) view;

            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    button_mostrar.setCardBackgroundColor(color1);
                    break;

                case MotionEvent.ACTION_UP:
                    button_mostrar.setCardBackgroundColor(color2);
                    break;
            }
            return false;
        }
    });

    }
}
