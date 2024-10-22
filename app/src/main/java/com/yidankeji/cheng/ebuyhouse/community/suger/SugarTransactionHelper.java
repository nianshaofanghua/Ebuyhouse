package com.yidankeji.cheng.ebuyhouse.community.suger;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SugarTransactionHelper {

    public static void doInTransaction(Callback callback) {
        SQLiteDatabase database = SugarContext.getSugarContext().getSugarDb().getDB();
        database.beginTransaction();

        try {
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Callback executing within transaction");
            callback.manipulateInTransaction();
            database.setTransactionSuccessful();
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Callback successfully executed within transaction");
        } catch (Throwable e) {
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Could execute callback within transaction", e);
        } finally {
            database.endTransaction();
        }
    }

    public interface Callback {
        void manipulateInTransaction();
    }
}
