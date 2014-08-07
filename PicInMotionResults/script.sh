#must delete relevent data from 1st file to have appropriate 2nd file
if [ $# -ne 2 ]; then
	#cat $1\/2log_erased_data.csv | sed 's/\:\s/ /' > $1\/3final.csv 
	cat $1\/2log_erased_data.csv | sed 's/\:/ /g' > $1\/3final.csv
	echo "Date Hour Minute Second R G B\n$(cat $1\/3final.csv)" > $1\/3final.csv
	#use libreoffice to find delta seconds, delta r, delta g, delta b
else
	echo No argument supplied
fi
