package com.juangm.myscrumboard_android.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juangm.myscrumboard_android.MainActivity;
import com.juangm.myscrumboard_android.MainStages;
import com.juangm.myscrumboard_android.R;
import com.juangm.myscrumboard_android.models.Card;

import java.util.List;

//Cards adapter to show every card in the corresponding list
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{

    private List<Card> mCards;

    public CardsAdapter(List<Card> cards) {
        mCards = cards;
        Log.i("","card set");
    }

    //Define the fields of every card item in the list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public TextView assign;
        public TextView wTime;
        public ImageView ATime;
        public ImageView delete;
        public ImageView edit;
        public LinearLayout leftColor;
        public Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.card_description);
            leftColor = (LinearLayout) itemView.findViewById(R.id.card_left_color);
            btn = (Button) itemView.findViewById(R.id.move);
            assign = (TextView) itemView.findViewById(R.id.assign);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            ATime = (ImageView) itemView.findViewById(R.id.imageView3);
            wTime = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Card card = mCards.get(position);

        TextView description = viewHolder.description;
        TextView assign = viewHolder.assign;
        TextView time = viewHolder.wTime;
        time.setText(card.getTime().toString()+'h');
        description.setText(card.getContent());
        assign.setText(card.getOwner());
        LinearLayout leftColor = viewHolder.leftColor;

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).moveCardDialog(card);
            }
        });
        viewHolder.ATime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).moveCardDialog(card);
            }
        });
        viewHolder.assign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).Assign(card);
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).editCardDialog(card);
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).deleteCardDialog(card);
            }
        });
        viewHolder.ATime.setOnClickListener(new View.OnClickListener() {
            //@SuppressWarnings("deprecation")
            public void onClick(final View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("Log Time");

                // Setting Dialog Message
                alertDialog.setMessage("Hours you worked");
                final EditText input = new EditText(view.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.key);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                ((MainActivity) view.getContext()).logTimeCard(card,card.getTime()+Integer.parseInt(input.getText().toString()));
                                dialog.cancel();
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }

        });



        if(card.getCategory().equals("ready")) {
            leftColor.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.readyColor));
        } else if(card.getCategory().equals("inprogress")) {
            leftColor.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.inprogressColor));
        } else if(card.getCategory().equals("paused")) {
            leftColor.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.pausedColor));
        } else if(card.getCategory().equals("testing")) {
            leftColor.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.testingColor));
        } else if(card.getCategory().equals("done")) {
            leftColor.setBackgroundColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.doneColor));
        }

        MainStages application =
                (MainStages) viewHolder.itemView.getContext().getApplicationContext();

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogLightTheme);
                builder.setTitle(v.getResources().getString(R.string.choose_action))
                        .setItems(R.array.actions_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0) {
                                    //Move card to another category (column)
                                    ((MainActivity) view.getContext()).moveCardDialog(card);
                                } else if(which == 1) {
                                    //Edit card
                                    ((MainActivity) view.getContext()).editCardDialog(card);
                                } else if(which == 2) {
                                    //Delete card
                                    ((MainActivity) view.getContext()).deleteCardDialog(card);
                                }
                            }
                        })
                        .show();
                return true;
            }
        });


        }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mCards.size();
    }

}
