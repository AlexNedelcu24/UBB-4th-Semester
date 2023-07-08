from cmath import sqrt
import csv
from sklearn.metrics import log_loss, mean_squared_error
import numpy as np
from sklearn.metrics import accuracy_score, precision_score, recall_score


Weight = []
Waist = []
Pulse = []
PredictedWeight = []
PredictedWaist = []
PredictedPulse = []

def citeste_sportcsv(nume_fisier):
    
    with open(nume_fisier, 'r') as fisier:
        cititor = csv.reader(fisier)
        next(cititor)
        
        for rand in cititor:
            Weight.append(float(rand[0]))
            Waist.append(float(rand[1]))
            Pulse.append(float(rand[2]))
            PredictedWeight.append(float(rand[3]))
            PredictedWaist.append(float(rand[4]))
            PredictedPulse.append(float(rand[5]))

nume_fisier = "sport.csv"
citeste_sportcsv(nume_fisier)


def MAE():
    errorL1 = sum( ( abs(r1 - c1) + abs(r2 - c2) + abs(r3 - c3) ) for r1, r2, r3, c1, c2, c3 in zip(Weight, Waist, Pulse, PredictedWeight, PredictedWaist, PredictedPulse)) / len(Weight)
    print('Error (L1): ', errorL1)


def RMSE():
    errorL2 = sqrt(sum( ( (r1 - c1)**2 + (r2 - c2)**2 + (r3 - c3)**2 ) for r1, r2, r3, c1, c2, c3 in zip(Weight, Waist, Pulse, PredictedWeight, PredictedWaist, PredictedPulse)) / (3 * len(Waist)))
    print('Error (L2): ', errorL2)


def RMSE_scikit_learn():
    mse_weight = mean_squared_error(Weight, PredictedWeight)
    mse_waist = mean_squared_error(Waist, PredictedWaist)
    mse_pulse = mean_squared_error(Pulse, PredictedPulse)
    
    rmse = np.sqrt((mse_weight + mse_waist + mse_pulse) / 3)
    print('RMSE (scikit-learn): ', rmse)

#RMSE_scikit_learn()
#RMSE()


realLabels = []
computedLabels = []

nume_fisier = "flowers.csv"
def citeste_flowerscsv(nume_fisier):
    
    with open(nume_fisier, 'r') as fisier:
        cititor = csv.reader(fisier)
        next(cititor)
        
        for rand in cititor:
            realLabels.append(str(rand[0]))
            computedLabels.append(str(rand[1]))

citeste_flowerscsv(nume_fisier)


def Classification(realLabels, computedLabels, category):
    noCorrect = 0
    for cat in range(0, len(realLabels)):
        if (realLabels[cat] == computedLabels[cat]):
            noCorrect += 1
    acc = noCorrect / len(realLabels)

    metrics = {}
    for cat in category:
        TP = sum([1 if (realLabels[i] == cat and computedLabels[i] == cat) else 0 for i in range(len(realLabels))])
        FP = sum([1 if (realLabels[i] != cat and computedLabels[i] == cat) else 0  for i in range(len(realLabels))])
        TN = sum([1 if (realLabels[i] != cat and computedLabels[i] != cat) else 0 for i in range(len(realLabels))])
        FN = sum([1 if (realLabels[i] == cat and computedLabels[i] != cat) else 0  for i in range(len(realLabels))])

        precision = TP / (TP + FP)
        recall = TP / (TP + FN)
        metrics[cat] = (precision, recall)

    return acc, metrics

def exec_classification():
    category = ['Tulip', 'Daisy', 'Rose']
    acc, metrics = Classification(realLabels, computedLabels, category)

    print('Accuracy: ', acc)
    for i in category:
        print(f'Class {i}: Precision = {metrics[i][0]}, Recall = {metrics[i][1]}')


    # Scikit-learn
    acc_sklearn = accuracy_score(realLabels, computedLabels)
    print('Accuracy (scikit-learn): ', acc_sklearn)

    precision_sklearn = precision_score(realLabels, computedLabels, labels=category, average=None)
    recall_sklearn = recall_score(realLabels, computedLabels, labels=category, average=None)

    for idx, cat in enumerate(category):
        print(f'Class {cat} (scikit-learn): Precision = {precision_sklearn[idx]}, Recall = {recall_sklearn[idx]}')


#exec_classification()





import numpy as np

def cross_entropy_loss(y_true, y_pred):
    """
    Calculează loss-ul folosind binary cross-entropy.
    """
    assert y_true.shape == y_pred.shape
    
    noSamples = y_true.shape[0]
    epsilon = 1e-8  #Evită division by zero
    
    loss = -np.sum(y_true * np.log(y_pred + epsilon)) / noSamples
    return loss

def exec_croos():
    y_true = np.array([[1, 0], [0, 1], [1, 0], [0, 1]])
    y_pred = np.array([[0.9, 0.1], [0.2, 0.8], [0.7, 0.3], [0.4, 0.6]])

    loss = cross_entropy_loss(y_true, y_pred)
    # Scikit-learn
    loss_sklearn = log_loss(y_true, y_pred)
    print(f"Loss (scikit-learn): {loss_sklearn}")
    print(f"Loss: {loss}")

    y_true = np.array([[1, 0, 0], [0, 1, 0], [0, 0, 1], [1, 0, 0]])
    y_pred = np.array([[0.8, 0.1, 0.1], [0.2, 0.7, 0.1], [0.1, 0.2, 0.7], [0.9, 0.05, 0.05]])

    loss = cross_entropy_loss(y_true, y_pred)
    # Scikit-learn
    loss_sklearn = log_loss(y_true, y_pred)
    print(f"Loss (scikit-learn): {loss_sklearn}")
    print(f"Loss: {loss}")

exec_croos()

