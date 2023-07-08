class Regressor:
    def __init__(self):
        self.__coef = []

    def fit(self, input_data, output_data):
        phase1 = self.__multiply(self.__transpusa(input_data), input_data)
        phase2 = self.__multiply(self.__transpusa(input_data), output_data)
        self.__coef = self.__gaussian_elimination(phase1, phase2)
        return self.__coef

    def __determinant(self, matrix):
        if len(matrix) == 1:
            return matrix[0][0]
        if len(matrix) == 2:
            return matrix[0][0] * matrix[1][1] - matrix[1][0]*matrix[0][1]
        result = 0
        for j in range(len(matrix)):
            minor = self.__minor(matrix, j)
            result += ((-1) ** (j + 2)) * matrix[0][j] * self.__determinant(minor)
        return result

    def __transpusa(self, matrix):
        transp = []
        for i in range(len(matrix[0])):
            line = []
            for j in range(len(matrix)):
                line.append(matrix[j][i])
            transp.append(line)
        return transp

    def __multiply(self, matrix1, matrix2):
        matrix = [[0 for _ in range(len(matrix2[0]))] for _ in range(len(matrix1))]
        for i in range(len(matrix1)):
            for j in range(len(matrix2[0])):
                for k in range(len(matrix1[0])):
                    matrix[i][j] += matrix1[i][k] * matrix2[k][j]
        return matrix

    def __minor(self, matrix, j_to_find):
        minor = []
        for i in range(1, len(matrix)):
            line = []
            for j in range(len(matrix)):
                if j != j_to_find:
                    line.append(matrix[i][j])
            minor.append(line)
        return minor

    def __gaussian_elimination(self, matrix, output):
        n = len(matrix)
        for i in range(n):
            max_row = max(range(i, n), key=lambda r: abs(matrix[r][i]))
            matrix[i], matrix[max_row] = matrix[max_row], matrix[i]
            output[i], output[max_row] = output[max_row], output[i]

            for j in range(i + 1, n):
                factor = matrix[j][i] / matrix[i][i]
                output[j] = [x - factor * y for x, y in zip(output[j], output[i])]
                for k in range(i, n):
                    matrix[j][k] -= factor * matrix[i][k]

        x = [0] * n
        for i in range(n - 1, -1, -1):
            x[i] = output[i][0] / matrix[i][i]
            for k in range(i - 1, -1, -1):
                output[k][0] -= matrix[k][i] * x[i]

        return [[xi] for xi in x]

    def predict(self, data):
        return sum([data[i] * self.__coef[i+1][0] for i in range(len(data))]) + self.__coef[0][0]
