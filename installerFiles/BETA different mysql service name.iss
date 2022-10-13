; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "ERP_System"
#define MyAppVersion "1.0"
#define MyAppPublisher "ERP Technologies by Bici, Inc."
#define MyAppExeName "ERP System.exe"
#define MyAppAssocName MyAppName + " File"
#define MyAppAssocExt ".myp"
#define MyAppAssocKey StringChange(MyAppAssocName, " ", "") + MyAppAssocExt

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{C6EAA4F3-0F76-4DB1-80E1-9758075AD2CF}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName=C:\{#MyAppName}
ChangesAssociations=yes
DisableProgramGroupPage=yes
LicenseFile=C:\Users\user\Desktop\README.txt
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputDir=C:\Users\user\Desktop
OutputBaseFilename=setup_erp
SetupIconFile=C:\Users\user\Desktop\Windows setup.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\FinalApplication\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\FinalApplication\req\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "C:\erp_database.sql"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\loadDB.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\mysql-5.5.11-win32.msi"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\removeDB.bat"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Registry]
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocExt}\OpenWithProgids"; ValueType: string; ValueName: "{#MyAppAssocKey}"; ValueData: ""; Flags: uninsdeletevalue
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocName}"; Flags: uninsdeletekey
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName},0"
Root: HKA; Subkey: "Software\Classes\{#MyAppAssocKey}\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" ""%1"""
Root: HKA; Subkey: "Software\Classes\Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".myp"; ValueData: ""

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: msiexec; Parameters: "/i mysql-5.5.11-win32.msi /qn INSTALLDIR=""C:\mysql"""; WorkingDir:{app}; StatusMsg: Please wait while we install Mysql 5.5.11;  Flags: runhidden
Filename: C:\mysql\bin\mysqld.exe; Parameters:" --install MySQL_KLE"; WorkingDir: {app}; StatusMsg: Installing MySQL services; Description: Installing MySQL Service; Flags: runhidden
Filename: net.exe; Parameters: start MySQL_KLE; StatusMsg: Starting MySQL server; Description: Starting MySQL Server; Flags: runhidden
Filename: "{app}\loadDB.bat"; StatusMsg: Loading Database, press enter on password!; Description: Loading database, press enter on password!
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Code]
function MySQL_Is(): Boolean;
var
iResultCode: Integer;
begin
  Result := true;
  if (not RegKeyExists(HKLM, 'SOFTWARE\MySQL AB\MySQL Server 5.5')) or 
   (not FileExists(ExpandConstant('{reg:HKLM\SOFTWARE\MySQL AB\MySQL Server 5.5,Location}\bin\mysql.exe'))) 
  then begin
     ExtractTemporaryFile('mysql-5.5.11-win32.msi');
     Exec('msiexec.exe', '/i mysql-5.5.11-win32.msi /qn INSTALLDIR="C:\mysql"', 
      ExpandConstant('{tmp}'), SW_HIDE, ewWaitUntilTerminated, iResultCode);
         if not FileExists(ExpandConstant('{reg:HKLM\SOFTWARE\MySQL AB\MySQL Server 5.5,Location}\bin\mysql.exe')) then begin
            MsgBox('Something went wrong! Installation should be terminated', 
              mbInformation, MB_OK);
            Result := false;
         end;
  end;
end;

[UninstallRun]
Filename: "{app}\removeDB.bat"; Flags: runhidden

