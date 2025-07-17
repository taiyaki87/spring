package com.study.S2A211.webportal.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 入力されたユーザーIDとパスワードにマッチするユーザーデータを取得します。
   *
   * @param userId 取得するユーザーデータのユーザーID
   * @param password 取得するユーザーデータのパスワード
   * @return 入力されたユーザーIDのユーザーデータ（存在しない場合はnull）
   */
  public UserData login(String userId, String password) {
    /*SQL ログインチェック */
    final String SQL_LOGIN = "SELECT user_name, role,enabled FROM user_m WHERE user_id = :userId AND password = :password AND enabled =true";

    //パラメータを格納するためのマップを作成
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("password", password);

    //データベースのクエリを実行し、結果を取得
    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LOGIN, params);

    UserData userData = null;
    if (resultList.size() == 1) {
      Map<String, Object> item = resultList.get(0);
      String userName = (String) item.get("user_name");
      String role = (String) item.get("role");
      boolean enabled = (boolean) item.get("enabled");
      userData = new UserData(userId, "****", userName, role, enabled);
    }
    return userData;
  }
}
