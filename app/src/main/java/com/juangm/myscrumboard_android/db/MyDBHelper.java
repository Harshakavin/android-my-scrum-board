package com.juangm.myscrumboard_android.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.juangm.myscrumboard_android.models.Board;
import com.juangm.myscrumboard_android.models.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydatabase.db";
   // private static UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    public MyDBHelper(Context context){

        super(context.getApplicationContext(), DB_NAME , null, 1);
        Log.i("","Db Creted");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL("CREATE TABLE my_cards (id text PRIMARY KEY, content text,owner text,category text,board_id text, time integer)");
            sqLiteDatabase.execSQL("CREATE TABLE my_boards (id text PRIMARY KEY, name text,description text)");
            Log.i("","Tables Creted");
        }catch (Exception E){
            Log.i("",E.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void saveCard(Card card){
     try{
         String uuid = UUID.randomUUID().toString();
         System.out.println(uuid);
        ContentValues cv = new ContentValues();
        cv.put("id" ,uuid);
        cv.put("content" , card.getContent());
        cv.put("owner" , card.getOwner());
        cv.put("category" , card.getCategory());
        cv.put("board_id" , card.getBoard_id());
        cv.put("time" , card.getTime());
        Log.i("",card.getOwner()+' '+card.getContent()+' '+card.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("my_cards", null ,cv);
        db.close();
    }catch (Exception E){
        Log.i("",E.toString());
    }
    }

    public void saveBoard(Board board){

     try{
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        ContentValues cv = new ContentValues();
        cv.put("id" ,uuid);
        cv.put("name" , board.getName());
        cv.put("description" , board.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("my_boards", null ,cv);
        db.close();
    }catch (Exception E){
        Log.i("",E.toString());
    }
    }

    public String read(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu = db.rawQuery("SELECT value FROM my_table WHERE id = " + id, null);
        cu.moveToNext();
        return cu.getString(cu.getColumnIndex("value"));
    }

    public List<Card> readALLcards(String bId){
        List<Card> cards = new ArrayList<>();
        Log.i("",bId+" board name is like");
     try{
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu = db.rawQuery("SELECT * FROM my_cards WHERE board_id = "+"'"+ bId +"'",null);
                // WHERE board_id = "+ bId
        cu.moveToNext();
        int idIdx = cu.getColumnIndex("id");
        int contentIdx =  cu.getColumnIndex("content");
        int ownerIdx =  cu.getColumnIndex("owner");
        int categoryIdx =  cu.getColumnIndex("category");
        int boardIdIdx =  cu.getColumnIndex("board_id");
        int timeIdIdx =  cu.getColumnIndex("time");

        while (!cu.isAfterLast()) {
            String id = cu.getString(idIdx);;
            String content = cu.getString(contentIdx);
            String owner = cu.getString(ownerIdx);
            String category = cu.getString(categoryIdx);
            String board_id = cu.getString(boardIdIdx);
            Integer time_id = cu.getInt(timeIdIdx);
            cards.add(new Card(id, content, owner, category, board_id,time_id));
            cu.moveToNext();
        }
     }catch (Exception E){
         Log.i("",E.toString());
     }
        return cards;

    }

    public List<Board> readALLboards(){
        List<Board> boards = new ArrayList<>();
      try{
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu = db.rawQuery("SELECT * FROM my_boards" , null);
        cu.moveToNext();
        int idIdx = cu.getColumnIndex("id");
        int nameIdx =  cu.getColumnIndex("name");
        int descriptionIdx =  cu.getColumnIndex("description");
        Log.i("","get all boards");

        while (!cu.isAfterLast()){
            String id = cu.getString(idIdx);;
            String name = cu.getString(nameIdx);
            String description = cu.getString(descriptionIdx);
           // Log.i("",name+' '+description);
            boards.add(new Board(name , description,id));
            cu.moveToNext();
        }
    }catch (Exception E){
        Log.i("",E.toString());
    }
        return boards;
    }

    public void DeleteBoard(String name) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("my_boards", "name = "+ "'" + name + "'",null);
            Log.i("","Board Deleted");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
    public void moveCard(String id,String category) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("category",category);
            db.update("my_cards",cv, "id = "+ "'" + id + "'",null);
            Log.i("","card moved");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
    public void DeleteCard(String id) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("my_cards", "id = "+ "'" + id + "'",null);
            Log.i("","Card Deleted");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
    public void UpdateCardContent(String id,String content) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("content",content);
            db.update("my_cards",cv, "id = "+ "'" + id + "'",null);
            Log.i("","card Updated");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
    public void UpdateCardTime(String id,Integer time) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("time",time);
            db.update("my_cards",cv, "id = "+ "'" + id + "'",null);
            Log.i("","card Updated");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
    public void UpdateCardOwner(String id,String Owner) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("owner",Owner);
            db.update("my_cards",cv, "id = "+ "'" + id + "'",null);
            Log.i("","card Updated");
        }catch (Exception E){
            Log.i("",E.toString());
        }

    }
}
