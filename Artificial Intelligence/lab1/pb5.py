#Complexitate O(n)

def repet(s):
    n = len(s)
    s2 = [0] * n

    for x in s:
       if s2[x] == 1:
        return x
       else:
          s2[x] += 1

def test_repet():
   assert repet([1,2,3,4,2]) == 2
   assert repet([1,2,3,4,4]) == 4
   assert repet([1,2,3,4,1]) == 1

test_repet()