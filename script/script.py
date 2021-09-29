import mysql.connector

# Settings
database = mysql.connector.connect(
  host="127.0.0.1",
  user="root",
  password="",
  database="bot"
)
table = "bot"

# Script
cursor = database.cursor()

for line in open("emails.txt", "r"):
    email = line.strip().lower();

    sql = "SELECT Email FROM " + table + " WHERE Email = %s"
    cursor.execute(sql, (email,))

    result = cursor.fetchall()

    if len(result) < 1:
        sql = "INSERT INTO " + table + " (Email) VALUES (%s)"
        cursor.execute(sql, (email,))
        print(email + " inserted!")

database.commit()