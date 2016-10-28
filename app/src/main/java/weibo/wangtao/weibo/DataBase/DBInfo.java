package weibo.wangtao.weibo.DataBase;

/**
 * Created by wangtao on 2016/10/27.
 */

public class DBInfo {
    /*
    数据库信息

     */
    public static class DB
    {
        public static final String DB_NAME="weibo.db";
        public static final int VERSION=1;
    }
    public static class User_Table
    {

        public static final String USER_TABLE="userinfo";

        public static final String _ID="_id";
        public static final String USER_ID="user_id";
        public static final String USER_NAME="user_name";
        public static final String TOKEN="token";
        public static final String TOKEN_SECRET="token_secret";
        public static final String DESCREPTION="descreption";
        public static final String USER_HEAD="user_head";


        public static final String CREATE_USER_TABLE="create table if not exists "
                +USER_TABLE
                +"("
                +_ID +" integer primary key autoincrement,"
                +USER_ID+" text,"
                +USER_NAME+" text,"
                +TOKEN+" text,"
                +TOKEN_SECRET+" text,"
                +DESCREPTION+" text,"
                +USER_HEAD+" blob);";

        public static final String DROP_USER_TABLE="drop table "+USER_TABLE;

    }
}
