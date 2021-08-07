package com.example.contactapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;

    public ContactsAdapter(Context context ,List<Contact> list){
        super(context ,R.layout.row ,list);
        this.context = context;
        this.contacts = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row ,parent ,false);

        TextView charTxtV = convertView.findViewById(R.id.charTxtV);
        TextView nameTxtV = convertView.findViewById(R.id.nameTxtV);
        TextView emailTxtV = convertView.findViewById(R.id.emailTxtV);

        charTxtV.setText(contacts.get(position).getName().toUpperCase().charAt(0) + "");
        nameTxtV.setText(contacts.get(position).getName());
        emailTxtV.setText(contacts.get(position).getEmail());

        return convertView;
    }
}
