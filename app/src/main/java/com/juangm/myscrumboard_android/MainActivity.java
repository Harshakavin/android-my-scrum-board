package com.juangm.myscrumboard_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
//import com.juangm.taskban_android.adapters.BoardsAdapter;
import com.juangm.myscrumboard_android.fragments.DoneCardsFragment;
import com.juangm.myscrumboard_android.fragments.InProgressCardsFragment;
import com.juangm.myscrumboard_android.fragments.PausedCardsFragment;
import com.juangm.myscrumboard_android.fragments.ReadyCardsFragment;
import com.juangm.myscrumboard_android.fragments.TestingCardsFragment;
import com.juangm.myscrumboard_android.models.Board;
import com.juangm.myscrumboard_android.models.Card;
import com.juangm.myscrumboard_android.db.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

//Main activity. Let the users see and modify the boards and cards
public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private Adapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionMenu menuCreateFab;
    private FloatingActionButton cardFab, boardFab;
    private AlertDialog.Builder logoutAltertDialog, deleteAlertDialog, alertDialog;
    private EditText cardDescription, boardName, boardDescription;
    private TextView usernameText, emailText,SboardName;
    private String userID, token, username, email;
    private List<Board> boards = new ArrayList<>();
    private ListView listView;
    private Board selectedBoard;
    private MainStages application;
    private RecyclerView boardsRecycler;
    //private BoardsAdapter boardsAdapter;
    private int adapterItemCount, selectedCategoryToMove;
    public MyDBHelper helper;
    private SharedPreferences.Editor prefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefsEditor = getSharedPreferences("SheardPreferences", MODE_PRIVATE).edit();
        try {
            this.helper = new MyDBHelper(this);
            Log.i("", "A started");
        } catch (Exception E) {
            Log.i("", E.toString());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scum);

        application = (MainStages) getApplication();
        application.readyCards.clear();
        application.inProgressCards.clear();
        application.pausedCards.clear();
        application.testingCards.clear();
        application.doneCards.clear();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My-Scrum-Board");

        mPrefs = getSharedPreferences("SheardPreferences", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        userID = mPrefs.getString("userID", "");
        username = mPrefs.getString("username", "");
        email = mPrefs.getString("email", "");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogLightTheme);
        alertDialog.setTitle(getResources().getString(R.string.error_dialog_title));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        logoutAltertDialog = new AlertDialog.Builder(this, R.style.AlertDialogLightTheme);
        logoutAltertDialog.setTitle(getResources().getString(R.string.logout_dialog_title));
        logoutAltertDialog.setMessage(getResources().getString(R.string.logout_dialog_content));
        logoutAltertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        logoutAltertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        usernameText = (TextView) findViewById(R.id.drawer_header_name);
        emailText = (TextView) findViewById(R.id.drawer_header_email);
        usernameText.setText(username);
        emailText.setText(email);

        //SboardName = (TextView) findViewById(R.id.board_name);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                prefsEditor.putString("Board",itemValue);
                prefsEditor.apply();
                getCards(itemValue, "ready");
                getSupportActionBar().setTitle(itemValue);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setupViewPager(viewPager, "ready");

            }

        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("","Deleted");
                int itemPosition = position;
                // ListView Clicked item value
                final String itemValue = (String) listView.getItemAtPosition(position);
                AlertDialog DialogBox =new AlertDialog.Builder(view.getContext())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                helper.DeleteBoard(itemValue);
                                getUserBoardAndCards();
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                DialogBox.show();
                return false;
            }
        });

        boardsRecycler = (RecyclerView) findViewById(R.id.boards_recycler);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager, "ready");
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager, "ready");
        }

        menuCreateFab = (FloatingActionMenu) findViewById(R.id.fab);
        cardFab = (FloatingActionButton) findViewById(R.id.fab_create_card);
        boardFab = (FloatingActionButton) findViewById(R.id.fab_create_board);

        cardFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show create card dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogLightTheme);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogCreateCardView = inflater.inflate(R.layout.dialog_card_create, null);
                cardDescription = (EditText) dialogCreateCardView.findViewById(R.id.edit_card_description);
                builder.setView(dialogCreateCardView)
                        .setPositiveButton(getResources().getString(R.string.create), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Positive click
                                boolean validateCardDescription = validateCardDescription();
                                if (validateCardDescription) {
                                    Card newCard = new Card(
                                            "",
                                            cardDescription.getText().toString(),
                                            "Assign",
                                            "ready",
                                             mPrefs.getString("Board",""),
                                            0);
                                    //Create card request
                                    helper.saveCard(newCard);
                                    getCards(mPrefs.getString("Board",""),"ready");
                                    setupViewPager(viewPager, "ready");
                                    Snackbar.make(mDrawerLayout, getResources().getString(R.string.card_created), Snackbar.LENGTH_LONG)
                                            .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Dismiss snackbar
                                                }
                                            })
                                            .show();
                                } else {
                                    //Validate error
                                    alertDialog.setMessage(getResources().getString(R.string.card_description_empty));
                                    alertDialog.show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel
                            }
                        })
                        .setTitle(getResources().getString(R.string.create_card_dialog_title))
                        .setMessage(getResources().getString(R.string.create_card_dialog_content))
                        .show();
            }
        });

        boardFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show create board dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogLightTheme);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogCreateBoardView = inflater.inflate(R.layout.dialog_board_create, null);
                boardName = (EditText) dialogCreateBoardView.findViewById(R.id.edit_board_name);
                boardDescription = (EditText) dialogCreateBoardView.findViewById(R.id.edit_board_description);
                builder.setView(dialogCreateBoardView)
                        .setPositiveButton(getResources().getString(R.string.create), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Positive click
                                boolean validateBoardDescription = validateBoardDescription();
                                boolean validateBoardName = validateBoardName();
                                if (validateBoardDescription && validateBoardName) {
                                    Board Board = new Board(
                                            boardName.getText().toString(),
                                            boardDescription.getText().toString(),
                                            mPrefs.getString("userID", "")
                                            );

                                    helper.saveBoard(Board);
                                    getUserBoardAndCards();
                                    Snackbar.make(mDrawerLayout, getResources().getString(R.string.board_created), Snackbar.LENGTH_LONG)
                                            .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Dismiss snackbar
                                                }
                                            })
                                            .show();


                                } else {
                                    //Validation error
                                    alertDialog.setMessage(getResources().getString(R.string.board_fields_error));
                                    alertDialog.show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.drawer_close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel
                            }
                        })
                        .setTitle(getResources().getString(R.string.create_board_dialog_title))
                        .setMessage(getResources().getString(R.string.create_board_dialog_content))
                        .show();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Get all boards and cards owned by the user
        getUserBoardAndCards();
       // getCards("","ready");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kanban, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {
            case android.R.id.home:
                //Open left panel
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_logout:
                //Show a close session alert dialog
                logoutAltertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Setup left panel
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //Set up the ViewPager with the card arrays
    private void setupViewPager(ViewPager viewPager, String category) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ReadyCardsFragment(), getResources().getString(R.string.todo));
        adapter.addFragment(new InProgressCardsFragment(), getResources().getString(R.string.inprogress));
        adapter.addFragment(new PausedCardsFragment(), getResources().getString(R.string.paused));
        adapter.addFragment(new TestingCardsFragment(), getResources().getString(R.string.testing));
        adapter.addFragment(new DoneCardsFragment(), getResources().getString(R.string.done));
        viewPager.setAdapter(adapter);
        if (category != null) {
            switchPageCategory(category);
        }
    }

    //Fragment ViewPager adapter
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //Get all boards owned by the user
    private void getUserBoardAndCards() {

        List<String> data = new ArrayList<>();
        for(Board board :  this.helper.readALLboards() ) {
            data.add(board.getName());
            //Log.i("",board.getName()+"galbanis");
        }
        Log.i("",boards.toString()+ "Got boards");
        if (data.size() > 0) {

            getSupportActionBar().setTitle(data.get(0).toString());
            prefsEditor.putString("Board",data.get(0).toString());
            prefsEditor.apply();
            getCards(mPrefs.getString("Board",""),"ready");
            Log.i("",mPrefs.getString("Board","")+"board name");

        } else {
            //Creates default board
            Board newboard = new Board(
                    getResources().getString(R.string.default_kanban_title),
                    getResources().getString(R.string.default_kanban_description),
                    mPrefs.getString("userID", ""));
            this.helper.saveBoard(newboard);
            getSupportActionBar().setTitle("My Scum board");
            prefsEditor.putString("Board","My Scum board");
            prefsEditor.apply();
            getCards(mPrefs.getString("Board",""),"ready");
            Log.i("",mPrefs.getString("Board","")+" board name");
        }
        data.clear();
        for(Board board :  this.helper.readALLboards() ) {
            data.add(board.getName());
          //  Log.i("",board.getName()+"galbanis");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, data);
        listView.setAdapter(adapter);


     }

    //Get all cards for a board and set up ViewPager
    private void getCards(String boardName, String categorySelected) {

        Log.i("",boardName+" "+categorySelected);
        List<Card> cards = this.helper.readALLcards(boardName);

                application.readyCards.clear();
                application.inProgressCards.clear();
                application.pausedCards.clear();
                application.testingCards.clear();
                application.doneCards.clear();
                //Add every card to his column or category
                for(Card card : cards ) {
                    checkCardCategoryAndAdd(card);
                }
                setupViewPager(viewPager, categorySelected);
    }

    //Close the user session
    private void logout() {

                    mPrefs.edit().clear().apply();
                    //Go to LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
    }

    //Check card category and put into the related array
    private void checkCardCategoryAndAdd(Card card) {

        switch (card.getCategory()){
            case "ready":
                application.readyCards.add(card);
                break;

            case "inprogress":
                application.inProgressCards.add(card);
                break;

            case "paused":
                application.pausedCards.add(card);
                break;

            case "testing":
                application.testingCards.add(card);
                break;

            case "done":
                application.doneCards.add(card);
                break;
        }
    }

    //Select a board and get his cards
    public void selectBoard(int position) {
        selectedBoard = boards.get(position);
        getCards(boards.get(position).get_id(), "ready");
        getSupportActionBar().setTitle(boards.get(position).getName());
        mDrawerLayout.closeDrawers();
    }

    //Launch a dialog to choose a column to move the card
    public void moveCardDialog(Card card) {
        selectedCategoryToMove = 0;
        CharSequence[] charSequence = {
                getResources().getString(R.string.todo),
                getResources().getString(R.string.inprogress),
                getResources().getString(R.string.paused),
                getResources().getString(R.string.testing),
                getResources().getString(R.string.done)};
        final Card newCard = card;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_category_dialog))
                .setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Select category
                        selectedCategoryToMove = which;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Move card to selected category
                        switch (selectedCategoryToMove) {
                            case 0:
                                moveCardToCategory(newCard, "ready");
                                break;
                            case 1:
                                moveCardToCategory(newCard, "inprogress");
                                break;
                            case 2:
                                moveCardToCategory(newCard, "paused");
                                break;
                            case 3:
                                moveCardToCategory(newCard, "testing");
                                break;
                            case 4:
                                moveCardToCategory(newCard, "done");
                                break;
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }





    public void Assign(Card card) {
        selectedCategoryToMove = 0;
        CharSequence[] charSequence = {
                "Assign to me",
                "Jhone",
                "Lasith",
                "Varuna",};
        final Card newCard = card;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Assign the task")
                .setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Select category
                        selectedCategoryToMove = which;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Move card to selected category
                        switch (selectedCategoryToMove) {
                            case 0:
                                helper.UpdateCardOwner(newCard.get_id(),mPrefs.getString("name",""));

                                break;
                            case 1:
                                helper.UpdateCardOwner(newCard.get_id(),"Jhone");

                                break;
                            case 2:
                                helper.UpdateCardOwner(newCard.get_id(),"Lasith");

                                break;
                            case 3:
                                helper.UpdateCardOwner(newCard.get_id(),"Varuna");

                                break;
                        }
                        getCards(mPrefs.getString("Board",""),"ready");
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //Move a card to a new category (column)
    private void moveCardToCategory(Card card, String category) {
        String cardID = card.get_id();
        helper.moveCard(cardID,category);
        getCards(mPrefs.getString("Board", ""), category);
        Snackbar.make(mDrawerLayout, getResources().getString(R.string.card_moved), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Dismiss snackbar
                                }
                            })
                            .show();
    }

    //Launch a dialog to edit the description
    public void editCardDialog(Card card) {
        final String cardID = card.get_id();
        final String boardID = card.getBoard_id();
        final String cardCategory = card.getCategory();

        //Show create card dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogLightTheme);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogCreateCardView = inflater.inflate(R.layout.dialog_card_create, null);
        cardDescription = (EditText) dialogCreateCardView.findViewById(R.id.edit_card_description);
        cardDescription.setText(card.getContent());
        final String initialContent = card.getContent();
        builder.setView(dialogCreateCardView)
                .setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Edit card
                        String editedContent = cardDescription.getText().toString();
                        if(!editedContent.equals(initialContent)) {
                            //Card edited

                                        helper.UpdateCardContent(cardID,editedContent);
                                        getCards(boardID, cardCategory);
                                        Snackbar.make(mDrawerLayout, getResources().getString(R.string.card_edited), Snackbar.LENGTH_LONG)
                                                .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //Dismiss snackbar
                                                    }
                                                })
                                                .show();
                                }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel
                        dialog.dismiss();
                    }
                })
                .setTitle(getResources().getString(R.string.edit_card_dialog_title))
                .setMessage(getResources().getString(R.string.edit_card_dialog_content))
                .show();
    }


    //Launch a dialog to confirm the deletion of the card
    public void deleteCardDialog(Card card) {
        Log.i("","delete called");
         final String cardID = card.get_id();
         final String boardID = card.getBoard_id();
         final String cardCategory = card.getCategory();

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        helper.DeleteCard(cardID);
                        getCards(boardID, cardCategory);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        myQuittingDialogBox.show();

    }

    public void logTimeCard(Card card,Integer time) {
        helper.UpdateCardTime(card.get_id(),time);
        getCards(card.getBoard_id(), "ready");
    }


    //Switch to the category page selected
    public void switchPageCategory(String category) {
        switch (category){
            case "ready":
                viewPager.setCurrentItem(0);
                break;

            case "inprogress":
                viewPager.setCurrentItem(1);
                break;

            case "paused":
                viewPager.setCurrentItem(2);
                break;

            case "testing":
                viewPager.setCurrentItem(3);
                break;

            case "done":
                viewPager.setCurrentItem(4);
                break;
        }
    }

    //Validate card description EditText
    private boolean validateCardDescription() {
        if(cardDescription.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //Validate board name EditText
    private boolean validateBoardName() {
        if(boardName.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //Validate Board description EditText
    private boolean validateBoardDescription() {
        if(boardDescription.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}