package com.weidi.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.weidi.QApp;

/**
 * 聊天回话列表的管理
 * 
 * @author Administrator
 * 
 */
public class NewsNotice {
	private SQLiteDatabase db;
	private static NewsNotice instance;

	public static NewsNotice getInstance() {
		if (instance == null) {
			instance = new NewsNotice(QApp.getInstance());
		}
		return instance;
	}

	public NewsNotice(Context context) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

	public NewsNotice(Context context, int version) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

    public int insert(ContentValues values){
		
		int id=(int)db.insert("table_newsnotice",null, values);
		Log.i("TAG", "insert ok!");
		
//		db.close();
		return id;
	}
    
    public List<Map<String,Object>> query(){
		/* Cursor c=db.query("table_newsnotice", new String[]{"news_id","title","link","imglink","createdatetime","content"},//列的名字
				 null,null, null, null, null,null);//id为where语句，new String为？的值
*/		
    	String sql="select * from table_newsnotice order by createdatetime desc limit ?,?";
    	//String sql="select * from table_newsnotice order by news_id desc limit ?,?";
    	//String sql="select * from table_newsnotice  limit ?,?";
         String[] args = new String[] {"0","1"};
		 Cursor c=db.rawQuery(sql,args); 
		
		 List<Map<String,Object>> list= 
		 new ArrayList<Map<String,Object>>();
		 Map<String,Object> map=null;
		 while(c.moveToNext()){
			 map=new HashMap<String, Object>();
			 map.put("news_id", c.getString(0));
			 map.put("title",c.getString(1));
			 map.put("link",c.getString(2));
			 map.put("imglink",c.getString(3));
			 map.put("createdatetime",c.getString(4));
			 map.put("content",c.getString(5));
			 map.put("isread",c.getString(6));
			 list.add(map);
			 
		 }
		Log.i("xxxxxxxxxxxxxxxxxxxxxxx", "list="+list);
		return list;
	}
    
    public List<Map<String,Object>> query(int count){
		/* Cursor c=db.query("table_newsnotice", new String[]{"news_id","title","link","imglink","createdatetime","content"},//列的名字
				 null,null, null, null, null,null);//id为where语句，new String为？的值
*/		
    	String scount=String.valueOf(count);
    	
    	String sql="select * from table_newsnotice order by createdatetime desc limit ?,?";
    //	String sql="select * from table_newsnotice order by news_id desc limit ?,?";
    	//String sql="select * from table_newsnotice  limit ?,?";
         String[] args = new String[] {"0",scount};
		 Cursor c=db.rawQuery(sql,args); 
		
		 List<Map<String,Object>> list= 
		 new ArrayList<Map<String,Object>>();
		 Map<String,Object> map=null;
		 while(c.moveToNext()){
			 map=new HashMap<String, Object>();
			 map.put("news_id", c.getString(0));
			 map.put("title",c.getString(1));
			 map.put("link",c.getString(2));
			 map.put("imglink",c.getString(3));
			 map.put("createdatetime",c.getString(4));
			 map.put("content",c.getString(5));
			 map.put("isread",c.getString(6));
			 list.add(map);
			 
		 }
		Log.i("xxxxxxxxxxxxxxxxxxxxxxx", "list="+list);
		return list;
	}
    
    
    
    public int querycount(){
    	String sql="select * from table_newsnotice order by createdatetime desc";
		 Cursor c=db.rawQuery(sql,null); 	
		 List<Map<String,Object>> list= 
		 new ArrayList<Map<String,Object>>();
		 Map<String,Object> map=null;
		 while(c.moveToNext()){
			 map=new HashMap<String, Object>();
			 map.put("news_id", c.getString(0));
			 map.put("title",c.getString(1));
			 map.put("link",c.getString(2));
			 map.put("imglink",c.getString(3));
			 map.put("createdatetime",c.getString(4));
			 map.put("content",c.getString(5));
			 map.put("isread",c.getString(6));
			 list.add(map);
			 
		 }
		return list.size();
	}
    
    public int queryIsContain(String id){
    	String sql="select * from table_newsnotice where news_id=?";
    	String[] args = new String[] {id};
		 Cursor c=db.rawQuery(sql,args);
		 if(c!=null){
			 return 1;
		 }
		 else{
			 return 0;
		 }
		 
	}
    
    public List<Map<String,Object>> queryFirst(){
    	String sql="select * from table_newsnotice order by createdatetime desc limit ?,?";
    	//String sql="select * from table_newsnotice order by news_id desc limit ?,?";
    	//String sql="select * from table_newsnotice  limit ?,?";
         String[] args = new String[] {"0","1"};
		 Cursor c=db.rawQuery(sql,args); 
		
		 List<Map<String,Object>> list= 
		 new ArrayList<Map<String,Object>>();
		 Map<String,Object> map=null;
		 while(c.moveToNext()){
			 map=new HashMap<String, Object>();
			 map.put("news_id", c.getString(0));
			 map.put("title",c.getString(1));
			 map.put("link",c.getString(2));
			 map.put("imglink",c.getString(3));
			 map.put("createdatetime",c.getString(4));
			 map.put("content",c.getString(5));
			 map.put("isread",c.getString(6));
			 list.add(map);
			 
		 }
		Log.i("xxxxxxxxxxxxxxxxxxxxxxx", "list="+list);
		return list;
    }
    public String queryLast(){
    	String sql="select * from table_newsnotice order by createdatetime asc limit ?,?";
    	//String sql="select * from table_newsnotice order by news_id desc limit ?,?";
    	//String sql="select * from table_newsnotice  limit ?,?";
         String[] args = new String[] {"0","1"};
		 Cursor c=db.rawQuery(sql,args); 
		 List<Map<String,Object>> list= 
		 new ArrayList<Map<String,Object>>();
		 Map<String,Object> map=null;
		 while(c.moveToNext()){
			 map=new HashMap<String, Object>();
			 map.put("news_id", c.getString(0));
			 map.put("title",c.getString(1));
			 map.put("link",c.getString(2));
			 map.put("imglink",c.getString(3));
			 map.put("createdatetime",c.getString(4));
			 map.put("content",c.getString(5));
			 map.put("isread",c.getString(6));
			 list.add(map);
		 }
		Log.i("xxxxxxxxxxxxxxxxxxxxxxx", "list="+list);
		return (String)list.get(0).get("createdatetime");
		
    }
  
    public void delNoticeById(String id){
    	db.delete("table_newsnotice", "news_id=?", new String[] { id });
    	Log.i("tag", "已经删除了");
    }
   
    public int updateRead(String id,ContentValues values){
    	
  	  int result=(int)db.update("table_newsnotice", values,"news_id=?",new String[]{id});  
        Log.i("TAG", "update ok!");      
        //db.close();
        return result;
  }

}
