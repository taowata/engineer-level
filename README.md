## アプリ概要
* エンジニアの能力を可視化するアプリ
* GitHubAPIにより、GitHubユーザーの情報を取得します。
* お気に入りエンジニアを保存することができます


ログイン画面 | エンジニア検索 | お気に入りリスト | お気に入り詳細画面 
---- | ---- | ---- | ---- 
<img src="https://user-images.githubusercontent.com/57245344/102074118-a1052080-3e47-11eb-9944-ec6715b6533d.jpg" width="320"/>   | <img src="https://user-images.githubusercontent.com/57245344/102074152-aa8e8880-3e47-11eb-9fad-eb8c27bb5062.jpg" width="320"/>   | <img src="https://user-images.githubusercontent.com/57245344/102074198-c003b280-3e47-11eb-84bd-5624b73121e0.jpg" width="320"/>  | <img src="https://user-images.githubusercontent.com/57245344/102074220-c7c35700-3e47-11eb-8236-97c2ffe3ce58.jpg" width="320"/>

## 機能
* メールアドレスによるサインアップ、ログイン(Firebase利用)
* エンジニア検索
* コントリビューション数、フォロワー数、獲得スター数の表示
* 使用言語トップ4を棒グラフで表示(Byte数)
* お気に入りのエンジニアを保存(Firebase Firestore)

## 工夫した点
* 年間コントリビューション数がGitHubAPIでは取得できなかったため、Jsoupを用いてスクレイピングにより取得した。
* 各リポジトリについて、言語ごとのByte数を計算し使用言語に順位付けする処理を実装した。

## やりきれなかった点

* GitHubアカウントによるユーザー認証

  現状はGitHubAPIを利用する際に一つの個人アクセストークンを使っている。GitHubアカウントでユーザー認証できるようにし、認証情報からアクセストークンを取得する仕様にできたらよかった。


## ライブラリ
* Android Jetpack
  * Foundation
    * AppCompat
    * Android KTX
  * Architecture
    * Data Binding
    * Lifecycles
    * LiveData
    * Navigation
    * ViewModel
  * UI
    * Fragment
    * ConstraintLayout
    * RecyclerView
* Third party
  * Kotlin Coroutines
  * Retrofit2
  * Moshi
  * MPAndroidChart
  * FirebaseUI
  * Firebase Firestore
  * Jsoup
  * Glide

