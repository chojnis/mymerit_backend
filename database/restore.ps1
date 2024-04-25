# settings
$username = "root"
$password = "password"
$database = "mymerit"
$dumpFolder = "dump/mymerit/"

# get all bson files in the dump folder
$files = Get-ChildItem -Path $dumpFolder -Filter "*.bson"

# iterate through bson files to restore all of them
foreach ($file in $files) {
    # check if file exists
    if (Test-Path $dumpFolder$file) {
        # exec mongorestore
        & .\mongorestore --username=$username --password=$password --db=$database --authenticationDatabase=admin $dumpFolder$file
        Write-Host ("Przywracanie bazy danych z pliku $file zakonczone.")
    } else {
        Write-Host ("Plik $file nie istnieje.")
    }
}
