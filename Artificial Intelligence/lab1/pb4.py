
#The function puts the words of a text into a list
def spl(text):
    words = []
    begin = 0
    end = len(text)
    k = ' '
        
    for i in range(end):
        if text[i:i+len(k)] == k:
            words.append(text[begin:i])
            begin = i+len(k)
    
    words.append(text[begin:end])
    
    return words


#The function returns the unique words in a text
#Complexity O(2n) = O(n)
def aparitii(text):
    text = text.lower()
    words = spl(text)

    count = {}

    for i in words:
      if i in count:
         count[i] += 1
      else:
         count[i] = 1

    one_word = []

    for word, count in count.items():
      if count == 1:
          one_word.append(word)

    return one_word


def test_aparitii():
   assert aparitii("ana are ana are mere rosii ana Mere") == ['rosii']
   assert aparitii("aici aici este este un nou nou model") == ['un', 'model']
   assert aparitii("nu nu") == []


test_aparitii()