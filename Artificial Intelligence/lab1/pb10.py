

def func(mat,n,m):
    
    for y in range(m):
        for x in range(n):
            if mat[x][y] == 1:
                
                return x+1
            
            

def find(mat):
    n, m = len(mat), len(mat[0])
    max_row, max_count = -1, -1
    
    # Căutare binară pe fiecare linie a matricei
    for i in range(n):
        left, right = 0, m-1
        while left <= right:
            mid = (left + right) // 2
            if mat[i][mid] == 1:
                left = mid + 1
            else:
                right = mid - 1
        if m - left > max_count:
            max_row, max_count = i, m - left
    
    return max_row




def test_func():
    mat = [[0,0,0,1,1],
        [0,1,1,1,1],
        [0,0,1,1,1]]
    
    assert find(mat) == 2

    mat = [[0,0,0,1,1],
        [0,0,1,1,1],
        [0,0,1,1,1]]
    
    assert find(mat) == 2

    mat3 = [[1,1],
        [0,0],
        [0,0]]
    
    assert find(mat3) == 1

#test_func()



mat = [[0,0,0,1,1],
        [0,1,1,1,1],
        [0,0,1,1,1]]
    
print( find(mat)  ) 