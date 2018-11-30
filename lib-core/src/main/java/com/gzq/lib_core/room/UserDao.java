package com.gzq.lib_core.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gzq.lib_core.bean.UserInfoBean;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserInfoBean user);

    /**
     * 查询所有User
     *
     * @return
     */
    @Query("SELECT * FROM user")
    List<UserInfoBean> getUsers();

    /**
     * 根据id删除User
     *
     * @param bid
     */
    @Query("DELETE FROM user WHERE bid=:bid")
    void deleteById(String bid);

}
