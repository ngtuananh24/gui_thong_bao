package com.example.smartphone_gv;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ListView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Thongbao"); // Cấu trúc chung
    EditText edt_tieude, edt_noidung;
    Button btn_send;
    Spinner spinner;
    ListView view_tb;
    ArrayList<ThongBao> list = new ArrayList<>();
    ArrayAdapter<ThongBao> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        edt_tieude = findViewById(R.id.edt_tieude);
        edt_noidung = findViewById(R.id.edt_noidung);
        btn_send = findViewById(R.id.btn_send);
        view_tb = findViewById(R.id.view_tb);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        view_tb.setAdapter(adapter);

        String[] options = {"Nguyễn Tuấn Anh", "Lý Thành An", "ALL"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Người nhận: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tieude = edt_tieude.getText().toString();
                String noidung = edt_noidung.getText().toString();
                String pick = spinner.getSelectedItem().toString();
                // Lấy thời gian hiện tại và định dạng theo dạng HH:mm:ss
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(new Date());  // Định dạng thời gian
                if (tieude.isEmpty() || noidung.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                ThongBao tb = new ThongBao(pick, tieude, noidung, time);

                if (pick.equals("ALL")) {
                    // Nếu chọn ALL, gửi thông báo cho tất cả mọi người
                    sendNotificationToAll(tb);
                } else {
                    // Gửi thông báo cho người được chọn
                    sendNotificationToUser(pick, tb);
                }
            }
        });
    }

    // Hàm gửi thông báo đến tất cả người dùng
    private void sendNotificationToAll(ThongBao tb) {
        // Gửi thông báo vào node "ALL"
        myRef.child("ALL").push().setValue(tb).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();
                // Thêm thông báo vào danh sách và cập nhật giao diện
                list.add(0, tb);
                adapter.notifyDataSetChanged();  // Cập nhật ListView
            } else {
                Toast.makeText(MainActivity.this, "Gửi thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm gửi thông báo đến người nhận cụ thể
    private void sendNotificationToUser(String pick, ThongBao tb) {
        DatabaseReference userRef = myRef.child(pick); // Tạo node với tên người nhận
        userRef.push().setValue(tb).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();
                // Thêm thông báo vào danh sách và cập nhật giao diện
                list.add(0, tb);
                adapter.notifyDataSetChanged();  // Cập nhật ListView
            } else {
                Toast.makeText(MainActivity.this, "Gửi thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
