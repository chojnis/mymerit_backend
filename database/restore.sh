#!/bin/bash

# settings
username="root"
password="password"
database="mymerit"
dumpFolder="dump/mymerit/"

# iterate through bson files to restore all of them
for file in "$dumpFolder"*.bson; do
	# check if file exists
	if [ -e "$file" ]; then
		# exec mongorestore
		mongorestore --username="$username" --password="$password" --db="$database" --authenticationDatabase=admin "$file"
		echo "Przywracanie bazy danych z pliku $file zakończone."
	else
		echo "Plik $file nie istnieje."
	fi
done
