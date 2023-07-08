#---------------------TOOL------------------
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from Utils import normalisation, normaliseTest
import numpy as np

import csv
from sklearn.metrics import accuracy_score
from sklearn.linear_model import LogisticRegression
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
model = LogisticRegression(solver='lbfgs', multi_class='ovr')
model.fit(X_train_std, y_train)

# Predicția și calculul acurateței
y_pred = model.predict(X_test_std)

# Calcularea acurateței
accuracy = accuracy_score(y_test, y_pred)
print("Acuratețea este (tool) ", accuracy * 100, "%")