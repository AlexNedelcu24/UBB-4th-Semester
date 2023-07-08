# prerequisites
import os
import random 
import numpy as np 
import networkx as nx
import matplotlib.pyplot as plt 
import warnings 

from networkx.algorithms import community

warnings.simplefilter('ignore')

# read the network details
def readNet(fileName):
    f = open(fileName, "r")
    net = {}
    n = int(f.readline())
    net['noNodes'] = n
    mat = []
    for i in range(n):
        mat.append([])
        line = f.readline()
        elems = line.split(" ")
        for j in range(n):
            mat[-1].append(int(elems[j]))
    net["mat"] = mat 
    degrees = []
    noEdges = 0
    for i in range(n):
        d = 0
        for j in range(n):
            if (mat[i][j] == 1):
                d += 1
            if (j > i):
                noEdges += mat[i][j]
        degrees.append(d)
    net["noEdges"] = noEdges
    net["degrees"] = degrees
    f.close()
    return net


def readGML(fileName):
    
    
    graph = nx.read_gml(fileName)
    
    net = {}  
    net['noNodes'] = graph.number_of_nodes()
    net['noEdges'] = graph.number_of_edges()
    
    ma = []
    ma = nx.to_numpy_matrix(graph)
    mat = [[int(val) for val in row] for row in ma.tolist()]
    
    net["mat"] = mat 
    degrees = []
    noEdges = 0
    
    for i in range(net['noNodes']):
        d = 0
        for j in range(net['noNodes']):
            if (mat[i][j] == 1):
            #if (mat.item((i,j)) == 1):
                d += 1
            if (j > i):
                noEdges += mat[i][j]
        degrees.append(d)
    net["degrees"] = degrees
    return net  




# plot a network
def plotNetwork(network, communities):
    
    np.random.seed(123) #to freeze the graph's view (networks uses a random view)
    mat = np.array(network["mat"])
    A=np.matrix(mat)
    G=nx.from_numpy_matrix(A)
    pos = nx.spring_layout(G)  # compute graph layout
    plt.figure(figsize=(8, 8))  # image is 8 x 8 inches 
    nx.draw_networkx_nodes(G, pos, node_size=500, cmap=plt.cm.RdYlBu, node_color = communities)
    nx.draw_networkx_edges(G, pos, alpha=0.3)
    plt.show()


#print(network)
#print(network2)

#plotNetwork(network2)


def greedyCommunitiesDetectionByTool(network):
    # Input: a graph
    # Output: list of comunity index (for every node)

    from networkx.algorithms import community

    A=np.matrix(network["mat"])
    G=nx.from_numpy_matrix(A)
    communities_generator = community.girvan_newman(G)
    top_level_communities = next(communities_generator)
    sorted(map(sorted, top_level_communities))
    communities = [0 for node in range(network['noNodes'])]
    index = 1
    for community in sorted(map(sorted, top_level_communities)):
        for node in community:
            communities[node] = index
        index += 1
    return communities

#plotNetwork(network2, greedyCommunitiesDetectionByTool(network2))

#print(greedyCommunitiesDetectionByTool(network))
#print(greedyCommunitiesDetectionByTool(network2))

def get_edge_list(matrix):
    n = len(matrix)
    list = []
    
    for i in range(n):
        for j in range(i+1):
            if matrix[i][j] != 0:
                list.append((i,j))
    
    return list



def calculate_modularity(network, partition):
    """
    Calculează modularitatea unei rețele dată o anumită partiționare a nodurilor.
    """
    Q = 0
    m = network['noEdges']
    for i in range(network['noNodes']):
        ki = network['degrees'][i]
        for j in range(network['noNodes']):
            kj = network['degrees'][j]
            Aij = network['mat'][i][j]
            if partition[i] == partition[j]:
                Q += (Aij - ki*kj/(2*m))
    Q = Q/(2*m)
    return Q

