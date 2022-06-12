import csv

# files have to exist
READ_PATH_1 = 'data/tigergraph/files/csv/users.csv'
READ_PATH_2 = 'data/tigergraph/files/csv/comments_no_date_short.csv'
WRITE_PATH = 'data/tigergraph/files/csv/f_comments_no_date_short.csv'

hm = {}
rowlist = []

with open(READ_PATH_1) as csv_file1:
    csv_reader = csv.reader(csv_file1, delimiter=',')
    for row in csv_reader:
        hm[row[2]] = row[0]
        
with open(READ_PATH_2) as csv_file2:
    csv_reader = csv.reader(csv_file2, delimiter=',')
    for row in csv_reader:
        if row[1] in hm.keys():
            # ..., userid
            rowlist.append(row + [hm.get(row[1])])

with open(WRITE_PATH, mode='w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(rowlist)

print("Conversion Done!")