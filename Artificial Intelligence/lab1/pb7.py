import random

#Determining the k-th largest element of an array
#Complexity O(n)

def quickselect(arr, k):
    if len(arr) == 1:
        return arr[0]

    pivot = random.choice(arr)

    lows = [x for x in arr if x < pivot]
    highs = [x for x in arr if x > pivot]
    pivots = [x for x in arr if x == pivot]

    if k < len(lows):
        return quickselect(lows, k)
    elif k < len(lows) + len(pivots):
        return pivots[0]
    else:
        return quickselect(highs, k - len(lows) - len(pivots))

def k_largest_element(arr, k):
    return quickselect(arr, len(arr) - k)

def test():
    assert k_largest_element([7, 4, 6, 3, 9, 1], 3) == 6
    assert k_largest_element([7, 4, 6, 3, 9, 1], 5) == 3
    assert k_largest_element([7, 4, 6, 3, 9, 1], 6) == 1

test()