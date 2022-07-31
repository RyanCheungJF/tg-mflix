import csv

# files have to exist
READ_PATH = 'data/tigergraph/files/csv/movies_no_date.csv'
WRITE_PATH = 'data/tigergraph/files/csv/f_directors.csv'

with open(READ_PATH) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    rowlist = []

    for row in csv_reader:
        cleanedString = row[9].replace(',', '|').replace('"', '').replace('[', '').replace(']', '')
        if cleanedString != 'undefined':
            arr = cleanedString.split('|')
            for writer in arr:
                entry = [row[0], writer]
                rowlist.append(entry)

with open(WRITE_PATH, mode='w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(rowlist)

print("Conversion Done!")
