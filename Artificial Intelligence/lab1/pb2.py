import math

#Determine the Euclidean distance between 2 points
#Complexity O(1)

def distanta(punct1, punct2):

    x1 = punct1[0]
    y1 = punct1[1]
    x2 = punct2[0]
    y2 = punct2[1]

    dist = math.sqrt( pow(x2-x1,2) + pow(y2-y1,2) )

    return dist

def test_distanta():
    assert distanta((1,5),(4,1)) == 5.0
    assert distanta((0,1),(0,0)) == 1
    assert distanta((2,3),(4,-1)) == 4.47213595499958

test_distanta()