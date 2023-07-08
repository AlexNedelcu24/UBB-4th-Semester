#Complexitate O(NxM)

def dfs(mat, i, j):
    # verificăm dacă poziția este validă și dacă este 0
    if i < 0 or i >= len(mat) or j < 0 or j >= len(mat[0]) or mat[i][j] != 0:
        return
    # marcam elementul ca vizitat
    mat[i][j] = -1
    # apelăm recursiv funcția pentru vecinii elementului
    dfs(mat, i+1, j)
    dfs(mat, i-1, j)
    dfs(mat, i, j+1)
    dfs(mat, i, j-1)

def modify(mat):
    # parcurgem prima și ultima linie
    for j in range(len(mat[0])):
        dfs(mat, 0, j)
        dfs(mat, len(mat)-1, j)
    # parcurgem prima și ultima coloană
    for i in range(1, len(mat)-1):
        dfs(mat, i, 0)
        dfs(mat, i, len(mat[0])-1)
    # modificăm matricea în consecință
    for i in range(len(mat)):
        for j in range(len(mat[0])):
            if mat[i][j] == 0:
                mat[i][j] = 1
            elif mat[i][j] == -1:
                mat[i][j] = 0

    return mat


mat = [    [1, 0, 1, 1],
    [1, 0, 1, 1],
    [1, 1, 0, 1],
    [0, 1, 1, 1]
]

def test_modify():
    assert modify([ [1, 0, 1, 1],
    [1, 0, 1, 1],
    [1, 1, 0, 1],
    [0, 1, 1, 1]
]) == [ [1, 0, 1, 1],
    [1, 0, 1, 1],
    [1, 1, 1, 1],
    [0, 1, 1, 1]
]
    
    assert modify([ [1, 0, 1, 1],
    [1, 1, 1, 1],
    [1, 0, 0, 1],
    [0, 1, 1, 1]
]) == [ [1, 0, 1, 1],
    [1, 1, 1, 1],
    [1, 1, 1, 1],
    [0, 1, 1, 1]
]

    
test_modify()
