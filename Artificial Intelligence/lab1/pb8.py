#Generate all numbers between 1 and n in binary representation

#Complexity O(n*log(numar))
def decimal_to_binary(numar):
    if numar == 0:
        return '0'
    rezultat = ''
    while numar > 0:
        rest = numar % 2
        rezultat = str(rest) + rezultat
        numar = numar // 2
    return rezultat

#Complexity O(n*logn)
def numbers(n):
    a = []
    for i in range(1, n+1):
        
        binary = decimal_to_binary(i)
        a.append(binary)

    return a

def test_numbers():
    assert numbers(6) == ['1', '10', '11', '100', '101', '110']
    assert numbers(3) == ['1', '10', '11']
    assert numbers(5) == ['1', '10', '11', '100', '101']
 
test_numbers()

