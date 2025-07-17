package com.study.S2A211.webportal.user;

public record UserData(
  /** ユーザーID(メールアドレス):主キー、必須入力、メールアドレス形式*/
  String userId,

  /**パスワード:必須入力、長さ4から100桁まで、 半角英数字のみ */
  String password,

  /** ユーザー名: 必須入力 */
  String userName,

  /** 権限：管理 : "ROLE_ADMIN"、 上位: "ROLE_TOP"、 一般 : "ROLE_GENERAL"*/
  String role,

  boolean enabled){
}
