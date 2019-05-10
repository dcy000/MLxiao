package com.example.module_control_volume.wifi;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.Generated;

@Generated("android.arch.persistence.room.RoomProcessor")
@SuppressWarnings("unchecked")
public class WifiDB_Impl extends WifiDB {
  private volatile WifiDao _wifiDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `WifiCache` (`BSSID` TEXT NOT NULL, `SSID` TEXT NOT NULL, `frequency` INTEGER NOT NULL, `password` TEXT, `capabilities` TEXT, PRIMARY KEY(`BSSID`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"0204e3238b780261f9703e59b1e1a35f\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `WifiCache`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsWifiCache = new HashMap<String, TableInfo.Column>(5);
        _columnsWifiCache.put("BSSID", new TableInfo.Column("BSSID", "TEXT", true, 1));
        _columnsWifiCache.put("SSID", new TableInfo.Column("SSID", "TEXT", true, 0));
        _columnsWifiCache.put("frequency", new TableInfo.Column("frequency", "INTEGER", true, 0));
        _columnsWifiCache.put("password", new TableInfo.Column("password", "TEXT", false, 0));
        _columnsWifiCache.put("capabilities", new TableInfo.Column("capabilities", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWifiCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWifiCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWifiCache = new TableInfo("WifiCache", _columnsWifiCache, _foreignKeysWifiCache, _indicesWifiCache);
        final TableInfo _existingWifiCache = TableInfo.read(_db, "WifiCache");
        if (! _infoWifiCache.equals(_existingWifiCache)) {
          throw new IllegalStateException("Migration didn't properly handle WifiCache(com.example.module_control_volume.wifi.WifiEntity).\n"
                  + " Expected:\n" + _infoWifiCache + "\n"
                  + " Found:\n" + _existingWifiCache);
        }
      }
    }, "0204e3238b780261f9703e59b1e1a35f", "6da66e1c58430ab8491c08542f1b60b1");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "WifiCache");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `WifiCache`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public WifiDao wifiDao() {
    if (_wifiDao != null) {
      return _wifiDao;
    } else {
      synchronized(this) {
        if(_wifiDao == null) {
          _wifiDao = new WifiDao_Impl(this);
        }
        return _wifiDao;
      }
    }
  }
}
