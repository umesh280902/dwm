n = int(input("Enter the number of websites: "))
relation = [[0] * n for _ in range(n)]

for i in range(n):
    print(f"Enter '1' if website {i + 1} contains a hyperlink directing to following website(s) else enter '0':")
    for j in range(n):
        x = int(input())
        relation[i][j] = x

pageRank = [1.0 / n] * n

print("Initially the page rank for each page is:", end=" ")
for pr in pageRank:
    print(pr, end=" ")

m = 0
while m < 3:
    m += 1
    temp = [0.0] * n
    for i in range(n):
        for j in range(n):
            if relation[j][i] == 1:
                count = sum(relation[j][k] == 1 for k in range(n))
                y = pageRank[j] / count if count != 0 else 0
                temp[j] = y
        sum_temp = sum(temp)
        pageRank[i] = sum_temp

    print(f"\nThe new Page Ranks after iteration {m} are:", end=" ")
    for pr in pageRank:
        print(pr, end=" ")

maxPageRank = pageRank[0]
minPageRank = pageRank[0]
maxIndex = 0
minIndex = 0

for i in range(1, n):
    if pageRank[i] > maxPageRank:
        maxPageRank = pageRank[i]
        maxIndex = i
    if pageRank[i] < minPageRank:
        minPageRank = pageRank[i]
        minIndex = i

print(f"\nWebsite {maxIndex + 1} has the highest page rank whereas Website {minIndex + 1} has the lowest page rank.")
