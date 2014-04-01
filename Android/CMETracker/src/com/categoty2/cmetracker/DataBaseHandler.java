package com.categoty2.cmetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper{
	
	
	Context ctx;
	SQLiteDatabase db;
	
	// DB Related
	public static final String DB_NAME = "CME.db";
	public static final int DB_VERSION = 1;
	
	// Table USER_DETAILS and details related
	public static final String USER_DETAILS_TABLE_NAME = "USER_DETAILS";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String MIDDLE_NAME = "MIDDLE_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String PROFESSION = "PROFESSION";
	public static final String EMAIL = "EMAIL";
	public static final String USER_NAME = "USER_NAME";
	public static final String PWD = "PWD";
	public static final String USER_ID = "USER_ID";
	public static final String TELEPHONE = "TELEPHONE";
	
	// Table LICENSE_DETAILS and details related
	public static final String LICENSE_DETAILS_TABLE_NAME = "LICENSE_DETAILS";
	public static final String LICENSE_NUMBER = "LICENSE_NUMBER";
	public static final String LICENSE_ISSUE_DT = "LICENSE_ISSUE_DT";
	public static final String LICENSE_EXPIRY_DT = "LICENSE_EXPIRY_DT";
	public static final String STATE = "STATE";
	public static final String LIC_USER_ID = "LIC_USER_ID";

	// Table STATE_DETAILS and details related	
	public static final String STATE_DETAILS_TABLE_NAME = "STATE_DETAILS";
	public static final String STATE_DETAILS_STATE_ID = "STATE_ID";
	public static final String STATE_DESC = "STATE_DESC";

	
	public static final String PRAGMA = " PRAGMA foreign_keys = ON ; ";
	
	// TABLE CREATION STRINGS
	public static final String USER_DETAILS_CREATE = " CREATE TABLE "
			+ USER_DETAILS_TABLE_NAME
			+ "( "
			+ " FIRST_NAME TEXT NOT NULL,  MIDDLE_NAME TEXT, LAST_NAME TEXT NOT NULL, PROFESSION TEXT, EMAIL TEXT, USER_NAME TEXT UNIQUE,"
			+ " PWD TEXT NOT NULL, USER_ID LONG PRIMARY KEY, TELEPHONE LONG ); ";

	public static final String LICENSE_DETAILS_CREATE = " CREATE TABLE "
			+ LICENSE_DETAILS_TABLE_NAME
			+ "( "
			+ " LICENSE_NUMBER LONG PRIMARY KEY, LICENSE_ISSUE_DT DATETIME, LICENSE_EXPIRY_DT DATETIME CHECK (LICENSE_EXPIRY_DT >= LICENSE_ISSUE_DT),"
			+ "  STATE TEXT, LIC_USER_ID LONG UNIQUE, FOREIGN KEY (LIC_USER_ID) REFERENCES USER_DETAILS(USER_ID) ); ";

	public static final String STATE_DETAILS_CREATE = " CREATE TABLE "+ STATE_DETAILS_TABLE_NAME +"( STATE_ID INT, STATE_DESC TEXT ); ";
	
	public DataBaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
			db.execSQL(PRAGMA);
			Log.i("DataBaseHandler : onCreate : ", "PRAGMA Successfully Executed");
			db.execSQL(USER_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ", "INSERTED USER_DETAILS TABLE Successfully");
			db.execSQL(LICENSE_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ", "INSERTED LICENSE_DETAILS TABLE Successfully");
			db.execSQL(STATE_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ", "INSERTED STATE_DETAILS TABLE Successfully");

		}catch(SQLException exception){
			Log.e("DataBaseHandler : onCreate : ", "SQLException Occured");
			Log.e("DataBaseHandler : onCreate : ", "SQLException occured", exception);
		}
				
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 // Drop older table if existed
        try{
        	db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
        	Log.i("DataBaseHandler : onCreate : ", "DROPPED USER_DETAILS TABLE Successfully");
        	db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
        	Log.i("DataBaseHandler : onCreate : ", "DROPPED LICENSE_DETAILS TABLE Successfully");
        	db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
        	Log.i("DataBaseHandler : onCreate : ", "DROPPED STATE_DETAILS TABLE Successfully");
        }catch(SQLException exception){
        	Log.e("DataBaseHandler : onUpgrade : ", "SQLException occured");
        	Log.e("DataBaseHandler : onUpgrade : ", "SQLException occured", exception);
        }
		 // Create tables again
        onCreate(db);
	}
	public long getMaxUserID() {
		SQLiteDatabase sqlDB = this.getReadableDatabase();
		if (sqlDB != null) {
			String maxQuery = "SELECT MAX(USER_ID) FROM "
					+ USER_DETAILS_TABLE_NAME + " ; ";
			Long maxUserId = -1L;
			Cursor c = sqlDB.rawQuery(maxQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				maxUserId = Long.valueOf(c.getLong(0));
			}
			if (maxUserId == null || maxUserId == -1) {
				maxUserId = 0L;
			}
			return maxUserId;
		}
		return -1;
	}

	public boolean isUserRegistered(String email) {
		if (email == null || email.length() == 0) {
			return false;
		} else {
			boolean result = false;
			Long noOfRows = -1L;
			SQLiteDatabase sqlDB1 = this.getReadableDatabase();
			String userIDQuery = "SELECT COUNT(USER_ID) FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '" + email
					+ "' ; ";
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				noOfRows = Long.valueOf(c.getLong(0));
			}
			if (noOfRows == 0) {
				result = false;
			} else {
				result = true;
			}
			return result;
		}
	}

	public Long registerUser(String firstName, String middleName,
			String lastName, String profession, String licenseNum,
			String state, String licIssueDt, String licExpDt, String telephone,
			String email, String userName, String passWord) {
			db = this.getWritableDatabase();
			db.beginTransaction();
		try {
			ContentValues userContent = new ContentValues();
			userContent.put(FIRST_NAME, firstName);
			userContent.put(MIDDLE_NAME, middleName);
			userContent.put(LAST_NAME, lastName);
			userContent.put(PROFESSION, profession);
			userContent.put(EMAIL, email);
			userContent.put(USER_NAME, userName);
			userContent.put(PWD, passWord);
			Long userIdLong = getMaxUserID();
			if (userIdLong == -1) {
				return -1L;
			}
			userIdLong++;
			userContent.put(USER_ID, String.valueOf(userIdLong));
			userContent.put(TELEPHONE, telephone);
			Long rowCountInsertedUserDetails = db.insert(
					USER_DETAILS_TABLE_NAME, null, userContent);
			Long rowCountInsertedLicenseDetails = -1L;
			if (rowCountInsertedUserDetails != null
					&& rowCountInsertedUserDetails > 0) {
				ContentValues licenseContent = new ContentValues();
				licenseContent.put(LICENSE_NUMBER, licenseNum);
				licenseContent.put(LICENSE_ISSUE_DT, licIssueDt);
				licenseContent.put(LICENSE_EXPIRY_DT, licExpDt);
				licenseContent.put(STATE, state);
				licenseContent.put(LIC_USER_ID, String.valueOf(userIdLong));
				rowCountInsertedLicenseDetails = db.insert(
						LICENSE_DETAILS_TABLE_NAME, null, licenseContent);
			}
			if (rowCountInsertedUserDetails > 0
					&& rowCountInsertedLicenseDetails > 0) {
				db.setTransactionSuccessful();
				return 1L;
			} else {
				db.endTransaction();
				return -1L;
			}

		} catch (Exception e) {
			Log.e("DataHandler : registerUser : ", "Exception", e);
		} finally{
			if(db.inTransaction()){
				db.endTransaction();
			}
		}
		return -1L;
	}

	public long getUserID(String userName) {
		if (userName == null || userName.length() == 0) {
			return -1;
		} else {
			SQLiteDatabase sqlDB1 = this.getReadableDatabase();
			Long maxNumber = -1L;
			String userIDQuery = "SELECT USER_ID FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "' ; ";
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				maxNumber = Long.valueOf(c.getLong(0));
			}
			if (maxNumber == null || maxNumber == -1) {
				maxNumber = 0L;
			}
			return maxNumber;
		}
	}

	

	public int validateUser(String email, String password) {
		/*
		 * if (email == null || email.length() == 0) { return null; }
		 */// else {
		Long emailRowCount = -1L;
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		String userIDQuery = "SELECT USER_ID,PWD FROM "
				+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '" + email + "' ; ";
		Cursor c = sqlDB1.rawQuery(userIDQuery, null);
		if (null != c && c.getCount() > 0) {
			c.moveToFirst();
			emailRowCount = Long.valueOf(c.getLong(0));
		}

		if (emailRowCount < 0) {
			return -1;
		} else {
			String pwdFromDB = c.getString(1);
			if (pwdFromDB.equals(password)) {
				return 1;
			} else {
				return -1;
			}
		}
		// }
	}

	public String validateUserName(String userName) {
		if (userName == null || userName.length() == 0) {
			return null;
		} else {
			SQLiteDatabase sqlDB1 = this.getReadableDatabase();
			Long emailRowCount = -1L;
			String userNameQuery = " SELECT COUNT(*) FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "' ; ";
			Cursor c = sqlDB1.rawQuery(userNameQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				emailRowCount = Long.valueOf(c.getLong(0));
			}
			if (emailRowCount > 0) {
				return "User Present";
			} else {
				return "User not present";
			}
		}
	}



	
}