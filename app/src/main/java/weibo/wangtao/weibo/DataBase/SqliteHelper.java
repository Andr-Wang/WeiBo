package weibo.wangtao.weibo.DataBase;



/**
 * Created by wangtao on 2016/10/27.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import weibo.wangtao.weibo.Bean.UserInfo;
/**
 * 对数据库的创建，更新，删除
 */

public class SqliteHelper extends SQLiteOpenHelper
{




    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public SqliteHelper(Context context) {
        super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBInfo.User_Table.CREATE_USER_TABLE);
        Log.e("Database","onCreate");
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBInfo.User_Table.DROP_USER_TABLE);
        onCreate(db);
        Log.e("Database","onUpgrade");
    }

}
