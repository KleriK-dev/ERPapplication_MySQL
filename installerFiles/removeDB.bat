net stop mysql

sc delete mysql

wmic product where name="MySQL Server 5.5" call uninstall /NoInteractive

rmdir C:\mysql