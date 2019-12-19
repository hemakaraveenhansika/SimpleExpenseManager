package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBcollaborator;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class DatabaseAccountDAO implements AccountDAO {
    private DBcollaborator databaseHelper;

    public DatabaseAccountDAO(DBcollaborator databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DBcollaborator.TABLE_ACCOUNTS, new String[] {DBcollaborator.KEY_ACCOUNT_NO}, null, null, null, null, null);

        //  adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding account
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        //  list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DBcollaborator.TABLE_ACCOUNTS, null, null, null, null, null, null);

        //  adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DBcollaborator.TABLE_ACCOUNTS, null, DBcollaborator.KEY_ACCOUNT_NO + "=?",
                new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcollaborator.KEY_ACCOUNT_NO, account.getAccountNo()); // Account Number
        values.put(DBcollaborator.KEY_BANK_NAME, account.getBankName()); // Bank
        values.put(DBcollaborator.KEY_ACCOUNT_HOLDER_NAME, account.getAccountHolderName()); // Holder
        values.put(DBcollaborator.KEY_BALANCE, account.getBalance()); // Balance

        // Inserting
        db.insert(DBcollaborator.TABLE_ACCOUNTS, null, values);

        db.close(); // Close db connection

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DBcollaborator.TABLE_ACCOUNTS, DBcollaborator.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(DBcollaborator.KEY_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DBcollaborator.KEY_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating
        db.update(DBcollaborator.TABLE_ACCOUNTS, values, DBcollaborator.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }
}
