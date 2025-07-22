package com.study.S2A211.webportal.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;

/**
 * タスク管理にかかわるDBアクセスを実現するクラスです。
 *
 * 処理が継続でいない場合は、呼び出し元へ例外をスローします。
 *
 * @author 情報太郎
 */
@Repository
public class TaskRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 指定されたユーザーIDに関連するタスク情報を検索します。
   *
   * @param userId ユーザーID
   * @return 指定されたユーザーIDに関連するタスク情報のリスト
   */
  public TaskEntity findAll(String userId) {
    //SQL文（Javaからの埋め込み部分は:変数名で指定する）
    final String SQL_SELECT_ALL = "SELECT id , title, limitday, complete FROM task_t WHERE user_id = :userId order by limitday";

    //クエリのパラメータを設定するマップ（キーはSQLの:変数名と合わせる）
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);

    //クエリを実行し、結果を取得
    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ALL, params);

    //取得結果をTaskEntityに変換（map.getのkeyはDBのSELECT句と合わせる）
    TaskEntity entity = new TaskEntity();
    for (Map<String, Object> map : resultList) {
      int id = (Integer) map.get("id");
      String title = (String) map.get("title");
      Date limitday = (Date) map.get("limitday");
      boolean complete = (boolean) map.get("complete");

      TaskData data = new TaskData(id, userId, title, limitday, complete);
      entity.tasklist().add(data);
    }

    return entity;
  }

  /**
   * タスク情報を1件登録します。
   *
   * @param taskData 保存するタスクデータ
   * @return 更新された行数
   * @throws SQLException  更新に失敗した倍にスローされる例外
   */
  public int save(TaskData taskData) throws SQLException{
    //SQL文（Javaからの埋め込み部分は:変数名で指定する）
    final String SQL_INSERT_ONE = "INSERT INTO task_t(id, user_id, title, limitday,complete) VALUES((SELECT COALESCE(MAX(id),0) +1 FROM task_t), :userId, :title, :limitday,false)";

    //クエリのパラメータを設定するマップ（キーはSQLの:変数名と合わせる）
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", taskData.userId());
    params.put("title", taskData.title());
    params.put("limitday", taskData.limitday());

    //クエリを実行し、更新された行数を取得
    int updateRow = jdbc.update(SQL_INSERT_ONE, params);
    if (updateRow != 1) {
      throw new SQLException("更新に失敗しました  件数:" + updateRow);
    }
    return updateRow;
  }
  /**
   * タスク情報を１件削除します。
   *
   * @param id 削除するデータのID
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int delete(int id) throws SQLException {
    //SQL文(Javaからの埋め込み部分は:変数名で指定する)
    final String SQL_DELETE_ONE = "DELETE FROM taks_t WHERE id = :id";

    //クエリのパラメータを設定するマップ（キーはSQLの:変数名と合わせる
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);

    //クエリを実行し、更新された行数を取得
    int updateRow = jdbc.update(SQL_DELETE_ONE, params);
    if (updateRow != 1) {
      //更新件数が異常な場合は例外をスロー
      throw new SQLException("更新に失敗しました  件数: " + updateRow);
    }
    return updateRow;
  }
  /**
   * タスク情報を１件更新します。
   *
   * @param id 更新するデータのID
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int update(int id) throws SQLException {
    // SQL文(Javaからの埋め込み部分は:変数名で指定する)
    final String SQL_UPDATE_ONE = "UPDATE task_t SET coplete =true WHERE id = :id";

    //クエリのパラメータを設定するマップ（キーはSQLの:変数名と合わせる）
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    // クエリを実行し、更新された行数を取得
    int updateRow = jdbc.update(SQL_UPDATE_ONE, params);
    if (updateRow != 1) {
      // 更新件数が異常な場合は例外をスロー
      throw new SQLException("更新に失敗しました  件数: " + updateRow);
    }
    return updateRow;
  }


}
