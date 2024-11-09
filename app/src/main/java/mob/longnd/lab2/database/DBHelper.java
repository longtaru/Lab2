package mob.longnd.lab2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(@Nullable Context context){
        super(context, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sSQL="create TABLE TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT, STATUS INTEGER)";
        db.execSQL(sSQL);
        String sSQL_Insert="INSERT INTO TASKS " +
                "(ID,TITLE,CONTENT,DATE,TYPE,STATUS) VALUES " +
                "('1','android','Hoc lap trinh android','12/2/2023','De','1')," +
                "('2','kotlin','Hoc lap trinh kotlin','12/2/2023','kho','0')," +
                "('3','react','Hoc lap trinh react','12/2/2023','De','1');";
        db.execSQL(sSQL_Insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TASKS");
        onCreate(db);
    }
}
