from statistics import mean, stdev
 
def normalisation(features):
    meanValue = mean(features)
    stdDevValue = stdev(features)
    normalisedFeatures = [(feat - meanValue) / stdDevValue for feat in features]
    return meanValue, stdDevValue, normalisedFeatures

def normaliseTest(features, meanValue, stdDevValue):
    return [(feat-meanValue) / stdDevValue for feat in features]

def error(computedOutputs, testOutputs):
    error = 0
    for t1, t2 in zip(computedOutputs, testOutputs):
        error += (t1 - t2) ** 2
    return error / len(testOutputs)