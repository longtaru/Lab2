package mob.longnd.lab2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import mob.longnd.lab2.database.DBHelper;
import mob.longnd.lab2.model.TaskInfo;

public class TaskInfoDAO {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public TaskInfoDAO(Context context) {
        dbHelper = new DBHelper(context);
        database=dbHelper.getWritableDatabase();
    }

    public long addInfo(TaskInfo info) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",info.getTitle());
        values.put("content",info.getContent());
        values.put("date",info.getDate());
        values.put("type",info.getType());

        long check = database.insert("TASKS",null,values);
        if(check <= 0){
            return -1;
        }
        return 1;
    }

    public long updateInfo(TaskInfo info) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",info.getId());
        values.put("title",info.getTitle());
        values.put("content",info.getContent());
        values.put("date",info.getDate());
        values.put("type",info.getType());
        values.put("status",info.getStatus());
        long check = database.update("TASKS",values,
                "id=?",new String[]{String.valueOf(info.getId())});
        if(check <= 0){
            return -1;
        }
        return 1;
    }

    public ArrayList<TaskInfo> getListInfo() {
        ArrayList<TaskInfo> list = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        try{
            Cursor cursor = database.rawQuery("select * from TASKS",null);
            if (cursor.getCount() >0) {
                cursor.moveToFirst();
                do {
                    list.add(new TaskInfo(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5)));
                }while (cursor.moveToNext());
            }
        }catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
        }
        return list;
    }

    public boolean removeInfo(int id) {
        int row = database.delete("TASKS","id=?",new String[]{String.valueOf(id)});
        return row !=-1;
    }

    public boolean updateTypeInfo(int id, boolean check) {
        int status = check ? 1 : 0;;
        ContentValues values = new ContentValues();
        values.put("status",status);
        long row = database.update("TASKS",values,"id=?",new String[]{String.valueOf(id)});
        return row !=-1;
    }

}
