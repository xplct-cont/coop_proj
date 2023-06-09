package com.bisu.pslat.admin;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bisu.pslat.AccountSettings;
import com.bisu.pslat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class WithdrawalRequestInformation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_request_information);
        EditText fullName = (EditText) findViewById(R.id.fullName);

        EditText paymentVal = (EditText) findViewById(R.id.payment);
        EditText paymentType = (EditText) findViewById(R.id.paymentType);
        EditText month = (EditText) findViewById(R.id.reqDate);

        Button back = (Button) findViewById(R.id.backButton);
        Button approveBtn = (Button) findViewById(R.id.approveButton);
        Button rejectBtn = (Button) findViewById(R.id.rejectButton);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar2);
        TextView barT = (TextView) findViewById(R.id.progressText);

        Intent intent = getIntent();
        String passed_username = intent.getExtras().getString("username");
        String passed_dc = intent.getExtras().getString("date_created");

        final String[] m_id = {""};
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").orderByChild("username").equalTo(AccountSettings.encode(passed_username))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot child : snapshot.getChildren()) {

                                String user_id = child.getKey();
                                mDatabase.child("withdrawal_requests").orderByChild("user_id").equalTo(user_id)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                String userpayment_type= "";
                                                for (DataSnapshot child2 : snapshot2.getChildren()) {
                                                    if(child2.child("date_created").getValue().toString().matches(passed_dc)){
                                                        m_id[0] = child2.getKey();
                                                        userpayment_type = child2.child("payment_type").getValue().toString();
                                                        fullName.setText(AccountSettings.decode(child.child("fullname").getValue().toString()));
//
                                                        paymentVal.setText(child2.child("amount").getValue().toString());
                                                        paymentType.setText(child2.child("payment_type").getValue().toString());
//                                                        months.setText(child2.child("months").getValue().toString() + " months");
                                                        month.setText(child2.child("date_created").getValue().toString());

                                                        String finalUserpayment_type = userpayment_type;
//                                                        approveBtn.setOnClickListener(new View.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                pbar.setVisibility(View.VISIBLE);
//                                                                pbar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
//                                                                ObjectAnimator.ofInt(pbar, "progress", 10000)
//                                                                        .setDuration(1000)
//                                                                        .start();
//                                                                barT.setText("Approving withdrawal request...");
//
//                                                                String dateToday = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//                                                                mDatabase.child("withdrawals").child(m_id[0]).child("date_updated").setValue(dateToday);
//                                                                mDatabase.child("withdrawals").child(m_id[0]).child("status").setValue("approved");
//                                                                mDatabase.child("withdrawals").child(user_id).child("user_id").setValue(user_id);
//                                                                barT.setText("Updating user table...");
//
//                                                                mDatabase.child("users").child(user_id).child("type").setValue("member");
//                                                                mDatabase.child("users").child(user_id).child("date_updated").setValue(dateToday);
//
//                                                                HashMap<String, Object> map = new HashMap<>();
//                                                                map.put("user_id", user_id);
//                                                                map.put("amount", child2.child("amount").getValue().toString());
//                                                                map.put("type", "Withdrawal");
//                                                                map.put("date_created", dateToday);
//
//                                                                HashMap<String, Object> map2 = new HashMap<>();
//                                                                map.put("user_id", user_id);
//                                                                map2.put("amount", child2.child("amount").getValue().toString());
//                                                                map2.put("type", "Withdrawal");
//                                                                map2.put("date_created", dateToday);
//
//                                                                barT.setText("Setting up user balance...");
//
//                                                                mDatabase.child("withdrawals").child(user_id).push().setValue(map2);
//                                                                mDatabase.child("balance").push().setValue(map)
//                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                            @Override
//                                                                            public void onSuccess(Void unused) {
//                                                                                Intent intent = new Intent(WithdrawalRequestInformation.this, WithdrawalRequests.class);
//                                                                                startActivity(intent);
//                                                                                finish();
//                                                                            }
//                                                                        });
//                                                            }
//                                                        });

                                                        approveBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                pbar.setVisibility(View.VISIBLE);
                                                                pbar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                                                                ObjectAnimator.ofInt(pbar, "progress", 10000)
                                                                        .setDuration(1000)
                                                                        .start();
                                                                barT.setText("Approving loan request...");

                                                                String dateToday = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                                                mDatabase.child("withdrawal_requests").child(m_id[0]).child("date_updated").setValue(dateToday);
                                                                mDatabase.child("withdrawal_requests").child(m_id[0]).child("status").setValue("approved");

                                                                barT.setText("Submitting loan request...");


                                                                Double withdrawalAmount = Double.parseDouble(paymentVal.getText().toString().replace("Loan Amount: ", ""));
                                                                HashMap<String, Object> map = new HashMap<>();

                                                                map.put("user_id", user_id);
                                                                map.put("amount",withdrawalAmount);
                                                                map.put("date_created", dateToday);
                                                                map.put("date_updated", dateToday);

                                                                HashMap<String, Object> map2 = new HashMap<>();
                                                                map2.put("user_id", user_id);
                                                                map2.put("amount", withdrawalAmount);
                                                                map2.put("type", "Withdrawal");
                                                                map2.put("date_created", dateToday);


                                                                mDatabase.child("withdrawals").push().setValue(map);
                                                                mDatabase.child("balance").push().setValue(map2)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(WithdrawalRequestInformation.this, "Withdrawal Request Approved", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(WithdrawalRequestInformation.this, WithdrawalRequests.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                        rejectBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                String dateToday = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                                                mDatabase.child("withdrawal_requests").child(m_id[0]).child("date_updated").setValue(dateToday);
                                                                mDatabase.child("withdrawal_requests").child(m_id[0]).child("status").setValue("rejected")
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(WithdrawalRequestInformation.this, "Request Rejected", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(WithdrawalRequestInformation.this, LoanRequests.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }
                        else {
                            Toast.makeText(WithdrawalRequestInformation.this, "User doesnt exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithdrawalRequestInformation.this, PaymentRequests.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
