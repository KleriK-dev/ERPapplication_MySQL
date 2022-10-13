cd /d C:\mysql\bin

*****Just press 2 times ENTER, there is no password!*****

mysql -uroot -p -e "create database erp_system;"

mysql -uroot -p erp_system < C:\ERP_System\erp_database.sql
