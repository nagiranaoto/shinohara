🛠 環境構築
📌 Java のインストール
Java がインストールされていることを確認してください。
インストールされていない場合は、公式サイト からダウンロードしてインストールしてください。

インストール確認コマンド：

sh
コピーする
編集する
java -version
javac -version
📌 PostgreSQL のインストール
macOS に PostgreSQL をインストールする場合（Homebrew を使用）
sh
コピーする
編集する
brew install postgresql
brew services start postgresql
📌 PostgreSQL にデータベースを作成
PostgreSQL にログイン
sh
コピーする
編集する
psql -U postgres
データベース・ユーザーの作成
sql
コピーする
編集する
CREATE DATABASE mydatabase;
CREATE USER myuser WITH PASSWORD 'mypassword';
ALTER ROLE myuser SET client_encoding TO 'utf8';
GRANT ALL PRIVILEGES ON DATABASE mydatabase TO myuser;
🔹 psql -U postgres ができない場合は、以下の記事を参照してください。
🔗 PostgreSQL Fatal: role "postgres" does not exist

🚀 Java アプリケーションの実行
📌 Spring Boot アプリの起動
Spring Boot アプリケーションを実行するには、SpringThymeleafAppApplication.java の main メソッドを実行してください。

java
コピーする
編集する
public static void main(String[] args) {
    SpringApplication.run(SpringThymeleafAppApplication.class, args);
}
🔑 PostgreSQL へのログイン
作成したデータベース mydatabase にログインするには、以下のコマンドを使用します。

sh
コピーする
編集する
psql -U myuser -d mydatabase
このコマンドの意味：

-U myuser → ユーザー myuser でログイン
-d mydatabase → データベース mydatabase に接続
📌 データの登録（users テーブルにデータを挿入）
アプリケーション起動後、データベースに以下のデータを追加できます。

sql
コピーする
編集する
INSERT INTO users (email, username) VALUES ('john.doe@example.com', 'johndoe');
INSERT INTO users (email, username) VALUES ('jane.smith@example.com', 'janesmith');
INSERT INTO users (email, username) VALUES ('bob.jones@example.com', 'bobjones');
📌 動作確認
アプリを起動後、ブラウザで以下の URL にアクセスしてください。

bash
コピーする
編集する
http://localhost:8080/users
→ 登録されたユーザー一覧が表示されることを確認できます。

✅ まとめ
ステップ	コマンド
Java のインストール確認	java -version
PostgreSQL のインストール	brew install postgresql
PostgreSQL を起動	brew services start postgresql
データベースを作成	CREATE DATABASE mydatabase;
ユーザーを作成	CREATE USER myuser WITH PASSWORD 'mypassword';
ログイン	psql -U myuser -d mydatabase
データを挿入	INSERT INTO users (email, username) VALUES ('john.doe@example.com', 'johndoe');
Spring Boot アプリを起動	mvn spring-boot:run
Web ページを開く	http://localhost:8080/users
💡 これで、Spring Boot + PostgreSQL + Thymeleaf を使用した Web アプリケーションが動作します！ 🚀