#Calculate the dot product of two vectors
#Complexity O(n)

def suma(v1,v2):
    sum = 0
    for i in range(len(v1)):
        if v1[i] != 0 and v2[i] != 0:
            sum += v1[i] * v2[i]
        
    return sum

def test_suma():
    assert suma([1,0,2,0,3],[1,2,0,3,1]) == 4
    assert suma([1,0,7,0,1],[1,2,2,3,1]) == 16
    assert suma([1,0,4,0,0],[0,2,0,3,1]) == 0
    assert suma([1,0,4,0,0,7],[0,2,0,3,1,2]) == 14

test_suma()
