package com.juangm.myscrumboard_android;

import android.app.Application;
import com.juangm.myscrumboard_android.models.Card;
import java.util.ArrayList;

//Kanban Application instance: contains global variables, like the card arrays
public class MainStages extends Application {

    public ArrayList<Card> readyCards = new ArrayList<>();
    public ArrayList<Card> inProgressCards = new ArrayList<>();
    public ArrayList<Card> pausedCards = new ArrayList<>();
    public ArrayList<Card> testingCards = new ArrayList<>();
    public ArrayList<Card> doneCards = new ArrayList<>();
}
