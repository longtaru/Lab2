package mob.longnd.lab2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mob.longnd.lab2.DAO.TaskInfoDAO;
import mob.longnd.lab2.MainActivity;
import mob.longnd.lab2.R;
import mob.longnd.lab2.model.TaskInfo;

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.ViewHolderInfo>{
    Context context;
    ArrayList<TaskInfo> list;
    TaskInfoDAO taskInfoDAO;

    public TaskInfoAdapter(Context context, ArrayList<TaskInfo> list) {
        this.context = context;
        this.list = list;
        taskInfoDAO = new TaskInfoDAO(context);
    }

    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_info,parent,false);
        return new ViewHolderInfo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInfo holder, @SuppressLint("RecyclerView") int position) {
        holder.tvContent.setText(list.get(position).getContent());
        holder.tvContent.setText(list.get(position).getContent());

        if(list.get(position).getStatus() ==1){
            holder.chkTask.setChecked(true);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.chkTask.setChecked(false);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean checkRS = taskInfoDAO.updateTypeInfo(id,holder.chkTask.isChecked());
                if(checkRS ){
                    Toast.makeText(context, "update thành công rồi", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Khoong thành công", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list =taskInfoDAO.getListInfo();
                notifyDataSetChanged();
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= list.get(holder.getAdapterPosition()).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa thông tin");
                builder.setIcon(R.drawable.warning);
                builder.setMessage("Bạn có chắc chắn muốn xóa thông tin này không");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context.getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        boolean check =taskInfoDAO.removeInfo(id);
                        if (check){
                            Toast.makeText(context.getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            list.clear();
                            list =taskInfoDAO.getListInfo();
                            notifyItemRemoved(holder.getAdapterPosition());
                        }else {
                            Toast.makeText(context.getApplicationContext(), "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View view1 = inflater.inflate(R.layout.custom_dialog_task_info,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view1);
                EditText edID,edTile,edContent,edDate,edType;
                edID=view1.findViewById(R.id.edID);
                edType=view1.findViewById(R.id.edType);
                edTile=view1.findViewById(R.id.edTitle);
                edContent=view1.findViewById(R.id.edContent);
                edDate=view1.findViewById(R.id.edDate);

                edID.setText(String.valueOf(list.get(position).getId()));
                edTile.setText(list.get(position).getTitle());
                edContent.setText(list.get(position).getContent());
                edDate.setText(list.get(position).getDate());
                edType.setText(list.get(position).getType());
                edType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] arrType= {"khó","tường","dễ"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                builder.setTitle("Cập nhật info");
                builder.setIcon(R.drawable.info);
                builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TaskInfo info = new TaskInfo();
                        info.setId(Integer.parseInt(edID.getText().toString()));
                        info.setTitle(edTile.getText().toString());
                        info.setContent(edContent.getText().toString());
                        info.setDate(edDate.getText().toString());
                        info.setType(edType.getText().toString());


                        long check = taskInfoDAO.updateInfo(info);
                        if(check <0){
                            Toast.makeText(context, "Không Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        }

                        list.set(position,info);
                        notifyItemChanged(holder.getAdapterPosition());

                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder{
        TextView tvContent,tvDate;
        CheckBox chkTask;
        ImageView imgUpdate,imgDelete;

        public ViewHolderInfo(@NonNull View itemView) {
            super(itemView);
            tvContent=itemView.findViewById(R.id.tvContent);
            tvDate=itemView.findViewById(R.id.tvDate);
            chkTask=itemView.findViewById(R.id.chkTask);
            imgUpdate=itemView.findViewById(R.id.imgUpdate);
            imgDelete=itemView.findViewById(R.id.imgDelete);
        }
    }
}
