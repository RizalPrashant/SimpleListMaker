package com.prashantrizal.android.testingoverflowmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREF";
    EditText user_input;
    ArrayList<String> list_of_items;
    ArrayAdapter adapter;
    ListView listView;
    static int pos;
     Button button_delete;
     Button button_unmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_of_items = getArray();

        button_delete = (Button) findViewById(R.id.button_delete);
        button_unmark = (Button) findViewById(R.id.button_unmark);
        button_delete.setVisibility(View.GONE);
        button_unmark.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_checked();
                hide_and_show_buttons();
            }
        });
        button_unmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheck_all();
                hide_and_show_buttons();
            }
        });

         adapter = new ArrayAdapter<String>(this, R.layout.listview, list_of_items);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hide_and_show_buttons();
            }
        });
    }

        public void hide_and_show_buttons(){
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            for (int i = 0; i < list_of_items.size(); i++) {
                if (checked.get(i)) {
                    button_delete.setVisibility(View.VISIBLE);
                    button_unmark.setVisibility(View.VISIBLE);
                    button_delete.setEnabled(true);
                    button_unmark.setEnabled(true);
                    return;
                } else {
                    button_delete.setVisibility(View.GONE);
                    button_unmark.setVisibility(View.GONE);
                }
            }
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Remove");
        menu.add("Rename");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        if (item.getTitle().equals("Remove")) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            list_of_items.remove(menuInfo.position);
            Toast.makeText(getApplicationContext(), "Removed 1 item", Toast.LENGTH_SHORT).show();
            listView.invalidateViews();
            saveArray();
            hide_and_show_buttons();


        }
        if (item.getTitle().equals("Rename")) {
            final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final AlertDialog.Builder ren_dialog = new AlertDialog.Builder(this).setTitle("Rename").setMessage("Rename the item");
            user_input = new EditText(this);
            ren_dialog.setView(user_input);
            ren_dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    list_of_items.set(menuInfo.position, user_input.getText().toString());
                    listView.invalidateViews();

                    Toast.makeText(getApplicationContext(), "Renamed", Toast.LENGTH_SHORT).show();
                }
            });
            ren_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Not added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            ren_dialog.show();
            saveArray();
            hide_and_show_buttons();

        }

        return true;
    }

    public void removeAll() {
        final AlertDialog.Builder dialog2 = new AlertDialog.Builder(this).setTitle("Remove everything!!!").setMessage("Are you sure you want to remove everyting?");
        dialog2.setNegativeButton("No, Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "No changes made", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog2.setPositiveButton("Yes, I m sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                list_of_items.clear();
//                recreate();
                list_of_items.clear();
                listView.invalidateViews();
                Toast.makeText(getApplicationContext(), "Removed everything", Toast.LENGTH_SHORT).show();
            }
        });
        dialog2.show();
        saveArray();
        button_delete.setVisibility(View.GONE);
        button_unmark.setVisibility(View.GONE);


    }

    public void addItem() {
        final AlertDialog.Builder myDialog = new AlertDialog.Builder(this).setTitle("Add").setMessage("Add an item");
        user_input = new EditText(this);
        myDialog.setView(user_input);
        myDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list_of_items.add(user_input.getText().toString());
                listView.invalidateViews();

                Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
            }
        });
        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Not added!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        myDialog.show();
        saveArray();
        hide_and_show_buttons();

    }

    public void uncheck_all(){
        for (int i = 0; i < listView.getCount(); i++) {
            //Replace R.id.checkbox with the id of CheckBox in your layout
            listView.setItemChecked(i, false);
        }
        hide_and_show_buttons();

    }
    public void remove_checked(){
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                adapter.remove(list_of_items.get(i));
            }
        }
        checkedItemPositions.clear();
        adapter.notifyDataSetChanged();
        hide_and_show_buttons();

    }

//        SparseBooleanArray checked = listView.getCheckedItemPositions();
//        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
//            if(checked.get(i)){
//                list_of_items.remove(i);
//            }
//        }
//        saveArray();
//        listView.invalidateViews();
//        Toast.makeText(getApplicationContext(), "Removed all checked items", Toast.LENGTH_SHORT).show();
//        listView.invalidateViews();
//        saveArray();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.remove_menu:
                removeAll();
                return true;
            case R.id.add_menu:
                addItem();
                return true;
            case R.id.phone_number:
                getNumber();
                return true;
            case R.id.uncheck_all_menu:
                uncheck_all();
                return true;
            case R.id.remove_checked_menu:
                remove_checked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
        public String makeStr(){

//            String init_str = "";
            StringBuffer sb = new StringBuffer();
            for (int i =0 ; i< list_of_items.size(); i++){
                sb.append("[");
                sb.append(i+1);
                sb.append("] ");
                sb.append(list_of_items.get(i));
                sb.append("\n");
//             init_str += "[" + i + "]  " + list_of_items.get(i) + "\n";
            }
            String appended_string = sb.toString();
           return appended_string; //String modify_list_format =
        }
        public void getNumber(){
            String message_list = makeStr();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message_list);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
//            final AlertDialog.Builder myDialog = new AlertDialog.Builder(this).setTitle("Enter Phone Number").setMessage("Please enter a number to send your grocery list");
//            user_input = new EditText(this);
//            myDialog.setView(user_input);
//            myDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO call sms manager to send the text message.
//                    String message_list = list_of_items.toString();
//                    String phone_number = user_input.getText().toString();
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(phone_number,null,message_list, null, null);
//                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
//                }
//            });
//            myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(), "Text not sent!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            });
//            myDialog.show();
        }
    public boolean saveArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list_of_items);
        mEdit1.putStringSet("list", set);
        return mEdit1.commit();
    }

    public ArrayList<String> getArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);

        //NOTE: if shared preference is null, the method return empty Hashset and not null
        Set<String> set = sp.getStringSet("list", new HashSet<String>());

        return new ArrayList<String>(set);
    }
    public void onStop() {
        saveArray();
        super.onStop();
    }
}
