package com.learning.careerconnect.Cashes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learning.careerconnect.Model.AppliedJobsModelSQLite


class JobAppliedDB(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT,"
                + NAME_OF_COMPANY + " TEXT,"
                + ABOUT_COMPANY + " TEXT,"
                + NAME_OF_ROLE + " TEXT,"
                + TYPE_OF_JOB + " TEXT,"
                + LOCATION + " TEXT,"
                + START_DATE + " TEXT,"
                + DURATION_OF_INTERNSHIP + " TEXT,"
                + CTC + " TEXT,"
                + LAST_DATE_TO_APPLY + " TEXT,"
                + DESCRIPTION_ABOUT_ROLE + " TEXT,"
                + SKILLS_REQUIRED + " TEXT,"
                + NO_OF_OPENINGS + " TEXT,"
                + PERKS + " TEXT,"
                + NO_OF_STUDENTS_APPLIED + " TEXT,"
                + RESPONSIBILITIES + " TEXT,"
                + ROLE_CATEGORY + " TEXT,"
                + MINIMUM_QUALIFICATION + " TEXT,"
                + COMPANY_LINKS + " TEXT,"
                + POSTED_DATE + " TEXT,"
                + TYPE + " TEXT)"
                )
        db.execSQL(query)
    }
    fun add(
        _id:String?,
        nameOfCompany: String?,
        aboutCompany: String?,
        nameOfRole: String?,
        typeOfJob: String?,
        location: String?,
        startDate: String?,
        durationOfInternship: String?,
        ctc: String?,
        lastDateToApply: String?,
        descriptionAboutRole: String?,
        skillsRequired: String?,
        noOfOpenings: String?,
        perks: String?,
        noOfStudentsApplied: String?,
        responsibilities: String?,
        roleCategory: String?,
        minimumQualification: String?,
        companyLinks: String?,
        postedDate: String?,
        type: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_COL,_id)
        values.put(NAME_OF_COMPANY, nameOfCompany)
        values.put(ABOUT_COMPANY, aboutCompany)
        values.put(NAME_OF_ROLE, nameOfRole)
        values.put(TYPE_OF_JOB, typeOfJob)
        values.put(LOCATION, location)
        values.put(START_DATE, startDate)
        values.put(DURATION_OF_INTERNSHIP, durationOfInternship)
        values.put(CTC, ctc)
        values.put(LAST_DATE_TO_APPLY, lastDateToApply)
        values.put(DESCRIPTION_ABOUT_ROLE, descriptionAboutRole)
        values.put(SKILLS_REQUIRED, skillsRequired)
        values.put(NO_OF_OPENINGS, noOfOpenings)
        values.put(PERKS, perks)
        values.put(NO_OF_STUDENTS_APPLIED, noOfStudentsApplied)
        values.put(RESPONSIBILITIES, responsibilities)
        values.put(ROLE_CATEGORY, roleCategory)
        values.put(MINIMUM_QUALIFICATION, minimumQualification)
        values.put(COMPANY_LINKS, companyLinks)
        values.put(POSTED_DATE, postedDate)
        values.put(TYPE, type)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "DataBase"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "jobAppliedTable"
        private const val ID_COL = "_id"
        private const val NAME_OF_COMPANY ="nameOfCompany"
        private const val ABOUT_COMPANY ="aboutCompany"
        private const val NAME_OF_ROLE ="nameOfRole"
        private const val TYPE_OF_JOB ="typeOfJob"
        private const val LOCATION ="location"
        private const val START_DATE ="startDate"
        private const val DURATION_OF_INTERNSHIP ="durationOfInternship"
        private const val CTC ="costToCompany"
        private const val LAST_DATE_TO_APPLY ="lastDateToApply"
        private const val DESCRIPTION_ABOUT_ROLE ="descriptionAboutRole"
        private const val SKILLS_REQUIRED ="skillsRequired"
        private const val NO_OF_OPENINGS ="noOfOpening"
        private const val PERKS ="perks"
        private const val NO_OF_STUDENTS_APPLIED ="noOfStudentsApplied"
        private const val RESPONSIBILITIES ="responsibilities"
        private const val ROLE_CATEGORY ="roleCategory"
        private const val MINIMUM_QUALIFICATION ="minimumQualification"
        private const val COMPANY_LINKS ="companyLinks"
        private const val POSTED_DATE ="postedDate"
        private const val TYPE ="type"
    }
    fun read(): ArrayList<AppliedJobsModelSQLite> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val appliedJobsList = ArrayList<AppliedJobsModelSQLite>()
        if (cursor.moveToFirst()) {
            do {
                val job = AppliedJobsModelSQLite(
                    _id = cursor.getString(cursor.getColumnIndexOrThrow(ID_COL)),
                    nameOfCompany = cursor.getString(cursor.getColumnIndexOrThrow(NAME_OF_COMPANY)),
                    aboutCompany = cursor.getString(cursor.getColumnIndexOrThrow(ABOUT_COMPANY)),
                    nameOfRole = cursor.getString(cursor.getColumnIndexOrThrow(NAME_OF_ROLE)),
                    typeOfJob = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_OF_JOB)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(LOCATION)),
                    startDate = cursor.getString(cursor.getColumnIndexOrThrow(START_DATE)),
                    durationOfInternship = cursor.getString(cursor.getColumnIndexOrThrow(DURATION_OF_INTERNSHIP)),
                    costToCompany = cursor.getString(cursor.getColumnIndexOrThrow(CTC)),
                    lastDateToApply = cursor.getString(cursor.getColumnIndexOrThrow(LAST_DATE_TO_APPLY)),
                    descriptionAboutRole = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_ABOUT_ROLE)),
                    skillsRequired = cursor.getString(cursor.getColumnIndexOrThrow(SKILLS_REQUIRED)),
                    noOfOpening = cursor.getString(cursor.getColumnIndexOrThrow(NO_OF_OPENINGS)),
                    perks = cursor.getString(cursor.getColumnIndexOrThrow(PERKS)),
                    noOfStudentsApplied = cursor.getString(cursor.getColumnIndexOrThrow(NO_OF_STUDENTS_APPLIED)),
                    responsibilities = cursor.getString(cursor.getColumnIndexOrThrow(RESPONSIBILITIES)),
                    roleCategory = cursor.getString(cursor.getColumnIndexOrThrow(ROLE_CATEGORY)),
                    minimumQualification = cursor.getString(cursor.getColumnIndexOrThrow(MINIMUM_QUALIFICATION)),
                    companyLinks = cursor.getString(cursor.getColumnIndexOrThrow(COMPANY_LINKS)),
                    postedDate = cursor.getString(cursor.getColumnIndexOrThrow(POSTED_DATE)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))
                )
                appliedJobsList.add(job)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return appliedJobsList
    }
    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_NAME)
        db.close()
    }
}
