package com.example.module_control_volume.wifi;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("android.arch.persistence.room.RoomProcessor")
@SuppressWarnings("unchecked")
public class WifiDao_Impl implements WifiDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfWifiEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByKey;

  public WifiDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWifiEntity = new EntityInsertionAdapter<WifiEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `WifiCache`(`BSSID`,`SSID`,`frequency`,`password`,`capabilities`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WifiEntity value) {
        if (value.BSSID == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.BSSID);
        }
        if (value.SSID == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.SSID);
        }
        stmt.bindLong(3, value.frequency);
        if (value.password == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.password);
        }
        if (value.capabilities == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.capabilities);
        }
      }
    };
    this.__preparedStmtOfDeleteByKey = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM WifiCache WHERE `BSSID`=?";
        return _query;
      }
    };
  }

  @Override
  public void insertWifiCache(WifiEntity cacheEntity) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfWifiEntity.insert(cacheEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByKey(String bssid) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByKey.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (bssid == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, bssid);
      }
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteByKey.release(_stmt);
    }
  }

  @Override
  public WifiEntity queryByKey(String bssid) {
    final String _sql = "SELECT * FROM WifiCache WHERE `BSSID`=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (bssid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, bssid);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfBSSID = _cursor.getColumnIndexOrThrow("BSSID");
      final int _cursorIndexOfSSID = _cursor.getColumnIndexOrThrow("SSID");
      final int _cursorIndexOfFrequency = _cursor.getColumnIndexOrThrow("frequency");
      final int _cursorIndexOfPassword = _cursor.getColumnIndexOrThrow("password");
      final int _cursorIndexOfCapabilities = _cursor.getColumnIndexOrThrow("capabilities");
      final WifiEntity _result;
      if(_cursor.moveToFirst()) {
        _result = new WifiEntity();
        _result.BSSID = _cursor.getString(_cursorIndexOfBSSID);
        _result.SSID = _cursor.getString(_cursorIndexOfSSID);
        _result.frequency = _cursor.getInt(_cursorIndexOfFrequency);
        _result.password = _cursor.getString(_cursorIndexOfPassword);
        _result.capabilities = _cursor.getString(_cursorIndexOfCapabilities);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<WifiEntity> getAllWifiCache() {
    final String _sql = "SELECT * FROM wificache";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfBSSID = _cursor.getColumnIndexOrThrow("BSSID");
      final int _cursorIndexOfSSID = _cursor.getColumnIndexOrThrow("SSID");
      final int _cursorIndexOfFrequency = _cursor.getColumnIndexOrThrow("frequency");
      final int _cursorIndexOfPassword = _cursor.getColumnIndexOrThrow("password");
      final int _cursorIndexOfCapabilities = _cursor.getColumnIndexOrThrow("capabilities");
      final List<WifiEntity> _result = new ArrayList<WifiEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final WifiEntity _item;
        _item = new WifiEntity();
        _item.BSSID = _cursor.getString(_cursorIndexOfBSSID);
        _item.SSID = _cursor.getString(_cursorIndexOfSSID);
        _item.frequency = _cursor.getInt(_cursorIndexOfFrequency);
        _item.password = _cursor.getString(_cursorIndexOfPassword);
        _item.capabilities = _cursor.getString(_cursorIndexOfCapabilities);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
