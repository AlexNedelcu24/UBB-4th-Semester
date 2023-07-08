import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.datasets import load_iris

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def sigmoid_derivat(x):
    return x * (1 - x)

# Încarcam datele
iris = load_iris()
X = iris['data']
y = iris['target']

# Codificare one-hot pentru etichete
y_one_hot = np.zeros((y.size, y.max()+1))
y_one_hot[np.arange(y.size), y] = 1

# Normalizam datele
X = (X - np.mean(X, axis=0)) / np.std(X, axis=0)

# impartim datele în set de antrenament si set de testare
X_train, X_test, y_train, y_test = train_test_split(X, y_one_hot, test_size=0.2, random_state=42)

# Initializam ponderile și pragurile (biases)
np.random.seed(42)
weights1 = np.random.rand(4, 10)
weights2 = np.random.rand(10, 3)
biases1 = np.random.rand(1, 10)
biases2 = np.random.rand(1, 3)

learning_rate = 0.1
epochs = 50000

for epoch in range(epochs):
    # Forward propagation
    z1 = np.dot(X_train, weights1) + biases1
    a1 = sigmoid(z1)
    z2 = np.dot(a1, weights2) + biases2
    a2 = sigmoid(z2)

    # Backward propagation
    error_out = (a2 - y_train)
    delta_out = error_out * sigmoid_derivat(a2)

    error_hidden = delta_out.dot(weights2.T)
    delta_hidden = error_hidden * sigmoid_derivat(a1)

    # Update weights and biases
    weights2 -= a1.T.dot(delta_out) * learning_rate
    biases2 -= np.sum(delta_out, axis=0, keepdims=True) * learning_rate
    weights1 -= X_train.T.dot(delta_hidden) * learning_rate
    biases1 -= np.sum(delta_hidden, axis=0) * learning_rate

    # Print error every 5000 epochs
    if epoch % 5000 == 0:
        print("Epoch", epoch)
        print("Error:", np.mean(np.abs(error_out)))

# Test the model
z1_test = np.dot(X_test, weights1) + biases1
a1_test = sigmoid(z1_test)
z2_test = np.dot(a1_test, weights2) + biases2
a2_test = sigmoid(z2_test)

predictions = np.argmax(a2_test, axis=1)
accuracy = np.mean(predictions == np.argmax(y_test, axis=1))
print("Accuracy:", accuracy)
