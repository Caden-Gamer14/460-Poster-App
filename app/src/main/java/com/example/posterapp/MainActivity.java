package com.example.posterapp;

import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostersListener {

    private Button buttonAddToWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**
         * Allows for the posters to be implemented with the other files
         */
        RecyclerView postersRecyclerView = findViewById(R.id.postersRecyclerView);

        buttonAddToWatchlist = findViewById(R.id.buttonAddToWatchlist);

        //prepare data

        List<Poster> posterList = new ArrayList<>();

        /**
         * Creates the actual poster object to implement
         */
        Poster curseOfTheFallen = new Poster();
        curseOfTheFallen.image = R.drawable.poster1;
        curseOfTheFallen.name = "Curse of the Fallen";
        curseOfTheFallen.createdBy = "Elite";
        curseOfTheFallen.rating = 5f;
        curseOfTheFallen.story = "An awesome cyberpunk sci-fi fantasy story involving magic and technology to stop the Lord of Shadows from taking over the realms.";
        posterList.add(curseOfTheFallen);

        Poster legacyNinja = new Poster();
        legacyNinja.image = R.drawable.poster2;
        legacyNinja.name = "Legacy of the Green Ninja";
        legacyNinja.createdBy = "Tommy Andreasen";
        legacyNinja.rating = 4f;
        legacyNinja.story = "A thrilling tale about the original defeat of evil, the final battle.";
        posterList.add(legacyNinja);

        Poster ftTournamentArc = new Poster();
        ftTournamentArc.image = R.drawable.poster3;
        ftTournamentArc.name = "The Grand Magic Games";
        ftTournamentArc.createdBy = "Hiro Mashima";
        ftTournamentArc.rating = 3f;
        ftTournamentArc.story = "A cool magic battle arc featuring some plot twists and awesome battles.";
        posterList.add(ftTournamentArc);

        Poster blackClover = new Poster();
        blackClover.image = R.drawable.poster4;
        blackClover.name = "Black Clover";
        blackClover.createdBy = "Yūki Tabata";
        blackClover.rating = 4f;
        blackClover.story = "A story about a boy with no magic who makes it into the magic knights. Little does he know there is danger lurking in the shadows.";
        posterList.add(blackClover);

        Poster blackOps3 = new Poster();
        blackOps3.image = R.drawable.poster5;
        blackOps3.name = "Black Ops 3";
        blackOps3.createdBy = "Treyarch";
        blackOps3.rating = 4f;
        blackOps3.story = "A large scale sci-fi story with an underrated story to tell about the future of the world as we know it.";
        posterList.add(blackOps3);

        Poster ftTartaros = new Poster();
        ftTartaros.image = R.drawable.poster6;
        ftTartaros.name = "Tartaros Invades";
        ftTartaros.createdBy = "Hiro Mashima";
        ftTartaros.rating = 5f;
        ftTartaros.story = "An all out assault on the final dark guild with the highest stakes. This ultimate battle could mean the end of magic as we know it, permanently!";
        posterList.add(ftTartaros);

        Poster swordOfTheWizardKing = new Poster();
        swordOfTheWizardKing.image = R.drawable.poster7;
        swordOfTheWizardKing.name = "Sword of the Wizard King";
        swordOfTheWizardKing.createdBy = "Yūki Tabata";
        swordOfTheWizardKing.rating = 4f;
        swordOfTheWizardKing.story = "A story about the untold legion of Wizard Kings. When these Wizard Kings are reawakened through unknown means, it means conflict for the present day magic knights.";
        posterList.add(swordOfTheWizardKing);

        Poster hunted = new Poster();
        hunted.image = R.drawable.poster8;
        hunted.name = "Hunted";
        hunted.createdBy = "Tommy Andreasen";
        hunted.rating = 4f;
        hunted.story = "The resistance never quits! In this action thriller, the destined one must confront his father to save his city from chaos, meanwhile his friends are in another realm fighting for their way home.";
        posterList.add(hunted);

        Poster shadowsOfEvil = new Poster();
        shadowsOfEvil.image = R.drawable.poster9;
        shadowsOfEvil.name = "Shadows of Evil";
        shadowsOfEvil.createdBy = "Treyarch";
        shadowsOfEvil.rating = 4f;
        shadowsOfEvil.story = "When our heroes are taken into the dream world by the Shadow Man, they must find their way out and complete the Easter Egg on Double XP Weekend!";
        posterList.add(shadowsOfEvil);

        Poster frogD00d = new Poster();
        frogD00d.image = R.drawable.poster10;
        frogD00d.name = "Gorf: The Movie";
        frogD00d.createdBy = "Gorf";
        frogD00d.rating = 5f;
        frogD00d.story = "The most action packed and amazing plot of all time! Involving the destined war between the Gorfs and Doats! Will Gorf be able to save his home from destruction? Or will he be too late to stop the Doats?";
        posterList.add(frogD00d);

        /**
         * Connects all of the posters added to the poster adapter
         */
        final PosterAdapter posterAdapter = new PosterAdapter(posterList, this);
        postersRecyclerView.setAdapter(posterAdapter);

        /**
         * Allows for the button to add posters to the watchlist
         */
        buttonAddToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Poster> selectPosters = posterAdapter.getSelectedPosters();

                StringBuilder posterNames = new StringBuilder();

                for (int i = 0; i < selectPosters.size(); i++) {

                    if (i == 0) {

                        posterNames.append(selectPosters.get(i).name);

                    } else {

                        posterNames.append("\n").append(selectPosters.get(i).name);

                    }
                }

                Toast.makeText(MainActivity.this, posterNames.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * Allows for the posters to be added to the watchlist
     * @param isSelected
     */
    @Override
    public void onPosterAction(Boolean isSelected) {

        if (isSelected) {

            buttonAddToWatchlist.setVisibility(View.VISIBLE);

        } else {

            buttonAddToWatchlist.setVisibility(View.GONE);

        }
    }
}