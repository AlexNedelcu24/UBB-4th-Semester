

#Complexitate O(n)
def suma2(m,l):
    sume = []
    for i in l:
        s = 0
        for x in range(i[0][0], i[1][0]+1):
            for y in range(i[0][1], i[1][1]+1):
                s = s + m[x][y]
        sume.append(s)

    return sume



#Complexity O(n*m+p)
#p - pairs
def suma(matrix, pairs):
    n, m = len(matrix), len(matrix[0])

    # Calculăm matricea cu sumele parțiale ale elementelor
    dp = [[0] * (m+1) for _ in range(n+1)]
    for i in range(n):
        for j in range(m):
            dp[i+1][j+1] = matrix[i][j] + dp[i][j+1] + dp[i+1][j] - dp[i][j]

    # Calculăm suma submatricelor pentru fiecare pereche din listă
    sums = []
    for pair in pairs:
        x1, y1 = pair[0][0]+1, pair[0][1]+1
        x2, y2 = pair[1][0]+1, pair[1][1]+1
        s = dp[x2][y2] - dp[x1-1][y2] - dp[x2][y1-1] + dp[x1-1][y1-1]
        sums.append(s)

    return sums




def test_suma():
    assert suma([[0, 2, 5, 4, 1],
[4, 8, 2, 3, 7],
[6, 3, 4, 6, 2],
[7, 3, 1, 8, 3],
[1, 5, 7, 9, 4]] ,
 ( ((1, 1), (3, 3)), ((2, 2), (4, 4))  )) == [38, 44]
    
    assert suma([[0, 2, 5, 4, 1],
[4, 8, 2, 3, 7],
[6, 3, 4, 6, 2],
[7, 3, 1, 8, 3],
[1, 5, 7, 9, 4]] ,
 ( ((0, 0), (1, 1)), ((3, 3), (4, 4))  )) == [14, 24]
    

test_suma()


