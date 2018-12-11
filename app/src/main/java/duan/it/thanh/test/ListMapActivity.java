package duan.it.thanh.test;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import duan.it.thanh.test.dao.MapDAO;
import duan.it.thanh.test.model.Maps;

public class ListMapActivity extends AppCompatActivity {
    private ListView list_test;
    MapDAO mapDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapDAO = new MapDAO(ListMapActivity.this);
        list_test = findViewById(R.id.lv_list);
        final List<Maps> students = mapDAO.getMapAll();
        CustomPrs customPrs = new CustomPrs(this,students);
        list_test.setAdapter(customPrs);
    }
    class CustomPrs extends BaseAdapter
    {
        LayoutInflater inflater;
        List<Maps> student;
        CustomPrs(Context context, List<Maps> students)
        {
            inflater = (LayoutInflater)(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            student = students;
        }
        @Override
        public int getCount() {
            return student.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v =  inflater.inflate(R.layout.map_custom,null);
            TextView stt = v.findViewById(R.id.stt);
            TextView vs = v.findViewById(R.id.va);
            TextView v1s = v.findViewById(R.id.v1a);
            TextView name = v.findViewById(R.id.titlea);
            String mstt = String.valueOf(student.get(position).getId());
            stt.setText(mstt);
            vs.setText(student.get(position).getV());
            v1s.setText(student.get(position).getV1());
            name.setText(student.get(position).getTiltles());
            return v;
        }
    }
}
