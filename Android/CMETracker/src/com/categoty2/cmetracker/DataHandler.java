package com.categoty2.cmetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler {
	public static final String USER_DETAILS_TABLE_NAME = "USER_DETAILS";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String MIDDLE_NAME = "MIDDLE_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String PROFESSION = "PROFESSION";
	public static final String EMAIL = "EMAIL";
	public static final String USERNAME = "USERNAME";
	public static final String PWD = "PWD";
	public static final String USER_ID = "USER_ID";
	public static final String TELEPHONE = "TELEPHONE";
	
	public static final String LICENSE_DETAILS_TABLE_NAME = "LICENSE_DETAILS";
	public static final String LICENSE_NUMBER = "LICENSE_NUMBER";
	public static final String LICENSE_ISSUE_DT = "LICENSE_ISSUE_DT";
	public static final String LICENSE_EXPIRATION_DT = "LICENSE_EXPIRATION_DT";
	public static final String STATE = "STATE";
	public static final String LICENSE_USER_ID = "USER_ID";
	
	
	public static final String STATUS_DETAILS_TABLE_NAME = "STATUS_DETAILS";
	public static final String STATUS_DETAILS_STATUS_ID = "STATUS_ID";
	public static final String STATUS_DESC = "STATUS_DESC";
	
	public static final String DB_NAME = "VMTest.db";
	public static final int DB_VERSION = 1;
	public static final  String PRAGMA = "PRAGMA foreign_keys=ON";
	public static final String USER_DETAILS_CREATE = " CREATE TABLE "
			+ USER_DETAILS_TABLE_NAME
			+ "( "
			+ " FIRST_NAME TEXT NOT NULL,  MIDDLE_NAME TEXT, LAST_NAME TEXT NOT NULL, PROFESSION TEXT, EMAIL TEXT, USER_NAME TEXT UNIQUE," +
			" PWD TEXT NOT NULL, USER_ID LONG PRIMARY KEY, TELEPHONE LONG ); ";
	/*CREATE TABLE LICENSE_DETAILS(
   LICENSE_NUMBER LONG PRIMARY KEY,
   LICENSE_ISSUE_DT DATETIME,
   LICENSE_EXPIRY_DT DATETIME CHECK (LICENSE_EXPIRY_DT >= LICENSE_ISSUE_DT),
   STATE TEXT, -- IMPLEMENT CHECK CONSTRAINT
   LIC_USER_ID LONG UNIQUE,
   FOREIGN KEY (LIC_USER_ID) REFERENCES USER_DETAILS(USER_ID)
   );
	 * */
	public static final String LICENSE_DETAILS_CREATE = " CREATE TABLE "
			+ USER_DETAILS_TABLE_NAME
			+ "( "
			+ " LICENSE_NUMBER LONG PRIMARY KEY, LICENSE_ISSUE_DT DATETIME, LICENSE_EXPIRY_DT DATETIME CHECK (LICENSE_EXPIRY_DT >= LICENSE_ISSUE_DT)," +
			"  STATE TEXT, LIC_USER_ID LONG UNIQUE, FOREIGN KEY (LIC_USER_ID) REFERENCES USER_DETAILS(USER_ID) ); ";

	/*public static final String STATUS_DETAILS_CREATE = " CREATE TABLE "
			+ STATUS_DETAILS_TABLE_NAME
			+ "( "
			+ " STATUS_ID INT PRIMARY KEY, STATUS_DESC TEXT);";
*/
	
	DataBaseHelper dbHelper;
	Context ctx;
	SQLiteDatabase db;

	public DataHandler(Context ctx) {
		this.ctx = ctx;
		dbHelper = new DataBaseHelper(ctx);
	}

	private static class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(PRAGMA);
				System.out.println("PRAGMA EXECUTED /////////////////////////////////////////////////////////////////////////////////// ");
				db.execSQL(USER_DETAILS_CREATE);
				db.execSQL(LICENSE_DETAILS_CREATE);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);
		}

	}

	public DataHandler open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long getMaxUserID() {
		SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
		if (sqlDB != null) {
			String maxQuery = "SELECT MAX(USER_ID) FROM " + USER_DETAILS_TABLE_NAME;
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
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			String userIDQuery = "SELECT COUNT(USER_ID) FROM "+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '"+ email + "';";
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				noOfRows = Long.valueOf(c.getLong(0));
			}
			if (noOfRows == 0) {
				result = false;
			}else{
				result = true;
			}
			return result;
		}
	}
	
	
	
	

	public Long registerUser(String firstName, String middleName, String lastName, String profession, String licenseNum, String state, String licIssueDt,
			String licExpDt, String telephone, String email, String userName, String passWord) {
		
		try{
			ContentValues userContent = new ContentValues();
			userContent.put(FIRST_NAME, firstName);
			userContent.put(MIDDLE_NAME, middleName);
			userContent.put(LAST_NAME, lastName);
			userContent.put(PROFESSION, profession);
			userContent.put(EMAIL, email);
			userContent.put(USERNAME, userName);
			userContent.put(PWD, passWord);
			Long userIdLong = getMaxUserID();
			if(userIdLong == -1){
				return -1L;
			}
			userIdLong++;
			userContent.put(USER_ID, String.valueOf(userIdLong));
			userContent.put(TELEPHONE, telephone);
			
			Long rowCountInsertedUserDetails = db.insert(USER_DETAILS_TABLE_NAME, null, userContent);
			Long rowCountInsertedLicenseDetails = -1L;
			if(rowCountInsertedUserDetails != null && rowCountInsertedUserDetails > 0){
				ContentValues licenseContent = new ContentValues();
				licenseContent.put(LICENSE_NUMBER, licenseNum);
				licenseContent.put(LICENSE_ISSUE_DT, licIssueDt);
				licenseContent.put(LICENSE_EXPIRATION_DT, licExpDt);
				licenseContent.put(STATE, state);
				licenseContent.put(LICENSE_USER_ID, String.valueOf(userIdLong));
				rowCountInsertedLicenseDetails = db.insert(LICENSE_DETAILS_TABLE_NAME, null, licenseContent);
			}
			if(rowCountInsertedUserDetails > 0 && rowCountInsertedLicenseDetails >0){
				return 1L;
			}else{
				return -1L;
			}
			
		}catch (Exception e){
			Log.e("DataHandler : registerUser : ", "Exception", e);
		}
		return -1L;
	}

	public long getUserID(String userName) {
		if (userName == null || userName.length() == 0) {
			return -1;
		} else {
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			Long maxNumber = -1L;
			String userIDQuery = "SELECT USER_ID FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "'";
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

/*	public long createJob(String userID, String creationDate, String selfTeam,  String priority, String targetTeam, String targetDuration,
			String description) {
		
		ContentValues content = new ContentValues();
		long maxJobID = getMaxJobID();
		if(maxJobID == -1){
			return -1;
		}
		maxJobID++;
		content.put(JOB_ID, String.valueOf(maxJobID));
		content.put(USER_ID_JOB_DETAILS, userID);
		content.put(CREATION_DATE, creationDate);
		content.put(END_DATE, new String());
		content.put(SELF_TEAM, selfTeam);
		content.put(TARGET_TEAM, targetTeam);
		content.put(PRIORITY, priority);
		content.put(TARGET_DURATION, targetDuration);
		content.put(ACTUAL_DURATION, new String());
		content.put(END_DATE, new String());
		content.put(DESCRIPTION, description);
		content.put(STATUS_ID, 1);
		content.put(CPU_USAGE, 0);
		content.put(MEMORY_USAGE, 0);
		content.put(URL, "www.vmware.com");
		
		if (db != null) {
			long rowIndexInserted = db.insert(JOB_DETAILS_TABLE_NAME, null, content);
			return rowIndexInserted;
		} else {
			return -1;
		}

	}

	public List<UserDetails> fetchUserDetails(String userName) {
		if (userName == null || userName.length() == 0) {
			return null;
		} else {
			String userIDDetailsQuery = "SELECT * FROM "+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"+ userName + "';";
			Cursor userCursor = db.rawQuery(userIDDetailsQuery, null);
			List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
			if(userCursor.getCount() <= 0){
				return null;
			}else{
				if(userCursor.moveToFirst()){
					do{
						UserDetails user = new UserDetails();
						user.setEmail(userCursor.getString(0));
						user.setUserName(userCursor.getString(1));
						user.setPassword(userCursor.getString(2));
						user.setUserID(userCursor.getLong(3));
						user.setManagerEmail(userCursor.getString(4));
						user.setRole(userCursor.getString(5));
						user.setTeam(userCursor.getString(6));
						userDetailsList.add(user);
					}while(userCursor.moveToNext());
				}
				return userDetailsList;
			}
			
		}
		
	}

	public String[] fetchJobDetails(String jobID,String userName) {
		Long userIDLong = getUserID(userName);
		String jobIDDetailsQuery = "SELECT * FROM "+JOB_DETAILS_TABLE_NAME+" WHERE JOB_ID = '"+jobID+"' ;";
			String userId = String.valueOf(userIDLong);
			Cursor jobCursor = db.rawQuery(jobIDDetailsQuery, null);
			List<String> job = new ArrayList<String>();
			if(jobCursor.moveToFirst()){
					job.add(jobCursor.getString(0));
					job.add(jobCursor.getString(1));
					job.add(jobCursor.getString(2));
					job.add(jobCursor.getString(3));
					job.add(jobCursor.getString(4));
					job.add(jobCursor.getString(5));
					job.add(jobCursor.getString(6));
					job.add(jobCursor.getString(7));
					job.add(jobCursor.getString(8));
					job.add(jobCursor.getString(9));
					job.add(jobCursor.getString(10));
					job.add(jobCursor.getString(11));
					job.add(jobCursor.getString(12));
					job.add(jobCursor.getString(13));
			}
			String[] toReturn = new String[job.size()];
			toReturn = job.toArray(toReturn);
			return  toReturn;
		
}

	public List<String> fetchJobList(String jobId, String priority, String statusId, String startDate, String endDate, String userName) {
		if(priority.equalsIgnoreCase("-SELECT PRIORITY-") ){
			priority = new String("");
		}
		String jobIDDetailsQuery = "SELECT JOB_ID FROM "+JOB_DETAILS_TABLE_NAME+" WHERE ";
		Long userIDLong = getUserID(userName);
		if(userIDLong < 0){
			return null;
		}else{
			
			String userId = String.valueOf(userIDLong);
			boolean userFlag = false;
			boolean priorityFlag = false;
			boolean statusFlag = false;
			if(jobId.length()> 0){
				userFlag = true;
			}
			if(priority.length()> 0){
				priorityFlag = true;
			}
			if(statusId.length()> 0){
				statusFlag= true;
			}
			boolean mainFlag = false;
			if(userFlag){
				jobIDDetailsQuery += " JOB_ID = '"+jobId+"' "+"AND USER_ID = '"+userId+"' ";
				if(priorityFlag){
					jobIDDetailsQuery += " AND PRIORITY = '"+priority+"' ";
				}
				if(statusFlag){
					jobIDDetailsQuery += " AND STATUS_ID = '"+statusId+"' ";
				}
				mainFlag = true;
			}
			if(priorityFlag && (mainFlag == false)){
				jobIDDetailsQuery += " PRIORITY = '"+priority+"' "+"AND USER_ID = '"+userId+"' ";
					if(statusFlag){
						jobIDDetailsQuery += " AND STATUS_ID = '"+statusId+"' ";
					}
				mainFlag = true;	
			}
			if(statusFlag && (mainFlag == false)){
				jobIDDetailsQuery += " STATUS_ID = '"+statusId+"' "+"AND USER_ID = '"+userId+"' ";
			}
			jobIDDetailsQuery+= " ; ";
			Cursor jobCursor = db.rawQuery(jobIDDetailsQuery, null);
			List<String> jobIdList = new ArrayList<String>();
			if(jobCursor!=null && jobCursor.getCount() > 0){
				if(jobCursor.moveToFirst()){
					do{
						jobIdList.add(jobCursor.getString(0));
					}while(jobCursor.moveToNext());
				}
			}else{
				jobIdList = null;
			}
			
			return  jobIdList;
		}
}
	*/
	
	public String validateUser(String email, String password) {
		if (email == null || email.length() == 0) {
			return null;
		} else {
			Long emailRowCount = -1L;
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			String userIDQuery = "SELECT USER_ID,PWD FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '" + email
					+ "';";
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				emailRowCount = Long.valueOf(c.getLong(0));
			}
			
			if (emailRowCount < 0) {
				return "Email is not present.";
			} else {
				String pwdFromDB = c.getString(1);
				if (pwdFromDB.equals(password)) {
					return "Success";
				} else {
					return "Failure";
				}
			}
		}
	}

	public String validateUserName(String userName) {
		if (userName == null || userName.length() == 0) {
			return null;
		} else {
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			Long emailRowCount = -1L;
			String userNameQuery = "SELECT COUNT(*) FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "';";
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