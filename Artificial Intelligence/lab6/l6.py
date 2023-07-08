import csv
import os
from matplotlib import pyplot as plt
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error

from myRegressor import Regressor

import csv

def loadData2(fileName, col1, col2, outputCols):
    data = []
    dataNames = []
    with open(fileName) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                dataNames = row
            else:
                data.append(row)
            line_count += 1

    inputs = []
    for i in range(len(data)):
        line = []
        columnIndex1 = dataNames.index(col1)
        columnIndex2 = dataNames.index(col2)
        cell1 = data[i][columnIndex1]
        cell2 = data[i][columnIndex2]
        if cell1 == '':
            
            cell1 = data[i][columnIndex2]
        else:
            cell1 = float(cell1)
        if cell2 == '':
            
            cell2 = data[i][columnIndex1]
        else:
            cell2 = float(cell2)
        line.append(cell1)
        line.append(cell2)
        inputs.append(line)

    columnIndex = dataNames.index(outputCols)
    outputs = []
    for i in range(len(data)):
        cell = data[i][columnIndex]
        if cell == '':
            
            cell = np.mean([float(row[columnIndex]) for row in data if row[columnIndex] != ''])
        else:
            cell = float(cell)
        outputs.append(cell)
    return inputs, outputs



def loadData(fileName, col1, col2, outputCols):
    data = []
    dataNames = []
    with open(fileName) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                dataNames = row
            else:
                data.append(row)
            line_count += 1

    inputs = []
    for i in range(len(data)):
        line = []
        columnIndex1 = dataNames.index(col1)
        columnIndex2 = dataNames.index(col2)
        cell1 = data[i][columnIndex1]
        cell2 = data[i][columnIndex2]
        if cell1 == '':
            cell1 = np.mean([float(data[j][columnIndex1]) for j in range(len(data)) if data[j][columnIndex1] != ''])
        else:
            cell1 = float(cell1)
        if cell2 == '':
            cell2 = np.mean([float(data[j][columnIndex2]) for j in range(len(data)) if data[j][columnIndex2] != ''])
        else:
            cell2 = float(cell2)
        line.append(cell1)
        line.append(cell2)
        inputs.append(line)

    columnIndex = dataNames.index(outputCols)
    outputs = []
    for i in range(len(data)):
        cell = data[i][columnIndex]
        if cell == '':
            cell = np.mean([float(data[j][columnIndex]) for j in range(len(data)) if data[j][columnIndex] != ''])
        else:
            cell = float(cell)
        outputs.append(cell)
    return inputs, outputs




crtDir =  os.getcwd()
filePath = os.path.join(crtDir, 'data', 'v1_world-happiness-report-2017.csv')
#filePath = os.path.join(crtDir, 'data', 'v2_world-happiness-report-2017.csv')
#filePath = os.path.join(crtDir, 'data2', 'v3_world-happiness-report-2017.csv')
input, output = loadData(filePath, 'Economy..GDP.per.Capita.', 'Freedom', 'Happiness.Score')


#input, output = loadData2(filePath, 'Economy..GDP.per.Capita.', 'Freedom', 'Happiness.Score')

def plotTrain(regressor, trainInput, trainOutput):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.scatter([trainInput[i][1] for i in range(len(trainInput))], [trainInput[i][2] for i in range(len(trainInput))],
               trainOutput, marker='.', color='red')
    plotArea(regressor, trainInput, trainOutput, ax)

def plotTest(regressor, trainInput, trainOutput, testInput, testOutput):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.scatter([testInput[i][0] for i in range(len(testInput))], [testInput[i][1] for i in range(len(testInput))],
               testOutput, marker='^', color='green')
    plotArea(regressor, trainInput, trainOutput, ax)

def plotArea(regressor, trainInput, trainOutput, ax):

    ax.set_xlabel("GDP")
    ax.set_ylabel("Freedom")
    ax.set_zlabel("Happiness")

    x = np.arange(min([trainInput[i][1] for i in range(len(trainInput))]),
                  max([trainInput[i][1] for i in range(len(trainInput))]), 0.01)

    y = np.arange(min([trainInput[i][2] for i in range(len(trainInput))]),
                  max([trainInput[i][2] for i in range(len(trainInput))]), 0.1)
    x, y = np.meshgrid(x, y)

    z = [regressor.predict([d1, d2]) for d1, d2 in zip(x,y)]
    z = np.array(z)

    ax.plot_surface(x, y, z.reshape(x.shape), alpha=0.7)
    plt.show()



indexes = [i for i in range(len(input))]
trainSample = np.random.choice(indexes, int(0.8 * len(input)))
ValidationSample = [i for i in indexes if i not in trainSample]

trainInput = [([1] + input[i]) for i in trainSample]
trainOutput = [[output[i]] for i in trainSample]

validationInputs = [input[i] for i in ValidationSample]
validationOutputs = [output[i] for i in ValidationSample]

# ------------------------
# TOOL regression
# ------------------------
regressor = LinearRegression()
regressor.fit(trainInput, trainOutput)

# Removed the addition of regressor.intercept_ here
testInputs = [[1] + validationInputs[i] for i in range(len(validationInputs))]
computedDataOutput = regressor.predict(testInputs)

error = mean_squared_error(validationOutputs, computedDataOutput)
print("Tool error: ", error)







#plotTrain(regressor, trainInput, trainOutput)
#plotTest(regressor, trainInput, trainOutput, validationInputs, computedDataOutput)





# MANUAL regression

regressor = Regressor()

regressor.fit(trainInput, trainOutput)

computedOutput = [regressor.predict(data) for data in validationInputs]

error = 0
for t1, t2 in zip(computedOutput, validationOutputs):
    error += (t1 - t2) ** 2
error = error / len(validationOutputs)
print("prediction error (manual): ", error)

plotTrain(regressor, trainInput, trainOutput)
plotTest(regressor, trainInput, trainOutput, validationInputs, computedOutput)