def greedy_algorithm(network):
    """
    Algoritmul Greedy pentru detectarea comunităților dintr-o rețea.
    """
    # la început, fiecare nod aparține unei comunități diferite
    partition = list(range(network['noNodes']))
    Mod = calculate_modularity(network, partition)
    
    while True:
        max_Mod = 0
        max_i = -1
        max_j = -1
        
        for i in range(network['noNodes']):
            for j in range(i+1, network['noNodes']):
                if network['mat'][i][j] == 1:
                # dacă nodurile i și j sunt în comunități diferite
                    if partition[i] != partition[j]:
                        # unim comunitățile și calculăm noua modularitate
                        new_partition = partition.copy()
                        new_partition[i] = new_partition[j]
                        new_Mod = calculate_modularity(network, new_partition) - Mod 
                        # dacă noua modularitate este maximă până acum, o memorăm
                        if new_Mod > max_Mod:
                            max_Mod = new_Mod
                            max_i = i
                            max_j = j
        #ieșim din buclă
        if max_Mod <= 0:
            break
        #unim comunitățile și actualizăm valoarea modularității
        partition[max_i] = partition[max_j]
        Mod += max_Mod
        #print(Mod)
    
    return partition








def calculate_modularity2(network, partition):
    """
    Calculează modularitatea unei rețele dată o anumită partiționare a nodurilor.
    """
    Q = 0
    m = network['noEdges']
    for i in range(network['noNodes']):
        ki = network['degrees'][i]
        for j in range(network['noNodes']):
            kj = network['degrees'][j]
            Aij = network['mat'][i][j]
            if partition[i] == partition[j]:
                Q += (Aij - ki*kj/(2*m))
    Q = Q/(2*m)
    return Q

def greedy_algorithm2(network):
    """
    Algoritmul Greedy pentru detectarea comunităților dintr-o rețea.
    """
    # la început, fiecare nod aparține unei comunități diferite
    partition = list(range(network['noNodes']))
    Mod = calculate_modularity(network, partition)

    ad = get_edge_list(network['mat'])
    
    while True:
        max_Mod = 0
        new_i = -1
        new_j = -1
        
        for i in ad:
                # dacă nodurile i și j sunt în comunități diferite
                    if partition[i[0]] != partition[i[1]]:
                        # unim comunitățile și calculăm noua modularitate
                        new_partition = partition.copy()
                        new_partition[i[0]] = new_partition[i[1]]
                        new_Mod = calculate_modularity(network, new_partition) - Mod 
                        # noua modularitate este maximă
                        if new_Mod > max_Mod:
                            max_Mod = new_Mod
                            new_i = i[0]
                            new_j = i[1]
        #ieșim din buclă
        if max_Mod <= 0:
            break
        #unim comunitățile și actualizăm valoarea modularității
        partition[new_i] = partition[new_j]
        Mod += max_Mod
        #print(Mod)
    
    return partition


























#filePath = os.path.join(crtDir,  'data', 'net.in')
#network0 = readNet(filePath)

def afisare_indici_valori_identice(lista):
    dictionar = {}
    for i, elem in enumerate(lista):
        if elem in dictionar:
            dictionar[elem].append(i)
        else:
            dictionar[elem] = [i]
    for elem, indici in dictionar.items():
        print(" ".join(str(i) for i in indici))




def exec(nr):
    crtDir =  os.getcwd()

    filePath = ""

    if nr == 2:
        filePath = os.path.join(crtDir,  'real', 'football', 'football.gml')

    if nr == 3:
        filePath = os.path.join(crtDir,  'real 2', 'real', 'dolphins', 'dolphins.gml')

    if nr == 4:
        filePath = os.path.join(crtDir,  'real 2', 'real', 'krebs', 'krebs.gml')

    if nr == 5:
        filePath = os.path.join(crtDir,  'real 2', 'real', 'karate', 'karate.gml')

    if nr == 6:
        filePath = os.path.join(crtDir,  'lesmis', 'lesmis.gml')

    if nr == 7:
        filePath = os.path.join(crtDir,  'adjnoun', 'adjnoun.gml')

    if nr == 8:
        filePath = os.path.join(crtDir,  'neural', 'celegansneural.gml')

    network = readGML(filePath)
    print(network)
    l = greedy_algorithm2(network)
    plotNetwork(network, l)
    num_distinct_values = len(set(l))
    print(num_distinct_values)
    afisare_indici_valori_identice(l)
    print(l)

exec(3)



