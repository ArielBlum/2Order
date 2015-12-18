package com.example.zakiva.tworder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class new_order_screen extends AppCompatActivity {

    private static final String TAG = ">>>>debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_screen);

        Log.i(TAG, "on create in new order screen .. ");

        Bundle extras = getIntent().getExtras();
        EditText customerPhone = (EditText) findViewById(R.id.customerPhoneInput);
        EditText customerName = (EditText) findViewById(R.id.customerNameInput);
        Intent i = this.getIntent();
        if(i.hasExtra("phone")){
            customerPhone.setText(i.getStringExtra("phone"));
            customerName.setText(i.getStringExtra("name"));
        }


        ParseRelation relation = ParseUser.getCurrentUser().getRelation("customers");
        ParseQuery query2 = relation.getQuery();
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> customers, ParseException e) {
                if (e == null) {

                    List<String> cust = new ArrayList<String>();
                    for (ParseObject po : customers) {
                        String id = po.getString("name");
                        cust.add(id);
                    }
                    AutoCompleteTextView actv;
                    actv = (AutoCompleteTextView) findViewById(R.id.customerNameInput);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(new_order_screen.this, android.R.layout.simple_list_item_1, cust);
                    actv.setAdapter(adapter);
                    /*
                    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(new_order_screen.this, android.R.layout.select_dialog_item, cust);

                    final Button button = (Button) findViewById(R.id.button_search_contact);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            new AlertDialog.Builder(new_order_screen.this)
                                    .setTitle("Your customers:")
                                    .setSingleChoiceItems(dataAdapter, 1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ListView lw = ((AlertDialog) dialog).getListView();
                                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                            //Log.d("aaa: " , checkedItem.toString());
                                            for (ParseObject po : customers) {
                                                String id2 = po.getString("name") + " " + po.getString("phone");
                                                if (id2.equals(checkedItem.toString())) {
                                                    EditText cust_phone = (EditText) findViewById(R.id.customerPhoneInput);
                                                    cust_phone.setText(po.getString("phone"));
                                                    //Log.d("aaa:", "kkkkk");
                                                    //EditText cust_name = (EditText) findViewById(R.id.customerNameInput);
                                                }

                                            }

                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                    });
                    */

                    EditText sv = (EditText) findViewById(R.id.customerPhoneInput);
                    sv.addTextChangedListener(new TextWatcher() {
                        int is_delete = 0;
                        String cur;
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            int cnt = 0;
                            for (ParseObject po : customers) {
                                if (po.getString("phone").equals(s.toString())) {
                                    EditText name = (EditText) findViewById(R.id.customerNameInput);
                                    name.setText(po.getString("name"));
                                    //name.setEnabled(false);
                                    //name.setFocusable(false);
                                    cnt = 1;
                                    is_delete = 1;
                                    cur = po.getString("name");
                                }
                            }
                            if (cnt == 0) {
                                EditText name = (EditText) findViewById(R.id.customerNameInput);
                                //name.setEnabled(true);
                                //name.setFocusable(true);
                                if (is_delete == 1) {
                                    if (name.getText().toString().equals(cur)) {
                                        name.setText("");
                                    }
                                    is_delete = 0;
                                }
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            // TODO Auto-generated method stub
                        }
                    });


                } else {
                    Log.d("Post retrieval", "Error: " + e.getMessage());
                }
            }
        });
    }



            public void onCreateNewOrder(View view) {
                final EditText customerPhone = (EditText) findViewById(R.id.customerPhoneInput);
                final EditText orderNumber = (EditText) findViewById(R.id.orderNumberInput);
                final EditText orderDetails = (EditText) findViewById(R.id.orderDetailsInput);
                final EditText customerName = (EditText) findViewById(R.id.customerNameInput);
                final RatingBar orderUrgent = (RatingBar) findViewById(R.id.setUrgentBar);

                int prior = (int) orderUrgent.getRating();

                String customer_phone = customerPhone.getText().toString();
                String order_number = orderNumber.getText().toString();
                String order_details = orderDetails.getText().toString();
                String customer_name = customerName.getText().toString();

                create_new_order(customer_phone, customer_name, order_number, order_details, prior);
            }

            public void create_new_order(final String phone, final String customer_name, String code, String details, int prior) {
                ParseObject order = new ParseObject("Order");
                order.put("business_user", ParseUser.getCurrentUser());
                order.put("business_id", ParseUser.getCurrentUser().getObjectId());
                order.put("business_name", ParseUser.getCurrentUser().getString("name"));
                order.put("business_address", ParseUser.getCurrentUser().getString("address"));
                order.put("customer_phone", phone);
                order.put("customer_name", customer_name);
                order.put("code", code);
                order.put("details", details);
                order.put("prior", prior);
                order.put("status", "In Progress");
                order.put("history", "no");
                Log.i(TAG, "before save1");
                Log.i(TAG, "before save2");
                order.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i(TAG, "handle_customer(phone);");
                            handle_customer(phone, customer_name);

                            Intent i = new Intent(new_order_screen.this, business_orders__screen.class);
                            startActivity(i);
                        } else {
                            Log.i(TAG, "e is not null");
                            Log.i(TAG, String.format("%s", e.toString()));
                        }
                    }
                });
            }

            public static void handle_customer(final String phone, final String name) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Customer");
                query.whereEqualTo("phone", phone);
                query.whereEqualTo("business_id", ParseUser.getCurrentUser().getObjectId().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                                           @Override
                                           public void done(List<ParseObject> customers,
                                                            ParseException e) {
                                               if (e == null) {
                                                   if (customers.size() == 0) {//add new customer
                                                       final ParseObject customer = new ParseObject("Customer");
                                                       customer.put("phone", phone);
                                                       customer.put("name", name);
                                                       customer.put("business_id", ParseUser.getCurrentUser().getObjectId().toString());
                                                       customer.put("orders_counter", 1);
                                                       Log.i(TAG, " customer.saveInBackground() .. ");

                                                       customer.saveInBackground(new SaveCallback() {
                                                           public void done(ParseException e) {
                                                               if (e == null) {
                                                                   ParseUser user = ParseUser.getCurrentUser();
                                                                   ParseRelation<ParseObject> relation = user.getRelation("customers");
                                                                   relation.add(customer);
                                                                   user.saveInBackground();
                                                               } else {
                                                               }
                                                           }
                                                       });
                                                   } else {//customer already exists
                                                       ParseObject customer = customers.get(0);
                                                       customer.put("orders_counter", customer.getInt("orders_counter") + 1);
                                                       customer.put("name", name);
                                                       customer.saveInBackground();
                                                   }
                                               } else {
                                                   Log.d("Post retrieval", "Error: " + e.getMessage());
                                               }
                                           }
                                       }
                );
            }


    public void search_contact_clicked(View view) {
        Intent intent = new Intent(new_order_screen.this, customers_search.class);
        startActivity(intent);
    }
}
