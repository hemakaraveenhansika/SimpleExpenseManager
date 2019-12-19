package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBcollaborator;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseTransactionDAO implements TransactionDAO {
    private DBcollaborator databaseHelper;

    public DatabaseTransactionDAO(DBcollaborator databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcollaborator.KEY_ACCOUNT_NO, accountNo); // Account Num
        values.put(DBcollaborator.KEY_EXPENSE_TYPE, expenseType.name()); // Bank
        values.put(DBcollaborator.KEY_AMOUNT, amount); //    Holder
        values.put(DBcollaborator.KEY_DATE, new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date)); // Holder Name

        // Inserting
        db.insert(DBcollaborator.TABLE_TRANSACTIONS, null, values);

        db.close(); // Close db connection
        Log.d("Came Here", values.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DBcollaborator.TABLE_TRANSACTIONS, null, null, null, null, null, null);

        // adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DBcollaborator.TABLE_TRANSACTIONS, null, null, null, null, null, null, limit + "");

        // adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return
        return transactionList;
    }
}
