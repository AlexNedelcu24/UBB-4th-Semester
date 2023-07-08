import os
import random 
from random import randint
import numpy as np 
import networkx as nx
import matplotlib.pyplot as plt 
import warnings 

from networkx.algorithms import community

warnings.simplefilter('ignore')

def readGML(fileName):
    
    

    #graph = nx.read_gml(fileName)

   # for i, node_data in graph.nodes(data=True):
    #    label = node_data.get('label', f'node_{i}')
    # process the node with the 'label' attribute or default value

    #if fileName == "power.gml":
    graph = nx.read_gml(fileName, label=None)
    #else:
    #    graph = nx.read_gml(fileName)
    
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









import random
from numpy import array, sum

def modularity(communities, param):
    noNodes = param['noNodes']
    mat = array(param['mat'])
    degrees = param['degrees']
    noEdges = param['noEdges']
    M = 2 * noEdges
    Q = 0.0
    for com in set(communities):
        nodes_in_com = [i for i in range(noNodes) if communities[i] == com]
        subgraph = mat[nodes_in_com][:, nodes_in_com]
        com_degrees = [degrees[i] for i in nodes_in_com]
        com_edges = sum(com_degrees) / M
        com_Q = sum(subgraph) / M - com_edges ** 2
        Q += com_Q
    return Q

def wheel(population, fitnes_list):
    sum1 = sum(fitnes_list)
    selected = []

    for i in range(len(population)):
        if fitnes_list[i] > sum1/3:
            selected.append(population[i])

    return selected

def swap_mutation(chromosome):
    pos1 = random.randint(0, len(chromosome) - 1)
    pos2 = random.randint(0, len(chromosome) - 1)
    aux = chromosome[pos1]
    chromosome[pos1] = chromosome[pos2]
    chromosome[pos2] = aux
    return chromosome

def crossover(parent1, parent2):
    child = parent1.copy()
    start = random.randint(0, len(child) - 1)
    end = random.randint(start, len(child) - 1)
    for i in range(start, end):
        child[i] = parent2[i]
    return child

def evolution(communities, param, pop_size, num_generations):

    population = []
    for i in range(pop_size):
        chromosome = [random.randint(0, max(communities)) for j in range(param['noNodes'])]
        population.append(chromosome)

    best_solution = population[0]
    best_Q = modularity(best_solution, param)

    for g in range(num_generations):

        print("Iteration: " + str(g))

        fitnes_list = [modularity(chromosome, param) for chromosome in population]
        parents = wheel(population, fitnes_list)

        if not parents:
            continue

        new_generation = []
        while len(new_generation) < pop_size:
            parent1 = random.choice(parents)
            parent2 = random.choice(parents)
            child = crossover(parent1, parent2)
            child = swap_mutation(child)
            new_generation.append(child)

        population = new_generation

        for chromosome in population:
            Q = modularity(chromosome, param)
            if Q > best_Q:
                best_Q = Q
                best_solution = chromosome

        return best_solution
    


num_generations = 300

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

    if nr == 0:
        filePath = os.path.join(crtDir,  'data', 'net.in')

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

    if nr == 9:
        filePath =os.path.join(crtDir, 'test', 'test.gml')

    if nr == 10:
        filePath =os.path.join(crtDir, 'k1', 'internet_routers-22july06.gml')

    if nr == 11:
        filePath =os.path.join(crtDir, 'k2', 'lesmiserables.gml')

    if nr == 12:
        filePath =os.path.join(crtDir, 'k3', 'power.gml')

    network = readGML(filePath)
    #network = readNet(filePath)
    #communities = [random.randint(0, random.randint(1, network['noNodes'])) for _ in range(network['noNodes'])]
    communities = [random.randint(0, 1) for _ in range(network['noNodes'])]
    #l = evolution(communities, network, network['noNodes'], num_generations)
    l = evolution(communities, network, 100, num_generations)
    
    plotNetwork(network, l)
    num_distinct_values = len(set(l))
    print(num_distinct_values)
    afisare_indici_valori_identice(l)


exec(11)
