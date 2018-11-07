package com.youngonessoft.android.actiondirecte.logbookmodule.ascentpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;

/**
 * Created by Bobek on 01/02/2018.
 */

public class AscentAdapter extends CursorAdapter {

    public AscentAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item_description, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        // Find fields to populate in inflated template
        final TextView listItemText = view.findViewById(R.id.list_item_desc_text);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AscentEntry.COLUMN_ASCENTTYPENAME));
        // Populate fields with extracted properties
        listItemText.setText(body);

        final String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AscentEntry.COLUMN_DESCRIPTION));

        ImageView infoButton = view.findViewById(R.id.list_item_desc_info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = listItemText.getContext();

                AlertDialog.Builder deleteAlert = descriptionAlert(description, mContext);

                deleteAlert.show();
            }
        });
    }

    public AlertDialog.Builder descriptionAlert(final String description, final Context context) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //alert.setTitle("Alert!!");
        alert.setMessage(description);
        alert.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return (alert);

    }
}