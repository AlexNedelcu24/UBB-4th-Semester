#Calculate the majority element of an array of numbers
#Complexity O(n)

def majoritar(s):
    m = s[0]
    count = 1

    for i in range(1, len(s)):
        if s[i] == m:
            count +=1
        elif count == 0:
            count = 1
            m = s[i]
        else:
            count -=1

    c = 0
    for i in s:
        if i == m:
            c +=1 

    if c > len(s)/2:
        return m
    else:
        return "Nu exista"
    
      
def test_majoritar():
   assert majoritar([2,7,7,2,2,5,2,3,1,2,2]) == 2
   assert majoritar([8,4,8,8,8,5,8,8,8,5,5]) == 8
   assert majoritar([1,1,1,2,2,2]) == "Nu exista"
   assert majoritar([2,7,2,7,2,7,2,7,2,7,2]) == 2

test_majoritar()