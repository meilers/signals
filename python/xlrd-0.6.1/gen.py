import xlrd
wb = xlrd.open_workbook('bars.xls')

wb.sheet_names()

sh = wb.sheet_by_index(0)

filename = "bars.sql"
file = open(filename, "w")

row = sh.row_values(0)
city = row[0]
state = row[1]
country = row[2]

"""str = "SELECT cityId FROM cities WHERE city = \'" + row[0] + "\', state = \'" + row[1] + "\', country = \'" + row[2] + "\';\n" """
""" file.write(str) """


for rownum in range(sh.nrows):
	if rownum > 3:
		row = sh.row_values(rownum)
 		name = row[0]
		genre = row[1]
		address = row[2]
		lat = row[3]
		lng = row[4]
		
		str = u"INSERT INTO places (cityId, genre, name, address, latitude, longitude) SELECT c.cityId, " + unicode(genre) + ", \'" + name + "\', \'" + address + "\', " + unicode(lat) + ", " + unicode(lng) + " FROM cities c WHERE city = \'" + city + "\' AND state = \'" + state + "\' AND country = \'" + country + "\';\n"
		file.write(str.encode('utf8'))


