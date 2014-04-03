package com.categoty2.cmetracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {

	
	// DB Related
	public static final String DB_NAME = "CME.db";
	public static final int DB_VERSION = 2;

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

	public static final String STATE_DETAILS_CREATE = " CREATE TABLE "
			+ STATE_DETAILS_TABLE_NAME + "( STATE_ID INT, STATE_DESC TEXT ); ";

	public DataBaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(PRAGMA);
			Log.i("DataBaseHandler : onCreate : ",
					"PRAGMA Successfully Executed");
			Log.i("DataBaseHandler : onCreate : ",
					"Query to create table USER_DETAILS : "
							+ USER_DETAILS_CREATE);
			db.execSQL(USER_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"INSERTED USER_DETAILS TABLE Successfully");
			Log.i("DataBaseHandler : onCreate : ",
					"Query to create table LICENSE_DETAILS : "
							+ LICENSE_DETAILS_CREATE);
			db.execSQL(LICENSE_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"INSERTED LICENSE_DETAILS TABLE Successfully");
			Log.i("DataBaseHandler : onCreate : ",
					"Query to create table STATE_DETAILS : "
							+ STATE_DETAILS_CREATE);
			db.execSQL(STATE_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"INSERTED STATE_DETAILS TABLE Successfully");
			
		} catch (SQLException exception) {
			Log.e("DataBaseHandler : onCreate : ", "SQLException Occured");
			Log.e("DataBaseHandler : onCreate : ", "SQLException occured",
					exception);
		} catch (Exception e) {
			Log.e("DataBaseHandler : onCreate() : ", "Exception occured");
			Log.e("DataBaseHandler : onCreate() : ", "Exception occured", e);
		}/*finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
			
		}*/
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		try {
			db.beginTransaction();
			db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"DROPPED USER_DETAILS TABLE Successfully");
			db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"DROPPED LICENSE_DETAILS TABLE Successfully");
			db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS_CREATE);
			Log.i("DataBaseHandler : onCreate : ",
					"DROPPED STATE_DETAILS TABLE Successfully");
			// Create tables again
			onCreate(db);
		} catch (SQLException exception) {
			Log.e("DataBaseHandler : onUpgrade : ", "SQLException occured");
			Log.e("DataBaseHandler : onUpgrade : ", "SQLException occured",
					exception);
		} catch (Exception e) {
			Log.e("DataBaseHandler : onUpgrade() : ", "Exception occured");
			Log.e("DataBaseHandler : onUpgrade() : ", "Exception occured", e);
		} /*finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
			
		}*/
	}

	public boolean insertStates() {
		boolean result = false;
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		try {

			String[] stateIdArray = { "1", "2", "3", "4", "5" };
			String[] stateDescArray = { "KANSAS", "MICHIGAN", "NORTH CAROLINA",
					"TEXAS", "VIRGINIA" };
			Long[] resultArray = { -1L, -1L, -1L, -1L, -1L };
			ContentValues userContent = new ContentValues();
			sqlDB.beginTransaction();
			for (int i = 0; i < stateIdArray.length; i++) {
				userContent.put(STATE_DETAILS_STATE_ID, stateIdArray[i]);
				userContent.put(STATE_DESC, stateDescArray[i]);
				resultArray[i] = sqlDB.insert(STATE_DETAILS_TABLE_NAME, null,
						userContent);
			}
			boolean notInserted = false;
			for (int i = 0; i < resultArray.length; i++) {
				if (resultArray[i] == -1L) {
					notInserted = true;
				}
			}
			if (notInserted == false) {
				sqlDB.setTransactionSuccessful();
			}
		} catch (SQLException exception) {
			Log.e("DataBaseHandler : insertStates : ", "SQLException occured");
			Log.e("DataBaseHandler : insertStates : ", "SQLException occured",
					exception);
		} catch (Exception e) {
			Log.e("DataBaseHandler : insertStates() : ", "Exception occured");
			Log.e("DataBaseHandler : insertStates() : ", "Exception occured", e);
		} finally {
			if (sqlDB.inTransaction()) {
				sqlDB.endTransaction();
			}
			
		}
		return result;
	}

	public long getMaxUserID() {
		SQLiteDatabase sqlDB = this.getReadableDatabase();
		try {

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
		} catch (SQLException e) {
			Log.e("DataBaseHandler : getMaxUserID() : ", "SQLException occured");
			Log.e("DataBaseHandler : getMaxUserID() : ",
					"SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : getMaxUserID() : ", "Exception occured");
			Log.e("DataBaseHandler : getMaxUserID() : ", "Exception occured", e);
		} /*finally {
			if (sqlDB.inTransaction()) {
				sqlDB.endTransaction();
			}
			
		}*/
		return -1;
	}

	public boolean isUserRegistered(String email) {
		boolean result = false;
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		try {
			Long noOfRows = -1L;

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
		} catch (SQLException e) {
			Log.e("DataBaseHandler : isUserRegistered() : ",
					"SQLException occured");
			Log.e("DataBaseHandler : isUserRegistered() : ",
					"SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : isUserRegistered() : ",
					"Exception occured");
			Log.e("DataBaseHandler : isUserRegistered() : ",
					"Exception occured", e);
		} /*finally {
			if (sqlDB1.inTransaction()) {
				sqlDB1.endTransaction();
			}
			
		}*/
		return false;
	}

	public Long registerUser(String firstName, String middleName,
			String lastName, String profession, String licenseNum,
			String state, String licIssueDt, String licExpDt, String telephone,
			String email, String userName, String passWord) {

		SQLiteDatabase sqlDB = this.getWritableDatabase();
		sqlDB.beginTransaction();
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
			Long rowCountInsertedUserDetails = sqlDB.insert(
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
				rowCountInsertedLicenseDetails = sqlDB.insert(
						LICENSE_DETAILS_TABLE_NAME, null, licenseContent);
			}
			if (rowCountInsertedUserDetails > 0
					&& rowCountInsertedLicenseDetails > 0) {
				if (sqlDB.inTransaction()) {
					sqlDB.setTransactionSuccessful();
				}//
				return 1L;
			} else {
				sqlDB.endTransaction();
				return -1L;
			}

		} catch (SQLException e) {
			Log.e("DataHandler : registerUser(): ", "SQLException");
			Log.e("DataHandler : registerUser(): ", "SQLException", e);

		} catch (Exception e) {
			Log.e("DataHandler : registerUser(): ", "Exception");
			Log.e("DataHandler : registerUser(): ", "Exception", e);

		} finally {
			if (sqlDB.inTransaction()) {
				sqlDB.endTransaction();
			}
			
		}
		return -1L;
	}

	public long getUserID(String userName) {
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		try {

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
		} catch (SQLException e) {
			Log.e("DataBaseHandler : getUserID() : ", "SQLException occured");
			Log.e("DataBaseHandler : getUserID() : ", "SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : getUserID() : ", "Exception occured");
			Log.e("DataBaseHandler : getUserID() : ", "Exception occured", e);
		} /*finally {
			if (sqlDB1.inTransaction()) {
				sqlDB1.endTransaction();
			}
			
		}*/
		return -1L;
	}

	// Used to validate username and password of a user. Used for login purpose
	public int validateUser(String email, String password) {
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		try {
			Long emailRowCount = -1L;

			String userIDQuery = "SELECT USER_ID,PWD FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '" + email
					+ "' ; ";
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
		} catch (SQLException e) {
			Log.e("DataBaseHandler : validateUser() : ", "SQLException occured");
			Log.e("DataBaseHandler : validateUser() : ",
					"SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : validateUser() : ", "Exception occured");
			Log.e("DataBaseHandler : validateUser() : ", "Exception occured", e);
		} /*finally {
			if (sqlDB1.inTransaction()) {
				sqlDB1.endTransaction();
			}
			
		}*/

		return -1;
	}

	public String validateUserName(String userName) {
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		try {
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
		} catch (SQLException e) {
			Log.e("DataBaseHandler : validateUserName() : ",
					"SQLException occured");
			Log.e("DataBaseHandler : validateUserName() : ",
					"SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : validateUserName() : ",
					"Exception occured");
			Log.e("DataBaseHandler : validateUserName() : ",
					"Exception occured", e);
		} /*finally {
			if (sqlDB1.inTransaction()) {
				sqlDB1.endTransaction();
			}
			
		}*/
		return null;
	}

	public List<String> getStates() {
		System.out
				.println("Entered getStates : /////////////////////////////////////////////");
		SQLiteDatabase sqlDB1 = this.getReadableDatabase();
		try {
			List<String> stateList = new ArrayList<String>();
			String stateQuery = " SELECT * FROM " + STATE_DETAILS_TABLE_NAME
					+ " ;";
			Cursor stateCursor = sqlDB1.rawQuery(stateQuery, null);
			System.out.println("cursor count : " + stateCursor.getCount()
					+ "///////////////////////////");
			if (stateCursor.getCount() <= 0) {
				return null;
			} else {
				stateList.add("-SELECT STATE-");
				if (stateCursor.moveToFirst()) {
					do {
						stateList.add(stateCursor.getString(0));
					} while (stateCursor.moveToNext());
				}
				for (String s : stateList) {
					System.out
							.println("state in getStates : ///////////////////// "
									+ s);
				}
				return stateList;
			}
		} catch (SQLException e) {
			Log.e("DataBaseHandler : getStates() : ", "SQLException occured");
			Log.e("DataBaseHandler : getStates() : ", "SQLException occured", e);
		} catch (Exception e) {
			Log.e("DataBaseHandler : getStates() : ", "Exception occured");
			Log.e("DataBaseHandler : getStates() : ", "Exception occured", e);
		} /*finally {
			if (sqlDB1.inTransaction()) {
				sqlDB1.endTransaction();
			}
			
		}*/
		return null;
	}
}