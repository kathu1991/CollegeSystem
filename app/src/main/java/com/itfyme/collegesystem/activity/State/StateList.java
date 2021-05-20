package com.itfyme.collegesystem.activity.State;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itfyme.collegesystem.R;
import com.itfyme.collegesystem.dbservices.StateService;
import com.itfyme.collegesystem.helpers.NetworkUtility;
import com.itfyme.collegesystem.interfaces.ResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class StateList extends AppCompatActivity {
    private JSONArray stateArr;
    private int pageNum;
    private String totalRec = "";
    private TextView txtTotalRec;
    private RecyclerView recyclerView;
    private FloatingActionButton actionBtn;
    StateAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_state_list);
            recyclerView = (RecyclerView) findViewById(R.id.list);
            txtTotalRec = (TextView) findViewById(R.id.totalRecords);
            actionBtn = (FloatingActionButton) findViewById(R.id.fab);
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StateList.this, StateAddActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            initDataSet();
            initListView();
            getStateListByPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // initializing data set
    private void initDataSet() {
        stateArr = new JSONArray();
        pageNum = 1;
    }

    // initializing listview
    private void initListView() {
        try {
            stateAdapter = new StateAdapter(stateArr);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager maneger = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(maneger);
            recyclerView.setAdapter(stateAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getting data from webservice
    private void getStateListByPage() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("page_num", String.valueOf(pageNum));
            params.put("page_size", NetworkUtility.numOfRecords);
            new StateService(this).getStateListByPage(params, new ResponseHandler() {
                @Override
                public void onSuccess(Object data) {
                    try {
                        JSONObject obj = new JSONObject(data.toString());
                        JSONObject pageObj = obj.optJSONObject("pages");
                        totalRec = pageObj.optString("total_records");
                        txtTotalRec.setText("Total Records: " + totalRec);
                        JSONArray arr = obj.optJSONArray("data");
                        stateArr = NetworkUtility.mergeArray(stateArr, arr);
                        showListView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(Object data) {

                }

                @Override
                public void onNoData(Object data) {


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to refresh listview
    private void showListView() {
        try {
            stateAdapter.setData(stateArr);
            stateAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // getting next page data
    private void getNextPage(int position) {
        try {
            if (position == stateArr.length() - 1) {
                pageNum++;
                getStateListByPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getting result back after add and refreshing the list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && requestCode == Activity.RESULT_OK) {
                initDataSet();
                getStateListByPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adapter calss
    private class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
        private JSONArray dataSource;

        public StateAdapter(JSONArray listdata) {
            this.dataSource = listdata;
        }

        public void setData(JSONArray listdata) {
            this.dataSource = listdata;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.template_state_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                final JSONObject obj = dataSource.optJSONObject(position);
                final String name = obj.optString("name");
                final String id = obj.optString("id");
                holder.stateName.setText(name);
                holder.stateID.setText(id);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                getNextPage(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dataSource.length();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            public TextView stateName, stateID;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                this.stateName = (TextView) itemView.findViewById(R.id.txtStateName);
                this.stateID = (TextView) itemView.findViewById(R.id.txtStateId);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.mainLay);
            }
        }
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