package com.itfyme.collegesystem.activity.State;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itfyme.collegesystem.R;
import com.itfyme.collegesystem.activity.BaseActivity;
import com.itfyme.collegesystem.dbservices.StateService;
import com.itfyme.collegesystem.interfaces.ResponseHandler;

import java.util.HashMap;

public class StateAddActivity extends BaseActivity {
    private EditText edtTxtStateName;
    private EditText edtTxtStateCode;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_add);
        edtTxtStateName = (EditText) findViewById(R.id.list);
        edtTxtStateCode = (EditText) findViewById(R.id.list);
        btnSubmit = (Button) findViewById(R.id.list);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });
    }

    private void postData() {
        String name=edtTxtStateName.getText().toString();
        String code=edtTxtStateCode.getText().toString();
        HashMap<String,String> params=new HashMap<>();
        params.put("name",name);
        params.put("code",code);
        new StateService(this).addState(params, new ResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                Toast.makeText(StateAddActivity.this,"State added successfully",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFail(Object data) {
                Toast.makeText(StateAddActivity.this,"Error while adding state",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNoData(Object data) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent();
            setResult(AppCompatActivity.RESULT_CANCELED, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}