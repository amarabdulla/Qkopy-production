package com.example.computer.qkopy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by computer on 3/3/2017.
 */

public class SettingsPage extends AppCompatActivity {
    ListView list;
    CharSequence[] items = {" Public "," Contacts "," Me "};
    AlertDialog levelDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        list=(ListView)findViewById(R.id.list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String[] values = new String[] { "Profile Photo Privacy", "Deactivate",
                "About",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        list.setAdapter(adapter);


        // ListView Item Click Listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                switch(itemPosition) {
                    case 0:
                        dialog();
                        break;
                    case  1:
                        break;
                    case 2:
                        break;
                }

                // ListView Clicked item value
                String  itemValue    = (String) list.getItemAtPosition(position);

                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//                        .show();

            }

        });
    }
    private void dialog(){
        final Dialog dialog = new Dialog(SettingsPage.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPage.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Choose Privacy");
        dialog.setCancelable(true);
        // there are a lot of settings, for dialog, check them all out!
        // set up radiobutton
        RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_ome);
        RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
        RadioButton rd3 = (RadioButton) dialog.findViewById(R.id.rd_3);
        Button okbtn=(Button)dialog.findViewById(R.id.okbtn);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rd1.setChecked(true);
        // now that the dialog is set up, it's time to show it
        dialog.show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
