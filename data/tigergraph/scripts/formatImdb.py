import csv

# files have to exist
READ_PATH = 'data/tigergraph/files/csv/movies_no_date.csv'
WRITE_PATH = 'data/tigergraph/files/csv/f_imdb.csv'

with open(READ_PATH) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    rowlist = []

    for row in csv_reader:
        if row[16] == '':
            row[16] = 0
        if row[17] == '':
            row[17] = 0
        entry = [row[0], row[16], row[17], row[18]]
        rowlist.append(entry)

with open(WRITE_PATH, mode='w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(rowlist)

print("Conversion Done!")
