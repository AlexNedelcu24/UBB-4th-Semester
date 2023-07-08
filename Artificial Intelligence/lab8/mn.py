import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from Utils import normalisation, error, normaliseTest
import numpy as np
import csv
from sklearn.metrics import accuracy_score


def readData():
    inputs = []
    outputs = []
    with open("Data/iris.data", "r") as file:
        csvreader = csv.reader(file)
        for row in csvreader:
            if not row:
                continue
            inputs.append([float(row[0]), float(row[2])])  # Take sepal length and petal length
            outputs.append(row[4])
    return inputs, outputs

import numpy as np

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

class MyLogisticRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    def fit(self, x, y, learningRate=0.001, noEpochs=10000):
        no_of_samples, no_of_features = x.shape
        self.coef_ = np.zeros(no_of_features)
        self.intercept_ = 0.0
        x = np.insert(x, 0, 1, axis=1)  # insert a column of ones for the intercept term
        for _ in range(noEpochs):
            linear_model = np.dot(x, np.insert(self.coef_, 0, self.intercept_))
            y_predicted = sigmoid(linear_model)
            # update the weights and bias
            self.coef_ -= learningRate * (1/no_of_samples) * np.dot(x[:, 1:].T, (y_predicted - y))
            self.intercept_ -= learningRate * np.mean(y_predicted - y)
            
    def predict_proba(self, x):
        linear_model = np.dot(x, self.coef_) + self.intercept_
        return sigmoid(linear_model)

    def predict(self, x):
        probabilities = self.predict_proba(x)
        return [1 if prob >= 0.5 else 0 for prob in probabilities]





# Citirea datelor
inputs, outputs = readData()

# Extrage datele de intrare și etichetele
X = np.array(inputs)
y = np.array(outputs)

# Crearea mapării pentru etichetele claselor
class_mapping = {label: idx for idx, label in enumerate(np.unique(y))}
y = np.array([class_mapping[label] for label in y])

# Împărțirea datelor
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=1)

# Normalizarea datelor
mean1, stDev1, X_train_std1 = normalisation(X_train[:, 0])
mean2, stDev2, X_train_std2 = normalisation(X_train[:, 1])
X_train_std = np.array([[feat1, feat2] for feat1, feat2 in zip(X_train_std1, X_train_std2)])

X_test_std1 = normaliseTest(X_test[:, 0], mean1, stDev1)
X_test_std2 = normaliseTest(X_test[:, 1], mean2, stDev2)
X_test_std = np.array([[feat1, feat2] for feat1, feat2 in zip(X_test_std1, X_test_std2)])

# Antrenarea modelului
model = MyLogisticRegression()
model.fit(X_train_std, y_train)

# Predicția și calculul erorii
y_pred = model.predict(X_test_std)
error = error(y_test, y_pred)

print("Eroarea este ", error)

# Calcularea acurateței
accuracy = accuracy_score(y_test, y_pred)
print("Acuratețea este ", accuracy * 100, "%")

# Crearea de plot-uri
def plot_data(X, y, title):
    plt.scatter(X[y == 0, 0], X[y == 0, 1], color='red', marker='o', label='Iris-setosa')
    plt.scatter(X[y == 1, 0], X[y == 1, 1], color='blue', marker='x', label='Iris-versicolor')
    plt.scatter(X[y == 2, 0], X[y == 2, 1], color='green', marker='s', label='Iris-virginica')
    plt.xlabel('sepal length')
    plt.ylabel('petal length')
    plt.legend(loc='upper left')
    plt.title(title)
    plt.show()

plot_data(X_train_std, y_train, 'Train Data')
plot_data(X_test_std, y_test, 'Test Data')
