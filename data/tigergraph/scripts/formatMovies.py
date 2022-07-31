import csv

# files have to exist
READ_PATH = 'data/tigergraph/files/csv/movies_no_date.csv'
WRITE_PATH = 'data/tigergraph/files/csv/f_movies_no_date.csv'

with open(READ_PATH) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    rowlist = []
    for row in csv_reader:
        # plot
        if row[1] == 'undefined' or row[1] == "":
            row[1] = ""
        else:
            if row[1][0] != '"':
                row[1] = '"' + row[1]
            if row[1][-1] != '"':
                row[1] = row[1] + '"'
            temp = row[1][1: - 1].replace('"', '')
            row[1] = temp
        # genre
        row[2] = row[2].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        # cast
        row[4] = row[4].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        # num_mflix_comments (cannot be undefined)
        if row[5] == 'undefined' or row[5] == 'null':
            row[5] = 0
        # fullplot -> since we're only using plot, its easier to just not deal with this
        row[7] = ""
        # countries
        row[8] = row[8].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        # directors
        row[9] = row[9].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        # languages
        row[24] = row[24].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        # writers
        row[30] = row[30].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')

        # rotten tomatoes
        if row[20] == 'undefined' or row[20] == 'null':
            row[20] = 0
        if row[21] == 'undefined' or row[21] == 'null':
            row[21] = 0
        if row[22] == 'undefined' or row[22] == 'null':
            row[22] = 0

        # awards 11 wins 12 nominations 13 text
        wins = "'{ " + "'" + 'awards' + "' : " + "'" + str(row[11]) + "', "
        nominations = wins + "'" + 'nominations' + "' : " + "'" + str(row[12]) + "', "
        text = nominations + "'" + 'text'  + "' : " + "'" + str(row[13]) + "' }'" 
        row[11] = text
        row[12], row[13] = "", ""

        # imdb 16 rating 17 votes 18 id (new node)
        if row[16] == '':
            row[16] = 0
        if row[17] == '':
            row[17] = 0
        rowlist.append(row)


with open(WRITE_PATH, mode='w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(rowlist)

print("Conversion Done!")
