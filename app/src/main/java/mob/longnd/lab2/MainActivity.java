package mob.longnd.lab2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mob.longnd.lab2.DAO.TaskInfoDAO;
import mob.longnd.lab2.adapter.TaskInfoAdapter;
import mob.longnd.lab2.model.TaskInfo;

public class MainActivity extends AppCompatActivity {
    TaskInfoDAO dao;
    ArrayList<TaskInfo> listTask;
    String Tag = "======";

    RecyclerView rcvTask;
    TaskInfoAdapter adapter;

    EditText edID,edTile,edContent,edDate,edType;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        anhXa();

        dao = new TaskInfoDAO(this);
        listTask = dao.getListInfo();
        Log.d(Tag,"onCreate"+listTask.size());
        adapter = new TaskInfoAdapter(MainActivity.this,listTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title =edTile.getText().toString().trim();
                String content =edContent.getText().toString().trim();
                String date =edDate.getText().toString().trim();
                String type =edType.getText().toString().trim();
                if(title.isEmpty()||content.isEmpty()||date.isEmpty()||type.isEmpty()){
                    Toast.makeText(MainActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                    if(title.isEmpty()){
                        edTile.setError("Nhập title");
                    }
                    if(content.isEmpty()){
                        edContent.setError("Nhập content");
                    }
                    if(date.isEmpty()){
                        edDate.setError("Nhập date");
                    }
                    if(type.isEmpty()){
                        edType.setError("Nhập type");
                    }
                }else {
                    TaskInfo info = new TaskInfo(1,title,content,date,type,0);
                    long check = dao.addInfo(info);
                    if (check < 0) {
                        Toast.makeText(MainActivity.this, "Thêm dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Thm dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                    listTask = dao.getListInfo();
                    adapter= new TaskInfoAdapter(MainActivity.this,listTask);
                    rcvTask.setLayoutManager(linearLayoutManager);
                    rcvTask.setAdapter(adapter);

                    reset();
                }
            }
        });

        edType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arrType= {"khó","tường","dễ"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chọn cấp độ");
                builder.setIcon(R.drawable.info);
                builder.setItems(arrType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edType.setText(arrType[which]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                        alertDialog.show();
            }
        });
    }

    public void anhXa() {
        rcvTask = findViewById(R.id.rcvTask);
        edID = findViewById(R.id.edID);
        edTile = findViewById(R.id.edTitle);
        edContent=findViewById(R.id.edContent);
        edDate=findViewById(R.id.edDate);
        edType=findViewById(R.id.edType);
        btnAdd=findViewById(R.id.btnADD);
    }

    public void reset() {
        edTile.setText("");
        edContent.setText("");
        edDate.setText("");
        edType.setText("");
        edID.setText("");
    }
}