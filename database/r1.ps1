# settings
$username="root"
$password="password"
$database="mymerit"
$location = Get-Location
$reg = "dump\mymerit\S*\.bson"

# iterate through bson files to restore all of them
foreach($file in [RegEx]::Matches($file1,$reg).value)  {
		# exec mongorestore
		# mongorestore --username="$username" --password="$password" --db="$database" --authenticationDatabase=admin "$file"
		write-host("Przywracanie bazy danych z pliku $file zakonczone.")
}
	write-host("Przywracanie bazy danych z pliku $content zakonczone.")
    
