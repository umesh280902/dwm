import pandas as pd

# Create a sample dataset
data = pd.DataFrame({
    'Outlook': ['Sunny', 'Sunny', 'Overcast', 'Rainy', 'Rainy', 'Rainy', 'Overcast', 'Sunny', 'Sunny', 'Rainy', 'Sunny', 'Overcast', 'Overcast', 'Rainy'],
    'Temperature': ['Hot', 'Hot', 'Hot', 'Mild', 'Cool', 'Cool', 'Cool', 'Mild', 'Cool', 'Mild', 'Mild', 'Mild', 'Hot', 'Mild'],
    'Humidity': ['High', 'High', 'High', 'High', 'Normal', 'Normal', 'Normal', 'High', 'Normal', 'Normal', 'Normal', 'High', 'Normal', 'High'],
    'Windy': [False, True, False, False, False, True, True, False, False, False, True, True, False, True],
    'PlayTennis': ['No', 'No', 'Yes', 'Yes', 'Yes', 'No', 'Yes', 'No', 'Yes', 'Yes', 'Yes', 'Yes', 'Yes', 'No']
})

# Precompute the frequency of classes
classification = 'PlayTennis'  # Change to your target variable
info = {}
prior = data[classification].value_counts()
row = 0

for val in data[classification]:
    if val not in info:
        info[val] = {}
    for e in data.iloc[row]:
        if e == val:
            continue
        if e in info[val]:
            info[val][e] += 1
        else:
            info[val][e] = 1
    row += 1

# Naive Bayes Formula
def Naive_Bayes(l):
    prob, sum_prob, class_name = 0, 0, ''
    
    for p in prior.keys():
        curr_prob = prior[p] / data.shape[0]
        for v in l:
            curr_prob = curr_prob * ((info[p].get(v, 0)) / prior[p])
        sum_prob += curr_prob
        if prob < curr_prob:
            prob, class_name = curr_prob, p

    return [class_name, (prob * 100) / sum_prob, prob]

# For all rows prediction
prediction, probability = [], []
accuracy = 0

for i in range(len(data)):
    row = data.iloc[i].tolist()
    row.pop(len(row) - 1)
    res = Naive_Bayes(row)
    prediction.append(res[0])
    probability.append(res[1])
    if res[0] == data.iloc[i][len(row)]:
        accuracy += 1

data['Prediction'] = prediction
data['Probability'] = probability

print(data)
print(f'Accuracy: {((accuracy * 100) / len(data)):.3f}%')
