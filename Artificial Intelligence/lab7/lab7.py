import csv
import numpy as np
from math import sqrt
from Regression import MyRegression

def readData(fileName, inputCols, outputCols):
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
        for column in inputCols:
            columnIndex = dataNames.index(column)
            line.append(float(data[i][columnIndex]))
        inputs.append(line)

    columnIndex = dataNames.index(outputCols)
    outputs = [float(data[i][columnIndex]) for i in range(len(data))]
    return inputs, outputs

def normalizeData(data):
    """
    Normalize the given data.
    """
    # Calculate means
    means = []
    for column in zip(*data):
        means.append(sum(column) / len(column))

    # Calculate standard deviations
    stds = []
    for i, column in enumerate(zip(*data)):
        variance = sum((x - means[i]) ** 2 for x in column) / len(column)
        stds.append(variance ** 0.5)

    # Normalize data
    normalizedData = []
    for row in data:
        newRow = [(x - means[i]) / stds[i] for i, x in enumerate(row)]
        normalizedData.append(newRow)

    return normalizedData


# getting data
input, output = readData("v1_world-happiness-report-2017.csv", ["Economy..GDP.per.Capita.", "Freedom"], "Happiness.Score")

# normalization
input = normalizeData(input)

indexes = [i for i in range(len(input))]
trainIndex = np.random.choice(indexes, int(0.8 * len(input)))
testIndex = [i for i in indexes if i not in trainIndex]

trainInput = [input[i] for i in trainIndex]
trainOutput = [output[i] for i in trainIndex]

testInput = [input[i] for i in testIndex]
testOutput = [output[i] for i in testIndex]

regression = MyRegression()
regression.fit(trainInput, trainOutput)

computedOutput = regression.predict(testInput)
error = 0
for i in range(len(testOutput)):
    error += sqrt ((testOutput[i] - computedOutput[i]) ** 2)
w0, w1 = regression.intercept_, regression.coef_[0]

print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x')

print(error / len(testOutput))





from sklearn import linear_model

regressor = linear_model.SGDRegressor(alpha = 0.01, max_iter = 1000)

regressor.fit(trainInput, trainOutput)

w0, w1 = regressor.intercept_[0], regressor.coef_[0]
print('tool: f(x) = ', w0, ' + ', w1, ' * x')
























"""
from Regression import MyMultiTargetRegression
import numpy as np
from sklearn.datasets import load_linnerud
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from math import sqrt

linnerud = load_linnerud()
X, y = linnerud.data, linnerud.target

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

regression = MyMultiTargetRegression()
regression.fit(X_train_scaled, y_train)
computedOutput = regression.predict(X_test_scaled)

error = 0
for i in range(len(y_test)):
    for j in range(len(y_test[i])):
        error += sqrt((y_test[i][j] - computedOutput[i][j]) ** 2)
print("Multi-target regression error:", error / (len(y_test) * len(y_test[0])))
"""