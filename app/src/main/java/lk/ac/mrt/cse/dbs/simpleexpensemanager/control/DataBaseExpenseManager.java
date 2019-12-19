package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBcollaborator;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;

public class DataBaseExpenseManager extends ExpenseManager{
    private DBcollaborator db;
    public DataBaseExpenseManager(DBcollaborator db) {
        this.db = db;
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO DatabaseTransactionDAO = new DatabaseTransactionDAO(db);
        setTransactionsDAO(DatabaseTransactionDAO);

        AccountDAO DatabaseAccountDAO = new DatabaseAccountDAO(db);
        setAccountsDAO(DatabaseAccountDAO);

    }
}
