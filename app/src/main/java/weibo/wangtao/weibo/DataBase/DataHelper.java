package weibo.wangtao.weibo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import weibo.wangtao.weibo.Bean.UserInfo;

import static weibo.wangtao.weibo.DataBase.DBInfo.DB.DB_NAME;
import static weibo.wangtao.weibo.DataBase.DBInfo.DB.VERSION;

/**
 * Created by wangtao on 2016/10/27.
 */

public class DataHelper {

    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    private String[] columns={DBInfo.User_Table._ID
            ,DBInfo.User_Table.USER_ID
            ,DBInfo.User_Table.USER_NAME
            ,DBInfo.User_Table.TOKEN
            ,DBInfo.User_Table.TOKEN_SECRET
            ,DBInfo.User_Table.DESCREPTION
            ,DBInfo.User_Table.USER_HEAD};
    public DataHelper(Context context){
        dbHelper=new SqliteHelper(context);

    }

    public void Close()
    {
        db.close();
        dbHelper.close();
    }
    //添加users表的记录
    public Long insertUser(UserInfo userInfo)
    {
        db= dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBInfo.User_Table._ID,userInfo.getId());
        values.put(DBInfo.User_Table.USER_ID,userInfo.getUser_id());
        values.put(DBInfo.User_Table.USER_NAME,userInfo.getUser_name());
        values.put(DBInfo.User_Table.TOKEN,userInfo.getToken());
        values.put(DBInfo.User_Table.TOKEN_SECRET,userInfo.getToken_secret());
        values.put(DBInfo.User_Table.DESCREPTION,userInfo.getDescription());
        ByteArrayOutputStream os =new ByteArrayOutputStream();
        BitmapDrawable newhead=(BitmapDrawable)userInfo.getUser_head();
        newhead.getBitmap().compress(Bitmap.CompressFormat.PNG,100,os);

        values.put(DBInfo.User_Table.USER_HEAD,os.toByteArray());

        Long rowId = db.insert(DBInfo.User_Table.USER_TABLE, DBInfo.User_Table.USER_NAME, values);
        Log.e("insertUserInfo",rowId+"    ");
        db.close();
        return rowId;
    }

    //获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserInfo> GetAllUserList()
    {
        db= dbHelper.getWritableDatabase();
        List<UserInfo> userList ;
        Cursor cursor=db.query(DBInfo.User_Table.USER_NAME, columns, null, null, null, null,null);

        UserInfo userInfo;

        if(cursor!=null&& cursor.getCount()>0)
        {
            userList = new ArrayList<UserInfo>(cursor.getCount());
            while(cursor.moveToNext()){
                userInfo=new UserInfo();
                userInfo.setId(cursor.getLong(cursor.getColumnIndex(DBInfo.User_Table._ID)));
                userInfo.setUser_id(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.USER_ID)));
                userInfo.setUser_name(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.USER_NAME)));
                userInfo.setToken(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.TOKEN)));
                userInfo.setToken_secret(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.TOKEN_SECRET)));
                userInfo.setDescription(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.DESCREPTION)));
                userInfo.setUser_id(cursor.getString(cursor.getColumnIndex(DBInfo.User_Table.USER_ID)));
                byte[] newhead=cursor.getBlob(cursor.getColumnIndex(DBInfo.User_Table.USER_HEAD));

                    ByteArrayInputStream is = new ByteArrayInputStream(newhead);
                    Drawable userhead= Drawable.createFromStream(is, "image");
                    userInfo.setUser_head(userhead);
                userList.add(userInfo);

            }

        }
        else
        {
            return null;
        }

        cursor.close();
        db.close();
        return userList;
    }

    //判断users表中的是否包含某个UserID的记录
//    public Boolean HaveUserInfo(String UserId)
//    {
//        Boolean b=false;
//        Cursor cursor=db.query(SqliteHelper.TB_NAME, null, UserInfo.USERID + "=" + UserId, null, null, null,null);
//        b=cursor.moveToFirst();
//        Log.e("HaveUserInfo",b.toString());
//        cursor.close();
//        return b;
//    }

//    //更新users表的记录，根据UserId更新用户昵称和用户图标
//    public int UpdateUserInfo(String userName,Bitmap userIcon,String UserId)
//    {
//        ContentValues values = new ContentValues();
//        values.put(UserInfo.USERNAME, userName);
//// BLOB类型
//        final ByteArrayOutputStream os = new ByteArrayOutputStream();
//// 将Bitmap压缩成PNG编码，质量为100%存储
//        userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
//// 构造SQLite的Content对象，这里也可以使用raw
//        values.put(UserInfo.USERICON, os.toByteArray());
//        int id= db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=" + UserId, null);
//        Log.e("UpdateUserInfo2",id+"");
//        return id;
//    }

//    //更新users表的记录
//    public int UpdateUserInfo(UserInfo user)
//    {
//        ContentValues values = new ContentValues();
//        values.put(UserInfo.USERID, user.getUserId());
//        values.put(UserInfo.TOKEN, user.getToken());
//        values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
//        int id= db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=" + user.getUserId(), null);
//        Log.e("UpdateUserInfo",id+"");
//        return id;
//    }
//
//
//
//    //删除users表的记录
//    public int DelUserInfo(String UserId){
//        int id= db.delete(SqliteHelper.TB_NAME, UserInfo.USERID +"="+UserId, null);
//        Log.e("DelUserInfo",id+"");
//        return id;
//    }
}
