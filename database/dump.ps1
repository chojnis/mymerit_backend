# settings
$username="root"
$password="password"
$database="mymerit"

& .\mongodump --username="$username" --password="$password" --db="$database" --authenticationDatabase=admin