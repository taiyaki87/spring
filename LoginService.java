package com.study.S2A211.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

/**
 * ログインサービスクラスです。
 * ログイン関連の操作を提供します。
 *
 * @author 情報太郎
 */
@Transactional
@Service
public class LoginService {
  /* セッションオブジェクト */
  @Autowired
  private HttpSession session;
  /** ユーザーリポジトリ */
  @Autowired
  private UserRepository userRepository;
  /**
   * ログイン処理を行います。
   *
   * @param userId   ユーザーID
   * @param password パスワード
   * @return ログイン成功時はtrue、失敗時はfalse
   */
  public boolean login(String userId, String password) {

    UserData userData = userRepository.login(userId, password);
    if (userData == null) {
      return false;
    }

    // セッションにユーザーデータを保存
    session.setAttribute("userData", userData); // もしこれが必要なら残す

    return true;
  }

  public void logout() {
      session.invalidate();
    }
}
