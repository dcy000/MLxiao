package com.example.lenovo.rto.unit;

/**
 * Created by huyin on 2017/9/13.
 */

public class UnitBody {
     int scene_id;
     String query;
     String session_id;

     public UnitBody(int scene_id, String query, String session_id) {
          this.scene_id = scene_id;
          this.query = query;
          this.session_id = session_id;
     }

     public int getScene_id() {
          return scene_id;
     }

     public void setScene_id(int scene_id) {
          this.scene_id = scene_id;
     }

     public String getQuery() {
          return query;
     }

     public void setQuery(String query) {
          this.query = query;
     }

     public String getSession_id() {
          return session_id;
     }

     public void setSession_id(String session_id) {
          this.session_id = session_id;
     }
}
