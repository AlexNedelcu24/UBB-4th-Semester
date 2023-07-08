import random

import numpy as np

from sklearn.preprocessing import StandardScaler

class MyRegression:
    def __init__(self):
        self.coef_ = None
        self.intercept_ = None
        self.scaler = StandardScaler()

    def fit(self, X, y, learning_rate=0.01, n_iter=1000):
        X = self._check_and_convert_X(X)
        y = self._check_and_convert_y(y)
        
        X = self.scaler.fit_transform(X)
        
        n_samples, n_features = X.shape
        self.coef_ = np.zeros(n_features)
        self.intercept_ = 0

        for _ in range(n_iter):
            y_predicted = self._predict(X)
            d_coef = -2 * (X.T @ (y - y_predicted)) / n_samples
            d_intercept = -2 * np.sum(y - y_predicted) / n_samples

            self.coef_ -= learning_rate * d_coef
            self.intercept_ -= learning_rate * d_intercept

    def _predict(self, X):
        return X @ self.coef_ + self.intercept_

    def predict(self, X):
        X = self._check_and_convert_X(X)
        X = self.scaler.transform(X)
        return self._predict(X)

    def _check_and_convert_X(self, X):
        X = np.array(X)
        if X.ndim == 1:
            X = X.reshape(-1, 1)
        return X

    def _check_and_convert_y(self, y):
        y = np.array(y)
        assert y.ndim == 1, "y should be a 1-dimensional array"
        return y


    














class MyMultiTargetRegression:
    
    def __init__(self):
        self.intercept_ = []
        self.coef_ = []

    def fit(self, x, y, learningRate = 0.01, noEpochs = 500):
        self.intercept_ = [0.0 for _ in range(y.shape[1])]
        self.coef_ = [[0.0 for _ in range(len(x[0]))] for _ in range(y.shape[1])]
        
        for epoch in range(noEpochs):
            for i in range(len(x)):
                ycomputed = [self.eval(x[i], k) for k in range(y.shape[1])]
                crtErrors = [ycomputed[k] - y[i][k] for k in range(y.shape[1])]
                for j in range(0, len(x[0])):
                    for k in range(y.shape[1]):
                        self.coef_[k][j] = self.coef_[k][j] - learningRate * crtErrors[k] * x[i][j]
                for k in range(y.shape[1]):
                    self.intercept_[k] = self.intercept_[k] - learningRate * crtErrors[k] * 1
        

    def eval(self, xi, k):
        yi = self.intercept_[k]
        for j in range(len(xi)):
            yi += self.coef_[k][j] * xi[j]
        return yi
    
    def predict(self, x):
        yComputed = [[self.eval(xi, k) for k in range(len(self.coef_))] for xi in x]
        return yComputed