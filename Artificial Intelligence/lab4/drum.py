import networkx as nx
import numpy as np
import random
from itertools import permutations
import os

import random
seed = 2
random.seed(seed)
np.random.seed(seed)


def generate_distance_matrix(n):
    distance_matrix = [[0 for _ in range(n)] for _ in range(n)]

    for i in range(n):
        for j in range(i+1, n):
            distance = random.randint(1, 100)  # Assign random distances between 1 and 100
            distance_matrix[i][j] = distance
            distance_matrix[j][i] = distance

    return distance_matrix

def save_data_to_file(x, matrix, y, filename):
    with open(filename, 'w') as file:
        # Scriem numărul x în primul rând
        file.write(str(x) + '\n')
        # Scriem matricea în următoarele x linii
        for row in matrix:
            file.write(','.join(str(num) for num in row) + '\n')
        # Scriem numărul y la sfârșit
        file.write(str(y))


#m = generate_distance_matrix(1000)
#save_data_to_file(1000,m,0,"test1000.txt")


def ordered_crossover(parent1, parent2):
    size = len(parent1)
    child1 = [None] * size
    child2 = [None] * size

    start = random.randint(1, size - 2)
    end = random.randint(start, size - 2)

    child1[start:end] = parent1[start:end]
    child2[start:end] = parent2[start:end]

    parent1_remaining = [item for item in parent2 if item not in child1]
    parent2_remaining = [item for item in parent1 if item not in child2]

    for i in range(size):
        if child1[i] is None:
            child1[i] = parent1_remaining.pop(0)
        if child2[i] is None:
            child2[i] = parent2_remaining.pop(0)

    return child1, child2



def mutation(individual):
    idx1, idx2 = random.sample(range(0, len(individual)-1), 2) 
    individual[idx1], individual[idx2] = individual[idx2], individual[idx1]
    return individual


def evaluate_fitness(path):
        cost = 0
        for i in range(len(path) - 1):
            if distance_matrix[path[i]][path[i+1]] == 0:
                return float('inf')
            cost += distance_matrix[path[i]][path[i+1]]
        
        if distance_matrix[path[-1]][path[0]] == 0:
            return float('inf')
        cost += distance_matrix[path[-1]][path[0]]
        return cost

def selection(population):
    costs = [(ind, evaluate_fitness(ind)) for ind in population]
    costs.sort(key=lambda x: x[1])
    return random.sample(costs[:len(population) // 2], 2)




def ga_tsp_all(n, distance_matrix, starting_node, population_size=100, generations=1000):
    best_individuals = []
    best_cost = float('inf')

    nodes = list(range(n))
    population = [random.sample(nodes, len(nodes)) for _ in range(population_size)]

    for _ in range(generations):
        parents = selection(population)
        offspring = ordered_crossover(parents[0][0], parents[1][0])

        offspring = (mutation(offspring[0]), mutation(offspring[1]))

        offspring_costs = [evaluate_fitness(ind) for ind in offspring]
        population_costs = [(ind, evaluate_fitness(ind)) for ind in population]
        population_costs.sort(key=lambda x: -x[1])

        for i, cost in enumerate(offspring_costs):
            if cost < population_costs[i][1]:
                population.remove(population_costs[i][0])
                population.append(offspring[i])

        current_best_individual = min(population, key=evaluate_fitness)
        current_best_cost = evaluate_fitness(current_best_individual)

        #print(population)

        
        if best_cost > current_best_cost:
            for i in range(len(best_individuals)):
                if evaluate_fitness(best_individuals[i]) > current_best_cost :
                    best_individuals.pop(i)
                    i-=1

            for i in population:
                if(evaluate_fitness(i) == current_best_cost and i not in best_individuals):
                    best_individuals.append(i)

    
    return best_individuals

distance_matrix = []
def read_file(filename):
    with open(filename, 'r') as f:
        data = f.read().strip().split('\n')

       
        n = int(data[0])

        
        
        for i in range(1, n + 1):
            row = list(map(float, data[i].strip().split(',')))
            distance_matrix.append(row)

        
        starting_node = int(data[n+1])

    #shortest_path = ga_tsp_from_matrix(n, distance_matrix, starting_node)
    #print("Cea mai scurtă cale:", shortest_path, evaluate_fitness(shortest_path))

    min_paths = ga_tsp_all(n, distance_matrix, starting_node)
    print("Toate drumurile minime:")
    for path in min_paths:
        print(evaluate_fitness(path))
        path.append(path[0])
        print(path)




#read_file("fricker26.txt")
#read_file("easy_06_tsp.txt")
#read_file("t50.txt")
#read_file("t500.txt")
read_file("test1000.txt")


#read_file("three.txt")

    
