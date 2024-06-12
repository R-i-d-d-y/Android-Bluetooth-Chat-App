package com.example.bluetoothchatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {
 ImageView img;
 TextView text1,text2,text3;
 Button cancel,submit,browse;
 Uri uri,urI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        img = (ImageView) findViewById(R.id.profileImage11);
        text1 = (TextView) findViewById(R.id.userName);
        text2 = (TextView) findViewById(R.id.displayName);
        cancel = (Button) findViewById(R.id.cancell);
        submit = (Button) findViewById(R.id.save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent inte1 = getIntent();
        uri = (Uri) inte1.getParcelableExtra("image");
        img.setImageURI(uri);
        browse = (Button) findViewById(R.id.browzeid);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);

            }
        });

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode==RESULT_OK && null!=data) {
            uri = data.getData();
            img.setImageURI(uri);

        }
    }
    public void onClick (View view){
        Intent i2 = new Intent();
        i2.putExtra("imgg", uri);
        Bundle bundle=new Bundle();
        bundle.putString("full",text1.getText().toString());
        bundle.putString("disp",text2.getText().toString());
        i2.putExtras(bundle);
        setResult(RESULT_OK, i2);
        finish();
    }
}