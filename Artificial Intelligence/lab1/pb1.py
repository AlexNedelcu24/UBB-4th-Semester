#Complexitate O(n)

def ultim(a):
    
    a = a.lower()
    b = a.split(" ")
    
    cuv = b[0]


    for x in b:
        if cuv < x:
            cuv = x

    return cuv

def test_ultim():
    assert ultim("Ana are mere rosii si galbene") == "si"
    assert ultim("Acolo este un Za laptop") == "za"
    assert ultim("Merge inapoi") == "merge"

test_ultim()